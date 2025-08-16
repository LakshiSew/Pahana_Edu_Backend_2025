
     package com.Pahana_Edu_Backend.Order.serviceImpl;

     import com.Pahana_Edu_Backend.Accessories.entity.Accessories;
     import com.Pahana_Edu_Backend.Accessories.repository.AccessoriesRepository;
     import com.Pahana_Edu_Backend.Book.entity.Book;
     import com.Pahana_Edu_Backend.Book.repository.BookRepository;
     import com.Pahana_Edu_Backend.Customer.entity.Customer;
     import com.Pahana_Edu_Backend.Customer.repository.CustomerRepository;
     import com.Pahana_Edu_Backend.Order.entity.Orders;
     import com.Pahana_Edu_Backend.Order.repository.OrdersRepository;
     import com.Pahana_Edu_Backend.Order.service.OrdersService;
     import com.itextpdf.kernel.pdf.PdfDocument;
     import com.itextpdf.kernel.pdf.PdfWriter;
     import com.itextpdf.layout.Document;
     import com.itextpdf.layout.element.Cell;
     import com.itextpdf.layout.element.Paragraph;
     import com.itextpdf.layout.element.Table;
     import jakarta.mail.MessagingException;
     import jakarta.mail.internet.MimeMessage;
     import org.springframework.beans.factory.annotation.Autowired;
     import org.springframework.mail.javamail.JavaMailSender;
     import org.springframework.mail.javamail.MimeMessageHelper;
     import org.springframework.stereotype.Service;
     import java.time.LocalDateTime;
     import java.util.ArrayList;
     import java.util.HashMap;
     import java.util.List;
     import java.util.Map;
     import java.util.Optional;
     import java.io.ByteArrayOutputStream;
     import java.util.stream.Collectors;

     @Service
     public class OrdersServiceImpl implements OrdersService {

         @Autowired
         private OrdersRepository ordersRepository;

         @Autowired
         private CustomerRepository customerRepository;

         @Autowired
         private BookRepository bookRepository;

         @Autowired
         private AccessoriesRepository accessoriesRepository;

         @Autowired
         private JavaMailSender mailSender;

         @Override
         public Orders createOrder(Orders order) {
             System.out.println("Received order payload: " + order);
             System.out.println("Product IDs: " + order.getProductIds());
             System.out.println("Product Quantities: " + order.getProductQuantities());
             System.out.println("Product Types: " + order.getProductTypes());

             // Validate required fields
             if (order.getProductIds() == null || order.getProductIds().isEmpty()) {
                 throw new IllegalArgumentException("Product IDs cannot be null or empty");
             }
             if (order.getProductQuantities() == null || order.getProductQuantities().isEmpty()) {
                 throw new IllegalArgumentException("Product quantities cannot be null or empty");
             }
             if (order.getProductTypes() == null || order.getProductTypes().isEmpty()) {
                 throw new IllegalArgumentException("Product types cannot be null or empty");
             }

             // Validate that all product IDs in quantities and types match productIds list
             for (String productId : order.getProductQuantities().keySet()) {
                 if (!order.getProductIds().contains(productId)) {
                     throw new IllegalArgumentException("Product ID " + productId + " in quantities but not in productIds");
                 }
             }
             for (String productId : order.getProductTypes().keySet()) {
                 if (!order.getProductIds().contains(productId)) {
                     throw new IllegalArgumentException("Product ID " + productId + " in types but not in productIds");
                 }
             }

             double totalPrice = 0.0;
             for (Map.Entry<String, Integer> entry : order.getProductQuantities().entrySet()) {
                 String productId = entry.getKey();
                 int quantity = entry.getValue();
                 String productType = order.getProductTypes().getOrDefault(productId, "Unknown");
                 if (productType == null || (!productType.equals("Book") && !productType.equals("Accessory"))) {
                     throw new IllegalArgumentException("Invalid or missing product type for product ID: " + productId);
                 }
                 if (productType.equals("Book")) {
                     Optional<Book> book = bookRepository.findById(productId);
                     if (book.isPresent()) {
                         double price = book.get().getPrice();
                         double discount = book.get().getDiscount() != null ? book.get().getDiscount() : 0.0;
                         totalPrice += quantity * price * (1 - discount / 100);
                     } else {
                         throw new IllegalArgumentException("Book with ID " + productId + " not found");
                     }
                 } else if (productType.equals("Accessory")) {
                     Optional<Accessories> accessory = accessoriesRepository.findById(productId);
                     if (accessory.isPresent()) {
                         double price = accessory.get().getPrice();
                         double discount = accessory.get().getDiscount() != null ? accessory.get().getDiscount() : 0.0;
                         totalPrice += quantity * price * (1 - discount / 100);
                     } else {
                         throw new IllegalArgumentException("Accessory with ID " + productId + " not found");
                     }
                 }
             }
             order.setTotalPrice(totalPrice);
             order.setCreatedAt(LocalDateTime.now());
             order.setUpdatedAt(LocalDateTime.now());
             order.setStatus("Pending");
             Orders savedOrder = ordersRepository.save(order);
             System.out.println("Saved order: " + savedOrder);
             return savedOrder;
         }

    @Override
    public Optional<Orders> getOrderById(String orderId) {
        return ordersRepository.findById(orderId);
    }

    @Override
    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    @Override
    public List<Orders> getOrdersByCustomerId(String customerId) {
        return ordersRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Orders> getOrdersByCustomerPhone(String customerPhone) {
        return ordersRepository.findByCustomerPhone(customerPhone);
    }

    @Override
    public Orders updateOrderStatus(String orderId, String status) {
        Optional<Orders> existingOrder = ordersRepository.findById(orderId);
        if (existingOrder.isPresent()) {
            Orders order = existingOrder.get();
            if (!status.equals("Pending") && !status.equals("Confirmed") && !status.equals("Canceled")) {
                throw new IllegalArgumentException("Status must be 'Pending', 'Confirmed', or 'Canceled'");
            }
            order.setStatus(status);
            order.setUpdatedAt(LocalDateTime.now());
            Orders updatedOrder = ordersRepository.save(order);

            Optional<Customer> customer = customerRepository.findById(order.getCustomerId());
            if (customer.isPresent()) {
                sendOrderStatusEmail(customer.get().getCustomerEmail(), customer.get().getCustomerName(), order);
            } else {
                throw new RuntimeException("Customer not found with id: " + order.getCustomerId());
            }
            return updatedOrder;
        } else {
            throw new RuntimeException("Order not found with id: " + orderId);
        }
    }

    @Override
    public void deleteOrder(String orderId) {
        if (ordersRepository.existsById(orderId)) {
            ordersRepository.deleteById(orderId);
        } else {
            throw new RuntimeException("Order not found with id: " + orderId);
        }
    }

    @Override
    public String generateBill(String orderId) {
        Optional<Orders> order = ordersRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new RuntimeException("Order not found with id: " + orderId);
        }

        Optional<Customer> customer = customerRepository.findById(order.get().getCustomerId());
        if (customer.isEmpty()) {
            throw new RuntimeException("Customer not found for order id: " + orderId);
        }

        StringBuilder bill = new StringBuilder();
        bill.append("=== Pahana Edu Order Bill ===\n")
            .append("Order ID: ").append(order.get().getId()).append("\n")
            .append("Customer Name: ").append(customer.get().getCustomerName()).append("\n")
            .append("Customer Email: ").append(customer.get().getCustomerEmail()).append("\n")
            .append("Customer Phone: ").append(order.get().getCustomerPhone()).append("\n")
            .append("Address: ").append(order.get().getAddress()).append("\n")
            .append("Order Date: ").append(order.get().getOrderDate()).append("\n")
            .append("Products:\n");

        double totalDiscount = 0.0;
        double totalPriceBeforeDiscount = 0.0;

        Map<String, String> productTypes = order.get().getProductTypes();
        Map<String, Integer> productQuantities = order.get().getProductQuantities();

        for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();
            String productType = productTypes.getOrDefault(productId, "Unknown");
            if (productType.equals("Book")) {
                Optional<Book> book = bookRepository.findById(productId);
                if (book.isPresent()) {
                    double price = book.get().getPrice();
                    double discount = book.get().getDiscount() != null ? book.get().getDiscount() : 0.0;
                    double discountedPrice = price * (1 - discount / 100);
                    totalDiscount += quantity * price * (discount / 100);
                    totalPriceBeforeDiscount += quantity * price;
                    bill.append("  - Book: ").append(book.get().getTitle())
                        .append(" (Qty: ").append(quantity)
                        .append(", Price: Rs. ").append(String.format("%.2f", price))
                        .append(", Discount: ").append(String.format("%.2f", discount)).append("%")
                        .append(", Final Price: Rs. ").append(String.format("%.2f", discountedPrice * quantity))
                        .append(")\n");
                } else {
                    bill.append("  - Book ID: ").append(productId).append(" (Not found, Qty: ").append(quantity).append(")\n");
                }
            } else if (productType.equals("Accessory")) {
                Optional<Accessories> accessory = accessoriesRepository.findById(productId);
                if (accessory.isPresent()) {
                    double price = accessory.get().getPrice();
                    double discount = accessory.get().getDiscount() != null ? accessory.get().getDiscount() : 0.0;
                    double discountedPrice = price * (1 - discount / 100);
                    totalDiscount += quantity * price * (discount / 100);
                    totalPriceBeforeDiscount += quantity * price;
                    bill.append("  - Accessory: ").append(accessory.get().getItemName())
                        .append(" (Qty: ").append(quantity)
                        .append(", Price: Rs. ").append(String.format("%.2f", price))
                        .append(", Discount: ").append(String.format("%.2f", discount)).append("%")
                        .append(", Final Price: Rs. ").append(String.format("%.2f", discountedPrice * quantity))
                        .append(")\n");
                } else {
                    bill.append("  - Accessory ID: ").append(productId).append(" (Not found, Qty: ").append(quantity).append(")\n");
                }
            } else {
                bill.append("  - Product ID: ").append(productId).append(" (Unknown product type, Qty: ").append(quantity).append(")\n");
            }
        }

        bill.append("Total Price Before Discount: Rs. ").append(String.format("%.2f", totalPriceBeforeDiscount)).append("\n")
            .append("Total Discount: Rs. ").append(String.format("%.2f", totalDiscount)).append("\n")
            .append("Final Total Price: Rs. ").append(String.format("%.2f", order.get().getTotalPrice())).append("\n")
            .append("Status: ").append(order.get().getStatus()).append("\n")
            .append("Created At: ").append(order.get().getCreatedAt()).append("\n")
            .append("=============================\n");

        return bill.toString();
    }

    private void sendOrderStatusEmail(String email, String name, Orders order) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Pahana Edu - Order " + order.getStatus());
            helper.setFrom("manojprasanka8@gmail.com");

            String statusMessage = order.getStatus().equals("Confirmed") ?
                "Your order has been successfully confirmed. Thank you for shopping with us! You will be contacted by our delivery service soon." :
                "Your order has been canceled.Your money will be refunded within two days.Please contact support if you have any questions.";

            StringBuilder productDetails = new StringBuilder();
            double totalDiscount = 0.0;
            double totalPriceBeforeDiscount = 0.0;

            Map<String, String> productTypes = order.getProductTypes();
            Map<String, Integer> productQuantities = order.getProductQuantities();

            for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
                String productId = entry.getKey();
                int quantity = entry.getValue();
                String productType = productTypes.getOrDefault(productId, "Unknown");
                if (productType.equals("Book")) {
                    Optional<Book> book = bookRepository.findById(productId);
                    if (book.isPresent()) {
                        double price = book.get().getPrice();
                        double discount = book.get().getDiscount() != null ? book.get().getDiscount() : 0.0;
                        double discountedPrice = price * (1 - discount / 100);
                        totalDiscount += quantity * price * (discount / 100);
                        totalPriceBeforeDiscount += quantity * price;
                        productDetails.append("Book: ").append(book.get().getTitle())
                            .append(" (Qty: ").append(quantity)
                            .append(", Price: Rs. ").append(String.format("%.2f", price))
                            .append(", Discount: ").append(String.format("%.2f", discount)).append("%")
                            .append(", Final Price: Rs. ").append(String.format("%.2f", discountedPrice * quantity))
                            .append(")<br>");
                    } else {
                        productDetails.append("Book ID: ").append(productId).append(" (Not found, Qty: ").append(quantity).append(")<br>");
                    }
                } else if (productType.equals("Accessory")) {
                    Optional<Accessories> accessory = accessoriesRepository.findById(productId);
                    if (accessory.isPresent()) {
                        double price = accessory.get().getPrice();
                        double discount = accessory.get().getDiscount() != null ? accessory.get().getDiscount() : 0.0;
                        double discountedPrice = price * (1 - discount / 100);
                        totalDiscount += quantity * price * (discount / 100);
                        totalPriceBeforeDiscount += quantity * price;
                        productDetails.append("Accessory: ").append(accessory.get().getItemName())
                            .append(" (Qty: ").append(quantity)
                            .append(", Price: Rs. ").append(String.format("%.2f", price))
                            .append(", Discount: ").append(String.format("%.2f", discount)).append("%")
                            .append(", Final Price: Rs. ").append(String.format("%.2f", discountedPrice * quantity))
                            .append(")<br>");
                    } else {
                        productDetails.append("Accessory ID: ").append(productId).append(" (Not found, Qty: ").append(quantity).append(")<br>");
                    }
                } else {
                    productDetails.append("Product ID: ").append(productId).append(" (Unknown product type, Qty: ").append(quantity).append(")<br>");
                }
            }

            String htmlContent = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <style>
                        body {
                            margin: 0;
                            padding: 0;
                            font-family: 'Arial', sans-serif;
                            background-color: #f4f4f4;
                        }
                        .container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #ffffff;
                            border-radius: 8px;
                            overflow: hidden;
                            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                        }
                        .header {
                            background: linear-gradient(90deg, #FBBF24, #D97706);
                            padding: 20px;
                            text-align: center;
                        }
                        .header img {
                            max-width: 150px;
                            height: auto;
                        }
                        .content {
                            padding: 30px;
                            color: #333333;
                            line-height: 1.6;
                        }
                        .content h1 {
                            font-size: 24px;
                            color: #1F2937;
                            margin-bottom: 20px;
                        }
                        .content p {
                            font-size: 16px;
                            margin-bottom: 20px;
                        }
                        .button {
                            display: inline-block;
                            padding: 12px 24px;
                            background-color: #FBBF24;
                            color: #000000;
                            text-decoration: none;
                            border-radius: 5px;
                            font-weight: bold;
                            font-size: 16px;
                        }
                        .footer {
                            background-color: #1F2937;
                            color: #ffffff;
                            padding: 20px;
                            text-align: center;
                            font-size: 14px;
                        }
                        .footer a {
                            color: #FBBF24;
                            text-decoration: none;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                                <h1>Pahana Edu</h1>
                            </div>
                        <div class="content">
                            <h1>Order %s</h1>
                            <p>Dear %s,</p>
                            <p>%s</p>
                            <p><strong>Order Details:</strong></p>
                            <p>Order ID: %s</p>
                            <p>Order Date: %s</p>
                            <p>Customer Phone: %s</p>
                            <p>Products:<br>%s</p>
                            <p>Total Price Before Discount: Rs. %s</p>
                            <p>Total Discount: Rs. %s</p>
                            <p>Final Total Price: Rs. %s</p>
                            <p>Status: %s</p>
                        </div>
                        <div class="footer">
                            <p>Thank you for choosing Pahana Edu!</p>
                            <p>Need help? <a href="mailto:support@pahanaedu.com">Contact Support</a></p>
                            <p>© 2025 Pahana Edu. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(
                    order.getStatus(),
                    name,
                    statusMessage,
                    order.getId(),
                    order.getOrderDate(),
                    order.getCustomerPhone(),
                    productDetails.toString(),
                    String.format("%.2f", totalPriceBeforeDiscount),
                    String.format("%.2f", totalDiscount),
                    String.format("%.2f", order.getTotalPrice()),
                    order.getStatus(),
                    order.getId()
                );

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send order status email: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> viewBillAsJson(String orderId) {
        Optional<Orders> orderOptional = ordersRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new RuntimeException("Order not found with id: " + orderId);
        }
        Orders order = orderOptional.get();

        Optional<Customer> customerOptional = customerRepository.findById(order.getCustomerId());
        if (customerOptional.isEmpty()) {
            throw new RuntimeException("Customer not found for order id: " + orderId);
        }
        Customer customer = customerOptional.get();

        List<Map<String, Object>> products = new ArrayList<>();
        double totalDiscount = 0.0;
        double totalPriceBeforeDiscount = 0.0;

        Map<String, String> productTypes = order.getProductTypes();
        Map<String, Integer> productQuantities = order.getProductQuantities();

        for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();
            Map<String, Object> productDetails = new HashMap<>();
            String productType = productTypes.getOrDefault(productId, "Unknown");
            if (productType.equals("Book")) {
                Optional<Book> book = bookRepository.findById(productId);
                if (book.isPresent()) {
                    double price = book.get().getPrice();
                    double discount = book.get().getDiscount() != null ? book.get().getDiscount() : 0.0;
                    double discountedPrice = price * (1 - discount / 100);
                    totalDiscount += quantity * price * (discount / 100);
                    totalPriceBeforeDiscount += quantity * price;
                    productDetails.put("type", "Book");
                    productDetails.put("name", book.get().getTitle());
                    productDetails.put("quantity", quantity);
                    productDetails.put("price", price);
                    productDetails.put("discount", discount);
                    productDetails.put("finalPrice", discountedPrice * quantity);
                } else {
                    productDetails.put("type", "Book");
                    productDetails.put("name", "Book ID: " + productId + " (Not found)");
                    productDetails.put("quantity", quantity);
                    productDetails.put("price", 0.0);
                    productDetails.put("discount", 0.0);
                    productDetails.put("finalPrice", 0.0);
                }
            } else if (productType.equals("Accessory")) {
                Optional<Accessories> accessory = accessoriesRepository.findById(productId);
                if (accessory.isPresent()) {
                    double price = accessory.get().getPrice();
                    double discount = accessory.get().getDiscount() != null ? accessory.get().getDiscount() : 0.0;
                    double discountedPrice = price * (1 - discount / 100);
                    totalDiscount += quantity * price * (discount / 100);
                    totalPriceBeforeDiscount += quantity * price;
                    productDetails.put("type", "Accessory");
                    productDetails.put("name", accessory.get().getItemName());
                    productDetails.put("quantity", quantity);
                    productDetails.put("price", price);
                    productDetails.put("discount", discount);
                    productDetails.put("finalPrice", discountedPrice * quantity);
                } else {
                    productDetails.put("type", "Accessory");
                    productDetails.put("name", "Accessory ID: " + productId + " (Not found)");
                    productDetails.put("quantity", quantity);
                    productDetails.put("price", 0.0);
                    productDetails.put("discount", 0.0);
                    productDetails.put("finalPrice", 0.0);
                }
            } else {
                productDetails.put("type", "Unknown");
                productDetails.put("name", "Product ID: " + productId + " (Unknown product type)");
                productDetails.put("quantity", quantity);
                productDetails.put("price", 0.0);
                productDetails.put("discount", 0.0);
                productDetails.put("finalPrice", 0.0);
            }
            products.add(productDetails);
        }

        Map<String, Object> bill = new HashMap<>();
        bill.put("orderId", order.getId());
        bill.put("customerName", customer.getCustomerName());
        bill.put("customerEmail", customer.getCustomerEmail());
        bill.put("customerPhone", order.getCustomerPhone());
        bill.put("address", order.getAddress());
        bill.put("orderDate", order.getOrderDate());
        bill.put("products", products);
        bill.put("totalPriceBeforeDiscount", String.format("%.2f", totalPriceBeforeDiscount));
        bill.put("totalDiscount", String.format("%.2f", totalDiscount));
        bill.put("finalTotalPrice", String.format("%.2f", order.getTotalPrice()));
        bill.put("status", order.getStatus());
        bill.put("createdAt", order.getCreatedAt().toString());

        return bill;
    }

    @Override
    public byte[] generateBillAsPdf(String orderId) {
        Optional<Orders> orderOptional = ordersRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new RuntimeException("Order not found with id: " + orderId);
        }
        Orders order = orderOptional.get();

        Optional<Customer> customerOptional = customerRepository.findById(order.getCustomerId());
        if (customerOptional.isEmpty()) {
            throw new RuntimeException("Customer not found for order id: " + orderId);
        }
        Customer customer = customerOptional.get();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Pahana Edu - Order Bill")
                    .setFontSize(20)
                    .setBold());
            document.add(new Paragraph("Order ID: " + order.getId()));
            document.add(new Paragraph("Customer Name: " + customer.getCustomerName()));
            document.add(new Paragraph("Customer Email: " + customer.getCustomerEmail()));
            document.add(new Paragraph("Customer Phone: " + order.getCustomerPhone()));
            document.add(new Paragraph("Address: " + order.getAddress()));
            document.add(new Paragraph("Order Date: " + order.getOrderDate()));
            document.add(new Paragraph("Status: " + order.getStatus()));

            Table table = new Table(new float[]{50, 200, 100, 100, 100});
            table.addHeaderCell(new Cell().add(new Paragraph("Qty")));
            table.addHeaderCell(new Cell().add(new Paragraph("Product Name")));
            table.addHeaderCell(new Cell().add(new Paragraph("Price")));
            table.addHeaderCell(new Cell().add(new Paragraph("Discount (%)")));
            table.addHeaderCell(new Cell().add(new Paragraph("Final Price")));

            double totalDiscount = 0.0;
            double totalPriceBeforeDiscount = 0.0;

            Map<String, String> productTypes = order.getProductTypes();
            Map<String, Integer> productQuantities = order.getProductQuantities();

            for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
                String productId = entry.getKey();
                int quantity = entry.getValue();
                String productType = productTypes.getOrDefault(productId, "Unknown");
                if (productType.equals("Book")) {
                    Optional<Book> book = bookRepository.findById(productId);
                    if (book.isPresent()) {
                        double price = book.get().getPrice();
                        double discount = book.get().getDiscount() != null ? book.get().getDiscount() : 0.0;
                        double discountedPrice = price * (1 - discount / 100);
                        totalDiscount += quantity * price * (discount / 100);
                        totalPriceBeforeDiscount += quantity * price;
                        table.addCell(new Cell().add(new Paragraph(String.valueOf(quantity))));
                        table.addCell(new Cell().add(new Paragraph("Book: " + book.get().getTitle())));
                        table.addCell(new Cell().add(new Paragraph(String.format("Rs. %.2f", price))));
                        table.addCell(new Cell().add(new Paragraph(String.format("%.2f", discount))));
                        table.addCell(new Cell().add(new Paragraph(String.format("Rs. %.2f", discountedPrice * quantity))));
                    } else {
                        table.addCell(new Cell().add(new Paragraph(String.valueOf(quantity))));
                        table.addCell(new Cell().add(new Paragraph("Book ID: " + productId + " (Not found)")));
                        table.addCell(new Cell().add(new Paragraph("N/A")));
                        table.addCell(new Cell().add(new Paragraph("N/A")));
                        table.addCell(new Cell().add(new Paragraph("N/A")));
                    }
                } else if (productType.equals("Accessory")) {
                    Optional<Accessories> accessory = accessoriesRepository.findById(productId);
                    if (accessory.isPresent()) {
                        double price = accessory.get().getPrice();
                        double discount = accessory.get().getDiscount() != null ? accessory.get().getDiscount() : 0.0;
                        double discountedPrice = price * (1 - discount / 100);
                        totalDiscount += quantity * price * (discount / 100);
                        totalPriceBeforeDiscount += quantity * price;
                        table.addCell(new Cell().add(new Paragraph(String.valueOf(quantity))));
                        table.addCell(new Cell().add(new Paragraph("Accessory: " + accessory.get().getItemName())));
                        table.addCell(new Cell().add(new Paragraph(String.format("Rs. %.2f", price))));
                        table.addCell(new Cell().add(new Paragraph(String.format("%.2f", discount))));
                        table.addCell(new Cell().add(new Paragraph(String.format("Rs. %.2f", discountedPrice * quantity))));
                    } else {
                        table.addCell(new Cell().add(new Paragraph(String.valueOf(quantity))));
                        table.addCell(new Cell().add(new Paragraph("Accessory ID: " + productId + " (Not found)")));
                        table.addCell(new Cell().add(new Paragraph("N/A")));
                        table.addCell(new Cell().add(new Paragraph("N/A")));
                        table.addCell(new Cell().add(new Paragraph("N/A")));
                    }
                } else {
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(quantity))));
                    table.addCell(new Cell().add(new Paragraph("Product ID: " + productId + " (Unknown product type)")));
                    table.addCell(new Cell().add(new Paragraph("N/A")));
                    table.addCell(new Cell().add(new Paragraph("N/A")));
                    table.addCell(new Cell().add(new Paragraph("N/A")));
                }
            }

            document.add(table);

            document.add(new Paragraph("Total Price Before Discount: Rs. " + String.format("%.2f", totalPriceBeforeDiscount)));
            document.add(new Paragraph("Total Discount: Rs. " + String.format("%.2f", totalDiscount)));
            document.add(new Paragraph("Final Total Price: Rs. " + String.format("%.2f", order.getTotalPrice())));
            document.add(new Paragraph("Created At: " + order.getCreatedAt()));

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF bill: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getBestSellers(int limit) {
        List<Orders> orders = ordersRepository.findAll();
        Map<String, Integer> productCount = new HashMap<>();
        Map<String, String> productTypes = new HashMap<>();

        for (Orders order : orders) {
            Map<String, Integer> quantities = order.getProductQuantities() != null ? order.getProductQuantities() : new HashMap<>();
            Map<String, String> types = order.getProductTypes() != null ? order.getProductTypes() : new HashMap<>();
            for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
                String productId = entry.getKey();
                int quantity = entry.getValue();
                productCount.put(productId, productCount.getOrDefault(productId, 0) + quantity);
                productTypes.put(productId, types.getOrDefault(productId, "Unknown"));
            }
        }

        List<Map.Entry<String, Integer>> sortedProducts = productCount.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(limit)
            .toList();

        List<Map<String, Object>> bestSellers = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedProducts) {
            String productId = entry.getKey();
            int quantity = entry.getValue();
            String productType = productTypes.getOrDefault(productId, "Unknown");
            Map<String, Object> productDetails = new HashMap<>();

            if (productType.equals("Book")) {
                Optional<Book> book = bookRepository.findById(productId);
                if (book.isPresent()) {
                    productDetails.put("type", "Book");
                    productDetails.put("id", book.get().getBookId());
                    productDetails.put("title", book.get().getTitle());
                    productDetails.put("price", book.get().getPrice());
                    productDetails.put("discount", book.get().getDiscount() != null ? book.get().getDiscount() : 0.0);
                    productDetails.put("image", book.get().getImage());
                    productDetails.put("status", book.get().getStatus());
                    productDetails.put("quantitySold", quantity);
                }
            } else if (productType.equals("Accessory")) {
                Optional<Accessories> accessory = accessoriesRepository.findById(productId);
                if (accessory.isPresent()) {
                    productDetails.put("type", "Accessory");
                    productDetails.put("id", accessory.get().getId());
                    productDetails.put("title", accessory.get().getItemName());
                    productDetails.put("price", accessory.get().getPrice());
                    productDetails.put("discount", accessory.get().getDiscount() != null ? accessory.get().getDiscount() : 0.0);
                    productDetails.put("image", accessory.get().getImage());
                    productDetails.put("status", accessory.get().getStatus());
                    productDetails.put("quantitySold", quantity);
                }
            }
            if (!productDetails.isEmpty()) {
                bestSellers.add(productDetails);
            }
        }

        return bestSellers;
    }
}
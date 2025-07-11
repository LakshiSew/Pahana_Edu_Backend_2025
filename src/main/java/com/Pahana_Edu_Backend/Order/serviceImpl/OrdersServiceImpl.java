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
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setStatus("Pending");
        return ordersRepository.save(order);
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

            // Send email notification
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
        if (order.isPresent()) {
            Optional<Customer> customer = customerRepository.findById(order.get().getCustomerId());
            if (customer.isPresent()) {
                StringBuilder bill = new StringBuilder();
                bill.append("=== Pahana Edu Order Bill ===\n");
                bill.append("Order ID: ").append(order.get().getId()).append("\n");
                bill.append("Customer Name: ").append(customer.get().getCustomerName()).append("\n");
                bill.append("Customer Email: ").append(customer.get().getCustomerEmail()).append("\n");
                bill.append("Address: ").append(order.get().getAddress()).append("\n");
                bill.append("Order Date: ").append(order.get().getOrderDate()).append("\n");
                bill.append("Product Type: ").append(order.get().getProductType()).append("\n");
                bill.append("Products:\n");

                double totalDiscount = 0.0;
                double totalPriceBeforeDiscount = 0.0;

                // Fetch product details and calculate discounts
                for (String productId : order.get().getProductIds()) {
                    if (order.get().getProductType().equals("Book")) {
                        Optional<Book> book = bookRepository.findById(productId);
                        if (book.isPresent()) {
                            double price = book.get().getPrice();
                            double discount = book.get().getDiscount() != null ? book.get().getDiscount() : 0.0;
                            double discountedPrice = price * (1 - discount / 100);
                            totalDiscount += price * (discount / 100);
                            totalPriceBeforeDiscount += price;
                            bill.append("  - ").append(book.get().getTitle())
                                .append(" (Price: $").append(String.format("%.2f", price))
                                .append(", Discount: ").append(discount).append("%")
                                .append(", Final Price: $").append(String.format("%.2f", discountedPrice))
                                .append(")\n");
                        } else {
                            bill.append("  - Product ID: ").append(productId).append(" (Not found)\n");
                        }
                    } else if (order.get().getProductType().equals("Accessory")) {
                        Optional<Accessories> accessory = accessoriesRepository.findById(productId);
                        if (accessory.isPresent()) {
                            double price = accessory.get().getPrice();
                            double discount = accessory.get().getDiscount() != null ? accessory.get().getDiscount() : 0.0;
                            double discountedPrice = price * (1 - discount / 100);
                            totalDiscount += price * (discount / 100);
                            totalPriceBeforeDiscount += price;
                            bill.append("  - ").append(accessory.get().getItemName())
                                .append(" (Price: $").append(String.format("%.2f", price))
                                .append(", Discount: ").append(discount).append("%")
                                .append(", Final Price: $").append(String.format("%.2f", discountedPrice))
                                .append(")\n");
                        } else {
                            bill.append("  - Product ID: ").append(productId).append(" (Not found)\n");
                        }
                    }
                }

                bill.append("Total Price Before Discount: $").append(String.format("%.2f", totalPriceBeforeDiscount)).append("\n");
                bill.append("Total Discount: $").append(String.format("%.2f", totalDiscount)).append("\n");
                bill.append("Final Total Price: $").append(String.format("%.2f", order.get().getTotalPrice())).append("\n");
                bill.append("Status: ").append(order.get().getStatus()).append("\n");
                bill.append("Created At: ").append(order.get().getCreatedAt()).append("\n");
                bill.append("=============================\n");
                return bill.toString();
            } else {
                throw new RuntimeException("Customer not found for order id: " + orderId);
            }
        } else {
            throw new RuntimeException("Order not found with id: " + orderId);
        }
    }

    private void sendOrderStatusEmail(String email, String name, Orders order) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Pahana Edu - Order " + order.getStatus());
            helper.setFrom("manojprasanka8@gmail.com");

            String statusMessage = order.getStatus().equals("Confirmed") ?
                "Your order has been successfully confirmed. Thank you for shopping with us!" :
                "Your order has been canceled. Please contact support if you have any questions.";

            StringBuilder productDetails = new StringBuilder();
            double totalDiscount = 0.0;
            double totalPriceBeforeDiscount = 0.0;

            for (String productId : order.getProductIds()) {
                if (order.getProductType().equals("Book")) {
                    Optional<Book> book = bookRepository.findById(productId);
                    if (book.isPresent()) {
                        double price = book.get().getPrice();
                        double discount = book.get().getDiscount() != null ? book.get().getDiscount() : 0.0;
                        double discountedPrice = price * (1 - discount / 100);
                        totalDiscount += price * (discount / 100);
                        totalPriceBeforeDiscount += price;
                        productDetails.append(book.get().getTitle())
                            .append(" (Price: $").append(String.format("%.2f", price))
                            .append(", Discount: ").append(discount).append("%")
                            .append(", Final Price: $").append(String.format("%.2f", discountedPrice))
                            .append(")<br>");
                    } else {
                        productDetails.append("Product ID: ").append(productId).append(" (Not found)<br>");
                    }
                } else if (order.getProductType().equals("Accessory")) {
                    Optional<Accessories> accessory = accessoriesRepository.findById(productId);
                    if (accessory.isPresent()) {
                        double price = accessory.get().getPrice();
                        double discount = accessory.get().getDiscount() != null ? accessory.get().getDiscount() : 0.0;
                        double discountedPrice = price * (1 - discount / 100);
                        totalDiscount += price * (discount / 100);
                        totalPriceBeforeDiscount += price;
                        productDetails.append(accessory.get().getItemName())
                            .append(" (Price: $").append(String.format("%.2f", price))
                            .append(", Discount: ").append(discount).append("%")
                            .append(", Final Price: $").append(String.format("%.2f", discountedPrice))
                            .append(")<br>");
                    } else {
                        productDetails.append("Product ID: ").append(productId).append(" (Not found)<br>");
                    }
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
                            <img src="https://via.placeholder.com/150x50?text=Pahana+Edu" alt="Pahana Edu Logo" />
                        </div>
                        <div class="content">
                            <h1>Order %s</h1>
                            <p>Dear %s,</p>
                            <p>%s</p>
                            <p><strong>Order Details:</strong></p>
                            <p>Order ID: %s</p>
                            <p>Order Date: %s</p>
                            <p>Product Type: %s</p>
                            <p>Products:<br>%s</p>
                            <p>Total Price Before Discount: $%s</p>
                            <p>Total Discount: $%s</p>
                            <p>Final Total Price: $%s</p>
                            <p>Status: %s</p>
                            <p><a href="https://pahanaedu.com/orders/%s" class="button">View Order</a></p>
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
                    order.getProductType(),
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
}
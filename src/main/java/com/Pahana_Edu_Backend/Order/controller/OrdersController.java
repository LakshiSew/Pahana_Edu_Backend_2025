// package com.Pahana_Edu_Backend.Order.controller;

// import com.Pahana_Edu_Backend.Order.entity.Orders;
// import com.Pahana_Edu_Backend.Order.service.OrdersService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.http.MediaType;
// import org.springframework.http.HttpHeaders;
// import org.springframework.web.bind.annotation.*;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;

// @RestController
// @CrossOrigin
// public class OrdersController {

//     @Autowired 
//     private OrdersService ordersService;

//     @PostMapping("/auth/create")
//     public ResponseEntity<Orders> createOrder(@RequestBody Orders order) {
//         Orders createdOrder = ordersService.createOrder(order);
//         return ResponseEntity.ok(createdOrder);
//     }

//     @GetMapping("/getorderbyid/{orderId}")
//     public ResponseEntity<?> getOrderById(@PathVariable String orderId) {
//         Optional<Orders> order = ordersService.getOrderById(orderId);
//         if (order.isPresent()) {
//             return ResponseEntity.ok(order.get());
//         }
//         return ResponseEntity.status(404).body("Order not found");
//     }

//     @GetMapping("/allorders")
//     public ResponseEntity<List<Orders>> getAllOrders() {
//         return ResponseEntity.ok(ordersService.getAllOrders());
//     }

//     @GetMapping("/getordersbycustomerid/{customerId}")
//     public ResponseEntity<List<Orders>> getOrdersByCustomerId(@PathVariable String customerId) {
//         return ResponseEntity.ok(ordersService.getOrdersByCustomerId(customerId));
//     }

//     @GetMapping("/getordersbycustomerphone/{customerPhone}")
//     public ResponseEntity<List<Orders>> getOrdersByCustomerPhone(@PathVariable String customerPhone) {
//         return ResponseEntity.ok(ordersService.getOrdersByCustomerPhone(customerPhone));
//     }

//     @PutMapping("/update-status/{orderId}")
//     public ResponseEntity<?> updateOrderStatus(@PathVariable String orderId, @RequestParam String status) {
//         try {
//             Orders updatedOrder = ordersService.updateOrderStatus(orderId, status);
//             return ResponseEntity.ok(updatedOrder);
//         } catch (Exception e) {
//             return ResponseEntity.status(400).body(e.getMessage());
//         }
//     }

//     @DeleteMapping("/delete/{orderId}")
//     public ResponseEntity<?> deleteOrder(@PathVariable String orderId) {
//         try {
//             ordersService.deleteOrder(orderId);
//             return ResponseEntity.noContent().build();
//         } catch (Exception e) {
//             return ResponseEntity.status(404).body("Order not found");
//         }
//     }

//     @GetMapping("/generatebill/{orderId}")
//     public ResponseEntity<String> generateBill(@PathVariable String orderId) {
//         try {
//             String bill = ordersService.generateBill(orderId);
//             return ResponseEntity.ok(bill);
//         } catch (Exception e) {
//             return ResponseEntity.status(404).body(e.getMessage());
//         }
//     }

//     @GetMapping(value = "/generatebillpdf/{orderId}", produces = MediaType.APPLICATION_PDF_VALUE)
//     public ResponseEntity<byte[]> generateBillPdf(@PathVariable String orderId) {
//         try {
//             byte[] pdfBytes = ordersService.generateBillAsPdf(orderId);
//             HttpHeaders headers = new HttpHeaders();
//             headers.setContentType(MediaType.APPLICATION_PDF);
//             headers.setContentDispositionFormData("attachment", "bill_" + orderId + ".pdf");
//             return ResponseEntity.ok().headers(headers).body(pdfBytes);
//         } catch (Exception e) {
//             return ResponseEntity.status(404).body(null);
//         }
//     }

//         @GetMapping("/viewbill/{orderId}")
//     public ResponseEntity<?> viewBill(@PathVariable String orderId) {
//         try {
//             Map<String, Object> bill = ordersService.viewBillAsJson(orderId);
//             return ResponseEntity.ok(bill);
//         } catch (Exception e) {
//             return ResponseEntity.status(404).body(e.getMessage());
//         }
//     }
// }





package com.Pahana_Edu_Backend.Order.controller;

import com.Pahana_Edu_Backend.Order.entity.Orders;
import com.Pahana_Edu_Backend.Order.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
public class OrdersController {

    @Autowired
    private OrdersService ordersService;


    @PostMapping("/auth/create")
    public ResponseEntity<?> createOrder(@RequestBody Orders order) {
        try {
            Orders createdOrder = ordersService.createOrder(order);
            return ResponseEntity.ok(createdOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/getorderbyid/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable String orderId) {
        Optional<Orders> order = ordersService.getOrderById(orderId);
        if (order.isPresent()) {
            return ResponseEntity.ok(order.get());
        }
        return ResponseEntity.status(404).body("Order not found");
    }

    @GetMapping("/allorders")
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok(ordersService.getAllOrders());
    }

    @GetMapping("/getordersbycustomerid/{customerId}")
    public ResponseEntity<List<Orders>> getOrdersByCustomerId(@PathVariable String customerId) {
        return ResponseEntity.ok(ordersService.getOrdersByCustomerId(customerId));
    }

    @GetMapping("/getordersbycustomerphone/{customerPhone}")
    public ResponseEntity<List<Orders>> getOrdersByCustomerPhone(@PathVariable String customerPhone) {
        return ResponseEntity.ok(ordersService.getOrdersByCustomerPhone(customerPhone));
    }

    @PutMapping("/update-status/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String orderId, @RequestParam String status) {
        try {
            Orders updatedOrder = ordersService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable String orderId) {
        try {
            ordersService.deleteOrder(orderId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Order not found");
        }
    }

    @GetMapping("/generatebill/{orderId}")
    public ResponseEntity<String> generateBill(@PathVariable String orderId) {
        try {
            String bill = ordersService.generateBill(orderId);
            return ResponseEntity.ok(bill);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping(value = "/generatebillpdf/{orderId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateBillPdf(@PathVariable String orderId) {
        try {
            byte[] pdfBytes = ordersService.generateBillAsPdf(orderId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "bill_" + orderId + ".pdf");
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/viewbill/{orderId}")
    public ResponseEntity<?> viewBill(@PathVariable String orderId) {
        try {
            Map<String, Object> bill = ordersService.viewBillAsJson(orderId);
            return ResponseEntity.ok(bill);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

     @GetMapping("/auth/getbestsellers")
    public ResponseEntity<List<Map<String, Object>>> getBestSellers() {
        return ResponseEntity.ok(ordersService.getBestSellers(10));
    }

    
}


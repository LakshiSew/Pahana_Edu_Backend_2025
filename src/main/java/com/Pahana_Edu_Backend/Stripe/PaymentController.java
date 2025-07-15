// package com.Pahana_Edu_Backend.Stripe;

// import com.stripe.model.PaymentIntent;
// import com.stripe.param.PaymentIntentCreateParams;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;
// import java.util.Map;

// @RestController
// @RequestMapping("/payment")
// public class PaymentController {

//     @Autowired
//     private StripeService stripeService;

//     @PostMapping("/create-payment-intent")

// public Map<String, String> createPaymentIntent(@RequestBody Map<String, Object> data) {
//     long amount = Long.parseLong(data.get("amount").toString());
//     String currency = data.get("currency").toString();
//     try {
//         PaymentIntent intent = stripeService.createPaymentIntent(amount, currency);
//         return Map.of("clientSecret", intent.getClientSecret());
//     } catch (Exception e) {
//         return Map.of("error", e.getMessage());
//     }
// }
// }

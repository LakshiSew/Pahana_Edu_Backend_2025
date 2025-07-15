// package com.Pahana_Edu_Backend.Stripe;

// import com.stripe.Stripe;
// import com.stripe.model.PaymentIntent;
// import com.stripe.param.PaymentIntentCreateParams;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;


// import java.util.Arrays;

// @Service
// public class StripeService {

//     @Value("${stripe.api.key}")
//     private String stripeApiKey;

//     public StripeService() {
//         Stripe.apiKey = stripeApiKey;
//     }

//     public PaymentIntent createPaymentIntent(long amount) throws Exception {
//         PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
//                 .setAmount(amount) // Set the amount in the smallest currency unit (cents)
//                 .setCurrency("usd")
//                 .addPaymentMethodType("card") // Use this instead of setPaymentMethodTypes
//                 .build();

//         // Create the payment intent
//         return PaymentIntent.create(params);
//     }
// }

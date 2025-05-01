package com.exactpay.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentController {

    @GetMapping("/confirmSuccess")
    public String confirmPaymentSuccess(@RequestParam String paymentId, 
                               @RequestParam(required = false, defaultValue = "success") String paymentStatus,
                               @RequestParam(required = false) String errorMessage,
                               @RequestParam(required = false) String amount,
                               Model model) {
        // Log the received parameters
        System.out.println("Confirm Payment - Parameters:");
        System.out.println("Payment ID: " + paymentId);
        System.out.println("Payment Status: " + paymentStatus);
        System.out.println("Amount: " + amount);
        System.out.println("Error Message: " + errorMessage);

        // Add parameters to the model
        model.addAttribute("paymentId", paymentId);
        model.addAttribute("paymentStatus", paymentStatus);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("amount", amount != null ? amount : "0.00");

        return "confirm";
    }

    @GetMapping("/confirmFailure")
    public String confirmPaymentFailure(@RequestParam(required = false, defaultValue = "failed") String paymentStatus,
                                        @RequestParam(required = false) String errorMessage,
                                        Model model) {
        
        System.out.println("Confirm Payment - Parameters:");
                                            
        System.out.println("Payment Status: " + paymentStatus);
        System.out.println("Error Message: " + errorMessage);                                    model.addAttribute("paymentStatus", paymentStatus);
        model.addAttribute("errorMessage", errorMessage);
        return "confirmFail";

    }
} 
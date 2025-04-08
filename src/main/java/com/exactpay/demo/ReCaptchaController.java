package com.exactpay.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ReCaptchaController {

    @Value("${google.recaptcha.secret}")
    private String recaptchaSecret;

    @PostMapping("/verify-recaptcha")
    public ResponseEntity<Map<String, Object>> verifyRecaptcha(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String token = request.get("token");
        
        System.out.println("reCAPTCHA verification requested with token length: " + 
                           (token != null ? token.length() : "null"));
        
        try {
            // Create the request to Google's reCAPTCHA API
            RestTemplate restTemplate = new RestTemplate();
            String verifyUrl = "https://www.google.com/recaptcha/api/siteverify";
            
            // Build the request parameters
            String params = "?secret=" + recaptchaSecret + "&response=" + token;
            
            System.out.println("Making reCAPTCHA verification request to Google...");
            
            // Make the request to Google
            Map<String, Object> googleResponse = restTemplate.getForObject(verifyUrl + params, Map.class);
            
            System.out.println("Google reCAPTCHA response received: " + googleResponse);
            
            // Check if the verification was successful
            boolean success = googleResponse != null && googleResponse.containsKey("success") ? 
                             (Boolean) googleResponse.get("success") : false;
            
            float score = googleResponse != null && googleResponse.containsKey("score") ? 
                         ((Number) googleResponse.get("score")).floatValue() : 0;
            
            // You can use the score to determine the risk level (0.0 is likely a bot, 1.0 is likely human)
            // For production, you might want to set a threshold, e.g., 0.5
            boolean isHuman = score >= 0.5;
            
            response.put("success", success && isHuman);
            response.put("score", score);
            
            System.out.println("reCAPTCHA verification result: success=" + (success && isHuman) + 
                              ", score=" + score);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error during reCAPTCHA verification: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 
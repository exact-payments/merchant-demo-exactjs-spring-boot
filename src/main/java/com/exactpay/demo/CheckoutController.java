package com.exactpay.demo;

import com.exactpay.demo.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Controller
public class CheckoutController {

    @Value("${csipay.url}")
    private String csipayURL;

    @Value("${csipayjs.url}")
    private String csipayjsUrl;

    @Value("${security.token}")
    private String securityToken;

    @Value("${account.terminalId}")
    private String terminalId;

    @Value("${account.paymentFlow}")
    private String paymentFlow;
    
    @Value("${recaptcha.enabled:false}")
    private boolean recaptchaEnabled;
    
    @Value("${recaptcha.site-key:}")
    private String recaptchaSiteKey;
    
    private final RestTemplate restTemplate;
    
    @Autowired
    public CheckoutController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/checkout")
    public String doCheckout(Model model, @RequestParam String amountStr) {
        System.out.println("Order amount: " + amountStr);
        System.out.println("Terminal Id: " + terminalId);
        System.out.println("Payment Flow: " + paymentFlow);

        try {
            int amount = Integer.parseInt(amountStr);
            
            Details details = new Details(amount, terminalId);
            OrderRequest orderRequest = new OrderRequest(paymentFlow, details);
            
            // Use ObjectMapper to convert the request to JSON string for logging
            try {
                String requestJson = JsonUtils.toJson(orderRequest);
                System.out.println("Request JSON: " + requestJson);
            } catch (JsonProcessingException e) {
                System.err.println("Error serializing request: " + e.getMessage());
            }
            
            String url = csipayURL + "orders";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + securityToken);
            headers.set("Accept", "application/json");
            headers.set("Content-Type", "application/json");

            HttpEntity<OrderRequest> request = new HttpEntity<>(orderRequest, headers);
            System.out.println("Request Body: " + request.getBody().getPaymentFlow());
            
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
            
            System.out.println("Status Code: " + responseEntity.getStatusCode());
            System.out.println("Response Body: " + responseEntity.getBody());
            
            // Use ObjectMapper to convert the response JSON string to Order object
            Order order;
            try {
                order = JsonUtils.fromJson(responseEntity.getBody(), Order.class);
                System.out.println("Access Token: " + order.getAccessToken());
                System.out.println("Order Id: " + order.getOrderId());
                
                String accessToken = order.getAccessToken().getToken();
                System.out.println("access token: " + accessToken);
                System.out.println("Order id: " + order.getOrderId());

                model.addAttribute("csipayjsUrl", csipayjsUrl);
                
                model.addAttribute("orderid", order.getOrderId());
                model.addAttribute("accesstoken", accessToken);
                model.addAttribute("amount", "$" + (amount / 100.0));
                
                // Add reCAPTCHA configuration to the model
                model.addAttribute("enableRecaptcha", recaptchaEnabled);
                model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
                
                return "pay";
            } catch (IOException e) {
                System.err.println("Error deserializing response: " + e.getMessage());
                throw new RuntimeException("Failed to process payment response", e);
            }
        } catch (Exception e) {
            System.err.println("Error processing checkout: " + e.getMessage());
            throw new RuntimeException("Failed to process checkout", e);
        }
    }

   // @GetMapping("/confirm")
   // public String doComplete(Model model, @RequestParam String paymentId) {
   //     model.addAttribute("paymentId", paymentId);
   //     return "confirm";
   // }
}

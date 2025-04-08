package com.exactpay.demo;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Controller
public class CheckoutController {

    @Value("${csipay.url}")
    private String csipayURL;

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

    @GetMapping("/checkout")
    public String doCheckout(Model model, @RequestParam String amountStr) {
        System.out.println("Order amount: "+amountStr);
        System.out.println("Terminal Id: "+terminalId);
        System.out.println("Payment Flow: "+paymentFlow);

        int amount = Integer.parseInt(amountStr);
        
        OrderRequest orderRequest = new OrderRequest(amount, terminalId, paymentFlow);
        System.out.println(orderRequest.toString());
        String url = csipayURL+"orders";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+securityToken);
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");

        HttpEntity<OrderRequest> request = new HttpEntity<OrderRequest>(orderRequest, headers);
        System.out.println("Request Body:"+request.getBody().getPaymentFlow());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Order> response = restTemplate.postForEntity(url,request,Order.class);
        System.out.println("Status Code: "+response.getStatusCode());
        System.out.println("Response Body:"+response.getBody().toString());
        System.out.println("Access Token:"+response.getBody().getAccessToken());
        System.out.println("Order Id:"+response.getBody().getOrderId());
        
        Order order = response.getBody();
        String accessToken = order.getAccessToken().getToken();
        System.out.println("access token: "+accessToken);
        System.out.println("Order id: "+order.getOrderId());
        model.addAttribute("orderid",order.getOrderId());
        model.addAttribute("accesstoken", accessToken);
        model.addAttribute("amount","$"+Integer.parseInt(amountStr)/100);
        
        // Add reCAPTCHA configuration to the model
        model.addAttribute("enableRecaptcha", recaptchaEnabled);
        model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
        
        return "pay";
    }

    @GetMapping("/confirm")
    public String doComplete(Model model, @RequestParam String paymentId) {
        model.addAttribute("paymentId",paymentId);
        return "confirm";
    }
}

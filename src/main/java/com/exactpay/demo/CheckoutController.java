package com.exactpay.demo;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.util.UUID;

@Controller
public class CheckoutController {

    @Value("${exact.url}")
    private String exactpayURL;

    @Value("${application.token}")
    private String appToken;

    @Value("${account.id}")
    private String accountId;

    @GetMapping("/checkout")
    public String doCheckout(Model model, @RequestParam String amountStr) {
        System.out.println("Order amount: "+amountStr);
        UUID uuid = UUID.randomUUID();
        String referenceNo = uuid.toString();

        int amount = Integer.parseInt(amountStr);
        Reference reference = new Reference(referenceNo);

        OrderRequest orderRequest = new OrderRequest(amount, reference);
        System.out.println(orderRequest.toString());
        System.out.println("Token: "+appToken);
        String url = "https://api.exactpaysandbox.com/account/"+accountId+"/orders";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", appToken);
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");

        HttpEntity<OrderRequest> request = new HttpEntity<OrderRequest>(orderRequest, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Order> response = restTemplate.postForEntity(url,request,Order.class);
        System.out.println(response.getStatusCode());
        Order order = response.getBody();
        String accessToken = order.getAccessToken().getToken();
        System.out.println("access token: "+accessToken);
        System.out.println("Order id: "+order.getId());
        model.addAttribute("orderid",order.getId());
        model.addAttribute("accesstoken", accessToken);
        model.addAttribute("amount","$"+Integer.parseInt(amountStr)/100);
        return "pay";
    }

    @GetMapping("/confirm")
    public String doComplete(Model model, @RequestParam String paymentId) {
        model.addAttribute("paymentId",paymentId);
        return "confirm";
    }
}

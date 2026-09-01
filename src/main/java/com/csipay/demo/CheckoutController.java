package com.csipay.demo;

import com.csipay.demo.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Locale;
import java.util.List;

@Controller
public class CheckoutController {

    // Accepted by POST /orders; anything else is rejected by Pyxis with a 400.
    public static final List<String> PAYMENT_FLOWS = List.of(
            "sale", "authorize", "tokenize", "sale-and-tokenize", "auth-and-tokenize");

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
    public String doCheckout(Model model,
            @RequestParam String amountStr,
            @RequestParam(required = false) String paymentFlow,
            @RequestParam(required = false) String terminalId,
            @RequestParam(required = false) Integer feeAmount,
            @RequestParam(required = false) Boolean storeOnly,
            @RequestParam(required = false) Boolean oneTime,
            @RequestParam(required = false) Boolean recurring,
            @RequestParam(required = false) Boolean allowPartialApprovals) {

        String flow = (paymentFlow == null || paymentFlow.isBlank()) ? this.paymentFlow : paymentFlow;
        if (!PAYMENT_FLOWS.contains(flow)) {
            model.addAttribute("orderError",
                    "paymentFlow must be one of " + PAYMENT_FLOWS + " but was '" + flow + "'");
            return "orderError";
        }
        String lane = (terminalId == null || terminalId.isBlank()) ? this.terminalId : terminalId;

        System.out.println("Order amount: " + amountStr);
        System.out.println("Terminal Id: " + lane);
        System.out.println("Payment Flow: " + flow);

        String requestJson = "";
        try {
            int amount = Integer.parseInt(amountStr);

            Details details = new Details(amount, lane);
            details.setFeeAmount(feeAmount);
            details.setStoreOnly(storeOnly);
            details.setOneTime(oneTime);
            details.setRecurring(recurring);
            details.setAllowPartialApprovals(allowPartialApprovals);
            OrderRequest orderRequest = new OrderRequest(flow, details);

            try {
                requestJson = JsonUtils.toPrettyJson(orderRequest);
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

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);

            System.out.println("Status Code: " + responseEntity.getStatusCode());
            System.out.println("Response Body: " + responseEntity.getBody());

            Order order;
            try {
                order = JsonUtils.fromJson(responseEntity.getBody(), Order.class);

                String accessToken = order.getAccessToken().getToken();
                System.out.println("access token: " + accessToken);
                System.out.println("Order id: " + order.getOrderId());

                model.addAttribute("csipayjsUrl", csipayjsUrl);

                model.addAttribute("orderid", order.getOrderId());
                model.addAttribute("accesstoken", accessToken);
                model.addAttribute("amount", String.format(Locale.US, "$%,.2f", amount / 100.0));

                // Surfaced by pay.html's debug panel so a tester can see exactly
                // what was sent and returned for the selected flow.
                model.addAttribute("paymentFlow", order.getPaymentFlow() != null ? order.getPaymentFlow() : flow);
                model.addAttribute("orderStatus", order.getOrderStatus());
                model.addAttribute("orderUrl", url);
                model.addAttribute("processUrl", csipayURL + "orders/" + order.getOrderId() + "/process");
                model.addAttribute("requestJson", requestJson);
                model.addAttribute("responseJson", JsonUtils.prettyPrint(responseEntity.getBody()));

                // Add reCAPTCHA configuration to the model
                model.addAttribute("enableRecaptcha", recaptchaEnabled);
                model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);

                return "pay";
            } catch (IOException e) {
                System.err.println("Error deserializing response: " + e.getMessage());
                throw new RuntimeException("Failed to process payment response", e);
            }
        } catch (HttpStatusCodeException e) {
            // Pyxis rejects invalid param combinations (e.g. feeAmount > totalAmount);
            // show its message so the tester can see which param was refused.
            System.err.println("Order rejected: " + e.getResponseBodyAsString());
            model.addAttribute("orderError",
                    e.getStatusCode() + "\n" + JsonUtils.prettyPrint(e.getResponseBodyAsString()));
            model.addAttribute("requestJson", requestJson);
            return "orderError";
        } catch (Exception e) {
            System.err.println("Error processing checkout: " + e.getMessage());
            throw new RuntimeException("Failed to process checkout", e);
        }
    }
}

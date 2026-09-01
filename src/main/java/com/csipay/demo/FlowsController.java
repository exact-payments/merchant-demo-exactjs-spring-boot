package com.csipay.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FlowsController {

    @Value("${csipay.url}")
    private String csipayUrl;

    @Value("${account.terminalId}")
    private String terminalId;

    @Value("${account.paymentFlow}")
    private String defaultPaymentFlow;

    @GetMapping({ "/", "/flows" })
    public String flows(Model model) {
        model.addAttribute("paymentFlows", CheckoutController.PAYMENT_FLOWS);
        model.addAttribute("defaultPaymentFlow",
                CheckoutController.PAYMENT_FLOWS.contains(defaultPaymentFlow) ? defaultPaymentFlow : "sale");
        model.addAttribute("terminalId", terminalId);
        model.addAttribute("csipayUrl", csipayUrl);
        return "flows";
    }
}

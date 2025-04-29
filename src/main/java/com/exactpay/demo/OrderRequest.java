package com.exactpay.demo;

public class OrderRequest {

    private String paymentFlow;
    private Details details;

    public String getPaymentFlow() {
        return paymentFlow;
    }

    public void setPaymentFlow(String paymentFlow) {
        this.paymentFlow = paymentFlow;
    }
    
    public Details getDetails() {
        return details;
    }
    
    public void setDetails(Details details) {
        this.details = details;
    }
    
    public OrderRequest(String paymentFlow, Details details) {
        this.paymentFlow = paymentFlow;
        this.details = details;
    }
}

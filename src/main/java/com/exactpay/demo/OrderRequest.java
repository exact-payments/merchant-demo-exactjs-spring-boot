package com.exactpay.demo;

public class OrderRequest {

    private int totalAmount;
    private String terminalId;
    private String paymentFlow;

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int amount) {
        this.totalAmount = totalAmount;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getPaymentFlow() {
        return paymentFlow;
    }

    public void setPaymentFlow(String paymentFlow) {
        this.paymentFlow = paymentFlow;
    }
    public OrderRequest(int totalAmount,String terminalId, String paymentFlow) {
        this.totalAmount = totalAmount;
        this.paymentFlow = paymentFlow;
        this.terminalId = terminalId;
    }
}

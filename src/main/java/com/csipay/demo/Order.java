package com.csipay.demo;

public class Order {

    private String orderId;
    private AccessToken accessToken;
    private String orderStatus;
    private String paymentFlow;
    private String generatedToken;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String id) {
        this.orderId = id;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentFlow() {
        return paymentFlow;
    }

    public void setPaymentFlow(String paymentFlow) {
        this.paymentFlow = paymentFlow;
    }

    public String getGeneratedToken() {
        return generatedToken;
    }

    public void setGeneratedToken(String generatedToken) {
        this.generatedToken = generatedToken;
    }
}

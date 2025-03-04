package com.exactpay.demo;

public class Order {

    private String orderId;
    private AccessToken accessToken;

    public String getOrderId() {
        return orderId;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public void setOrderId(String id) {
        this.orderId = id;
    }
}

package com.exactpay.demo;

public class Order {

    private String id;
    private AccessToken accessToken;

    public String getId() {
        return id;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public void setId(String id) {
        this.id = id;
    }
}

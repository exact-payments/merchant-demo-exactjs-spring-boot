package com.exactpay.demo;

public class OrderRequest {

    private int amount;
    private Reference reference;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

    public OrderRequest(int amount, Reference reference) {
        this.amount = amount;
        this.reference = reference;
    }
}

package com.exactpay.demo;

public class Details {

    private int totalAmount;
    private String terminalId;

    public int getTotalAmount() {
        return totalAmount;
    }

    public Details(int totalAmount, String terminalId) {
        this.totalAmount = totalAmount;
        this.terminalId = terminalId;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }
    
}

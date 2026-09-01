package com.csipay.demo;

import com.fasterxml.jackson.annotation.JsonInclude;

// Nulls omitted so unset params fall back to Pyxis defaults.
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Details {

    private int totalAmount;
    private String terminalId;
    private Integer feeAmount;
    private Boolean storeOnly;
    private Boolean oneTime;
    private Boolean recurring;
    private Boolean allowPartialApprovals;

    public Details(int totalAmount, String terminalId) {
        this.totalAmount = totalAmount;
        this.terminalId = terminalId;
    }

    public int getTotalAmount() {
        return totalAmount;
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

    public Integer getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Integer feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Boolean getStoreOnly() {
        return storeOnly;
    }

    public void setStoreOnly(Boolean storeOnly) {
        this.storeOnly = storeOnly;
    }

    public Boolean getOneTime() {
        return oneTime;
    }

    public void setOneTime(Boolean oneTime) {
        this.oneTime = oneTime;
    }

    public Boolean getRecurring() {
        return recurring;
    }

    public void setRecurring(Boolean recurring) {
        this.recurring = recurring;
    }

    public Boolean getAllowPartialApprovals() {
        return allowPartialApprovals;
    }

    public void setAllowPartialApprovals(Boolean allowPartialApprovals) {
        this.allowPartialApprovals = allowPartialApprovals;
    }
}

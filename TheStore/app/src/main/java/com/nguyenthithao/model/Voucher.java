package com.nguyenthithao.model;

import java.io.Serializable;

public class Voucher implements Serializable {
    private float amount;
    private String code;
    private float condition;
    private String expiration;

    public Voucher() {
    }

    public Voucher(float amount, String code, float condition, String expiration) {
        this.amount = amount;
        this.code = code;
        this.condition = condition;
        this.expiration = expiration;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getCondition() {
        return condition;
    }

    public void setCondition(float condition) {
        this.condition = condition;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
}

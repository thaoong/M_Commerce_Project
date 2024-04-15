package com.nguyenthithao.model;

import java.io.Serializable;

public class Voucher implements Serializable {
    private String promotion;
    private String condition;
    private String voucherID;
    private String expiration;

    public Voucher() {
    }

    public Voucher(String promotion, String condition, String voucherID, String expiration) {
        this.promotion = promotion;
        this.condition = condition;
        this.voucherID = voucherID;
        this.expiration = expiration;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(String voucherID) {
        this.voucherID = voucherID;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
}

package com.nguyenthithao.model;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderHistory implements Serializable {
    private String orderID;
    private String oderStatus;
    private String date;
    private String name;
    private String totalPrice;

    public OrderHistory() {
    }

    public OrderHistory(String orderID, String oderStatus, String date, String name, String totalPrice) {
        this.orderID = orderID;
        this.oderStatus = oderStatus;
        this.date = date;
        this.name = name;
        this.totalPrice = totalPrice;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOderStatus() {
        return oderStatus;
    }

    public void setOderStatus(String oderStatus) {
        this.oderStatus = oderStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}

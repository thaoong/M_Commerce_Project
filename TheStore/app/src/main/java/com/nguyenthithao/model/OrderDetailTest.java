package com.nguyenthithao.model;

import java.io.Serializable;

public class OrderDetailTest implements Serializable {
    private String name;
    private String price;
    private float quantity;

    public OrderDetailTest() {
    }

    public OrderDetailTest(String name, String price, float quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }
}

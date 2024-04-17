package com.nguyenthithao.model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private String ID;
    private String name;
    private float unitPrice;
    private String imageLink;
    private float oldPrice;
    private int quantity;

    public CartItem() {
    }

    public CartItem(String ID) {
        this.ID = ID;
    }

    public CartItem(String name, float unitPrice, String imageLink, float oldPrice, int quantity) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.imageLink = imageLink;
        this.oldPrice = oldPrice;
        this.quantity = quantity;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public float getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(float oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}


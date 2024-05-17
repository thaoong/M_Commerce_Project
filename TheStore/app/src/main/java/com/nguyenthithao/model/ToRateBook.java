package com.nguyenthithao.model;

import java.io.Serializable;

public class ToRateBook implements Serializable {
    private String name;
    private String imageLink;
    private float unitPrice;
    private float oldPrice;
    private int quantity;
    private String Id;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public float getPrice() {
        return unitPrice;
    }

    public void setPrice(float price) {
        unitPrice = price;
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

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public ToRateBook(String name, String imageLink, float price, float oldPrice, int quantity, String Id) {
        this.name = name;
        this.imageLink = imageLink;
        unitPrice = price;
        this.oldPrice = oldPrice;
        this.quantity = quantity;
        this.Id = Id;
    }

    public ToRateBook() {
    }
}

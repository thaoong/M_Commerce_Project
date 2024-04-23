package com.nguyenthithao.model;

import java.io.Serializable;

public class PaymentMethod implements Serializable {
    private String Id;
    private String description;
    private String imageLink;
    private String name;

    public PaymentMethod() {
    }

    public PaymentMethod(String id, String description, String imageLink, String name) {
        Id = id;
        this.description = description;
        this.imageLink = imageLink;
        this.name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

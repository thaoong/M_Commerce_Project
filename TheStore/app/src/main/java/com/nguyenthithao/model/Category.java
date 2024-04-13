package com.nguyenthithao.model;

import java.io.Serializable;

public class Category implements Serializable {
    private String imageLink;
    private String name;
    private String id;
    public Category() {
    }

    public Category(String imageLink, String name, String id) {
        this.imageLink = imageLink;
        this.name = name;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

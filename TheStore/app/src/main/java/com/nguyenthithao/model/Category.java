package com.nguyenthithao.model;

public class Category {
    private String imageLink;
    private String name;

    public Category() {
    }

    public Category(String imageLink, String name) {
        this.imageLink = imageLink;
        this.name = name;
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

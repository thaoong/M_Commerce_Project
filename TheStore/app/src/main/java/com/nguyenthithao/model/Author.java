package com.nguyenthithao.model;

import java.io.Serializable;

public class Author implements Serializable {
    private String imageLink;
    private String name;
    public Author() {
    }

    public Author(String imageLink, String name) {
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

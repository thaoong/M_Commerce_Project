package com.nguyenthithao.model;

import java.io.Serializable;

public class WishlistBook implements Serializable {
    private String id;
    private String name;
    private String author;
    private String description;
    private String imageLink;
    private String category;
    private String publicationDate;
    private float rating;
    private int reviewNum;
    private float unitPrice;
    private float oldPrice;
    private int bestSelling;

    public WishlistBook() {
        // Để trống để Firebase có thể khởi tạo
    }

    public WishlistBook(String id, String name, String author, String description, String imageLink, String category, String publicationDate, float rating, int reviewNum, float unitPrice, float oldPrice, int bestSelling) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.imageLink = imageLink;
        this.category = category;
        this.publicationDate = publicationDate;
        this.rating = rating;
        this.reviewNum = reviewNum;
        this.unitPrice = unitPrice;
        this.oldPrice = oldPrice;
        this.bestSelling = bestSelling;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getReviewNum() {
        return reviewNum;
    }

    public void setReviewNum(int reviewNum) {
        this.reviewNum = reviewNum;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public float getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(float oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getBestSelling() {
        return bestSelling;
    }

    public void setBestSelling(int bestSelling) {
        this.bestSelling = bestSelling;
    }
}
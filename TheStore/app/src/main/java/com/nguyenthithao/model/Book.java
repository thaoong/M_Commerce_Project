package com.nguyenthithao.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable {
    private String Id;
    private String author;
    private int bestSelling;
    private String category;
    private String description;
    private ArrayList<String> imageLink;
    private String name;
    private float oldPrice;

    private String publicationDate;

    private float rating;
    private int reviewNum;
    private float unitPrice;
    private int numberInCart;


    public Book() {
    }

    public Book(int bestSelling) {
        this.bestSelling = bestSelling;
    }

    public Book(String Id, String author, String category, String description, ArrayList<String> imageLink, String name, float oldPrice, String publicationDate, float rating, int reviewNum, float unitPrice) {
        this.Id = Id;
        this.author = author;
        this.category = category;
        this.description = description;
        this.imageLink = imageLink;
        this.name = name;
        this.oldPrice = oldPrice;
        this.publicationDate = publicationDate;
        this.rating = rating;
        this.reviewNum = reviewNum;
        this.unitPrice = unitPrice;
    }

    public Book(String id, String name, String author, String description, ArrayList<String> imageLink) {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getImageLink() {
        return imageLink;
    }

    public void setImageLink(ArrayList<String> imageLink) {
        this.imageLink = imageLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(float oldPrice) {
        this.oldPrice = oldPrice;
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

    public int getBestSelling() {
        return bestSelling;
    }

    public void setBestSelling(int bestSelling) {
        this.bestSelling = bestSelling;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }
}

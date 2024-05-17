package com.nguyenthithao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rating implements Serializable {
    private String userId;
    private float rating;
    private String comment;
    private ArrayList<String> imageUrls;


    public Rating() {
    }

    public Rating(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Rating(String userId, float rating, String comment) {
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}

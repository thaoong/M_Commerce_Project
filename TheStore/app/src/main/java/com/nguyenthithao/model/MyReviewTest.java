package com.nguyenthithao.model;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class MyReviewTest implements Serializable {
    private String name;

    private String date;
    private String review;
    private String dayReview;
    private float like;
    private float comment;

    public MyReviewTest() {
    }

    public MyReviewTest(String name, String date, String review, String dayReview, float like, float comment) {
        this.name = name;
        this.date = date;
        this.review = review;
        this.dayReview = dayReview;
        this.like = like;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDayReview() {
        return dayReview;
    }

    public void setDayReview(String dayReview) {
        this.dayReview = dayReview;
    }

    public float getLike() {
        return like;
    }

    public void setLike(float like) {
        this.like = like;
    }

    public float getComment() {
        return comment;
    }

    public void setComment(float comment) {
        this.comment = comment;
    }
}

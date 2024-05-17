package com.nguyenthithao.model;

import java.util.ArrayList;

public class ReviewedBook {
    private String reviewerName;
    private long rating;
    private String bookImgUrl;
    private String bookName;
    private String comment;
    private ArrayList<String> imageUrls;
    private String ratingDate;

    public ReviewedBook() {
    }

    public ReviewedBook(String reviewerName, long rating, String bookImgUrl, String bookName, String comment, ArrayList<String> imageUrls, String ratingDate) {
        this.reviewerName = reviewerName;
        this.rating = rating;
        this.bookImgUrl = bookImgUrl;
        this.bookName = bookName;
        this.comment = comment;
        this.imageUrls = imageUrls;
        this.ratingDate = ratingDate;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public float getRating() {
        return rating;
    }

    public String getBookImgUrl() {
        return bookImgUrl;
    }

    public void setBookImgUrl(String bookImgUrl) {
        this.bookImgUrl = bookImgUrl;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
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

    public void setRating(long rating) {
        this.rating = rating;
    }

    public String getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(String ratingDate) {
        this.ratingDate = ratingDate;
    }
}

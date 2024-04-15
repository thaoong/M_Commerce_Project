package com.nguyenthithao.model;

public class OldRating {
    int productThumb;
    String productName, review, daterating;

    public OldRating(int productThumb, String productName, String review, String daterating) {
        this.productThumb = productThumb;
        this.productName = productName;
        this.review = review;
        this.daterating = daterating;
    }

    public int getProductThumb() {
        return productThumb;
    }

    public void setProductThumb(int productThumb) {
        this.productThumb = productThumb;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDaterating() {
        return daterating;
    }

    public void setDaterating(String daterating) {
        this.daterating = daterating;
    }
}
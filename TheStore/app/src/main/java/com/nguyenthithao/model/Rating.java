package com.nguyenthithao.model;

import android.graphics.Bitmap;

public class Rating {
    int productThumb;
    String productName;
    Bitmap ratingPhoto;

    public Rating(int productThumb, String productName, Bitmap ratingPhoto) {
        this.productThumb = productThumb;
        this.productName = productName;
        this.ratingPhoto = ratingPhoto;
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

    public Bitmap getRatingPhoto() {
        return ratingPhoto;
    }

    public void setRatingPhoto(Bitmap ratingPhoto) {
        this.ratingPhoto = ratingPhoto;
    }
}
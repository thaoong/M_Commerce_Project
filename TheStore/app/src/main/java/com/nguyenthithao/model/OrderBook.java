package com.nguyenthithao.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderBook implements Parcelable {
    private String id;
    private String name;
    private String imageLink;
    private float unitPrice;
    private float oldPrice;
    private int quantity;
    private boolean isReview; // New field

    public OrderBook() {
    }

    public OrderBook(String id, String name, String imageLink, float unitPrice, float oldPrice, int quantity, boolean isReview) {
        this.id = id;
        this.name = name;
        this.imageLink = imageLink;
        this.unitPrice = unitPrice;
        this.oldPrice = oldPrice;
        this.quantity = quantity;
        this.isReview = isReview;
    }

    // Getter and Setter methods for all fields, including isReview
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

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isReview() {
        return isReview;
    }

    public void setReview(boolean review) {
        isReview = review;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(imageLink);
        dest.writeFloat(unitPrice);
        dest.writeFloat(oldPrice);
        dest.writeInt(quantity);
        dest.writeByte((byte) (isReview ? 1 : 0)); // Write isReview as a byte
    }

    protected OrderBook(Parcel in) {
        id = in.readString();
        name = in.readString();
        imageLink = in.readString();
        unitPrice = in.readFloat();
        oldPrice = in.readFloat();
        quantity = in.readInt();
        isReview = in.readByte() != 0; // Read isReview as a byte
    }

    public static final Creator<OrderBook> CREATOR = new Creator<OrderBook>() {
        @Override
        public OrderBook createFromParcel(Parcel in) {
            return new OrderBook(in);
        }

        @Override
        public OrderBook[] newArray(int size) {
            return new OrderBook[size];
        }
    };
}

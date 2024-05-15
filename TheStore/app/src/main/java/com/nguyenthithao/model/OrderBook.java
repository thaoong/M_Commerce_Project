package com.nguyenthithao.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderBook implements Parcelable {
    private String Id;
    private String name;
    private float unitPrice;
    private String imageLink;
    private float oldPrice;
    private int quantity;

    public OrderBook() {
    }

    public OrderBook(String Id, String name, float unitPrice, String imageLink, float oldPrice, int quantity) {
        this.Id = Id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.imageLink = imageLink;
        this.oldPrice = oldPrice;
        this.quantity = quantity;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(name);
        dest.writeFloat(unitPrice);
        dest.writeString(imageLink);
        dest.writeFloat(oldPrice);
        dest.writeInt(quantity);
    }

    public static final Parcelable.Creator<OrderBook> CREATOR = new Parcelable.Creator<OrderBook>() {
        @Override
        public OrderBook createFromParcel(Parcel in) {
            return new OrderBook(in);
        }

        @Override
        public OrderBook[] newArray(int size) {
            return new OrderBook[size];
        }
    };

    protected OrderBook(Parcel in) {
        Id = in.readString();
        name = in.readString();
        unitPrice = in.readFloat();
        imageLink = in.readString();
        oldPrice = in.readFloat();
        quantity = in.readInt();
    }
}
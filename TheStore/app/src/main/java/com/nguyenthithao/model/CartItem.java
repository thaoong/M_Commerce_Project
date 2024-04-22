package com.nguyenthithao.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {
    private String ID;
    private String name;
    private float unitPrice;
    private String imageLink;
    private float oldPrice;
    private int quantity;

    public CartItem() {
    }

    public CartItem(String ID, String name, float unitPrice, String imageLink, float oldPrice, int quantity) {
        this.ID = ID;
        this.name = name;
        this.unitPrice = unitPrice;
        this.imageLink = imageLink;
        this.oldPrice = oldPrice;
        this.quantity = quantity;
    }

    protected CartItem(Parcel in) {
        ID = in.readString();
        name = in.readString();
        unitPrice = in.readFloat();
        imageLink = in.readString();
        oldPrice = in.readFloat();
        quantity = in.readInt();
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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
        dest.writeString(ID);
        dest.writeString(name);
        dest.writeFloat(unitPrice);
        dest.writeString(imageLink);
        dest.writeFloat(oldPrice);
        dest.writeInt(quantity);
    }
}
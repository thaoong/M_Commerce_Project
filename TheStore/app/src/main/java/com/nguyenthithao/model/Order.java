package com.nguyenthithao.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Order implements Parcelable {
    private String Id;
    private ArrayList<OrderBook> orderBooks;
    private String name;
    private String phone;
    private String street;
    private String ward;
    private String district;
    private String province;
    public float prePrice;
    private float shippingFee;
    private float discount;
    private float total;
    private String paymentMethod;
    private String status;
    private String orderDate;
    private String receivedDate;
    private String userID;

    public Order() {
    }

    public Order(String id, ArrayList<OrderBook> orderBooks, String name, String phone, String street, String ward, String district, String province, float prePrice, float shippingFee, float discount, float total, String paymentMethod, String status, String orderDate, String receivedDate, String userID) {
        Id = id;
        this.orderBooks = orderBooks;
        this.name = name;
        this.phone = phone;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.province = province;
        this.prePrice = prePrice;
        this.shippingFee = shippingFee;
        this.discount = discount;
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.orderDate = orderDate;
        this.receivedDate = receivedDate;
        this.userID = userID;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public ArrayList<OrderBook> getOrderBooks() {
        return orderBooks;
    }

    public void setOrderBooks(ArrayList<OrderBook> orderBooks) {
        this.orderBooks = orderBooks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public float getPrePrice() {
        return prePrice;
    }

    public void setPrePrice(float prePrice) {
        this.prePrice = prePrice;
    }

    public float getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(float shippingFee) {
        this.shippingFee = shippingFee;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    // getters and setters...

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeTypedList(orderBooks);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(street);
        dest.writeString(ward);
        dest.writeString(district);
        dest.writeString(province);
        dest.writeFloat(prePrice);
        dest.writeFloat(shippingFee);
        dest.writeFloat(discount);
        dest.writeFloat(total);
        dest.writeString(paymentMethod);
        dest.writeString(status);
        dest.writeString(orderDate);
        dest.writeString(receivedDate);
        dest.writeString(userID);
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    protected Order(Parcel in) {
        Id = in.readString();
        orderBooks = in.createTypedArrayList(OrderBook.CREATOR);
        name = in.readString();
        phone = in.readString();
        street = in.readString();
        ward = in.readString();
        district = in.readString();
        province = in.readString();
        prePrice = in.readFloat();
        shippingFee = in.readFloat();
        discount = in.readFloat();
        total = in.readFloat();
        paymentMethod = in.readString();
        status = in.readString();
        orderDate = in.readString();
        receivedDate = in.readString();
        userID = in.readString();
    }
}
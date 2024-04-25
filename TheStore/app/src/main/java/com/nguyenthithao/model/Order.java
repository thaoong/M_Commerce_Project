package com.nguyenthithao.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
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
    private String note;

    public Order() {
    }

    public Order(String id, ArrayList<OrderBook> orderBooks, String name, String phone, String street, String ward, String district, String province, float prePrice, float shippingFee, float discount, float total, String paymentMethod, String status, String orderDate, String receivedDate, String note) {
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
        this.note = note;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

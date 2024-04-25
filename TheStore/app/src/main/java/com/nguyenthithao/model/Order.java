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
}

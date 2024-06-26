package com.nguyenthithao.model;

import android.location.Address;

import java.util.List;

public class User {
    private String name, email, password, date_of_birth, phone;
    private List<Address> addresses;

    // Constructor
    public User(String name, String email, String password, String date_of_birth, String phone, String s) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.date_of_birth = date_of_birth;
        this.phone = phone;
    }

    // Getter và Setter cho các thuộc tính

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    // Phương thức kiểm tra mật khẩu
    public boolean checkPassword(String enteredPassword) {
        return this.password.equals(enteredPassword);
    }
}

package com.nguyenthithao.model;

public class User {
    private String name, email, password,
            date_of_birth
            , phone;

    public User(String name, String email, String password, String
            date_of_birth
            , String phone) {
    }

    public User(String name, String email, String password, String phone) {
    }


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
    

    public User(String name, String email, String password, String date_of_birth, String phone, String userName) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.date_of_birth = date_of_birth;
        this.phone = phone;

    }

    public User() {
    }
}

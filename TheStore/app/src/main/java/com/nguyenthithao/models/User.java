package com.nguyenthithao.models;

public class User {
    private String fullname;
    private String email;
    private String password;
    private String dob;
    private String phone;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String fullname, String email, String password, String dob, String phone) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.phone = phone;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getPhone(String phone){
        return phone;
    }
}

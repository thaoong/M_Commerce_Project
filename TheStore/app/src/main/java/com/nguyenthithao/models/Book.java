package com.nguyenthithao.models;

public class Book {
    private String Name;
    private float Price;
    private float FakePrice;
    private int imageFavoBook;

    public Book() {
    }

    public Book(String name, float price, float fakePrice, int imageFavoBook) {
        Name = name;
        Price = price;
        FakePrice = fakePrice;
        this.imageFavoBook = imageFavoBook;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public float getFakePrice() {
        return FakePrice;
    }

    public void setFakePrice(float fakePrice) {
        FakePrice = fakePrice;
    }

    public int getImageFavoBook() {
        return imageFavoBook;
    }

    public void setImageFavoBook(int imageFavoBook) {
        this.imageFavoBook = imageFavoBook;
    }
}

package com.nguyenthithao.models;

public class Book {
    private String Name;
    private String Price;
    private String FakePrice;
    private int imageFavoBook;

    public Book() {
    }

    public Book(String name, String price, String fakePrice, int imageFavoBook) {
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

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getFakePrice() {
        return FakePrice;
    }

    public void setFakePrice(String fakePrice) {
        FakePrice = fakePrice;
    }

    public int getImageFavoBook() {
        return imageFavoBook;
    }

    public void setImageFavoBook(int imageFavoBook) {
        this.imageFavoBook = imageFavoBook;
    }
}

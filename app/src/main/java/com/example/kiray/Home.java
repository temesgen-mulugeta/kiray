package com.example.kiray;

class Home{
    private String title;
    private String description;
    private int rooms;
    private double price;

    private String SubCity;
    private int woreda;

    private String phone;
    private String photo;


    @Override
    public String toString() {
        return "Home{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", rooms=" + rooms +
                ", price=" + price +
                ", SubCity='" + SubCity + '\'' +
                ", woreda=" + woreda +
                ", phone='" + phone + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSubCity() {
        return SubCity;
    }

    public void setSubCity(String subCity) {
        SubCity = subCity;
    }

    public int getWoreda() {
        return woreda;
    }

    public void setWoreda(int woreda) {
        this.woreda = woreda;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Home(String title, String description, int rooms, double price, String subCity, int woreda, String phone, String photo) {
        this.title = title;
        this.description = description;
        this.rooms = rooms;
        this.price = price;
        SubCity = subCity;
        this.woreda = woreda;
        this.phone = phone;
        this.photo = photo;
    }
}
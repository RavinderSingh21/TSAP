package com.example.newtsap;

public class PhoneSignIn {
    String name,email,photo,phone;

    public PhoneSignIn(String name, String email, String photo, String phone) {
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.phone = phone;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public PhoneSignIn(){

    }
}

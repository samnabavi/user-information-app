package com.example.userinformation;

import java.io.Serializable;

public class User  {
    public int id;
    public String username;
    public String email;
    //public Address address;
    public String phone;
    public String website;
    //public Company company;

    public User(int id, String username, String email, Address address, String phone, String website, Company company) {
        this.id = id;
        this.username = username;
        this.email = email;
        //this.address = address;
        this.phone = phone;
        this.website = website;
        //this.company = company;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                //", address=" + address +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                //", company=" + company +
                '}';
    }
}
class Address {
    public String street;
    public String suite;
    public String city;
    public String zipcode;
    public Geo geo;

    public Address(String street, String suite, String city, String zipcode, Geo geo) {
        this.street = street;
        this.suite = suite;
        this.city = city;
        this.zipcode = zipcode;
        this.geo = geo;
    }

    public Address() {
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", suite='" + suite + '\'' +
                ", city='" + city + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", geo=" + geo +
                '}';
    }
}
class Geo {
    public double lat;
    public double lng;

    public Geo(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Geo() {
    }

    @Override
    public String toString() {
        return "Geo{" +
                "lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                '}';
    }
}

class Company {
    public String name;
    public String catchPhrase;
    public String bs;

    public Company(String name, String catchPhrase, String bs) {
        this.name = name;
        this.catchPhrase = catchPhrase;
        this.bs = bs;
    }

    public Company() {
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", catchPhrase='" + catchPhrase + '\'' +
                ", bs='" + bs + '\'' +
                '}';
    }
}

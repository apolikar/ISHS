package irl.lyit.DublinSmartHouseSearch.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class House {

    @Id
    @GeneratedValue
    private Long id;
    private String link;
    private double price;
    private String address;
    private String cityOrCounty;
    private int bedrooms;
    private double lat;
    private double lng;


    public House(String link, double price, String address, String cityOrCounty, int bedrooms, double lat, double lng) {
        this.link = link;
        this.price = price;
        this.address = address;
        this.cityOrCounty = cityOrCounty;
        this.bedrooms = bedrooms;
        this.lat = lat;
        this.lng = lng;
    }

    public House() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityOrCounty() {
        return cityOrCounty;
    }

    public void setCityOrCounty(String cityOrCounty) {
        this.cityOrCounty = cityOrCounty;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "House{" +
                "id=" + id +
                ", link='" + link + '\'' +
                ", price=" + price +
                ", address='" + address + '\'' +
                ", cityOrCounty='" + cityOrCounty + '\'' +
                ", bedrooms=" + bedrooms +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}

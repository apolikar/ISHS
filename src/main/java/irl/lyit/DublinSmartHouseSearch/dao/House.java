package irl.lyit.DublinSmartHouseSearch.dao;

import irl.lyit.DublinSmartHouseSearch.service.geoCoordinates.GeoCoordinates;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table
public class House implements Serializable{

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
    private long updateTime;


    public House(
            String link,
            double price,
            String address,
            String cityOrCounty,
            int bedrooms,
            double lat,
            double lng,
            long updateTime
    ) {
        this.link = link;
        this.price = price;
        this.address = address;
        this.cityOrCounty = cityOrCounty;
        this.bedrooms = bedrooms;
        this.lat = lat;
        this.lng = lng;
        this.updateTime = updateTime;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof House)) {
            return false;
        }
        return lat == ((House)obj).getLat()
                && lng == ((House)obj).getLng();
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

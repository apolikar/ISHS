package irl.lyit.DublinSmartHouseSearch.old;

import irl.lyit.DublinSmartHouseSearch.service.TransportionType;

public class SearchAttributes {


    private String address;
    private double lat;
    private double lng;
    private String dateAndTime;
    private long timeLimit;
    private TransportionType transportationType;
    private long minPrice;
    private long maxPrice;
    private long minBeds;
    private long maxBeds;


    public SearchAttributes(
            String address,
            double lat,
            double lng,
            String dateAndTime,
            long timeLimit,
            TransportionType transportationType,
            long minPrice,
            long maxPrice,
            long minBeds,
            long maxBeds
    ) {
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.dateAndTime = dateAndTime;
        this.timeLimit = timeLimit;
        this.transportationType = transportationType;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minBeds = minBeds;
        this.maxBeds = maxBeds;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public TransportionType getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(TransportionType transportationType) {
        this.transportationType = transportationType;
    }

    public long getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(long minPrice) {
        this.minPrice = minPrice;
    }

    public long getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(long maxPrice) {
        this.maxPrice = maxPrice;
    }

    public long getMinBeds() {
        return minBeds;
    }

    public void setMinBeds(long minBeds) {
        this.minBeds = minBeds;
    }

    public long getMaxBeds() {
        return maxBeds;
    }

    public void setMaxBeds(long maxBeds) {
        this.maxBeds = maxBeds;
    }

    @Override
    public String toString() {
        return "SearchAttributes{" +
                "address='" + address + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", dateAndTime='" + dateAndTime + '\'' +
                ", timeLimit=" + timeLimit +
                ", transportationType=" + transportationType +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", minBeds=" + minBeds +
                ", maxBeds=" + maxBeds +
                '}';
    }
}

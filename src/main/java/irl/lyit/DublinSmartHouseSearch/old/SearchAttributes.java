package irl.lyit.DublinSmartHouseSearch.old;

import irl.lyit.DublinSmartHouseSearch.controller.HouseController;
import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.dao.HouseRepository;
import irl.lyit.DublinSmartHouseSearch.service.HouseCollector;
import irl.lyit.DublinSmartHouseSearch.service.HouseService;
import irl.lyit.DublinSmartHouseSearch.service.TransportionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Time;
import java.util.List;


public class SearchAttributes {

    private GeoCoordinates coordinates;
    private String dateAndTime;
    private long timeLimit;
    private String transportationType;
    private long minPrice;
    private long maxPrice;
    private long minBeds;
    private long maxBeds;

    private List<BoundingBox> boundingBoxes;

    public SearchAttributes(
            GeoCoordinates coordinates,
            String dateAndTime,
            long timeLimit,
            String transportationType,
            long minPrice,
            long maxPrice,
            long minBeds,
            long maxBeds
    ) throws IOException, InterruptedException {
        this.coordinates = coordinates;
        this.dateAndTime = dateAndTime;
        this.timeLimit = timeLimit;
        this.transportationType = transportationType;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minBeds = minBeds;
        this.maxBeds = maxBeds;


        if(coordinates.getLat() != 0 && coordinates.getLng() != 0){
            // create isochrone map for search
            boundingBoxes = new CreateIsochroneMap(coordinates, dateAndTime, transportationType, timeLimit).boundingBox();
        }
    }

    public GeoCoordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(GeoCoordinates coordinates) {
        this.coordinates = coordinates;
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

    public String getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(String transportationType) {
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

    public List<BoundingBox> getBoundingBoxes() {
        return boundingBoxes;
    }

    public void setBoundingBoxes(List<BoundingBox> boundingBoxes) {
        this.boundingBoxes = boundingBoxes;
    }

    @Override
    public String toString() {
        return "SearchAttributes{" +
                "coordinates=" + coordinates +
                ", dateAndTime='" + dateAndTime + '\'' +
                ", timeLimit=" + timeLimit +
                ", transportationType='" + transportationType + '\'' +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", minBeds=" + minBeds +
                ", maxBeds=" + maxBeds +
                ", boundingBoxes=" + boundingBoxes +
                '}';
    }
}

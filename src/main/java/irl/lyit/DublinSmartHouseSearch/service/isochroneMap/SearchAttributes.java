package irl.lyit.DublinSmartHouseSearch.service.isochroneMap;

import irl.lyit.DublinSmartHouseSearch.service.geoCoordinates.GeoCoordinates;

import java.io.IOException;


public final class SearchAttributes {

    private final GeoCoordinates coordinates;
    private final String dateAndTime;
    private final long timeLimit;
    private final String transportationType;
    private final long minPrice;
    private final long maxPrice;
    private final long minBeds;
    private final long maxBeds;

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
    }

    public GeoCoordinates getCoordinates() {
        return coordinates;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public String getTransportationType() {
        return transportationType;
    }

    public long getMinPrice() {
        return minPrice;
    }

    public long getMaxPrice() {
        return maxPrice;
    }

    public long getMinBeds() {
        return minBeds;
    }

    public long getMaxBeds() {
        return maxBeds;
    }
}

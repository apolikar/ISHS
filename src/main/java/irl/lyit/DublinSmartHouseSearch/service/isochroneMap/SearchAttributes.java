package irl.lyit.DublinSmartHouseSearch.service.isochroneMap;

import irl.lyit.DublinSmartHouseSearch.service.geoCoordinates.GeoCoordinates;

import java.io.IOException;


public final class SearchAttributes {

    private final GeoCoordinates coordinates;
    private final String dateAndTime;
    private final Long timeLimit;
    private final String transportationType;
    private final Integer minPrice;
    private final Integer maxPrice;
    private final Integer minBeds;
    private final Integer maxBeds;

    public SearchAttributes(
            GeoCoordinates coordinates,
            String dateAndTime,
            Integer timeLimit,
            String transportationType,
            SearchAttributeHousePriceType minPrice,
            SearchAttributeHousePriceType maxPrice,
            Integer minBeds,
            Integer maxBeds
    ) throws IOException, InterruptedException {
        this.coordinates = coordinates;
        this.dateAndTime = dateAndTime;
        this.timeLimit = timeLimit == null ? null : timeLimit * 60L;
        this.transportationType = transportationType;

        if (minPrice == null) {
            this.minPrice = null;
        } else {
            this.minPrice = minPrice.getAmount();
        }

        if (minPrice == null) {
            this.maxPrice = null;
        } else {
            this.maxPrice = maxPrice.getAmount();
        }

        this.minBeds = minBeds;
        this.maxBeds = maxBeds;
    }

    public GeoCoordinates getCoordinates() {
        return coordinates;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public Long getTimeLimit() {
        return timeLimit;
    }

    public String getTransportationType() {
        return transportationType;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public Integer getMinBeds() {
        return minBeds;
    }

    public Integer getMaxBeds() {
        return maxBeds;
    }

    public boolean isCoordinatesValid() {
        return getCoordinates() != null
                && getCoordinates().getLat() != 0
                && getCoordinates().getLng() != 0;
    }

    public boolean isPriceValid() {
        return getMaxPrice() != null
                && getMinPrice() != null
                && getMaxPrice() >= getMinPrice();
    }

    public boolean isBedsAmountValid() {
        return getMaxBeds() != null
                && getMinBeds() != null
                 && getMaxBeds() >= getMinBeds();
    }

}

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
            String minPrice,
            String maxPrice,
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
            this.minPrice = setPrice(minPrice);
        }

        if (minPrice == null) {
            this.maxPrice = null;
        } else {
            this.maxPrice = setPrice(maxPrice);
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

    private int setPrice(String price) {

        return switch (price) {
            case "€100K" -> 100_000;
            case "€150K" -> 150_000;
            case "€200K" -> 200_000;
            case "€250K" -> 250_000;
            case "€300K" -> 300_000;
            case "€350K" -> 350_000;
            case "€450K" -> 450_000;
            case "€500K" -> 500_000;
            case "€550K" -> 550_000;
            case "€600K" -> 600_000;
            case "€650K" -> 650_000;
            case "€700K" -> 700_000;
            case "€750K" -> 750_000;
            case "€800K" -> 800_000;
            case "€850K" -> 850_000;
            case "€900K" -> 900_000;
            case "€950K" -> 950_000;
            case "€1M" -> 1_000_000;
            case "€1.5M" -> 1_500_000;
            case "€2M" -> 2_000_000;
            case "€3M" -> 3_000_000;
            case "€4M" -> 4_000_000;
            default -> 5_000_000;
        };
    }
}

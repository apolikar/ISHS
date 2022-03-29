package irl.lyit.DublinSmartHouseSearch.old;

public class SearchAttributes {


    private String address;
    private String dateAndTime;
    private long timeLimit;
    private String transportationType;
    private long minPrice;
    private long maxPrice;
    private long minBeds;
    private long maxBeds;


    public SearchAttributes(String address, String dateAndTime,
                            long timeLimit, String transportationType,
                            long minPrice, long maxPrice, long minBeds, long maxBeds) {
        this.address = address;
        this.dateAndTime = dateAndTime;
        this.timeLimit = timeLimit;
        this.transportationType = transportationType;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minBeds = minBeds;
        this.maxBeds = maxBeds;
    }


    public SearchAttributes() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    @Override
    public String toString() {
        return "SearchAttributes{" +
                "address='" + address + '\'' +
                ", dateAndTime='" + dateAndTime + '\'' +
                ", timeLimit=" + timeLimit +
                ", transportationType='" + transportationType + '\'' +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", minBeds=" + minBeds +
                ", maxBeds=" + maxBeds +
                '}';
    }
}

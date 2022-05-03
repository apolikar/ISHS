package irl.lyit.DublinSmartHouseSearch.service.daftScrapper;

public class TimeInMillisecondsUserFriendlyInfo {

    private long timeInMilliseconds;

    public TimeInMillisecondsUserFriendlyInfo(long timeInMilliseconds) {
        this.timeInMilliseconds = timeInMilliseconds;
    }

    public String getTimeInfoString() {

        long minutes = (timeInMilliseconds / 1000) / 60;
        long seconds = (timeInMilliseconds / 1000) % 60;
        return minutes + " minutes " + seconds + " seconds";
    }
}

package irl.lyit.DublinSmartHouseSearch.service;

public class TravelTimeInSecondsToUserFriendlyInfo {

    private long secondsToTravel;

    public TravelTimeInSecondsToUserFriendlyInfo(long secondsToTravel) {
        this.secondsToTravel = secondsToTravel;
    }

    public String getUserFriendlyTimeInfo() {
        long minutes = (secondsToTravel % 3600) / 60;
        long seconds = secondsToTravel % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }
}

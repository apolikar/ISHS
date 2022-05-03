package irl.lyit.DublinSmartHouseSearch.presentation.homePanel;

import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.service.TravelTimeInSecondsToUserFriendlyInfo;

import java.io.Serializable;

public class ResultMatchHouse implements Serializable {

    private House house;
    private int secondsToTravel;
    private String timeString;

    public ResultMatchHouse(House house, int secondsToTravel) {
        this.house = house;
        this.secondsToTravel = secondsToTravel;
        TravelTimeInSecondsToUserFriendlyInfo travelTimeInSeconds =
                new TravelTimeInSecondsToUserFriendlyInfo(secondsToTravel);
        timeString = travelTimeInSeconds.getUserFriendlyTimeInfo();
    }

    public ResultMatchHouse() {
    }

    public House getHouse() {
        return house;
    }

    public double getPrice() {
        return house.getPrice();
    }


    public void setHouse(House house) {
        this.house = house;
    }

    public int getSecondsToTravel() {
        return secondsToTravel;
    }

    public void setSecondsToTravel(int secondsToTravel) {
        this.secondsToTravel = secondsToTravel;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }


//    private static String timeInfo(long secondsToTravel) {
//        long minutes = (secondsToTravel % 3600) / 60;
//        long seconds = secondsToTravel % 60;
//
//        return String.format("%02d:%02d", minutes, seconds);
//    }

    @Override
    public String toString() {
        return "secondsToTravel=" + secondsToTravel +
                "\nHouse: " + house +
                "\n";
    }
}

package irl.lyit.DublinSmartHouseSearch.presentation.searchPanel;

import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.service.TravelTimeInSecondsToUserFriendlyInfo;

import java.io.Serializable;

public class ResultMatchHouse implements Serializable {

    private House house;
    private final int secondsToTravel;
    private final String timeString;

    public ResultMatchHouse(House house, int secondsToTravel) {
        this.house = house;
        this.secondsToTravel = secondsToTravel;
        TravelTimeInSecondsToUserFriendlyInfo travelTimeInSeconds =
                new TravelTimeInSecondsToUserFriendlyInfo(secondsToTravel);
        timeString = travelTimeInSeconds.getUserFriendlyTimeInfo();
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public int getSecondsToTravel() {
        return secondsToTravel;
    }

    public String getTimeString() {
        return timeString;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ResultMatchHouse)) {
            return false;
        }

        return house.equals(((ResultMatchHouse)obj).getHouse())
                && secondsToTravel == ((ResultMatchHouse)obj).getSecondsToTravel();
    }

    @Override
    public String toString() {
        return "secondsToTravel=" + secondsToTravel +
                "\nHouse: " + house +
                "\n";
    }
}

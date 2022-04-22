package irl.lyit.DublinSmartHouseSearch.service;

import irl.lyit.DublinSmartHouseSearch.dao.House;

import java.io.Serializable;

public class ResultMatchHouse implements Serializable {

    private House house;
    private int secondsToTravel;

    public ResultMatchHouse(House house, int secondsToTravel) {
        this.house = house;
        this.secondsToTravel = secondsToTravel;
    }

    public ResultMatchHouse() {
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

    public void setSecondsToTravel(int secondsToTravel) {
        this.secondsToTravel = secondsToTravel;
    }

    @Override
    public String toString() {
        return "secondsToTravel=" + secondsToTravel +
                "\nHouse: " + house +
                "\n";
    }
}

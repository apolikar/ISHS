package irl.lyit.DublinSmartHouseSearch.service.timeMatrix;

import com.fasterxml.jackson.databind.JsonNode;
import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.presentation.searchPanel.ResultMatchHouse;

import java.util.ArrayList;
import java.util.List;

public class PropertiesInTimeList {

    private JsonNode locationsNode;
    private List<House> houseList;

    public PropertiesInTimeList(JsonNode locationsNode, List<House> houseList) {
        this.locationsNode = locationsNode;
        this.houseList = houseList;
    }

    public List<ResultMatchHouse> returnResultMatchHouseList() {

        List<ResultMatchHouse> resultMatchHouseList = new ArrayList<>();

        for (int i = 0; i < locationsNode.size(); i++) {
            String locationIndexString = locationsNode.get(i).get("id").asText();
            int locationIndex = Integer.parseInt(locationIndexString.substring(locationIndexString.lastIndexOf("-") + 1));
            int secondsToTravel = locationsNode.get(i).get("properties").get(0).get("travel_time").asInt();
            resultMatchHouseList.add(new ResultMatchHouse(houseList.get(locationIndex), secondsToTravel));
        }

        return resultMatchHouseList;
    }



}

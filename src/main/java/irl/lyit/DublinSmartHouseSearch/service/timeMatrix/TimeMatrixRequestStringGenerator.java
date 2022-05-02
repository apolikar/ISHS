package irl.lyit.DublinSmartHouseSearch.service.timeMatrix;

import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.old.GeoCoordinates;

import java.util.List;

public class TimeMatrixRequestStringGenerator {

    public TimeMatrixRequestStringGenerator() {
    }

    public String generateRequestString(
            GeoCoordinates startingPoint,
            List<House> list,
            String transportTime,
            long travelTime,
            String dateAndTravelTime
    ){

        StringBuilder inputJson = new StringBuilder();
        inputJson.append(startingLocation(startingPoint.getLat(), startingPoint.getLng()));

        for (int i = 0; i < list.size(); i++) {
            inputJson.append(locationDetails(i, list.get(i).getLat(), list.get(i).getLng()));
        }

        int last = inputJson.lastIndexOf(",");
        inputJson.replace(last, last + 1, "");

        inputJson.append(departureSearches(transportTime, travelTime, list.size(), dateAndTravelTime));
        return inputJson.toString();
    }


    private String startingLocation(double lat, double lng) {

        String result = "{\n" +
                "  \"locations\": [\n" +
                "    {\n" +
                "      \"id\": \"starting-location\",\n" +
                "      \"coords\": {\n" +
                "        \"lat\": " + lat + ",\n" +
                "        \"lng\": " + lng + "\n" +
                "      }\n" +
                "    },\n";
        return result;
    }


    private String locationDetails(int locationCount, double lat, double lng) {

        String result = "    {\n" +
                "      \"id\": \"other-location-" + locationCount + "\",\n" +
                "      \"coords\": {\n" +
                "        \"lat\": " + lat + ",\n" +
                "        \"lng\": " + lng + "\n" +
                "      }\n" +
                "    },\n";
        return result;
    }


    private String departureSearches(String transportType, long travelTime, int houseCount, String departureDateTime) {

        StringBuilder result = new StringBuilder();
        result.append(
                "  ],\n" +
                        "  \"departure_searches\": [\n" +
                        "    {\n" +
                        "      \"transportation\": {\n" +
                        "        \"type\": \"" + transportType + "\"\n" +
                        "      },\n" +
                        "      \"travel_time\": " + travelTime + ",\n" +
                        "      \"properties\": [\n" +
                        "        \"travel_time\"\n" +
                        "      ],\n" +
                        "      \"id\": \"Departure search\",\n" +
                        "      \"arrival_location_ids\": [\n"
        );


        for (int i = 0; i < houseCount; i++) {
            if (i + 1 < houseCount) {
                result.append(
                        "        \"other-location-" + i + "\",\n"
                );
            } else {
                result.append(
                        "        \"other-location-" + i + "\"\n"
                );
            }

        }

        result.append(
                "      ],\n" +
                        "      \"departure_location_id\": \"starting-location\",\n" +
                        "      \"departure_time\": \"" + departureDateTime + "\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}"
        );

        return result.toString();
    }
}

package irl.lyit.DublinSmartHouseSearch.old;


import com.fasterxml.jackson.databind.JsonNode;
import irl.lyit.DublinSmartHouseSearch.controller.exception.TooManyPointsException;
import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.service.ResultMatchHouse;
import irl.lyit.DublinSmartHouseSearch.service.client.TimeTravelTimeMatrixHTTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimeTravelMatrix {
    private Logger log = LoggerFactory.getLogger(TimeTravelMatrix.class);
    private static final int API_LIMIT = 2000;

    public List<ResultMatchHouse> getInTime(
            GeoCoordinates startingPoint,
            List<House> list,
            String transportTime,
            long travelTime,
            String dateAndTravelTime
    ) throws IOException, InterruptedException, TooManyPointsException {

        if(list.size() <= API_LIMIT) {
            return houseInRangeCheck(startingPoint, list, transportTime, travelTime, dateAndTravelTime);
        }

        int start = 0;
        int end = 50;

        List<ResultMatchHouse> result = new ArrayList<>();
        try {
            while (start < list.size()) {
                result.addAll(
                        houseInRangeCheck(
                                startingPoint,
                                list.subList(start, end - 1),
                                transportTime,
                                travelTime,
                                dateAndTravelTime
                        ));
                start = end;

                end += API_LIMIT;

                if (end > list.size()){
                    end = list.size();
                }
            }
        } catch (TooManyPointsException e) {
            if (result.isEmpty()) {
               throw e;
            }

            // if result list has some houses already
            // made because of time travel api limitation per minute
            return result;
        }


        return result;
    }

    private List<ResultMatchHouse> houseInRangeCheck(
            GeoCoordinates startingPoint,
            List<House> list,
            String transportTime,
            long travelTime,
            String dateAndTravelTime
    ) throws IOException, InterruptedException, TooManyPointsException {

        List<ResultMatchHouse> result = new ArrayList<>();

        String inputJson = generateRequestJsonString(startingPoint, list, transportTime, travelTime, dateAndTravelTime);

        TimeTravelTimeMatrixHTTPClient client = new TimeTravelTimeMatrixHTTPClient(inputJson);
        JsonNode response = client.generateInTimeJsonResult();
        if (response.get("http_status") != null
                && response.get("http_status").asInt() != HttpStatus.OK.value()
        ) {
            log.error(response.get("description").asText());
            throw new TooManyPointsException();
        }

        JsonNode jsonNode = response.get("results").get(0).get("locations");

        for (int i = 0; i < jsonNode.size(); i++) {
            String locationIndexString = jsonNode.get(i).get("id").asText();
            int locationIndex = Integer.parseInt(locationIndexString.substring(locationIndexString.lastIndexOf("-") + 1));
            int secondsToTravel = jsonNode.get(i).get("properties").get(0).get("travel_time").asInt();
            result.add(new ResultMatchHouse(list.get(locationIndex), secondsToTravel));
        }
        return result;
    }


    private String generateRequestJsonString(
            GeoCoordinates startingPoint,
            List<House> list,
            String transportTime,
            long travelTime,
            String dateAndTravelTime) {

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
package irl.lyit.DublinSmartHouseSearch.old;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.service.ResultMatchHouse;
import irl.lyit.DublinSmartHouseSearch.service.client.TimeTravelTimeMatrixHTTPClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class TimeTravelMatrix {


//    public List<House> getInTime(
//            GeoCoordinates startingPoint,
//            List<House> list,
//            String transportTime,
//            long travelTime,
//            String dateAndTravelTime
//    ) throws IOException, InterruptedException {
//
//        List<House> result = new ArrayList<>();
//
//        String inputJson = generateRequestJsonString(startingPoint, list,  transportTime, travelTime, dateAndTravelTime);
//
//        TimeTravelTimeMatrixHTTPClient client = new TimeTravelTimeMatrixHTTPClient(inputJson);
//        JsonNode response = client.generateInTimeJsonResult();
//        JsonNode jsonNode = response.get("results").get(0).get("locations");
//
//        for (int i = 0; i < jsonNode.size(); i++) {
//            String locationIndexString = jsonNode.get(i).get("id").asText();
//            int locationIndex = Integer.parseInt(locationIndexString.substring(locationIndexString.lastIndexOf("-") + 1));
//            System.out.println(jsonNode.get(i).get("properties").get(0).get("travel_time"));
//            result.add(list.get(locationIndex));
//        }
//
//        return result;
//    }


    public List<ResultMatchHouse> getInTime(
            GeoCoordinates startingPoint,
            List<House> list,
            String transportTime,
            long travelTime,
            String dateAndTravelTime
    ) throws IOException, InterruptedException {

        List<ResultMatchHouse> result = new ArrayList<>();

        String inputJson = generateRequestJsonString(startingPoint, list,  transportTime, travelTime, dateAndTravelTime);

        TimeTravelTimeMatrixHTTPClient client = new TimeTravelTimeMatrixHTTPClient(inputJson);
        JsonNode response = client.generateInTimeJsonResult();
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

        for (int i = 0; i < list.size(); i ++) {
            inputJson.append(locationDetails(i, list.get(i).getLat(), list.get(i).getLng()));
        }

        int last = inputJson.lastIndexOf(",");
        inputJson.replace(last, last + 1, "");

        inputJson.append(departureSearches(transportTime, travelTime, list.size(), dateAndTravelTime));
        return inputJson.toString();
    }


    private String startingLocation (double lat, double lng) {

        String result =         "{\n" +
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



    private String locationDetails (int locationCount, double lat, double lng) {

        String result  =        "    {\n" +
                                "      \"id\": \"other-location-" + locationCount + "\",\n" +
                                "      \"coords\": {\n" +
                                "        \"lat\": " + lat + ",\n" +
                                "        \"lng\": " + lng + "\n" +
                                "      }\n" +
                                "    },\n";
        return result;
    }



    private String departureSearches (String transportType, long travelTime, int houseCount, String departureDateTime) {

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
            if(i + 1 < houseCount){
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




    // -------------------------------------------------------------------------------------------------
    //                                BACK UP

    /*
    public List<House> getInTime(GeoCoordinates startingPoint, List<House> list, String transportTime,
                                 long travelTime, String dateAndTravelTime) throws IOException, InterruptedException {

        List<House> result = new ArrayList<>();

        StringBuilder inputJson = new StringBuilder();
        inputJson.append(startingLocation(startingPoint.getLat(), startingPoint.getLng()));

        for (int i = 0; i < list.size(); i ++) {
            inputJson.append(locationDetails(i, list.get(i).getLat(), list.get(i).getLng()));
        }

        int last = inputJson.lastIndexOf(",");
        inputJson.replace(last, last + 1, "");

        inputJson.append(departureSearches(transportTime, travelTime, list.size(), dateAndTravelTime));

        String postEndpoint = "https://api.traveltimeapp.com/v4/time-filter";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(postEndpoint))
                .header("Content-Type", "application/json")
                .header("X-Application-Id", "a8605824")
                .header("X-Api-Key", "390add03321ba0b75ceda50d6a0baa82")
                .POST(HttpRequest.BodyPublishers.ofString(inputJson.toString()))
                .build();


        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());

        jsonNode = jsonNode.get("results").get(0).get("locations");

        for (int i = 0; i < jsonNode.size(); i++) {
            String locationIndexString = jsonNode.get(i).get("id").asText();
            int locationIndex = Integer.parseInt(locationIndexString.substring(locationIndexString.lastIndexOf("-") + 1));
            result.add(list.get(locationIndex));

        }

        return result;
    }


    private String startingLocation (double lat, double lng) {

        String result =         "{\n" +
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



    private String locationDetails (int locationCount, double lat, double lng) {

        String result  =        "    {\n" +
                                "      \"id\": \"other-location-" + locationCount + "\",\n" +
                                "      \"coords\": {\n" +
                                "        \"lat\": " + lat + ",\n" +
                                "        \"lng\": " + lng + "\n" +
                                "      }\n" +
                                "    },\n";
        return result;
    }



    private String departureSearches (String transportType, long travelTime, int houseCount, String departureDateTime) {

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
            if(i + 1 < houseCount){
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
     */


}

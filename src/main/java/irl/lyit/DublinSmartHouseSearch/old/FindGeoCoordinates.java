package irl.lyit.DublinSmartHouseSearch.old;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FindGeoCoordinates {

    private String userAdress;

    public FindGeoCoordinates(String userAdress) {
        this.userAdress = userAdress;
    }

    public FindGeoCoordinates() {
    }

    public GeoCoordinates getCoordinates() throws IOException, InterruptedException {


        String formattedAddress = splitAddress();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://maps.googleapis.com/maps/api/geocode/json?address=" + formattedAddress + "Ireland&key=AIzaSyBDoe7QtoHzRpGj9RX52T4mzSjkaXzx8lo"))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());


        jsonNode = jsonNode.get("results").get(0).get("geometry").get("location");

        String latLng = jsonNode.toString();
        System.out.println(latLng);

        double lat = jsonNode.get("lat").asDouble();
        double lng = jsonNode.get("lng").asDouble();

        return new GeoCoordinates(lat, lng);
    }


    private String splitAddress() {

        if(userAdress == null)
            return "";


        StringBuilder result = new StringBuilder();

        String[] addressArray = userAdress.split(" ");
        for(String part : addressArray) {
            result.append(part).append("+");
        }

        return result.toString();
    }




}

package irl.lyit.DublinSmartHouseSearch.service.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class GMapsHTTPClient {


    public JsonNode finalDestination(String address) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://maps.googleapis.com/maps/api/geocode/json?address=" +
                        address + "Ireland&key=AIzaSyBDoe7QtoHzRpGj9RX52T4mzSjkaXzx8lo"))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());


        jsonNode = jsonNode.get("results").get(0).get("geometry").get("location");

        return jsonNode;
    }


}

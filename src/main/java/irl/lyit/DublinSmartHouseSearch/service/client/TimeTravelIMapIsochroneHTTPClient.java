package irl.lyit.DublinSmartHouseSearch.service.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TimeTravelIMapIsochroneHTTPClient {

    private final String requestString;

    public TimeTravelIMapIsochroneHTTPClient(String requestString) {
        this.requestString = requestString;
    }

    public JsonNode generateResponseString() throws IOException, InterruptedException {
        String postEndpoint = "http://api.traveltimeapp.com/v4/time-map";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(postEndpoint))
                .header("Content-Type", "application/json")
                .header("X-Application-Id", "a8605824")
                .header("X-Api-Key", "390add03321ba0b75ceda50d6a0baa82")
                .POST(HttpRequest.BodyPublishers.ofString(requestString))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(response.body());
    }
}

package irl.lyit.DublinSmartHouseSearch.service.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public final class TimeTravelMapIsochroneHTTPClient {

    private final String requestString;
    private final String apiKey;
    private final String applicationId;

    public TimeTravelMapIsochroneHTTPClient(String requestString, String apiKey, String applicationId) {
        this.apiKey = apiKey;
        this.applicationId = applicationId;
        this.requestString = requestString;
    }

    public JsonNode requestIsochroneMap() throws IOException, InterruptedException {
        String postEndpoint = "http://api.traveltimeapp.com/v4/time-map";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(postEndpoint))
                .header("Content-Type", "application/json")
                .header("X-Application-Id", applicationId)
                .header("X-Api-Key", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestString))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(response.body());
    }
}

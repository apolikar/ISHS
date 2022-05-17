package irl.lyit.DublinSmartHouseSearch.service.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import irl.lyit.DublinSmartHouseSearch.config.Credentials;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class GMapsHTTPClient {

    @Autowired
    private Credentials credentials;

    public JsonNode requestAddressInfo(String address) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://maps.googleapis.com/maps/api/geocode/json?address=" +
                        address + "Ireland&key=" + credentials.getGoogleApiKey()))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(response.body());
    }


}

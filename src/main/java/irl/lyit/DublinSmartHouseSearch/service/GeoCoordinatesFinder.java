package irl.lyit.DublinSmartHouseSearch.service;

import com.fasterxml.jackson.databind.JsonNode;
import irl.lyit.DublinSmartHouseSearch.old.GeoCoordinates;
import irl.lyit.DublinSmartHouseSearch.service.addressFormatter.GoogleAddressFormatter;
import irl.lyit.DublinSmartHouseSearch.service.client.GMapsHTTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;


@Service
public class GeoCoordinatesFinder {

    private final GoogleAddressFormatter addressFormatter;
    private final GMapsHTTPClient gMapsHTTPClient;


    @Autowired
    public GeoCoordinatesFinder(GoogleAddressFormatter googleAddressFormatter, GMapsHTTPClient gMapsHTTPClient) {

        this.addressFormatter = googleAddressFormatter;
        this.gMapsHTTPClient = gMapsHTTPClient;
    }

    public GeoCoordinates getCoordinates(String address) throws IOException, InterruptedException {


        String formattedAddress = addressFormatter.formatAddress(address);
        JsonNode jsonNode = gMapsHTTPClient.finalDestination(formattedAddress);

        double lat = jsonNode.get("lat").asDouble();
        double lng = jsonNode.get("lng").asDouble();

        return new GeoCoordinates(lat, lng);
    }


}

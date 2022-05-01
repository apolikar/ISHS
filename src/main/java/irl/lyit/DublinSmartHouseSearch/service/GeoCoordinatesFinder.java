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

    @Autowired
    private  GoogleAddressFormatter addressFormatter;
    @Autowired
    private  GMapsHTTPClient gMapsHTTPClient;


    public GeoCoordinates getUserAddressCoordinates(String address)
            throws IOException, InterruptedException {

        String formattedAddress = addressFormatter.formatAddress(address);
        JsonNode jsonNode = gMapsHTTPClient.requestAddressInfo(formattedAddress);

        GeoCoordinates result = new GeoCoordinates();

        if(!jsonNode.toString().contains("natural_feature")) {
            jsonNode = jsonNode.get("results").get(0).get("geometry").get("location");
            result.setLat(jsonNode.get("lat").asDouble());
            result.setLng(jsonNode.get("lng").asDouble());
        }

        return result;
    }


}

package irl.lyit.DublinSmartHouseSearch.service.geoCoordinates;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import irl.lyit.DublinSmartHouseSearch.service.addressFormatter.GoogleAddressFormatter;
import irl.lyit.DublinSmartHouseSearch.service.client.GMapsHTTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.sql.Array;


@Service
public class AddressGeoCoordinatesFinder {


    private  GoogleAddressFormatter addressFormatter;
    private  GMapsHTTPClient gMapsHTTPClient;

    @Autowired
    public void setAddressFormatter(GoogleAddressFormatter addressFormatter) {
        this.addressFormatter = addressFormatter;
    }

    @Autowired
    public void setgMapsHTTPClient(GMapsHTTPClient gMapsHTTPClient) {
        this.gMapsHTTPClient = gMapsHTTPClient;
    }

    public GeoCoordinates getUserAddressCoordinates(String address)
            throws IOException, InterruptedException {

        String formattedAddress = addressFormatter.formatAddress(address);
        JsonNode jsonNode = gMapsHTTPClient.requestAddressInfo(formattedAddress);

        GeoCoordinates result = new GeoCoordinates();

        if(!jsonNode.toString().contains("natural_feature")) {
            JsonNode results = jsonNode.get("results");

            if (!(results instanceof ArrayNode) || results.size() == 0) {
                return result;
            }


            jsonNode = results.get(0).get("geometry").get("location");
            result.setLat(jsonNode.get("lat").asDouble());
            result.setLng(jsonNode.get("lng").asDouble());
        }

        return result;
    }


}

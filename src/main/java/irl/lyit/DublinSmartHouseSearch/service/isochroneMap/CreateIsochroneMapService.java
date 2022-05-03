package irl.lyit.DublinSmartHouseSearch.service.isochroneMap;

import com.fasterxml.jackson.databind.JsonNode;
import irl.lyit.DublinSmartHouseSearch.config.Credentials;
import irl.lyit.DublinSmartHouseSearch.service.geoCoordinates.GeoCoordinates;
import irl.lyit.DublinSmartHouseSearch.service.client.TimeTravelMapIsochroneHTTPClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreateIsochroneMapService {

    public List<BoundingBox> boundingBox(
            SearchAttributes searchAttributes,
            Credentials credentials
    ) throws IOException, InterruptedException {

        JsonNode allShapes = getAllShapesNode(
                searchAttributes,
                credentials
        );
        List<List<GeoCoordinates>> allShellsList = setupEachShellBoundingBox(allShapes);
        return new CalculateBoundingBox(allShellsList).calculateBoundingBoxResult();
    }


    private String generateRequestString(SearchAttributes searchAttributes){

        return "{\n" +
                "  \"arrival_searches\": [\n" +
                "    {\n" +
                "      \"id\": \"isochrone-0\",\n" +
                "      \"coords\": {\n" +
                "        \"lat\": " + searchAttributes.getCoordinates().getLat() + ",\n" +
                "        \"lng\": " + searchAttributes.getCoordinates().getLng() + "\n" +
                "      },\n" +
                "      \"travel_time\": " + searchAttributes.getTimeLimit() + ",\n" +
                "      \"transportation\": {\n" +
                "        \"type\": " + '"' + searchAttributes.getTransportationType() + '"' + "\n" +
                "      },\n" +
                "      \"arrival_time\": " + '"' + searchAttributes.getDateAndTime() + '"' + "\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }


    private JsonNode getAllShapesNode(
            SearchAttributes searchAttributes,
            Credentials credentials
    ) throws IOException, InterruptedException {
        TimeTravelMapIsochroneHTTPClient client = new TimeTravelMapIsochroneHTTPClient(
                generateRequestString(searchAttributes),
                credentials.getTravelTimeApiKey(),
                credentials.getTravelTimeApplicationId()
        );
        JsonNode response = client.requestIsochroneMap();
        JsonNode resultsNode = response.get("results");
        JsonNode firstResultNode = resultsNode.get(0);

        return firstResultNode.get("shapes");
    }


    private List<List<GeoCoordinates>> setupEachShellBoundingBox(
            JsonNode allShapes) {

        System.out.println(allShapes);


        List<List<GeoCoordinates>> coordinatesList = new ArrayList<>();

        // loop through all isochrone map sections
        for (int i = 0; i < allShapes.size(); i++) {

            List<GeoCoordinates> shellList = new ArrayList<>();

            // single isochrone map section
            JsonNode shell = allShapes.get(i).get("shell");


            for (int z = 0; z < shell.size(); z++) {
                JsonNode tempCoord = shell.get(z);
                double lat = tempCoord.get("lat").asDouble();
                double lng = tempCoord.get("lng").asDouble();
                shellList.add(new GeoCoordinates(lat, lng));
            }

            coordinatesList.add(shellList);
        }

        return coordinatesList;
    }



}

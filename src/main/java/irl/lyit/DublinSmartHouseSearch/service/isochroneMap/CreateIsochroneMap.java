package irl.lyit.DublinSmartHouseSearch.service.isochroneMap;

import com.fasterxml.jackson.databind.JsonNode;
import irl.lyit.DublinSmartHouseSearch.config.Credentials;
import irl.lyit.DublinSmartHouseSearch.old.GeoCoordinates;
import irl.lyit.DublinSmartHouseSearch.service.client.TimeTravelMapIsochroneHTTPClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateIsochroneMap {

    private final GeoCoordinates address;
    private final String dateAndTime;
    private final String transportType;
    private final Long timeLimit;
    private final Credentials credentials;

    public CreateIsochroneMap(GeoCoordinates address,
                              String dateAndTime,
                              String transportType,
                              Long timeLimit,
                              Credentials credentials
    ) {
        this.address = address;
        this.dateAndTime = dateAndTime;
        this.transportType = transportType;
        this.timeLimit = timeLimit;
        this.credentials = credentials;
    }

    private String generateRequestString() {

        return "{\n" +
                "  \"arrival_searches\": [\n" +
                "    {\n" +
                "      \"id\": \"isochrone-0\",\n" +
                "      \"coords\": {\n" +
                "        \"lat\": " + address.getLat() + ",\n" +
                "        \"lng\": " + address.getLng() + "\n" +
                "      },\n" +
                "      \"travel_time\": " + timeLimit + ",\n" +
                "      \"transportation\": {\n" +
                "        \"type\": " + '"' + transportType + '"' + "\n" +
                "      },\n" +
                "      \"arrival_time\": " + '"' + dateAndTime + '"' + "\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }


    private JsonNode getAllShapesNode() throws IOException, InterruptedException {
        TimeTravelMapIsochroneHTTPClient client = new TimeTravelMapIsochroneHTTPClient(
                generateRequestString(),
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

    public List<BoundingBox> boundingBox() throws IOException, InterruptedException {

        JsonNode allShapes = getAllShapesNode();
        List<List<GeoCoordinates>> allShellsList = setupEachShellBoundingBox(allShapes);
        return new CalculateBoundingBox(allShellsList).calculateBoundingBoxResult();
    }


}

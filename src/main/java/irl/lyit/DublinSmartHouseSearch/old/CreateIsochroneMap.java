package irl.lyit.DublinSmartHouseSearch.old;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateIsochroneMap {

  private GeoCoordinates address;

  public CreateIsochroneMap(GeoCoordinates address) {
    this.address = address;
  }


  public CreateIsochroneMap() {
  }


  public List<BoundingBox> boundingBox() throws IOException, InterruptedException {

    List<BoundingBox> result = new ArrayList<>();

    String postEndpoint = "http://api.traveltimeapp.com/v4/time-map";
    String inputJson = "{\n" +
            "  \"arrival_searches\": [\n" +
            "    {\n" +
            "      \"id\": \"isochrone-0\",\n" +
            "      \"coords\": {\n" +
            "        \"lat\": " + address.getLat() +",\n" +
            "        \"lng\": " + address.getLng() + "\n" +
            "      },\n" +
            "      \"travel_time\": 1800,\n" +
            "      \"transportation\": {\n" +
            "        \"type\": \"public_transport\"\n" +
            "      },\n" +
            "      \"arrival_time\": \"2022-03-26T11:00:00.000Z\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";


    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(postEndpoint))
            .header("Content-Type", "application/json")
            .header("X-Application-Id", "a8605824")
            .header("X-Api-Key", "390add03321ba0b75ceda50d6a0baa82")
            .POST(HttpRequest.BodyPublishers.ofString(inputJson))
            .build();


    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(request, HttpResponse.BodyHandlers.ofString());



    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(response.body());

    List<GeoCoordinates> coordinatesList = new ArrayList<>();

    jsonNode = jsonNode.get("results").get(0).get("shapes");

    for( int i = 0; i < jsonNode.size(); i++) {

      JsonNode shell = jsonNode.get(i).get("shell");
      System.out.println(jsonNode.get(i));


      for (int z = 0; z < shell.size(); z++) {
        JsonNode tempCoord = shell.get(z);
        double lat = tempCoord.get("lat").asDouble();
        double lng = tempCoord.get("lng").asDouble();
        coordinatesList.add(new GeoCoordinates(lat, lng));
      }


      double minLat = coordinatesList.get(0).getLat();
      double maxLat = coordinatesList.get(0).getLat();
      double minLng = coordinatesList.get(0).getLng();
      double maxLng = coordinatesList.get(0).getLng();

      for(GeoCoordinates elem : coordinatesList) {

        minLat = Math.min(minLat, elem.getLat());
        maxLat = Math.max(maxLat, elem.getLat());
        minLng = Math.min(minLng, elem.getLng());
        maxLng = Math.max(maxLng, elem.getLng());

      }

      coordinatesList = new ArrayList<>();

      GeoCoordinates bottom = new GeoCoordinates(minLat, minLng);
      GeoCoordinates top = new GeoCoordinates(maxLat, maxLng);

      BoundingBox boundingBox = new BoundingBox(bottom, top);
      result.add(boundingBox);
    }

    return result;
  }

}

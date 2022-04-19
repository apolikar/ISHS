package irl.lyit.DublinSmartHouseSearch.old;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class CreateIsochroneMap {

  private GeoCoordinates address;
  private String dateAndTime;
  private String transportType;
  private Long timeLimit;

  public CreateIsochroneMap(GeoCoordinates address, String dateAndTime, String transportType, Long timeLimit) {
    this.address = address;
    this.dateAndTime = dateAndTime;
    this.transportType = transportType;
    this.timeLimit = timeLimit;
  }

  public CreateIsochroneMap() {
  }


  private String generateRequestString () {

    return "{\n" +
           "  \"arrival_searches\": [\n" +
           "    {\n" +
           "      \"id\": \"isochrone-0\",\n" +
           "      \"coords\": {\n" +
           "        \"lat\": " + address.getLat() +",\n" +
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

  private String generateResponseString() throws IOException, InterruptedException {
    String postEndpoint = "http://api.traveltimeapp.com/v4/time-map";

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(postEndpoint))
            .header("Content-Type", "application/json")
            .header("X-Application-Id", "a8605824")
            .header("X-Api-Key", "390add03321ba0b75ceda50d6a0baa82")
            .POST(HttpRequest.BodyPublishers.ofString(generateRequestString()))
            .build();


    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(request, HttpResponse.BodyHandlers.ofString());

    System.out.println(response.body());
    return response.body();
  }


  public List<BoundingBox> boundingBox() throws IOException, InterruptedException {

    List<BoundingBox> result = new ArrayList<>();

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(generateResponseString());


    List<GeoCoordinates> coordinatesList = new ArrayList<>();

    jsonNode = jsonNode.get("results").get(0).get("shapes");


    for( int i = 0; i < jsonNode.size(); i++) {

      JsonNode shell = jsonNode.get(i).get("shell");

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
    System.out.println(result);
    return result;
  }

}

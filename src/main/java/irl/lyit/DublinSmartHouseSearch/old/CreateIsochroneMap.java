package irl.lyit.DublinSmartHouseSearch.old;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class CreateIsochroneMap {

  public static void main(String[] args) throws IOException, InterruptedException {

    String postEndpoint = "http://api.traveltimeapp.com/v4/time-map";
    String inputJson = "{\n" +
            "  \"arrival_searches\": [\n" +
            "    {\n" +
            "      \"id\": \"isochrone-0\",\n" +
            "      \"coords\": {\n" +
            "        \"lat\": 53.2789047,\n" +
            "        \"lng\": -9.0108671\n" +
            "      },\n" +
            "      \"travel_time\": 1800,\n" +
            "      \"transportation\": {\n" +
            "        \"type\": \"public_transport\"\n" +
            "      },\n" +
            "      \"arrival_time\": \"2022-03-22T09:00:00.000Z\"\n" +
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
//    System.out.println(response.statusCode());
//    System.out.println(response.body());

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(response.body());

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

      System.out.println(elem);

    }

    System.out.println();
    System.out.println("minimum lat : " + minLat);
    System.out.println("maximum lat : " + maxLat);
    System.out.println("minimum lng : " + minLng);
    System.out.println("maximum lng : " + maxLng);

  }

}

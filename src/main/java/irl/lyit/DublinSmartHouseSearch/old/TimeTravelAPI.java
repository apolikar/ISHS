package irl.lyit.DublinSmartHouseSearch.old;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveltime.sdk.TravelTimeSDK;
import com.traveltime.sdk.auth.TravelTimeCredentials;
import com.traveltime.sdk.dto.common.Coordinates;
import com.traveltime.sdk.dto.common.transportation.PublicTransport;
import com.traveltime.sdk.dto.requests.GeocodingRequest;
import com.traveltime.sdk.dto.requests.TimeMapBoxesRequest;
import com.traveltime.sdk.dto.requests.TimeMapRequest;
import com.traveltime.sdk.dto.requests.timemap.ArrivalSearch;
import com.traveltime.sdk.dto.responses.TimeMapResponse;
import com.traveltime.sdk.dto.responses.errors.TravelTimeError;
import io.vavr.control.Either;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

public class TimeTravelAPI {

  public static void main(String[] args) throws JsonProcessingException {

    TravelTimeCredentials credentials = new TravelTimeCredentials("a8605824", "390add03321ba0b75ceda50d6a0baa82");
    TravelTimeSDK sdk = new TravelTimeSDK(credentials);

    ArrivalSearch arrivalSearch1 = ArrivalSearch
        .builder()
        .id("Travel to work")
        .coords(new Coordinates(53.4419137, -6.2028496))
        .transportation(PublicTransport.builder().build())
        .arrivalTime(Instant.now())
        .travelTime(300)
        .build();


//    DepartureSearch departureSearch1 = DepartureSearch
//        .builder()
//        .id("Public transport from Trafalgar Square")
//        .coords(new Coordinates(51.507609, -0.128315))
//        .transportation(PublicTransport.builder().build())
//        .departureTime(Instant.now())
//        .travelTime(900)
//        .build();
//
//    DepartureSearch departureSearch2 = DepartureSearch
//        .builder()
//        .id("Driving from Trafalgar Square")
//        .coords(new Coordinates(51.507609, -0.128315))
//        .transportation(Driving.builder().build())
//        .departureTime(Instant.now())
//        .travelTime(900)
//        .build();
//
//    ArrivalSearch arrivalSearch = ArrivalSearch
//        .builder()
//        .id("Public transport to Trafalgar Square")
//        .coords(new Coordinates(51.507609, -0.128315))
//        .transportation(Driving.builder().build())
//        .arrivalTime(Instant.now())
//        .travelTime(900)
//        .build();
//
//    Union union = Union
//        .builder()
//        .id("Union of driving and public transport")
//        .searchIds(Arrays.asList("Public transport from Trafalgar Square", "Driving from Trafalgar Square"))
//        .build();
//
//    Intersection intersection = Intersection
//        .builder()
//        .id("Intersection of driving and public transport")
//        .searchIds(Arrays.asList("Public transport from Trafalgar Square", "Driving from Trafalgar Square"))
//        .build();
//
//    TimeMapRequest request = TimeMapRequest
//        .builder()
//        .departureSearches(Arrays.asList(departureSearch1, departureSearch2))
//        .arrivalSearches(Collections.singletonList(arrivalSearch))
//        .unions(Collections.singletonList(union))
//        .intersections(Collections.singletonList(intersection))
//        .build();
//
//    Either<TravelTimeError, TimeMapResponse> response = sdk.send(request);
//
//    if(response.isRight()) {
//      System.out.println(response.get().getResults().size());
//      System.out.println(response);
//    } else {
//      System.out.println(response.getLeft().getMessage());
//    }

    TimeMapRequest request = TimeMapRequest
        .builder()
        .arrivalSearches(Collections.singletonList(arrivalSearch1))
        .build();

    Either<TravelTimeError, TimeMapResponse> response = sdk.send(request);

    if(response.isRight()) {

      String result = response.get().getResults().toString();
      System.out.println(result);
      int index = result.indexOf("shell");
      String shell = result.substring(index, result.indexOf(']') + 1);

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode node = objectMapper.readTree(shell);
      System.out.println(node.get("shell"));
    } else {
      System.out.println(response.getLeft().getMessage());
    }
  }


}

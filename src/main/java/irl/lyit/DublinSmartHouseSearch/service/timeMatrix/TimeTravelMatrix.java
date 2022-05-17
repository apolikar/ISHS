package irl.lyit.DublinSmartHouseSearch.service.timeMatrix;


import com.fasterxml.jackson.databind.JsonNode;
import irl.lyit.DublinSmartHouseSearch.config.Credentials;
import irl.lyit.DublinSmartHouseSearch.controller.exception.TooManyPointsException;
import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.service.geoCoordinates.GeoCoordinates;
import irl.lyit.DublinSmartHouseSearch.presentation.searchPanel.ResultMatchHouse;
import irl.lyit.DublinSmartHouseSearch.service.client.TimeTravelTimeMatrixHTTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TimeTravelMatrix {
    private Logger log = LoggerFactory.getLogger(TimeTravelMatrix.class);
    private static final int API_LIMIT = 2000;
    private Credentials credentials;


    public TimeTravelMatrix(Credentials credentials) {
        this.credentials = credentials;
    }


    public List<ResultMatchHouse> getInTime(
            GeoCoordinates destinationCoordinates,
            List<House> list,
            String transportTime,
            long travelTime,
            String dateAndTravelTime
    ) throws IOException, InterruptedException, TooManyPointsException {

        // if list size is less than limit
        if(list.size() <= API_LIMIT) {
            return houseInRangeCheck(
                    destinationCoordinates,
                    list, transportTime,
                    travelTime,
                    dateAndTravelTime);
        }

        int start = 0;
        int end = 50;

        List<ResultMatchHouse> result = new ArrayList<>();
        try {
            while (start < list.size()) {
                result.addAll(
                        houseInRangeCheck(
                                destinationCoordinates,
                                list.subList(start, end - 1),
                                transportTime,
                                travelTime,
                                dateAndTravelTime
                        ));
                start = end;
                end += API_LIMIT;

                if (end > list.size()){
                    end = list.size();
                }
            }
        } catch (TooManyPointsException e) {
            if (result.isEmpty()) {
               throw e;
            }

            // if result list has some houses already
            // made because of time travel api limitation per minute
            return result;
        }

        return result;
    }

    private List<ResultMatchHouse> houseInRangeCheck(
            GeoCoordinates startingPoint,
            List<House> list,
            String transportTime,
            long travelTime,
            String dateAndTravelTime
    ) throws IOException, InterruptedException, TooManyPointsException {

        TimeMatrixRequestStringGenerator apiRequestStringGenerator = new TimeMatrixRequestStringGenerator();
        String requestString = apiRequestStringGenerator.generateRequestString(
                startingPoint,
                list,
                transportTime,
                travelTime,
                dateAndTravelTime
        );

        TimeTravelTimeMatrixHTTPClient client = new TimeTravelTimeMatrixHTTPClient(
                requestString,
                credentials.getTravelTimeApiKey(),
                credentials.getTravelTimeApplicationId()
        );

        JsonNode response = client.generateInTimeJsonResult();
        if (response.get("http_status") != null
                && response.get("http_status").asInt() != HttpStatus.OK.value()
        ) {
            log.error(response.get("description").asText());
            throw new TooManyPointsException();
        }
        JsonNode locationsTravelTimeWithinBoundingBox = response.get("results").get(0).get("locations");

        return new PropertiesInTimeList(locationsTravelTimeWithinBoundingBox, list).returnResultMatchHouseList();
    }

}
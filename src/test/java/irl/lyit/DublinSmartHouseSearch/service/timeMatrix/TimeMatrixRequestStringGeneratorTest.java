package irl.lyit.DublinSmartHouseSearch.service.timeMatrix;

import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.service.geoCoordinates.GeoCoordinates;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class TimeMatrixRequestStringGeneratorTest extends TestCase {

    public void testGenerateRequestJsonString() {

        TimeMatrixRequestStringGenerator testRequestGenerator = new TimeMatrixRequestStringGenerator();

        List<House> testHouseList = new ArrayList<>();
        testHouseList.add( new House(
                "link",
                2,
                "address",
                "Donegal",
                3,
                51.51987769050541,
                -0.12230083149308632,
                165
                )
        );

        String actualResult = testRequestGenerator.generateRequestString(
                new GeoCoordinates(51.5119637, -0.1279543),
                testHouseList,
                "public_transport",
                1800,
                "2022-05-02T08:00:00.000Z"

        );

        String expectedResult = "{\n" +
                "  \"locations\": [\n" +
                "    {\n" +
                "      \"id\": \"starting-location\",\n" +
                "      \"coords\": {\n" +
                "        \"lat\": 51.5119637,\n" +
                "        \"lng\": -0.1279543\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"other-location-0\",\n" +
                "      \"coords\": {\n" +
                "        \"lat\": 51.51987769050541,\n" +
                "        \"lng\": -0.12230083149308632\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"departure_searches\": [\n" +
                "    {\n" +
                "      \"transportation\": {\n" +
                "        \"type\": \"public_transport\"\n" +
                "      },\n" +
                "      \"travel_time\": 1800,\n" +
                "      \"properties\": [\n" +
                "        \"travel_time\"\n" +
                "      ],\n" +
                "      \"id\": \"Departure search\",\n" +
                "      \"arrival_location_ids\": [\n" +
                "        \"other-location-0\"\n" +
                "      ],\n" +
                "      \"departure_location_id\": \"starting-location\",\n" +
                "      \"departure_time\": \"2022-05-02T08:00:00.000Z\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        assertEquals(expectedResult, actualResult);


    }
}
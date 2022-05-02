package irl.lyit.DublinSmartHouseSearch.service.timeMatrix;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.service.ResultMatchHouse;
import junit.framework.TestCase;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PropertiesInTimeListTest extends TestCase {


    private House h1 =  new House("link", 100.00,
            "address", "Donegal", 1, 5, -10, 100);
    private House h2 = new House("link", 200.00,
            "address", "Donegal", 2, 6, -11, 100);
    private House h3 = new House("link", 300.00,
            "address", "Donegal", 3, 7, -12, 100);
    private House h4 =  new House("link", 400.00,
            "address", "Donegal", 4, 8, -13, 100);


    public void testReturnResultMatchHouseList() throws IOException {

        String jsonNode = "[" +
                "{\"id\":\"other-location-0\",\"properties\":[{\"travel_time\":1033}]}," +
                "{\"id\":\"other-location-2\",\"properties\":[{\"travel_time\":226}]}" +
                "]";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode testNode = mapper.readTree(jsonNode);
        List<House> testHouseList = Arrays.asList(h1, h2, h3, h4);


        List<ResultMatchHouse> expectedResult = Arrays.asList(
                new ResultMatchHouse(h1, 1033),
                new ResultMatchHouse(h3, 226)
        );

        PropertiesInTimeList test = new PropertiesInTimeList(testNode, testHouseList);
        List<ResultMatchHouse> actualResult = test.returnResultMatchHouseList();

        assertEquals(expectedResult.size(), actualResult.size());
        assertEquals(2, actualResult.size());

        assertEquals(expectedResult.get(0).getSecondsToTravel(), actualResult.get(0).getSecondsToTravel());
        assertEquals(expectedResult.get(0).getHouse().getPrice(), actualResult.get(0).getHouse().getPrice());

        assertEquals(expectedResult.get(1).getSecondsToTravel(), actualResult.get(1).getSecondsToTravel());
        assertEquals(expectedResult.get(1).getHouse().getPrice(), actualResult.get(1).getHouse().getPrice());


    }
}
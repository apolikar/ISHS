package irl.lyit.DublinSmartHouseSearch.service;

import irl.lyit.DublinSmartHouseSearch.config.Credentials;
import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.old.GeoCoordinates;
import irl.lyit.DublinSmartHouseSearch.old.SearchAttributes;
import irl.lyit.DublinSmartHouseSearch.service.isochroneMap.BoundingBox;
import junit.framework.TestCase;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HousesInBoundingBoxListTest extends TestCase {

    private Credentials credentials = new Credentials();
    private House h1 =  new House("link", 100.00,
            "address", "Donegal", 1, 1, -10, 100);
    private House h2 = new House("link", 200.00,
            "address", "Donegal", 2, 2, -11, 100);
    private House h3 = new House("link", 300.00,
            "address", "Donegal", 3, 3, -12, 100);
    private House h4 =  new House("link", 400.00,
            "address", "Donegal", 4, 4, -13, 100);
    private House h5 =  new House("link", 500.00,
            "address", "Donegal", 1, 5, -10, 100);
    private House h6 = new House("link", 600.00,
            "address", "Donegal", 2, 6, -11, 100);
    private House h7 = new House("link", 700.00,
            "address", "Donegal", 3, 7, -12, 100);
    private House h8 =  new House("link", 300.00,
            "address", "Donegal", 4, 8, -13, 100);



    public void testGetHousesInBoundingBoxList() throws IOException, InterruptedException {

//        List<House> testHouseList = Arrays.asList(h1, h2, h3, h4, h5, h6, h7, h8);
//        List<BoundingBox> testBoundingBoxList = Arrays.asList(
//                new BoundingBox(
//                        new GeoCoordinates(2, -17),
//                        new GeoCoordinates(4, -10)
//                ),
//                new BoundingBox(
//                        new GeoCoordinates(7, -20),
//                        new GeoCoordinates(10, -11)
//                )
//        );
//        SearchAttributes testSearchAttributes = new SearchAttributes(
//                new GeoCoordinates(2, -3),
//                "some date and time string",
//                300,
//                "some transport",
//                200,
//                400,
//                2,
//                4,
//                credentials
//        );
//
//        List<House> expectedResult = Arrays.asList(h2, h3, h4, h8);
//
//        HousesInBoundingBoxList housesInBoundingBoxList = new HousesInBoundingBoxList(
//                testHouseList,
//                testBoundingBoxList,
//                testSearchAttributes
//                );
//        List<House> actualResult = housesInBoundingBoxList.getHousesInBoundingBoxList();
//
//        assertEquals(expectedResult, actualResult);

    }
}
package irl.lyit.DublinSmartHouseSearch.old;

import irl.lyit.DublinSmartHouseSearch.service.geoCoordinates.GeoCoordinates;
import irl.lyit.DublinSmartHouseSearch.service.isochroneMap.SearchAttributes;
import junit.framework.TestCase;

import java.io.IOException;

public class SearchAttributesTest extends TestCase {

    public void testSearchAttributesConstructor() throws IOException, InterruptedException {


        SearchAttributes searchAttributes = new SearchAttributes(
            new GeoCoordinates(52.1383154, -8.6603111),
                "2022-05-01T08:00:00.000Z",
                1800,
                "public_transport",
                5,
                10,
                1,
                3
        );

        assertEquals("Time Limit Test", 1800, searchAttributes.getTimeLimit());
        // finish all fields




    }
}
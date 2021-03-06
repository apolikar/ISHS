package irl.lyit.DublinSmartHouseSearch.old;

import irl.lyit.DublinSmartHouseSearch.service.geoCoordinates.GeoCoordinates;
import irl.lyit.DublinSmartHouseSearch.service.isochroneMap.SearchAttributeHousePriceType;
import irl.lyit.DublinSmartHouseSearch.service.isochroneMap.SearchAttributes;
import junit.framework.TestCase;

import java.io.IOException;

public class SearchAttributesTest extends TestCase {

    public void testSearchAttributesConstructor() throws IOException, InterruptedException {


        SearchAttributes searchAttributes = new SearchAttributes(
            new GeoCoordinates(52.1383154, -8.6603111),
                "2022-05-01T08:00:00.000Z",
                30,
                "public_transport",
                SearchAttributeHousePriceType.PRICE_1_5M,
                SearchAttributeHousePriceType.PRICE_2M,
                1,
                3
        );

        assertEquals("Time Limit Test", 1800L, (long) searchAttributes.getTimeLimit());
        // finish all fields




    }
}
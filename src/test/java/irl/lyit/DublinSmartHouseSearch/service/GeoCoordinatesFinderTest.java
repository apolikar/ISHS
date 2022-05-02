package irl.lyit.DublinSmartHouseSearch.service;

import irl.lyit.DublinSmartHouseSearch.config.Credentials;
import irl.lyit.DublinSmartHouseSearch.old.GeoCoordinates;
import irl.lyit.DublinSmartHouseSearch.service.addressFormatter.AddressFormatter;
import irl.lyit.DublinSmartHouseSearch.service.addressFormatter.GoogleAddressFormatter;
import irl.lyit.DublinSmartHouseSearch.service.client.GMapsHTTPClient;
import junit.framework.TestCase;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

public class GeoCoordinatesFinderTest extends TestCase {

    @SpringBean
    private Credentials credentials;

    @Test
    public void testGetUserAddressCoordinates() throws IOException, InterruptedException {

//        GeoCoordinatesFinder geoCoordinatesFinder = new GeoCoordinatesFinder();
//
//        String address = "13+Westbury+Heights";
//        GeoCoordinates expectedResult = new GeoCoordinates(52.1383154, -8.6603111);
//
//        GeoCoordinates actualResult = geoCoordinatesFinder.getUserAddressCoordinates(address);
//
//        assertEquals(expectedResult, actualResult);

    }
}
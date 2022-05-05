package irl.lyit.DublinSmartHouseSearch.service;

import com.fasterxml.jackson.databind.JsonNode;
import irl.lyit.DublinSmartHouseSearch.service.geoCoordinates.GeoCoordinates;
import irl.lyit.DublinSmartHouseSearch.service.addressFormatter.GoogleAddressFormatter;
import irl.lyit.DublinSmartHouseSearch.service.client.GMapsHTTPClient;
import irl.lyit.DublinSmartHouseSearch.service.geoCoordinates.AddressGeoCoordinatesFinder;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class AddressGeoCoordinatesFinderTest {



    @Test
    public void testGetUserAddressCoordinates() throws IOException, InterruptedException {

        AddressGeoCoordinatesFinder addressGeoCoordinatesFinder = new AddressGeoCoordinatesFinder();

        GoogleAddressFormatter googleAddressFormater = mock(GoogleAddressFormatter.class);
        addressGeoCoordinatesFinder.setAddressFormatter(googleAddressFormater);
        when(googleAddressFormater.formatAddress(anyString())).thenReturn("13+Westbury+Heights");

        GMapsHTTPClient gMapsHTTPClient = mock(GMapsHTTPClient.class);
        addressGeoCoordinatesFinder.setgMapsHTTPClient(gMapsHTTPClient);

        JsonNode jsonNode = mock(JsonNode.class);
        when(gMapsHTTPClient.requestAddressInfo(anyString())).thenReturn(jsonNode);

        String address = "13+Westbury+Heights";
        GeoCoordinates expectedResult = new GeoCoordinates(10.0, 10.0);

        when(jsonNode.get(any())).thenReturn(jsonNode);
        when(jsonNode.get(0)).thenReturn(jsonNode);
        when(jsonNode.toString()).thenReturn("abc_test");
        when(jsonNode.asDouble()).thenReturn(10.0);

        GeoCoordinates actualResult = addressGeoCoordinatesFinder.getUserAddressCoordinates(address);

        // we specify that every time we call method user address get coordinates
        // from GoogleAddressFormatter at least once method format address were called
        verify(googleAddressFormater, times(1)).formatAddress(address);

        assertEquals(expectedResult, actualResult);

    }
}
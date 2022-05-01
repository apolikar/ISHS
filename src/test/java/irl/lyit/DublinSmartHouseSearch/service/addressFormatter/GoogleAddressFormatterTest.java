package irl.lyit.DublinSmartHouseSearch.service.addressFormatter;

import junit.framework.TestCase;
import org.junit.Test;

public class GoogleAddressFormatterTest extends TestCase {

    @Test
    public void testFormatAddress() {


        GoogleAddressFormatter googleAddressFormatter = new GoogleAddressFormatter();

        String expectedResult = "Baggot+Plaza+";
        String actualResult = googleAddressFormatter.formatAddress("Baggot Plaza");

        assertEquals(expectedResult, actualResult);
    }
}
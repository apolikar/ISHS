package irl.lyit.DublinSmartHouseSearch.service.addressFormatter;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class GoogleAddressFormatterTest {

    @Test
    public void testFormatAddress() {


        GoogleAddressFormatter googleAddressFormatter = new GoogleAddressFormatter();

        String expectedResult = "Baggot+Plaza+";
        String actualResult = googleAddressFormatter.formatAddress("Baggot Plaza");

        Assert.assertEquals(expectedResult, actualResult);
    }
}
package irl.lyit.DublinSmartHouseSearch.service.daftScrapper;

import junit.framework.TestCase;

public class TimeInMillisecondsUserFriendlyInfoTest extends TestCase {

    public void testGetTimeInfoString() {

        long timeInMilliseconds = 1233000L;
        String expectedResult = "20 minutes 33 seconds";

        TimeInMillisecondsUserFriendlyInfo testTime = new TimeInMillisecondsUserFriendlyInfo(timeInMilliseconds);
        String actualResult = testTime.getTimeInfoString();

        assertEquals(expectedResult, actualResult);
    }
}
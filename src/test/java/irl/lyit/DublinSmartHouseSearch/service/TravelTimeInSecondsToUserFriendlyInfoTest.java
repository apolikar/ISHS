package irl.lyit.DublinSmartHouseSearch.service;

import junit.framework.TestCase;

public class TravelTimeInSecondsToUserFriendlyInfoTest extends TestCase {

    public void testGetUserFriendlyTimeInfoBelowTen() {

        long testTravelTimeInSeconds = 568;

        String expectedResult = "09:28";

        TravelTimeInSecondsToUserFriendlyInfo travelTimeInSecondsToUserFriendlyInfo =
                new TravelTimeInSecondsToUserFriendlyInfo(testTravelTimeInSeconds);
        String actualResult = travelTimeInSecondsToUserFriendlyInfo.getUserFriendlyTimeInfo();

        assertEquals(expectedResult, actualResult);
    }

    public void testGetUserFriendlyTimeInfoAfterTen() {

        long testTravelTimeInSeconds = 1240;

        String expectedResult = "20:40";

        TravelTimeInSecondsToUserFriendlyInfo travelTimeInSecondsToUserFriendlyInfo =
                new TravelTimeInSecondsToUserFriendlyInfo(testTravelTimeInSeconds);
        String actualResult = travelTimeInSecondsToUserFriendlyInfo.getUserFriendlyTimeInfo();

        assertEquals(expectedResult, actualResult);
    }
}
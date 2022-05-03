package irl.lyit.DublinSmartHouseSearch.service.timeMatrix;

import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.presentation.homePanel.ResultMatchHouse;
import junit.framework.TestCase;

public class ResultMatchHouseConstructorTest extends TestCase {

    public void testResultMatchHouseConstructor() {

        ResultMatchHouse testObject = new ResultMatchHouse(
                new House(),
                1240
        );

        String expectedResult = "20:40";
        String actualResult = testObject.getTimeString();

        assertEquals(expectedResult, actualResult);
    }
}

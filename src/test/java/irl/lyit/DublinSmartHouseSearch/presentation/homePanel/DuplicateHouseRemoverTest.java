package irl.lyit.DublinSmartHouseSearch.presentation.homePanel;

import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.service.ResultMatchHouse;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class DuplicateHouseRemoverTest extends TestCase {


    //duplicate results
    private ResultMatchHouse r1 = new ResultMatchHouse(new House("link", 100.00,
            "address", "Donegal", 3, 5, -10, 100), 100);

    private ResultMatchHouse r2 = new ResultMatchHouse(new House("link", 200.00,
            "address", "Donegal", 3, 5, -10, 200), 100);

    // non-duplicate results
    private ResultMatchHouse r3 = new ResultMatchHouse(new House("link", 300.00,
            "address", "Donegal", 1, 6, -11, 100), 200);

    private ResultMatchHouse r4 = new ResultMatchHouse(new House("link", 400.00,
            "address", "Donegal", 2, 7, -12, 200), 300);


    @Test
    public void testRemoveDuplicatesStartLowerFist() {

        List<ResultMatchHouse> resultMatchHouseList = Arrays.asList(r1, r2, r3, r4);

        List<ResultMatchHouse> expectedResult = Arrays.asList(r2, r3, r4);
        List<ResultMatchHouse> actualResult = new DuplicateHouseRemover(resultMatchHouseList).removeDuplicates();

        assertEquals(expectedResult, actualResult);
    }


    public void testRemoveDuplicatesStartGreaterFist() {

        List<ResultMatchHouse> resultMatchHouseList = Arrays.asList(r2, r1, r3, r4);

        List<ResultMatchHouse> expectedResult = Arrays.asList(r2, r3, r4);
        List<ResultMatchHouse> actualResult = new DuplicateHouseRemover(resultMatchHouseList).removeDuplicates();

        assertEquals(expectedResult, actualResult);
    }


    @Test
    public void testRemoveDuplicatesMiddleLowerFist() {

        List<ResultMatchHouse> resultMatchHouseList = Arrays.asList(r3, r1, r2, r4);

        List<ResultMatchHouse> expectedResult = Arrays.asList(r3, r2, r4);
        List<ResultMatchHouse> actualResult = new DuplicateHouseRemover(resultMatchHouseList).removeDuplicates();

        assertEquals(expectedResult, actualResult);
    }


    @Test
    public void testRemoveDuplicatesMiddleGreaterFist() {

        List<ResultMatchHouse> resultMatchHouseList = Arrays.asList(r3, r2, r1, r4);

        List<ResultMatchHouse> expectedResult = Arrays.asList(r3, r2, r4);
        List<ResultMatchHouse> actualResult = new DuplicateHouseRemover(resultMatchHouseList).removeDuplicates();

        assertEquals(expectedResult, actualResult);
    }


    public void testRemoveDuplicatesEndLowerFist() {

        List<ResultMatchHouse> resultMatchHouseList = Arrays.asList(r3, r4, r1, r2);

        List<ResultMatchHouse> expectedResult = Arrays.asList(r3, r4, r2);
        List<ResultMatchHouse> actualResult = new DuplicateHouseRemover(resultMatchHouseList).removeDuplicates();

        assertEquals(expectedResult, actualResult);
    }

    public void testRemoveDuplicatesEndGreaterFist() {

        List<ResultMatchHouse> resultMatchHouseList = Arrays.asList(r3, r4, r2, r1);

        List<ResultMatchHouse> expectedResult = Arrays.asList(r3, r4, r2);
        List<ResultMatchHouse> actualResult = new DuplicateHouseRemover(resultMatchHouseList).removeDuplicates();

        assertEquals(expectedResult, actualResult);
    }


}
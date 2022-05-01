package irl.lyit.DublinSmartHouseSearch.old;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalculateBoundingBoxTest extends TestCase {

    @Test
    public void testCalculateBoundingBoxResult() {

        List<List<GeoCoordinates>> testList = new ArrayList<>();

        List<GeoCoordinates> firstList = Arrays.asList(
                new GeoCoordinates(1, -10),
                new GeoCoordinates(2, -11),
                new GeoCoordinates(3, -12),
                new GeoCoordinates(4, -13),
                new GeoCoordinates(5, -14)
        );
        testList.add(firstList);

        List<GeoCoordinates> secondList = Arrays.asList(
                new GeoCoordinates(6, -15),
                new GeoCoordinates(7, -16),
                new GeoCoordinates(8, -17),
                new GeoCoordinates(9, -18)
        );
        testList.add(secondList);

        List<BoundingBox> actualResult = new CalculateBoundingBox(testList).calculateBoundingBoxResult();
        List<BoundingBox> expectedResult = Arrays.asList(
                new BoundingBox(
                        new GeoCoordinates(1, -14),
                        new GeoCoordinates(5, -10)
                ),
                new BoundingBox(
                        new GeoCoordinates(6, -18),
                        new GeoCoordinates(9, -15)
                )
        );


        assertEquals(expectedResult.get(0).getBottom().getLat(), actualResult.get(0).getBottom().getLat());
        assertEquals(expectedResult.get(0).getBottom().getLng(), actualResult.get(0).getBottom().getLng());
        assertEquals(expectedResult.get(0).getTop().getLat(), actualResult.get(0).getTop().getLat());
        assertEquals(expectedResult.get(0).getTop().getLng(), actualResult.get(0).getTop().getLng());

        assertEquals(expectedResult.get(1).getBottom().getLat(), actualResult.get(1).getBottom().getLat());
        assertEquals(expectedResult.get(1).getBottom().getLng(), actualResult.get(1).getBottom().getLng());
        assertEquals(expectedResult.get(1).getTop().getLat(), actualResult.get(1).getTop().getLat());
        assertEquals(expectedResult.get(1).getTop().getLng(), actualResult.get(1).getTop().getLng());
    }
}
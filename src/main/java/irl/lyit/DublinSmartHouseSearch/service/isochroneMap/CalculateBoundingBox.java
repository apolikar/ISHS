package irl.lyit.DublinSmartHouseSearch.service.isochroneMap;

import irl.lyit.DublinSmartHouseSearch.old.GeoCoordinates;

import java.util.ArrayList;
import java.util.List;

public class CalculateBoundingBox {

    private List<List<GeoCoordinates>> coordinatesList;

    public CalculateBoundingBox(List<List<GeoCoordinates>> coordinatesList) {
        this.coordinatesList = coordinatesList;
    }

    public List<BoundingBox> calculateBoundingBoxResult() {


       List<BoundingBox> result = new ArrayList<>();

        for(List<GeoCoordinates> coordinatesSectionList : coordinatesList) {
            double minLat = coordinatesSectionList.get(0).getLat();
            double maxLat = coordinatesSectionList.get(0).getLat();
            double minLng = coordinatesSectionList.get(0).getLng();
            double maxLng = coordinatesSectionList.get(0).getLng();

            // finding borders
            for(GeoCoordinates elem : coordinatesSectionList) {
                minLat = Math.min(minLat, elem.getLat());
                maxLat = Math.max(maxLat, elem.getLat());
                minLng = Math.min(minLng, elem.getLng());
                maxLng = Math.max(maxLng, elem.getLng());
            }

            GeoCoordinates bottom = new GeoCoordinates(minLat, minLng);
            GeoCoordinates top = new GeoCoordinates(maxLat, maxLng);
            result.add(new BoundingBox(bottom, top));
        }

        return result;
    }
}

package irl.lyit.DublinSmartHouseSearch.service.isochroneMap;

import irl.lyit.DublinSmartHouseSearch.old.GeoCoordinates;

public class BoundingBox {


    GeoCoordinates bottom;
    GeoCoordinates top;

    public BoundingBox(GeoCoordinates bottom, GeoCoordinates top) {
        this.bottom = bottom;
        this.top = top;
    }

    public BoundingBox() {
    }

    public GeoCoordinates getBottom() {
        return bottom;
    }

    public GeoCoordinates getTop() {
        return top;
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
                "bottom=" + bottom +
                ", top=" + top +
                '}';
    }
}
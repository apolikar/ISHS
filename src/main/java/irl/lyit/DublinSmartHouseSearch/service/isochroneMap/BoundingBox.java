package irl.lyit.DublinSmartHouseSearch.service.isochroneMap;

import irl.lyit.DublinSmartHouseSearch.service.geoCoordinates.GeoCoordinates;

public class BoundingBox {


    GeoCoordinates bottom;
    GeoCoordinates top;

    public BoundingBox(GeoCoordinates bottom, GeoCoordinates top) {
        this.bottom = bottom;
        this.top = top;
    }

    public GeoCoordinates getBottom() {
        return bottom;
    }

    public GeoCoordinates getTop() {
        return top;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof BoundingBox)) {
            return false;
        }
        return bottom.equals(((BoundingBox)obj).getBottom())
                && top.equals(((BoundingBox)obj).getTop());
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
                "bottom=" + bottom +
                ", top=" + top +
                '}';
    }
}

package irl.lyit.DublinSmartHouseSearch.old;

import irl.lyit.DublinSmartHouseSearch.dao.HouseRepository;
import irl.lyit.DublinSmartHouseSearch.service.HouseCollector;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {


        FindGeoCoordinates findGeoCoordinates = new FindGeoCoordinates("4 muileann mews");
        GeoCoordinates workCoordinates = findGeoCoordinates.getCoordinates();

        CreateIsochroneMap createIsochroneMap = new CreateIsochroneMap(workCoordinates);
        List<BoundingBox> boundingBox = createIsochroneMap.boundingBox();

        System.out.println(boundingBox);



    }

}

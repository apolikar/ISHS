package irl.lyit.DublinSmartHouseSearch.service;

import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.dao.HouseRepository;
import irl.lyit.DublinSmartHouseSearch.old.BoundingBox;
import irl.lyit.DublinSmartHouseSearch.old.GeoCoordinates;
import irl.lyit.DublinSmartHouseSearch.old.SearchAttributes;
import irl.lyit.DublinSmartHouseSearch.old.TimeTravelMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HouseService{

    private final HouseRepository houseRepository;


    @Autowired
    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }


    public List<House> getInBoundary(SearchAttributes searchAttributes) {

        List<House> all = houseRepository.findAll();
        List<BoundingBox> allBoundingBoxes = searchAttributes.getBoundingBoxes();

        List<House> inBoundary = new ArrayList<>();

        for (House house : all) {

            double houseLat = house.getLat();
            double houseLng = house.getLng();

            for (BoundingBox box : allBoundingBoxes) {

                if ((houseLat >= box.getBottom().getLat() && houseLat <= box.getTop().getLat()) &&
                        (houseLng <= box.getTop().getLng() && houseLng >= box.getBottom().getLng())) {
                    inBoundary.add(house);
                }
            }
        }

        return inBoundary;
    }



    public List<House> inTime(SearchAttributes searchAttributes) throws IOException, InterruptedException {

        GeoCoordinates start = searchAttributes.getCoordinates();
        String dateTime = searchAttributes.getDateAndTime();
        String transportType = searchAttributes.getTransportationType();
        long travelTime = searchAttributes.getTimeLimit();

        TimeTravelMatrix ttm = new TimeTravelMatrix();

        return ttm.getInTime(start, getInBoundary(searchAttributes), transportType, travelTime, dateTime);
    }

}

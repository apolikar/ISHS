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


    public List<House> getInBoundary() {

        List<House> all = houseRepository.findAll();
        List<BoundingBox> allBoundingBoxes = new ArrayList<>();
        allBoundingBoxes.add(new BoundingBox(new GeoCoordinates(53.38586318467314, -6.233945130048145),
                new GeoCoordinates(53.4721215851735, -6.161806902892333)));

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



    public List<House> inTime() throws IOException, InterruptedException {

        GeoCoordinates start = new GeoCoordinates(53.4419137, -6.2028496);
        String dateTime = "2022-03-30T08:00:00.000Z";
        String transportType = "public_transport";
        long travelTime = 1800;

        TimeTravelMatrix ttm = new TimeTravelMatrix();

        return ttm.getInTime(start, getInBoundary(), transportType, travelTime, dateTime);
    }



}

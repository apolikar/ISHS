package irl.lyit.DublinSmartHouseSearch.service;

import irl.lyit.DublinSmartHouseSearch.config.Credentials;
import irl.lyit.DublinSmartHouseSearch.controller.exception.TooManyPointsException;
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

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private Credentials credentials;


    public List<House> getInBoundary(SearchAttributes searchAttributes) {

        List<House> all = houseRepository.findAll();

        List<BoundingBox> allBoundingBoxes = searchAttributes.getBoundingBoxes();

        List<House> inBoundary = new ArrayList<>();

        if (allBoundingBoxes == null) {
            return inBoundary;
        }

        for (House house : all) {

            double houseLat = house.getLat();
            double houseLng = house.getLng();

            for (BoundingBox box : allBoundingBoxes) {

                if ((houseLat >= box.getBottom().getLat() && houseLat <= box.getTop().getLat()) &&
                        (houseLng <= box.getTop().getLng() && houseLng >= box.getBottom().getLng())) {

                    if (isPriceBedsRange(house, searchAttributes)){
                        inBoundary.add(house);
                    }

                }
            }
        }

        return inBoundary;
    }

    public List<ResultMatchHouse> getHouseInTimeLimit(SearchAttributes searchAttributes) throws IOException, InterruptedException, TooManyPointsException {

        GeoCoordinates destinationAddress = searchAttributes.getCoordinates();
        String dateTime = searchAttributes.getDateAndTime();
        String transportType = searchAttributes.getTransportationType();
        long travelTime = searchAttributes.getTimeLimit();

        TimeTravelMatrix ttm = new TimeTravelMatrix(credentials);

        List<House> housesInBound = getInBoundary(searchAttributes);

        if (housesInBound.isEmpty()) {
            return new ArrayList<>();
        }

        return ttm.getInTime(destinationAddress, housesInBound, transportType, travelTime, dateTime);
    }


    private boolean isPriceBedsRange(House house, SearchAttributes searchAttributes) {

        return (house.getBedrooms() >= searchAttributes.getMinBeds()
                && house.getBedrooms() <= searchAttributes.getMaxBeds())
                && (house.getPrice() >= searchAttributes.getMinPrice()
                && house.getPrice() <= searchAttributes.getMaxPrice());
    }

}

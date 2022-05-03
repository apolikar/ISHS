package irl.lyit.DublinSmartHouseSearch.service;

import irl.lyit.DublinSmartHouseSearch.config.Credentials;
import irl.lyit.DublinSmartHouseSearch.controller.exception.TooManyPointsException;
import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.dao.HouseRepository;
import irl.lyit.DublinSmartHouseSearch.presentation.homePanel.ResultMatchHouse;
import irl.lyit.DublinSmartHouseSearch.service.isochroneMap.BoundingBox;
import irl.lyit.DublinSmartHouseSearch.service.geoCoordinates.GeoCoordinates;
import irl.lyit.DublinSmartHouseSearch.service.isochroneMap.SearchAttributes;
import irl.lyit.DublinSmartHouseSearch.service.isochroneMap.CreateIsochroneMapService;
import irl.lyit.DublinSmartHouseSearch.service.timeMatrix.TimeTravelMatrix;
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

    @Autowired
    private CreateIsochroneMapService createIsochroneMapService;


    public List<House> getInBoundary(SearchAttributes searchAttributes) {


        List<House> allHousesToCheckList = houseRepository.findAll();

        List<BoundingBox> allBoundingBoxes = null;
        try {
            allBoundingBoxes = createIsochroneMapService.boundingBox(searchAttributes, credentials);
        } catch (IOException | InterruptedException e) {
            return new ArrayList<>();
        }
        HousesInBoundingBoxList housesInBoundingBoxList = new HousesInBoundingBoxList(
                allHousesToCheckList,
                allBoundingBoxes,
                searchAttributes
        );

        return housesInBoundingBoxList.getHousesInBoundingBoxList();
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

}

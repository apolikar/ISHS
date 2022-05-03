package irl.lyit.DublinSmartHouseSearch.service;

import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.old.SearchAttributes;
import irl.lyit.DublinSmartHouseSearch.service.isochroneMap.BoundingBox;

import java.util.ArrayList;
import java.util.List;

public class HousesInBoundingBoxList {

    private List<House> houseList;
    private List<BoundingBox> allBoundingBoxesList;
    private SearchAttributes searchAttributes;

    public HousesInBoundingBoxList(List<House> houseList, List<BoundingBox> allBoundingBoxesList, SearchAttributes searchAttributes) {
        this.houseList = houseList;
        this.allBoundingBoxesList = allBoundingBoxesList;
        this.searchAttributes = searchAttributes;
    }

    public List<House> getHousesInBoundingBoxList() {

        List<House> housesInBoundaryList = new ArrayList<>();

        for(House house :houseList){

            double houseLat = house.getLat();
            double houseLng = house.getLng();

            for (BoundingBox box : allBoundingBoxesList) {

                if ((houseLat >= box.getBottom().getLat() && houseLat <= box.getTop().getLat()) &&
                        (houseLng <= box.getTop().getLng() && houseLng >= box.getBottom().getLng())) {

                    if (isHousePriceBedsInRange(house, searchAttributes)) {
                        housesInBoundaryList.add(house);
                    }

                }
            }
        }

        return housesInBoundaryList;
    }


    private boolean isHousePriceBedsInRange (House house, SearchAttributes searchAttributes){

        return (house.getBedrooms() >= searchAttributes.getMinBeds()
                && house.getBedrooms() <= searchAttributes.getMaxBeds())
                && (house.getPrice() >= searchAttributes.getMinPrice()
                && house.getPrice() <= searchAttributes.getMaxPrice());
    }

}

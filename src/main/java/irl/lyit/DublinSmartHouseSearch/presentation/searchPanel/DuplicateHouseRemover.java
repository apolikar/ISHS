package irl.lyit.DublinSmartHouseSearch.presentation.searchPanel;

import java.util.ArrayList;
import java.util.List;

public class DuplicateHouseRemover {

    private List<ResultMatchHouse> resultMatchHouseList;

    public DuplicateHouseRemover(List<ResultMatchHouse> resultMatchHouseList) {
        this.resultMatchHouseList = resultMatchHouseList;
    }

    public List<ResultMatchHouse> removeDuplicates() {

        List<ResultMatchHouse> sortedListForDuplicates = new ArrayList<>();

        for (int i = 0; i < resultMatchHouseList.size(); i++) {

            if (i + 1 >= resultMatchHouseList.size()
                    || resultMatchHouseList.get(i).getSecondsToTravel() != resultMatchHouseList.get(i + 1).getSecondsToTravel()) {
                sortedListForDuplicates.add(resultMatchHouseList.get(i));
                continue;
            }

            if (resultMatchHouseList.get(i).getHouse().getLat() != resultMatchHouseList.get(i + 1).getHouse().getLat()
                            || resultMatchHouseList.get(i).getHouse().getLng() != resultMatchHouseList.get(i + 1).getHouse().getLng()) {
                continue;
            }

            if (resultMatchHouseList.get(i).getHouse().getUpdateTime() > resultMatchHouseList.get(i + 1).getHouse().getUpdateTime()) {
                sortedListForDuplicates.add(resultMatchHouseList.get(i));
            } else {
                sortedListForDuplicates.add(resultMatchHouseList.get(i + 1));
            }
            i++;

        }

        return sortedListForDuplicates;
    }
}

package irl.lyit.DublinSmartHouseSearch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class HouseCollectorScheduler {


    private final HouseCollector houseCollector;

    @Autowired
    public HouseCollectorScheduler(DaftIEHouseCollector daftIEHouseCollector) {
        this.houseCollector = daftIEHouseCollector;
    }

    // after 12 hours
//    @Scheduled(fixedDelay = 43200000)
    public void searchHouses() {
        this.houseCollector.collect();
    }


}


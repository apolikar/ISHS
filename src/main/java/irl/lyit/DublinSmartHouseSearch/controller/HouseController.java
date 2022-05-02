package irl.lyit.DublinSmartHouseSearch.controller;

import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.dao.HouseRepository;
import irl.lyit.DublinSmartHouseSearch.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping(value = "")
public class HouseController {


    private final HouseRepository houseRepository;
    private final HouseService houseService;

    @Autowired
    public HouseController(HouseRepository houseRepository, HouseService houseService) {
        this.houseRepository = houseRepository;
        this.houseService = houseService;
    }

    @GetMapping(value = "/all")
    public List<House> getAll() {
        return houseRepository.findAll();
    }

//    @GetMapping(value = "/inbox")
//    public List<House> getInBox() {
////        return houseService.getInBoundary();
//        return null;
//    }
//
//
//    @GetMapping(value = "/intime")
//    public List<House> listAll() throws IOException, InterruptedException {
////        return houseService.inTime();
//        return null;
//    }

}

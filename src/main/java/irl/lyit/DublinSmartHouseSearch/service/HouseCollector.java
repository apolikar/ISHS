package irl.lyit.DublinSmartHouseSearch.service;

import irl.lyit.DublinSmartHouseSearch.dao.User;
import irl.lyit.DublinSmartHouseSearch.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class HouseCollector {

    private final UserRepository userRepository;

    @Autowired
    public HouseCollector(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Scheduled(fixedDelay = 1000)
    private void collectInfo(){
        userRepository.save(new User("Alex"));
        System.out.println("hello ");
    }
}



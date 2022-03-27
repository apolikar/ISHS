package irl.lyit.DublinSmartHouseSearch;

import irl.lyit.DublinSmartHouseSearch.service.HouseCollector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = "irl.lyit.DublinSmartHouseSearch.dao")
public class DublinSmartHouseSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(DublinSmartHouseSearchApplication.class, args);
		while (true);

	}

}

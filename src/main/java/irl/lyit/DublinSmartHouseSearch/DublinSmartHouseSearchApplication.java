package irl.lyit.DublinSmartHouseSearch;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = "irl.lyit.DublinSmartHouseSearch.dao")
public class DublinSmartHouseSearchApplication {

	public static void main(String[] args) {
		//SpringApplication.run(DublinSmartHouseSearchApplication.class, args);
		new SpringApplicationBuilder()
				.sources(DublinSmartHouseSearchApplication.class)
				.run(args);

	}


}

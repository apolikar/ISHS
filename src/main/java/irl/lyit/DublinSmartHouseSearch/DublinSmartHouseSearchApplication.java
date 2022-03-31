package irl.lyit.DublinSmartHouseSearch;

import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration;
import irl.lyit.DublinSmartHouseSearch.presentation.HomePage;
import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = "irl.lyit.DublinSmartHouseSearch.dao")
public class DublinSmartHouseSearchApplication extends WebApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(DublinSmartHouseSearchApplication.class)
                .run(args);
    }

    @Autowired(required = false)
    private List<WicketApplicationInitConfiguration> configurations = new ArrayList<>();

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

    @Override
    protected void init() {
        super.init();

        for (WicketApplicationInitConfiguration configuration : configurations) {
            configuration.init(this);
        }
    }

}

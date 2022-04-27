package irl.lyit.DublinSmartHouseSearch;

import irl.lyit.DublinSmartHouseSearch.presentation.HomePage;
import org.apache.wicket.Page;
import org.apache.wicket.core.request.mapper.HomePageMapper;
import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.spring.SpringWebApplicationFactory;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.RequestContextFilter;
import javax.servlet.Filter;
import static irl.lyit.DublinSmartHouseSearch.service.WicketConfiguration.PARAM_APP_BEAN;
import static org.apache.wicket.protocol.http.WicketFilter.APP_FACT_PARAM;

@Configuration(value = DublinSmartHouseSearchApplication.PORTAL_APPLICATION)
@SpringBootApplication(scanBasePackageClasses = {
        DublinSmartHouseSearchApplication.class
})
@EnableScheduling
@EnableCaching
@EnableAsync
public class DublinSmartHouseSearchApplication extends WebApplication {


    public static final String PORTAL_APPLICATION = "portalApplication";

    public static void main(String[] args) {
        SpringApplication.run(DublinSmartHouseSearchApplication.class, args);
    }

    @Override
    public void init()
    {
        super.init();

        getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");

        getDebugSettings().setOutputMarkupContainerClassName(true);
        getDebugSettings().setAjaxDebugModeEnabled(true);

        getComponentInstantiationListeners().add(new SpringComponentInjector(this));

        mount(new HomePageMapper(HomePage.class));
        mount(new MountedMapper("/home", HomePage.class));    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

    @Bean
    @Order
    public FilterRegistrationBean wicketFilter() {
        final Filter filter = new WicketFilter();
        final FilterRegistrationBean result = new FilterRegistrationBean(filter);

        result.addUrlPatterns("/*");

        result.addInitParameter(APP_FACT_PARAM, SpringWebApplicationFactory.class.getName());
        result.addInitParameter(PARAM_APP_BEAN, PORTAL_APPLICATION);
        result.addInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");

        return result;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public FilterRegistrationBean requestContextFilter() {
        final Filter filter = new RequestContextFilter();
        final FilterRegistrationBean result = new FilterRegistrationBean(filter);

        result.addUrlPatterns("/*");

        return result;
    }

}

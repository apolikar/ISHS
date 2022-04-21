package irl.lyit.DublinSmartHouseSearch.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Configuration
public class WicketConfiguration implements ServletContextInitializer {
    public static final String PARAM_APP_BEAN = "applicationBean";

    @Value("${wicket.refreshingDuration:5}")
    private int refreshingDuration;

    @Override
    public void onStartup(ServletContext sc) throws ServletException {
    }

    public int getRefreshingDuration() {
        return refreshingDuration;
    }
}
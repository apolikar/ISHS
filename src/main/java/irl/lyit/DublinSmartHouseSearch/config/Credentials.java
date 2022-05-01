package irl.lyit.DublinSmartHouseSearch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "credentials")
public class Credentials {

    private String googleApiKey;
    private String travelTimeApiKey;
    private String travelTimeApplicationId;

    public String getGoogleApiKey() {
        return googleApiKey;
    }

    public void setGoogleApiKey(String googleApiKey) {
        this.googleApiKey = googleApiKey;
    }

    public String getTravelTimeApiKey() {
        return travelTimeApiKey;
    }

    public void setTravelTimeApiKey(String travelTimeApiKey) {
        this.travelTimeApiKey = travelTimeApiKey;
    }

    public String getTravelTimeApplicationId() {
        return travelTimeApplicationId;
    }

    public void setTravelTimeApplicationId(String travelTimeApplicationId) {
        this.travelTimeApplicationId = travelTimeApplicationId;
    }
}

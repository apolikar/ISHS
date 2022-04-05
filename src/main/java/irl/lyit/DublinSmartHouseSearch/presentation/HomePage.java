package irl.lyit.DublinSmartHouseSearch.presentation;

import com.fasterxml.jackson.databind.JsonNode;
import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import irl.lyit.DublinSmartHouseSearch.old.GeoCoordinates;
import irl.lyit.DublinSmartHouseSearch.service.TransportionType;
import irl.lyit.DublinSmartHouseSearch.service.addressFormatter.GoogleAddressFormatter;
import irl.lyit.DublinSmartHouseSearch.service.client.GMapsHTTPClient;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.form.datetime.LocalDateTimeField;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.Model;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;


@WicketHomePage
public class HomePage extends WebPage {
    public HomePage() {
        add(new AddressForm("form"));

    }

    private static class AddressForm extends Form<Void> {

        private final Model<String> addressModel;
        private final Model<TransportionType> transportModel;
        private final Model<Integer> travelTimeModel;
        private final Model<Integer> minBedsModel;
        private final Model<Integer> maxBedsModel;
        private final Model<Integer> minPriceModel;
        private final Model<Integer> maxPriceModel;


        public AddressForm(String id) {
            super(id);

            this.addressModel = new Model<>();
            this.transportModel = new Model<>();
            this.travelTimeModel = new Model<>();
            this.minBedsModel = new Model<>();
            this.maxPriceModel = new Model<>();
            this.minPriceModel = new Model<>();
            this.maxBedsModel = new Model<>();

            add(new Label("addressLabel", "workplace address"));
            add(new TextField<>("workInput", addressModel));

            add(new DropDownChoice<>(
                    "travelSelect",
                    transportModel,
                    Arrays.asList(TransportionType.values())
            ));


            add(new Label("travelDuration", "max travel time"));
            add(new NumberTextField<>("timeInput", travelTimeModel, Integer.class));


            add(new Label("minBedsLabel", "minimum beds "));
            add(new NumberTextField<>("bedsInputMin", minBedsModel, Integer.class));


            add(new Label("maxBedsLabel", "  max beds  "));
            add(new NumberTextField<>("bedsInputMax", maxBedsModel, Integer.class));


            add(new Label("minPriceLabel", " max price"));
            add(new NumberTextField<>("priceInputMin", minPriceModel, Integer.class));

            add(new Label("maxPriceLabel", " max price"));
            add(new NumberTextField<>("priceInputMax", maxPriceModel, Integer.class));

        }


        @Override
        protected void onSubmit() {

            // for test
            GoogleAddressFormatter addressFormatter = new GoogleAddressFormatter();
            String formattedAddress = addressFormatter.formatAddress(addressModel.getObject());

            String transportType = transportModel.getObject().toString();
            System.out.println("Transport Type: " + transportType);

            int travelTime = travelTimeModel.getObject();
            System.out.println("Travel Time: " + travelTime);

            int bedsMin = minBedsModel.getObject();
            int bedsMax = maxBedsModel.getObject();
            System.out.println("Min beds: " + bedsMin + " Max Beds: " + bedsMax);

            int minPrice = minPriceModel.getObject();
            int maxPrice = maxPriceModel.getObject();
            System.out.println("Min Price: " + minPrice + " Max Price: " + maxPrice);


            GMapsHTTPClient gMapsHTTPClient = new GMapsHTTPClient();
            GeoCoordinates finalDestinationCoord;

            try {

                JsonNode addressResponse = gMapsHTTPClient.finalDestination(formattedAddress);
                double lat = addressResponse.get("lat").asDouble();
                double lng = addressResponse.get("lng").asDouble();
                finalDestinationCoord = new GeoCoordinates(lat, lng);
                System.out.println(finalDestinationCoord);

            } catch (IOException | InterruptedException e) {
                System.out.println("exception caught");
            }

        }

    }
}

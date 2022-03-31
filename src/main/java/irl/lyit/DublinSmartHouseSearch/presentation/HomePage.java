package irl.lyit.DublinSmartHouseSearch.presentation;

import com.fasterxml.jackson.databind.JsonNode;
import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import irl.lyit.DublinSmartHouseSearch.old.GeoCoordinates;
import irl.lyit.DublinSmartHouseSearch.service.GeoCoordinatesFinder;
import irl.lyit.DublinSmartHouseSearch.service.addressFormatter.GoogleAddressFormatter;
import irl.lyit.DublinSmartHouseSearch.service.client.GMapsHTTPClient;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

import java.io.IOException;


@WicketHomePage
public class HomePage extends WebPage {
    public HomePage() {
        add(new AddressForm("form"));

    }

    private static class AddressForm extends Form<Void> {

        private final Model<String> addressModel;

        public AddressForm(String id) {
            super(id);

            this.addressModel = new Model<>();


            add(new Label("addressLabel", "address"));
            add(new TextField<>("workInput", addressModel));


        }


        @Override
        protected void onSubmit() {

            // for test

            GoogleAddressFormatter addressFormatter = new GoogleAddressFormatter();
            String formattedAddress = addressFormatter.formatAddress(addressModel.getObject());

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


            //System.out.println(addressModel.getObject());
        }

    }
}

package irl.lyit.DublinSmartHouseSearch.presentation;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import irl.lyit.DublinSmartHouseSearch.old.GeoCoordinates;
import irl.lyit.DublinSmartHouseSearch.old.SearchAttributes;
import irl.lyit.DublinSmartHouseSearch.service.GeoCoordinatesFinder;
import irl.lyit.DublinSmartHouseSearch.service.TransportionType;
import irl.lyit.DublinSmartHouseSearch.service.addressFormatter.GoogleAddressFormatter;
import irl.lyit.DublinSmartHouseSearch.service.client.GMapsHTTPClient;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.form.datetime.TimeField;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.Model;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;



@WicketHomePage
public class HomePage extends WebPage {

    public HomePage() {
        add(new AddressForm("form"));

    }

    private static class AddressForm extends Form<Void> {

        private final Model<String> addressModel;
        private final Model<Date> dateModel;
        private final Model<LocalTime> timeModel;
        private final Model<TransportionType> transportModel;
        private final Model<Integer> travelTimeModel;
        private final Model<Integer> minBedsModel;
        private final Model<Integer> maxBedsModel;
        private final Model<Integer> minPriceModel;
        private final Model<Integer> maxPriceModel;


        public AddressForm(String id) {
            super(id);

            this.addressModel = new Model<>();

            this.dateModel = new Model<>();

            this.timeModel = new Model<>();
            this.transportModel = new Model<>();
            this.travelTimeModel = new Model<>();
            this.minBedsModel = new Model<>();
            this.maxPriceModel = new Model<>();
            this.minPriceModel = new Model<>();
            this.maxBedsModel = new Model<>();

            add(new Label("addressLabel", ""));
            add(new TextField<>("workInput", addressModel));

            add(new Label("dateLabel", ""));
            add(new DateTextField("dateInput", dateModel, "yyyy-MM-dd"));

            add(new Label("timeDayLabel", ""));
            add(new TimeField("timeDayInput", timeModel));

            add(new DropDownChoice<>(
                    "travelSelect",
                    transportModel,
                    Arrays.asList(TransportionType.values())
            ));

            add(new Label("travelDuration", ""));
            add(new NumberTextField<>("timeInput", travelTimeModel, Integer.class));


            add(new Label("minBedsLabel", ""));
            add(new NumberTextField<>("bedsInputMin", minBedsModel, Integer.class));


            add(new Label("maxBedsLabel", ""));
            add(new NumberTextField<>("bedsInputMax", maxBedsModel, Integer.class));


            add(new Label("minPriceLabel", ""));
            add(new NumberTextField<>("priceInputMin", minPriceModel, Integer.class));

            add(new Label("maxPriceLabel", ""));
            add(new NumberTextField<>("priceInputMax", maxPriceModel, Integer.class));

        }


        @Override
        protected void onSubmit() {

            SearchAttributes searchAttributes = new SearchAttributes(
                    getWorkCoordinates(),
                    getDateAndTime(),
                    travelTimeModel.getObject(),
                    getTransportType(),
                    minPriceModel.getObject(),
                    maxPriceModel.getObject(),
                    minBedsModel.getObject(),
                    maxBedsModel.getObject()
            );

            if(!searchValidation(searchAttributes)){
                HomePage homePage = new HomePage();
                System.out.println("error");
                return;
            }

            System.out.println(searchAttributes);

        }

        private GeoCoordinates getWorkCoordinates(){
            // for test
            GoogleAddressFormatter addressFormatter = new GoogleAddressFormatter();
            String formattedAddress = addressFormatter.formatAddress(addressModel.getObject());
            GeoCoordinatesFinder gc = new GeoCoordinatesFinder(addressFormatter, new GMapsHTTPClient());
            GeoCoordinates workCoordinates = new GeoCoordinates();
            try {
                workCoordinates = gc.getCoordinates(formattedAddress);
            } catch (IOException | InterruptedException ignored) {
            }

            return workCoordinates;
        }

        private String getDateAndTime(){

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateModel.getObject();
            String time = String.valueOf(timeModel.getObject());
            return sdf.format(date) + "T" + time + ":00.000Z";
        }

        private String getTransportType(){
            switch (transportModel.getObject()) {
                case PUBLIC_TRANSPORT -> {
                    return "public_transport";
                }
                case DRIVING -> {
                    return "driving";
                }
                case WALKING -> {
                    return "walking";
                }
            }
            return "cycling";
        }


        private boolean searchValidation(SearchAttributes currentSearch) {

            LocalDateTime now = LocalDateTime.now();
            String trimmedSearchTime = getDateAndTime().substring(0, 19);
            LocalDateTime searchDateTime = LocalDateTime.parse(trimmedSearchTime);

            if(currentSearch.getMaxPrice() < currentSearch.getMinPrice()){
                return false;
            }

            if(currentSearch.getMaxBeds() < currentSearch.getMinBeds()){
                return false;
            }

            if(currentSearch.getTimeLimit() <= 0){
                return false;
            }
            return now.isBefore(searchDateTime);
        }

    }
}

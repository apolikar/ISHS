package irl.lyit.DublinSmartHouseSearch.presentation;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import com.google.gson.Gson;
import irl.lyit.DublinSmartHouseSearch.old.GeoCoordinates;
import irl.lyit.DublinSmartHouseSearch.old.SearchAttributes;
import irl.lyit.DublinSmartHouseSearch.service.GeoCoordinatesFinder;
import irl.lyit.DublinSmartHouseSearch.service.HouseService;
import irl.lyit.DublinSmartHouseSearch.service.TransportionType;
import irl.lyit.DublinSmartHouseSearch.service.addressFormatter.GoogleAddressFormatter;
import irl.lyit.DublinSmartHouseSearch.service.client.GMapsHTTPClient;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.form.datetime.TimeField;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@WicketHomePage
public class HomePage extends WebPage {

    public HomePage() {
        add(new AddressForm("form"));
        add(new AjaxLink<String>("aboutMeLink") {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                setResponsePage(AboutMe.class);
            }
        });
    }

    private static class AddressForm extends Form<Void> {

        private final Model<String> addressModel;
        private final Model<Date> dateModel;
        private final Model<LocalTime> timeModel;
        private final Model<TransportionType> transportModel;
        private final Model<Integer> travelTimeModel;
        private final Model<Integer> minBedsModel;
        private final Model<Integer> maxBedsModel;
        private final Model<String> minPriceModel;
        private final Model<String> maxPriceModel;


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

            List<Integer> timeList = Arrays.asList(10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 59);
            add(new DropDownChoice<Integer>("travelTime", travelTimeModel, timeList));

            List<Integer> bedsNumber = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
            add(new DropDownChoice<Integer>("bedsInputMin", minBedsModel, bedsNumber));
            add(new DropDownChoice<Integer>("bedsInputMax", maxBedsModel, bedsNumber));

            List<String> priceList = Arrays.asList("€100K", "€150K", "€200K", "€250K", "€300K",
                    "€350K", "€400K", "€450K", "€500K", "€550K", "€600K", "€650K", "€700K", "€750K",
                    "€800K", "€850K", "€900K", "€950K", "€1M", "€1.5M", "€2M", "€3M", "€4M", "€5M");
            add(new DropDownChoice<String>("priceInputMin", minPriceModel, priceList));
            add(new DropDownChoice<String>("priceInputMax", maxPriceModel, priceList));

            add(new FeedbackPanel("feedbackMessage",
                    new ExactErrorLevelFilter(FeedbackMessage.ERROR)));
        }


        @Override
        protected void onSubmit() {

            SearchAttributes searchAttributes = null;
            try {
                searchAttributes = new SearchAttributes(
                        getWorkCoordinates(),
                        getDateAndTime(),
                        travelTimeModel.getObject() * 60,
                        getTransportType(),
                        setPrice(minPriceModel.getObject()),
                        setPrice(maxPriceModel.getObject()),
                        minBedsModel.getObject(),
                        maxBedsModel.getObject()
                );
            } catch (IOException | InterruptedException ignored) {
            }


            if (!searchValidation(searchAttributes)) {
                System.out.println("error");
                error("Your input is incorrect. Please try again.");
                setResponsePage(HomePage.class);
                return;
            }

            PageParameters pageParameters = new PageParameters();
            pageParameters.add("searchAttributes", new Gson().toJson(searchAttributes));

            setResponsePage(ResultPage.class, pageParameters);
        }

        private GeoCoordinates getWorkCoordinates() {
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

        private String getDateAndTime() {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateModel.getObject();
            String time = String.valueOf(timeModel.getObject());
            return sdf.format(date) + "T" + time + ":00.000Z";
        }

        private String getTransportType() {
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

            if (currentSearch == null) {
                return false;
            }

            if (currentSearch.getCoordinates().getLat() == 0
                    && currentSearch.getCoordinates().getLng() == 0){
                return false;
            }

            if (currentSearch.getMaxPrice() < currentSearch.getMinPrice()) {
                return false;
            }

            if (currentSearch.getMaxBeds() < currentSearch.getMinBeds()) {
                return false;
            }

            if (currentSearch.getTimeLimit() <= 0) {
                return false;
            }
            return now.isBefore(searchDateTime);
        }

        private int setPrice(String price) {

            return switch (price) {
                case "€100K" -> 100_000;
                case "€150K" -> 150_000;
                case "€200K" -> 200_000;
                case "€250K" -> 250_000;
                case "€300K" -> 300_000;
                case "€350K" -> 350_000;
                case "€450K" -> 450_000;
                case "€500K" -> 500_000;
                case "€550K" -> 550_000;
                case "€600K" -> 600_000;
                case "€650K" -> 650_000;
                case "€700K" -> 700_000;
                case "€750K" -> 750_000;
                case "€800K" -> 800_000;
                case "€850K" -> 850_000;
                case "€900K" -> 900_000;
                case "€950K" -> 950_000;
                case "€1M" -> 1_000_000;
                case "€1.5M" -> 1_500_000;
                case "€2M" -> 2_000_000;
                case "€3M" -> 3_000_000;
                case "€4M" -> 4_000_000;
                default -> 5_000_000;
            };
        }

    }
}

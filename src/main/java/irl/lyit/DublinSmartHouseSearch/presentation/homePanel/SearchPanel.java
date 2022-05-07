package irl.lyit.DublinSmartHouseSearch.presentation.homePanel;

import com.googlecode.wicket.kendo.ui.form.button.ButtonBehavior;
import irl.lyit.DublinSmartHouseSearch.controller.exception.TooManyPointsException;
import irl.lyit.DublinSmartHouseSearch.presentation.HomePage;
import irl.lyit.DublinSmartHouseSearch.presentation.homePanel.Exception.FormValidationError;
import irl.lyit.DublinSmartHouseSearch.service.geoCoordinates.GeoCoordinates;
import irl.lyit.DublinSmartHouseSearch.service.isochroneMap.SearchAttributes;
import irl.lyit.DublinSmartHouseSearch.presentation.AboutMe;
import irl.lyit.DublinSmartHouseSearch.presentation.resultPanel.ResultPanel;
import irl.lyit.DublinSmartHouseSearch.service.geoCoordinates.AddressGeoCoordinatesFinder;
import irl.lyit.DublinSmartHouseSearch.service.HouseService;
import irl.lyit.DublinSmartHouseSearch.service.TransportionType;
import irl.lyit.DublinSmartHouseSearch.service.addressFormatter.GoogleAddressFormatter;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.util.Comparator.comparing;

public class SearchPanel extends Panel {


    @SpringBean
    private HouseService houseService;
    @SpringBean
    private AddressGeoCoordinatesFinder addressGeoCoordinatesFinder;


    public SearchPanel() {
        super("searchPanel");
        setOutputMarkupId(true);

        add(new AddressForm("form"));
        add(new AjaxLink<String>("aboutMeLink") {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                setResponsePage(AboutMe.class);
            }
        });
    }

    private class AddressForm extends Form<Void> {

        private WebMarkupContainer informationBox;
        private WebMarkupContainer spinnerBox;
        private final Model<String> addressModel;
        private final Model<Date> dateModel;
        private final Model<String> hourModel;
        private final Model<String> minuteModel;
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
            this.hourModel = new Model<>();
            this.minuteModel = new Model<>();
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


            List<String> hourList = Arrays.asList("00", "01", "02", "03", "04", "05",
                    "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
                    "16", "17", "18", "19", "20", "21", "22", "23");
            DropDownChoice<String> hour = new DropDownChoice<>("hourInput", hourModel, hourList) {
                @Override
                protected String getNullValidDisplayValue() {
                    return "hour";
                }
            };
            hour.setNullValid(true);
            add(hour);


            List<String> munutesList = Arrays.asList("00", "05", "10", "15", "20", "25",
                    "30", "35", "40", "45", "50", "55");
            DropDownChoice<String> minutes = new DropDownChoice<>("minutesInput", minuteModel, munutesList) {
                @Override
                protected String getNullValidDisplayValue() {
                    return "minutes";
                }
            };
            minutes.setNullValid(true);
            add(minutes);


            add(new DropDownChoice<>(
                    "travelSelect",
                    transportModel,
                    Arrays.asList(TransportionType.values())
            ) {
                @Override
                protected String getNullValidDisplayValue() {
                    return "type";
                }
            });


            List<Integer> timeList = Arrays.asList(5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 59);
            DropDownChoice<Integer> time = new DropDownChoice<>("travelTime", travelTimeModel, timeList) {
                @Override
                protected String getNullValidDisplayValue() {
                    return "max minutes";
                }
            };
            time.setNullValid(true);
            add(time);


            List<Integer> bedsNumber = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
            DropDownChoice<Integer> beds1 = new DropDownChoice<>("bedsInputMin", minBedsModel, bedsNumber) {
                @Override
                protected String getNullValidDisplayValue() {
                    return "min beds";
                }
            };
            beds1.setNullValid(true);
            add(beds1);


            DropDownChoice<Integer> beds2 = new DropDownChoice<>("bedsInputMax", maxBedsModel, bedsNumber) {
                @Override
                protected String getNullValidDisplayValue() {
                    return "max beds";
                }
            };
            beds2.setNullValid(true);
            add(beds2);


            List<String> priceList = Arrays.asList("€100K", "€150K", "€200K", "€250K", "€300K",
                    "€350K", "€400K", "€450K", "€500K", "€550K", "€600K", "€650K", "€700K", "€750K",
                    "€800K", "€850K", "€900K", "€950K", "€1M", "€1.5M", "€2M", "€3M", "€4M", "€5M");
            DropDownChoice<String> price1 = new DropDownChoice<>("priceInputMin", minPriceModel, priceList) {
                @Override
                protected String getNullValidDisplayValue() {
                    return "min price";
                }
            };
            price1.setNullValid(true);
            add(price1);


            DropDownChoice<String> price2 = new DropDownChoice<>("priceInputMax", maxPriceModel, priceList) {
                @Override
                protected String getNullValidDisplayValue() {
                    return "max price";
                }
            };
            price2.setNullValid(true);
            add(price2);

            informationBox = new WebMarkupContainer("alertInfo");
            informationBox.add(feedbackMessage(null));
            add(informationBox);


            spinnerBox = new WebMarkupContainer("spinnerLoad");
            spinnerBox.add(new FeedbackPanel("spinnerIcon", new ExactErrorLevelFilter(FeedbackMessage.SUCCESS)));
            add(spinnerBox);
            spinnerBox.setVisible(false);


            add(new AjaxButton("searchBtn") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    SearchAttributes searchAttributes;

                    try {
                        searchAttributes = new SearchAttributes(
                                getWorkCoordinates(),
                                getDateAndTime(),
                                travelTimeModel.getObject(),
                                getTransportType(),
                                minPriceModel.getObject(),
                                maxPriceModel.getObject(),
                                minBedsModel.getObject(),
                                maxBedsModel.getObject()
                        );
                    } catch (IOException | InterruptedException e) {

                        informationBox.setVisible(true);
                        error("Not enough information to proceed ... ");
                        error("Hint: Please fill all form fields");
                        form.clearInput();
                        setResponsePage(HomePage.class);
                        return;
                    }

                    try {
                        searchValidation(searchAttributes);
                    } catch (FormValidationError e) {
                        Label feedback = feedbackMessage(e.getMessage() + " " + e.getErrorDescription());
                        informationBox.replace(feedback);
                        target.add(feedback);

                        return;
                    }

                    List<ResultMatchHouse> results;

                    try {
                        results = SearchPanel.this.houseService.getHouseInTimeLimit(searchAttributes);
                    } catch (IOException | InterruptedException ignored) {
                        //ToDo show Internal server error
                        return;
                    } catch (TooManyPointsException e) {
                        //ToDo show error
                        informationBox.setVisible(true);
                        error("Time Travel API free location points limit is exceeded");
                        error("To process more points Enterprise plan is needed (starting from 250 euro per month)");
                        error("Hint: Try to narrow down search attributes");
                        setResponsePage(getPage());
                        return;
                    }

                    informationBox.setVisible(false);

//                    if (results.isEmpty()) {
//                        return;
//                    }

                    // sort by travel time (low to high)
                    results.sort(comparing(ResultMatchHouse::getSecondsToTravel));
                    results = new DuplicateHouseRemover(results).removeDuplicates();

                    changePanelToResult(target, results);
                }
            });
        }

        private GeoCoordinates getWorkCoordinates() {
            if (addressModel.getObject() == null) {
                return null;
            }

            GoogleAddressFormatter addressFormatter = new GoogleAddressFormatter();
            String formattedAddress = addressFormatter.formatAddress(addressModel.getObject());
            GeoCoordinates workCoordinates = new GeoCoordinates();
            try {
                workCoordinates = addressGeoCoordinatesFinder.getUserAddressCoordinates(formattedAddress);
            } catch (IOException | InterruptedException ignored) {
            }

            return workCoordinates;
        }

        private String getDateAndTime() {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateModel.getObject();

            if (date == null
                    || hourModel.getObject() == null
                    || minuteModel.getObject() == null) {
                return null;
            }

            String time = hourModel.getObject() + ":" + minuteModel.getObject();
            return sdf.format(date) + "T" + time + ":00.000Z";
        }

        private String getTransportType() {
            if (transportModel.getObject() == null) {
                return null;
            }

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


        private void searchValidation(SearchAttributes currentSearch) throws FormValidationError {
            if (!currentSearch.isCoordinatesValid()) {
                throw new FormValidationError(
                        "Your address doesn't exist",
                        "Hint: Eircode might be entered as address as well"
                );
            }


            if (!currentSearch.isPriceValid()) {
                throw new FormValidationError(
                        "Please select min and max price in proper order",
                        "Hint: min price <= max price"
                );
            }

            if (!currentSearch.isBedsAmountValid()) {
                throw new FormValidationError(
                        "Please select min and max beds in proper order",
                        "Hint: min beds <= max beds"
                );
            }

            LocalDateTime now = LocalDateTime.now();
            String dateAndTime = getDateAndTime();

            if (dateAndTime == null
                    || LocalDateTime.parse(dateAndTime.substring(0, 19)).isBefore(now)) {
                throw new FormValidationError(
                        "Please enter date and time which are in future",
                        "Hint: date and time should be greater than now"
                );
            }
        }
    }

    private Label feedbackMessage(String message) {
        Label feedbackMessage = new Label("feedbackMessage", message);
        feedbackMessage.setOutputMarkupId(true);
        return feedbackMessage;
    }

    private void changePanelToResult(AjaxRequestTarget target, List<ResultMatchHouse> houses) {


        MarkupContainer parent = this.getParent();

        ResultPanel resultPanel = new ResultPanel(houses);
        parent.addOrReplace(resultPanel);
        target.add(resultPanel);

        WebMarkupContainer searchPanel = new WebMarkupContainer("searchPanel");
        searchPanel.setOutputMarkupId(true);
        parent.addOrReplace(searchPanel);
        target.add(searchPanel);

//        try {
//            MarkupContainer parent = this.getParent();
//
//            ResultPanel resultPanel = new ResultPanel(houses);
//            parent.addOrReplace(resultPanel);
//            target.add(resultPanel);
//
//            WebMarkupContainer searchPanel = SearchPanel.this;
//            searchPanel.setVisible(false);
//            parent.replace(searchPanel);
////            target.add(searchPanel);
//        } catch (ComponentNotFoundException ignored) {
//        }


    }
}

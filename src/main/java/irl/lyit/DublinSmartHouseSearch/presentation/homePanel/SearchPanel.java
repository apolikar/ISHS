package irl.lyit.DublinSmartHouseSearch.presentation.homePanel;

import irl.lyit.DublinSmartHouseSearch.controller.exception.TooManyPointsException;
import irl.lyit.DublinSmartHouseSearch.old.GeoCoordinates;
import irl.lyit.DublinSmartHouseSearch.old.SearchAttributes;
import irl.lyit.DublinSmartHouseSearch.presentation.AboutMe;
import irl.lyit.DublinSmartHouseSearch.presentation.HomePage;
import irl.lyit.DublinSmartHouseSearch.presentation.resultPanel.ResultPanel;
import irl.lyit.DublinSmartHouseSearch.service.GeoCoordinatesFinder;
import irl.lyit.DublinSmartHouseSearch.service.HouseService;
import irl.lyit.DublinSmartHouseSearch.service.ResultMatchHouse;
import irl.lyit.DublinSmartHouseSearch.service.TransportionType;
import irl.lyit.DublinSmartHouseSearch.service.addressFormatter.GoogleAddressFormatter;
import irl.lyit.DublinSmartHouseSearch.service.client.GMapsHTTPClient;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Comparator.comparing;

public class SearchPanel extends Panel {
    @SpringBean
    private HouseService houseService;


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


            List<Integer> timeList = Arrays.asList(10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 59);
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
            informationBox.add(new FeedbackPanel("feedbackMessage",
                    new ExactErrorLevelFilter(FeedbackMessage.ERROR)));
            add(informationBox);
            informationBox.setVisible(false);


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
                                travelTimeModel.getObject() * 60,
                                getTransportType(),
                                setPrice(minPriceModel.getObject()),
                                setPrice(maxPriceModel.getObject()),
                                minBedsModel.getObject(),
                                maxBedsModel.getObject()
                        );
                    }catch (NullPointerException e){
                        informationBox.setVisible(true);
                        error("Please fill all form fields");
                        throw new RestartResponseAtInterceptPageException(this.getPage());
                    } catch (IOException | InterruptedException e) {
                        error("Internal server error: 500");
                        return;
                    }

                    if (!searchValidation(searchAttributes)) {
                        informationBox.setVisible(true);
                        setResponsePage(getPage());
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
                        error("To process more points Enterprise plan is needed (250 euro per month)");
                        error("Hint: Try to narrow down search attributes");
                        setResponsePage(getPage());
                        return;
                    }

                    informationBox.setVisible(false);

                    if (results.isEmpty()) {
                        return;
                    }

                    // sort by travel time (low to high)
                    results.sort(comparing(ResultMatchHouse::getSecondsToTravel));

//                    //ToDo iterate list check same Lnt and Lat remove old object (timestamp)
//                    for(int i = 0; i < results.size() - 1; i++) {
//
//                        if(results.get(i).getSecondsToTravel() == results.get(i+1).getSecondsToTravel()){
//
//                            if(
//                                    results.get(i).getHouse().getLat() == results.get(i + 1).getHouse().getLat()
//                                    && results.get(i).getHouse().getLng() == results.get(i + 1).getHouse().getLng()
//                            ){
//                                if(results.get(i).getHouse().getUpdateTime()  > results.get(i + 1).getHouse().getUpdateTime()){
//
//                                    System.out.println("Stay:    " + results.get(i).getHouse().getUpdateTime());
//                                    System.out.println("removed: " + results.get(i + 1).getHouse().getUpdateTime());
//                                    System.out.println("------------------------------------------------");
//
//                                    results.remove(i + 1);
//                                } else {
//
//                                    System.out.println("Stay:    " + results.get(i + 1).getHouse().getUpdateTime());
//                                    System.out.println("removed: " + results.get(i).getHouse().getUpdateTime());
//                                    System.out.println("------------------------------------------------");
//
//                                    results.remove(i);
//                                }
//                            }
//                        }
//                    }

                    changePanelToResult(target, results);
                }
            });
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
            String time = hourModel.getObject() + ":" + minuteModel.getObject();
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

            if (currentSearch.getCoordinates().getLat() == 0
                    && currentSearch.getCoordinates().getLng() == 0) {
                error("Your address doesn't exist");
                error("Hint: Eircode might be entered as address as well");
                return false;
            }

            if (currentSearch.getMaxPrice() < currentSearch.getMinPrice()) {
                error("Please select min and max price in proper order");
                error("Hint: min price <= max price");
                return false;
            }

            if (currentSearch.getMaxBeds() < currentSearch.getMinBeds()) {
                error("Please select min and max beds in proper order");
                error("Hint: min beds <= max beds");
                return false;
            }

            if (searchDateTime.isBefore(now)) {
                error("Please enter date and time which are in future");
                error("Hint: date and time should be greater than now");
                return false;
            }

            return true;
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

    private void changePanelToResult(AjaxRequestTarget target, List<ResultMatchHouse> houses) {
        MarkupContainer parent = this.getParent();

        ResultPanel resultPanel = new ResultPanel(houses);
        parent.addOrReplace(resultPanel);
        target.add(resultPanel);

        WebMarkupContainer searchPanel = new WebMarkupContainer("searchPanel");
        searchPanel.setOutputMarkupId(true);
        parent.addOrReplace(searchPanel);
        target.add(searchPanel);

    }
}

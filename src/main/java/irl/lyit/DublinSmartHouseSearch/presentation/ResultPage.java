package irl.lyit.DublinSmartHouseSearch.presentation;

import com.google.gson.Gson;
import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.old.SearchAttributes;
import irl.lyit.DublinSmartHouseSearch.service.HouseService;
import irl.lyit.DublinSmartHouseSearch.service.ResultMatchHouse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.io.IOException;
import java.util.List;

import static java.util.Comparator.comparing;

public class ResultPage extends WebPage {

    @SpringBean
    private HouseService houseService;


    public ResultPage(PageParameters parameters) throws IOException, InterruptedException {
        super(parameters);

        StringValue searchAttributes = parameters.get("searchAttributes");
        SearchAttributes searchAttributes1 = new Gson().fromJson(searchAttributes.toString(), SearchAttributes.class);
        System.out.println(searchAttributes1);

//        List<House> houses = houseService.inTime(searchAttributes1);
//
//        RepeatingView housesView = new RepeatingView("house");
//        add(housesView);
//
//        if (houses.isEmpty()) {
//            housesView.setVisible(false);
//        }
//
//        for (House house : houses) {
//            housesView.add(new HousePanel(housesView.newChildId(), house));













//
//
//        List<ResultMatchHouse> results = houseService.inTime(searchAttributes1);
//        // sort by travel time (low to high)
//        results.sort(comparing(ResultMatchHouse::getSecondsToTravel));
//
//        RepeatingView housesView = new RepeatingView("house");
//        add(housesView);
//
//        if (results.isEmpty()) {
//            housesView.setVisible(false);
//        }
//
//        for (ResultMatchHouse resultHouse : results) {
//            housesView.add(new HousePanel(housesView.newChildId(), resultHouse.getHouse()));
//        }
//



























        List<ResultMatchHouse> results = houseService.inTime(searchAttributes1);
        ListDataProvider<ResultMatchHouse> listDataProvider = new ListDataProvider<ResultMatchHouse>(results);

        DataView<ResultMatchHouse> dataView = new DataView<ResultMatchHouse>("rows", listDataProvider) {

            @Override
            protected void populateItem(Item<ResultMatchHouse> item) {
                ResultMatchHouse house = item.getModelObject();
                RepeatingView repeatingView = new RepeatingView("dataRow");

                repeatingView.add(new Label(repeatingView.newChildId(), house.getSecondsToTravel()));
                repeatingView.add(new Label(repeatingView.newChildId(), house.getHouse().getAddress()));
                repeatingView.add(new Label(repeatingView.newChildId(), house.getHouse().getPrice()));
                repeatingView.add(new Label(repeatingView.newChildId(), house.getHouse().getBedrooms()));
                repeatingView.add(new Label(repeatingView.newChildId(), house.getHouse().getCityOrCounty()));
                repeatingView.add(new Label(repeatingView.newChildId(), house.getHouse().getLink()));
                item.add(repeatingView);
            }
        };
        add(dataView);
    }

}

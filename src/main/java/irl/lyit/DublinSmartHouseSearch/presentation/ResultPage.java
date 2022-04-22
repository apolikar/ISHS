package irl.lyit.DublinSmartHouseSearch.presentation;

import com.google.gson.Gson;
import irl.lyit.DublinSmartHouseSearch.old.SearchAttributes;
import irl.lyit.DublinSmartHouseSearch.service.HouseService;
import irl.lyit.DublinSmartHouseSearch.service.ResultMatchHouse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import java.io.IOException;
import java.util.ArrayList;
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

        List<ResultMatchHouse> results = houseService.inTime(searchAttributes1);
        List<ResultMatchHouse> inPriceBedsRangeHouses = inPriceBedsRangeHouses(results, searchAttributes1);
        // sort by travel time (low to high)
        inPriceBedsRangeHouses.sort(comparing(ResultMatchHouse::getSecondsToTravel));


        ListDataProvider<ResultMatchHouse> listDataProvider = new ListDataProvider<ResultMatchHouse>(inPriceBedsRangeHouses);

        DataView<ResultMatchHouse> dataView = new DataView<ResultMatchHouse>("rows", listDataProvider) {
            @Override
            protected void populateItem(Item<ResultMatchHouse> item) {
                ResultMatchHouse houses = item.getModelObject();
                RepeatingView repeatingView = new RepeatingView("dataRow");

                repeatingView.add(new Label(repeatingView.newChildId(), houses.getSecondsToTravel()));
                repeatingView.add(new Label(repeatingView.newChildId(), houses.getHouse().getAddress()));
                repeatingView.add(new Label(repeatingView.newChildId(), houses.getHouse().getPrice()));
                repeatingView.add(new Label(repeatingView.newChildId(), houses.getHouse().getBedrooms()));
                repeatingView.add(new Label(repeatingView.newChildId(), houses.getHouse().getCityOrCounty()));
                repeatingView.add(new Label(repeatingView.newChildId(), houses.getHouse().getLink()));

                item.add(repeatingView);
            }
        };

        dataView.setItemsPerPage(20);
        add(dataView);
        add(new PagingNavigator("pagingNavigator", dataView));
    }


    private List<ResultMatchHouse> inPriceBedsRangeHouses(List<ResultMatchHouse> notSortedHouses, SearchAttributes searchAttributes) {

        List<ResultMatchHouse> resultMatchHouseList = new ArrayList<>();
        for (ResultMatchHouse searchResult : notSortedHouses) {

            if ((searchResult.getHouse().getBedrooms() >= searchAttributes.getMinBeds() &&
                    searchResult.getHouse().getBedrooms() <= searchAttributes.getMaxBeds()) &&
                    (searchResult.getHouse().getPrice() >= searchAttributes.getMinPrice() &&
                            searchResult.getHouse().getPrice() <= searchAttributes.getMaxPrice())) {

                resultMatchHouseList.add(searchResult);
            }
        }
        return resultMatchHouseList;
    }
}


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


package irl.lyit.DublinSmartHouseSearch.presentation;

import com.google.gson.Gson;
import irl.lyit.DublinSmartHouseSearch.old.SearchAttributes;
import irl.lyit.DublinSmartHouseSearch.service.HouseService;
import irl.lyit.DublinSmartHouseSearch.service.ResultMatchHouse;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupCloseLink;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Comparator.comparing;

public class ResultPage extends WebPage {

    @SpringBean
    private HouseService houseService;


    public ResultPage(PageParameters parameters) throws IOException, InterruptedException {
        super(parameters);

        add(new AjaxLink<String>("homePage") {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                setResponsePage(HomePage.class);
            }
        });

        add(new AjaxLink<String>("aboutMeLink") {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                setResponsePage(AboutMe.class);
            }
        });

        add(new AjaxLink<String>("newSearch") {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                setResponsePage(HomePage.class);
            }
        });



        StringValue searchAttributes = parameters.get("searchAttributes");
        SearchAttributes searchAttributes1 = new Gson().fromJson(searchAttributes.toString(), SearchAttributes.class);
        System.out.println(searchAttributes1);

        List<ResultMatchHouse> results = houseService.inTime(searchAttributes1);
        List<ResultMatchHouse> inPriceBedsRangeHouses = inPriceBedsRangeHouses(results, searchAttributes1);

        // sort by travel time (low to high)
        inPriceBedsRangeHouses.sort(comparing(ResultMatchHouse::getSecondsToTravel));

        add(new Label("totalHouses", new Model<>(inPriceBedsRangeHouses.size())));
        System.out.println(new Date(inPriceBedsRangeHouses.get(0).getHouse().getUpdateTime()));

        ListDataProvider<ResultMatchHouse> listDataProvider = new ListDataProvider<ResultMatchHouse>(inPriceBedsRangeHouses);




        DataView<ResultMatchHouse> dataView = new DataView<>("rows", listDataProvider) {
            @Override
            protected void populateItem(Item<ResultMatchHouse> item) {
                ResultMatchHouse houses = item.getModelObject();
                RepeatingView repeatingView = new RepeatingView("dataRow");

                repeatingView.add(new Label(repeatingView.newChildId(), houses.getTimeString()));
                repeatingView.add(new Label(repeatingView.newChildId(), houses.getHouse().getAddress()));
                repeatingView.add(new Label(repeatingView.newChildId(), houses.getHouse().getPrice()));
                repeatingView.add(new Label(repeatingView.newChildId(), houses.getHouse().getBedrooms()));

                repeatingView.add(new ExternalLink(repeatingView.newChildId(), new Model<>(houses.getHouse().getLink())));
//                repeatingView.add(new Label(repeatingView.newChildId(), houses.getHouse().getLink()));

                item.add(repeatingView);
            }
        };

        dataView.setItemsPerPage(10);
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


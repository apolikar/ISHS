package irl.lyit.DublinSmartHouseSearch.presentation.resultPanel;

import irl.lyit.DublinSmartHouseSearch.presentation.AboutMe;
import irl.lyit.DublinSmartHouseSearch.presentation.HomePage;
import irl.lyit.DublinSmartHouseSearch.service.ResultMatchHouse;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.Model;

import java.util.Date;
import java.util.List;

public class ResultPanel extends Panel {
    public ResultPanel(List<ResultMatchHouse> houses) {
        super("resultPanel");
        setOutputMarkupId(true);

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

        add(new Label("totalHouses", new Model<>(houses.size())));
        System.out.println(new Date(houses.get(0).getHouse().getUpdateTime()));

        ListDataProvider<ResultMatchHouse> listDataProvider = new ListDataProvider<>(houses);




        DataView<ResultMatchHouse> dataView = new DataView<>("rows", listDataProvider) {
            @Override
            protected void populateItem(Item<ResultMatchHouse> item) {
                ResultMatchHouse houses = item.getModelObject();
                RepeatingView repeatingView = new RepeatingView("dataRow");
                item.add(repeatingView);

                repeatingView
                        .add(new WebMarkupContainer(repeatingView.newChildId())
                                .add(new Label("item", houses.getTimeString())));
                repeatingView
                        .add(new WebMarkupContainer(repeatingView.newChildId())
                                .add(new Label("item", houses.getHouse().getAddress())));
                repeatingView
                        .add(new WebMarkupContainer(repeatingView.newChildId())
                                .add(new Label("item", houses.getHouse().getPrice())));
                repeatingView
                        .add(new WebMarkupContainer(repeatingView.newChildId())
                                .add(new Label("item", houses.getHouse().getBedrooms())));
                repeatingView
                        .add(new WebMarkupContainer(repeatingView.newChildId())
                                .add(new ExternalLink("item", houses.getHouse().getLink(), "link") {

                                    @Override
                                    protected void onComponentTag(ComponentTag tag) {
                                        tag.setName("a");
                                        tag.put("target", "_blank");
                                        super.onComponentTag(tag);
                                    }
                                }));
            }
        };

        dataView.setItemsPerPage(15);
        add(dataView);
        add(new PagingNavigator("pagingNavigator", dataView));
    }
}

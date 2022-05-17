package irl.lyit.DublinSmartHouseSearch.presentation;

import irl.lyit.DublinSmartHouseSearch.presentation.searchPanel.SearchPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;

public class HomePage extends WebPage {
    public HomePage() {
        this.setOutputMarkupId(true);

        add(new SearchPanel());

        WebMarkupContainer resultPanel = new WebMarkupContainer("resultPanel");
        resultPanel.setOutputMarkupId(true);
        add(resultPanel);
    }
}

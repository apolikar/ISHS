package irl.lyit.DublinSmartHouseSearch.presentation;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;

public class AboutMe extends WebPage {

    public AboutMe() {
        add(new AjaxLink<String>("homePage") {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                setResponsePage(HomePage.class);
            }
        });
    }
}

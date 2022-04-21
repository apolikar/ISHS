package irl.lyit.DublinSmartHouseSearch.presentation.component;

import irl.lyit.DublinSmartHouseSearch.dao.House;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;

public class HousePanel extends Panel {
    public HousePanel(String id, House house) {
        super(id);

        add(new ContextImage("image", "https://www.w3schools.com/html/pic_trulli.jpg"));
        add(new Label("address", house.getAddress()));
        add(new Label("price", house.getPrice()));
    }
}

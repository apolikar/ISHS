package irl.lyit.DublinSmartHouseSearch.presentation;

import irl.lyit.DublinSmartHouseSearch.dao.House;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.ComponentModel;
import org.apache.wicket.model.ComponentPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.Iterator;
import java.util.List;

public final class HouseDataProvider implements IDataProvider<House> {

    private final List<House> houses;

    public HouseDataProvider(List<House> houses) {
        this.houses = houses;
    }


    @Override
    public Iterator<? extends House> iterator(long l, long l1) {
        return houses.iterator();
    }

    @Override
    public long size() {
        return houses.size();
    }

    @Override
    public IModel<House> model(House house) {
        return new Model<>(house);
    }

    @Override
    public void detach() {

    }
}

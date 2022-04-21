package irl.lyit.DublinSmartHouseSearch.presentation;

import com.google.gson.Gson;
import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.kendo.ui.datatable.DataTable;
import com.googlecode.wicket.kendo.ui.datatable.column.CurrencyPropertyColumn;
import com.googlecode.wicket.kendo.ui.datatable.column.IColumn;
import com.googlecode.wicket.kendo.ui.datatable.column.IdPropertyColumn;
import com.googlecode.wicket.kendo.ui.datatable.column.PropertyColumn;
import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.old.SearchAttributes;
import irl.lyit.DublinSmartHouseSearch.service.HouseService;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Generics;
import org.apache.wicket.util.string.StringValue;

import java.io.IOException;
import java.util.List;

public class ResultPage extends WebPage {

    @SpringBean
    private HouseService houseService;


    public ResultPage(PageParameters parameters) throws IOException, InterruptedException {
        super(parameters);

        StringValue searchAttributes = parameters.get("searchAttributes");
        SearchAttributes searchAttributes1 = new Gson().fromJson(searchAttributes.toString(), SearchAttributes.class);
        System.out.println(searchAttributes1);

        List<House> houses = houseService.inTime(searchAttributes1);

        // DataTable //
        Options options = new Options();
        options.set("height", 430);
        options.set("pageable", "{ pageSizes: [ 25, 50, 100 ] }");
        // options.set("sortable", true); // already set, as provider IS-A ISortStateLocator
        options.set("groupable", true);
        options.set("columnMenu", true);
        options.set("selectable", false); // Options.asString("multiple, row"). Caution does not work for 'cell'

        final DataTable<House> table =
                new DataTable<House>("datatable", newColumnList(), newDataProvider(houses), 25, options);

        add(table);


    }


    private static IDataProvider<House> newDataProvider(List<House> houses)
    {
        return new HouseDataProvider(houses);
    }

    private static List<IColumn> newColumnList()
    {
        List<IColumn> columns = Generics.newArrayList();

        columns.add(new PropertyColumn("Address:", "address"));
        columns.add(new PropertyColumn("Price:", "price"));
        columns.add(new CurrencyPropertyColumn("Price", "price", 70));
        // columns.add(new DatePropertyColumn("Date", "date"));

        return columns;
    }


}

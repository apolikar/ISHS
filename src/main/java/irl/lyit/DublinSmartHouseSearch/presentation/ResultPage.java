package irl.lyit.DublinSmartHouseSearch.presentation;

import com.google.gson.Gson;
import irl.lyit.DublinSmartHouseSearch.old.SearchAttributes;
import irl.lyit.DublinSmartHouseSearch.service.HouseService;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class ResultPage extends WebPage {

    @SpringBean
    private HouseService houseService;


    public ResultPage(PageParameters parameters) throws IOException, InterruptedException {
        super(parameters);

        StringValue searchAttributes = parameters.get("searchAttributes");
        SearchAttributes searchAttributes1 = new Gson().fromJson(searchAttributes.toString(), SearchAttributes.class);
        System.out.println(searchAttributes1);



        houseService.inTime(searchAttributes1);
    }
}

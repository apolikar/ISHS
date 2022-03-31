package irl.lyit.DublinSmartHouseSearch;

import com.giffing.wicket.spring.boot.context.condition.ConditionalOnWicket;
import com.giffing.wicket.spring.boot.context.extensions.ApplicationInitExtension;
import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration;
import org.apache.wicket.protocol.http.WebApplication;

@ApplicationInitExtension
@ConditionalOnWicket(value = 7, range = ConditionalOnWicket.Range.EQUALS_OR_HIGHER)
public class ConditionalConfig implements WicketApplicationInitConfiguration {
    @Override
    public void init(WebApplication webApplication) {
        webApplication.getCspSettings().blocking().disabled();
    }

}

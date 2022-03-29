package irl.lyit.DublinSmartHouseSearch.service.addressFormatter;

import org.springframework.stereotype.Component;

@Component
public class GoogleAddressFormatter implements AddressFormatter {


    @Override
    public String formatAddress(String address) {

        StringBuilder result = new StringBuilder();

        String[] addressArray = address.split(" ");
        for(String part : addressArray) {
            result.append(part).append("+");
        }

        return result.toString();

    }
}

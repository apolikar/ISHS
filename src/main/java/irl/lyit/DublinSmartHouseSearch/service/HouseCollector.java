package irl.lyit.DublinSmartHouseSearch.service;

import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.dao.HouseRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class HouseCollector {

    private final HouseRepository houseRepository;

    @Autowired
    public HouseCollector(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    public List<House> getHouses() {
        return houseRepository.findAll();
    }

    // after 12 hours
    @Scheduled(fixedDelay = 43200000)
    private void searchHouse() throws IOException {

        // measure time
        long start = System.currentTimeMillis();

        houseRepository.deleteAll();

        int from = 0;

        String first = "https://www.daft.ie/property-for-sale/ireland/houses?from=" + from;

        Document page = Jsoup.connect(first).get();
        String propertiesForSale = page.getElementsByClass("styles__SearchH1-sc-1t5gb6v-3 bekXMP").text();
        int total = Integer.parseInt(propertiesForSale.substring(0, propertiesForSale.indexOf(" ")).replace(",", ""));

        while (from < total) {

            page = Jsoup.connect(first + from).get();


            Elements houses = page.select("#__next > main > div.styles__MainFlexWrapper-sc-1t5gb6v-0.fLpeuJ >" +
                    " div.SearchPage__MainColumn-gg133s-0.jsbRqT > ul > li");

            for (org.jsoup.nodes.Element house : houses) {

                // link for each property
                String link = house.select("a").attr("abs:href");


                Document property = null;

                try {

                    property = Jsoup.connect(link).get();

                    String geoLocation = property.getElementsByClass("NewButton__StyledButtonHrefLink-yem86a-3 hzIHZl")
                            .select("a").attr("abs:href");

                    if (geoLocation.length() == 0)
                        continue;

                    double lat = Double.parseDouble(geoLocation.substring(geoLocation.lastIndexOf(":") + 1, geoLocation.indexOf("+")));
                    double lng = Double.parseDouble(geoLocation.substring(geoLocation.lastIndexOf("-")));

                    String priceStr = property.getElementsByClass("TitleBlock__StyledSpan-sc-1avkvav-5 gUFuYZ").text().replace(",", "");
                    if (priceStr.isEmpty() || priceStr.contains("Price on Application") || priceStr.contains("£")) {
                        continue;
                    }
                    double price = Double.parseDouble(priceStr.substring(priceStr.indexOf("€") + 1));

                    String address = property.getElementsByClass("TitleBlock__Address-sc-1avkvav-8 fdWqfc").text();

                    int beds = 0;

                    // sometimes advertisements dont have any information
                    // handling out of bounds exception
                    try {
                        String bedsStr = property.getElementsByClass("TitleBlock__CardInfoItem-sc-1avkvav-9 enhPnH").get(0).text();
                        if(!bedsStr.contains("Bed"))
                            continue;

                        beds = Integer.parseInt(bedsStr.substring(0, bedsStr.indexOf(" ")));

                    }catch (IndexOutOfBoundsException e) {
                        continue;
                    }

                    House newHouse = new House(link, price, address, beds, lat, lng);

                    houseRepository.save(newHouse);
                    System.out.println(newHouse);

                } catch (IOException e) {

                    e.printStackTrace();
                }

            }
            from += 20;
        }

        long end = System.currentTimeMillis();
        System.out.println("To process " + total + " houses took: " + timeInfo(end - start));

    }

    // get time in minutes and seconds
    private static String timeInfo(long ms) {

        long minutes = (ms / 1000) / 60;
        long seconds = (ms / 1000) % 60;

        return minutes + " minutes " + seconds + " seconds.";
    }


}



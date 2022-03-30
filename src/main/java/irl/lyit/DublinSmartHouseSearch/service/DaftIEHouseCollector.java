package irl.lyit.DublinSmartHouseSearch.service;

import irl.lyit.DublinSmartHouseSearch.dao.House;
import irl.lyit.DublinSmartHouseSearch.dao.HouseRepository;
import irl.lyit.DublinSmartHouseSearch.old.GeoCoordinates;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class DaftIEHouseCollector implements HouseCollector {

    private final HouseRepository houseRepository;

    @Autowired
    public DaftIEHouseCollector(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }



    /**
     * Parse houses for sale information from property website to update DB
     */
    @Override
    public void collect() {

        houseRepository.deleteAllInBatch();

        long start = System.currentTimeMillis();
        String now = getDateTime();

        int housesPerPage = 0;
        String url = "https://www.daft.ie/property-for-sale/houses?from=" + housesPerPage;

        Document page;
        try {
            page = Jsoup.connect(url).get();
        } catch (IOException e) {
            return;
        }

        int allHousesForSaleNumber = getPropertiesNumber(page);
        while (housesPerPage < allHousesForSaleNumber) {

            try {
                page = Jsoup.connect(url + housesPerPage).get();
            } catch (IOException e) {
                continue;
            }

            Elements allHousesOnPage = getAllHousesOnPage(page);

            for (Element house : allHousesOnPage) {
                // link for each property
                String link = getHouseLink(house);
                collectAllHouseDetails(link);
            }
            housesPerPage += 20;
        }

        long end = System.currentTimeMillis();
        System.out.println("Last DB update: " + now);
        System.out.println("To process " + allHousesForSaleNumber + " houses took: " + timeInfo(end - start));

    }


    /**
     * collect all data for house advertisement page
     * @param link for the house advertisement
     */
    private void collectAllHouseDetails(String link){

        try {
            Document property = Jsoup.connect(link).get();
            GeoCoordinates  houseCoordinates = getHouseCoordinates(property);
            double price = getHousePrice(property);
            String address = getHouseAddress(property);
            String cityOrCounty = getHouseCityCounty(address);
            int beds = getHouseBedsNumber(property);

            if(price <= 0 || beds <= 0 || (houseCoordinates.getLat() == 0 && houseCoordinates.getLng() == 0))
                return;

            House newHouse = new House(link, price, address, cityOrCounty, beds, houseCoordinates.getLat(),
                    houseCoordinates.getLng());
            houseRepository.save(newHouse);
            System.out.println(newHouse);
        } catch (IOException e) {
            return;
        }
    }


    /**
     * Get total number of properties for sale on the site.
     * @param webSiteIndexPage website index page
     * @return total number of properties for sale at the moment
     */
    private int getPropertiesNumber(Document webSiteIndexPage) {

        String propertiesForSale = webSiteIndexPage.getElementsByClass("styles__SearchH1-sc-1t5gb6v-3 bekXMP").text();
        return Integer.parseInt(propertiesForSale.substring(0, propertiesForSale.indexOf(" ")).replace(",",
                ""));
    }


    /**
     * Get all houses for sale on the single webpage
     * @param page webpage
     * @return list of elements on the webpage (houses)
     */
    private Elements getAllHousesOnPage(Document page) {
        return page.select("#__next > main > div.styles__MainFlexWrapper-sc-1t5gb6v-0.fLpeuJ >" +
                " div.SearchPage__MainColumn-gg133s-0.jsbRqT > ul > li");
    }


    /**
     * Get link for the house advertisement
     * @param house house element
     * @return link string value
     */
    private String getHouseLink(Element house){
        return house.select("a").attr("abs:href");
    }


    /**
     * Get house geo coordinates (lat and lng)
     * @param housePage house advertisement webpage
     * @return house GeoCoordinates object
     */
    private GeoCoordinates getHouseCoordinates(Document housePage) {

        String geoLocation = housePage.getElementsByClass(
                        "NewButton__StyledButtonHrefLink-yem86a-3 hzIHZl")
                .select("a").attr("abs:href");

        if(geoLocation.isEmpty())
            return new GeoCoordinates();

        double lat = Double.parseDouble(geoLocation.substring(geoLocation.lastIndexOf(":") + 1,
                geoLocation.indexOf("+")));
        double lng = Double.parseDouble(geoLocation.substring(geoLocation.lastIndexOf("-")));
        return new GeoCoordinates(lat, lng);
    }


    /**
     * Get house price
     * @param housePage house advertisement webpage
     * @return house price
     */
    private double getHousePrice(Document housePage) {

        String priceStr = housePage.getElementsByClass(
                "TitleBlock__StyledSpan-sc-1avkvav-5 gUFuYZ").text().replace(",", "");
        if (priceStr.isEmpty() || priceStr.contains("Price on Application") || priceStr.contains("£")) {
            return -1;
        }
        return Double.parseDouble(priceStr.substring(priceStr.indexOf("€") + 1));
    }


    /**
     * Get house address
     * @param housePage house advertisement webpage
     * @return house address
     */
    private String getHouseAddress(Document housePage) {

        return housePage.getElementsByClass("TitleBlock__Address-sc-1avkvav-8 fdWqfc").text();
    }


    /**
     * Get house city or county
     * @param houseAddress house advertisement webpage
     * @return house city or county
     */
    private String getHouseCityCounty(String houseAddress) {

        return houseAddress.substring(houseAddress.lastIndexOf(",") + 1).trim();
    }


    /**
     * Get house number of beds
     * @param housePage house advertisement webpage
     * @return house number of beds
     */
    private int getHouseBedsNumber(Document housePage) {

        int beds;

        try {
            String bedsStr = housePage.getElementsByClass("TitleBlock__CardInfoItem-sc-1avkvav-9 enhPnH").get(0).text();
            if (!bedsStr.contains("Bed")) {
                return -1;
            }

            beds = Integer.parseInt(bedsStr.substring(0, bedsStr.indexOf(" ")));
        } catch (IndexOutOfBoundsException e) {
            return -1;
        }
        return beds;
    }


    /**
     * Get runtime value string from start to end
     * @param ms end - start in ms
     * @return string with number of minutes and seconds
     */
    private static String timeInfo(long ms) {

        long minutes = (ms / 1000) / 60;
        long seconds = (ms / 1000) % 60;
        return minutes + " minutes " + seconds + " seconds.";
    }


    /**
     * Get date and time at the moment
     * @return date and time now
     */
    private String getDateTime() {

        Date dateNow = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(dateNow);
    }




}



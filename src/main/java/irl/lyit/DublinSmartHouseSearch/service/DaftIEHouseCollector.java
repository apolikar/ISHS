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

        // connect to the first page with all available houses
        Document page = checkConnection(url);

        if (page == null) {
            return;
        }

        int allHousesForSaleNumber = getPropertiesNumber(page);
        while (housesPerPage < allHousesForSaleNumber) {

            // connect to the next website pages (will stay on first page on the first run)
            page = checkConnection(url + housesPerPage);
            if (page == null) {
                return;
            }
            // get list of all house objects (HTML)
            Elements allHousesOnPage = getAllHousesOnPage(page);

            for (Element house : allHousesOnPage) {

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
     * Check website connection for information parsing.
     *
     * @param url website url
     * @return website page HTML structure
     */
    private Document checkConnection(String url) {

        Document page;
        try {
            page = Jsoup.connect(url).get();
        } catch (IOException e) {
            page = null;
        }
        return page;
    }


    /**
     * collect all data for house advertisement page
     *
     * @param link for the house advertisement
     */
    private void collectAllHouseDetails(String link) {

        try {
            Document property = Jsoup.connect(link).get();
            GeoCoordinates houseCoordinates = getHouseCoordinates(property);
            double price = getHousePrice(property);
            String address = getHouseAddress(property);
            String cityOrCounty = getHouseCityCounty(address);
            int beds = getHouseBedsNumber(property);

            if (price <= 0
                    || beds <= 0
                    || (houseCoordinates.getLat() == 0 && houseCoordinates.getLng() == 0)) {
                return;
            }

            House newHouse = new House(
                    link,
                    price,
                    address,
                    cityOrCounty,
                    beds,
                    houseCoordinates.getLat(),
                    houseCoordinates.getLng()
            );
            houseRepository.save(newHouse);
            System.out.println(newHouse);
        } catch (IOException ignored) {
        }
    }


    /**
     * Get total number of properties for sale on the site.
     *
     * @param webSiteIndexPage website index page
     * @return total number of properties for sale at the moment
     */
    private int getPropertiesNumber(Document webSiteIndexPage) {

        Elements propertiesForSale = webSiteIndexPage.getElementsByClass("SearchPage__SearchResultsHeaderWrapper-gg133s-4 iEmWdr");
        String s = propertiesForSale.text();
        String d = s.substring(0, s.indexOf(" ")).replace(",", "");

        return Integer.parseInt(d);

    }


    /**
     * Get all houses for sale on the single webpage
     *
     * @param page webpage
     * @return list of elements on the webpage (houses)
     */
    private Elements getAllHousesOnPage(Document page) {

          return page.getElementsByClass("SearchPage__SearchResults-gg133s-3 jGQNan");

    }


    /**
     * Get link for the house advertisement
     *
     * @param house house element
     * @return link string value
     */
    private String getHouseLink(Element house) {
        return house.select("a").attr("abs:href");
    }


    /**
     * Get house geo coordinates (lat and lng)
     *
     * @param housePage house advertisement webpage
     * @return house GeoCoordinates object
     */
    private GeoCoordinates getHouseCoordinates(Document housePage) {

        String geoLocation = housePage.getElementsByClass(
                        "NewButton__ButtonContainer-yem86a-4 deYANw button-container")
                .select("a").attr("abs:href");

        GeoCoordinates houseCoordinates;

        try {
            double lat = Double.parseDouble(geoLocation.substring(geoLocation.lastIndexOf(":") + 1,
                    geoLocation.indexOf("+")));
            double lng = Double.parseDouble(geoLocation.substring(geoLocation.lastIndexOf("-")));
            houseCoordinates = new GeoCoordinates(lat, lng);

        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            houseCoordinates = new GeoCoordinates();
        }

        return houseCoordinates;
    }


    /**
     * Get house price
     *
     * @param housePage house advertisement webpage
     * @return house price
     */
    private double getHousePrice(Document housePage) {

        String priceStr = housePage.getElementsByClass(
                "TitleBlock__StyledSpan-sc-1avkvav-5 fKAzIL").text().replace(",", "");

        double price;

        try {
            price = Double.parseDouble(priceStr.substring(priceStr.indexOf("€") + 1));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            price = -1;
        }
        return price;
    }


    /**
     * Get house address
     *
     * @param housePage house advertisement webpage
     * @return house address
     */
    private String getHouseAddress(Document housePage) {

        return housePage.getElementsByClass("TitleBlock__Address-sc-1avkvav-8 dzihxK").text();
    }


    /**
     * Get house city or county
     *
     * @param houseAddress house advertisement webpage
     * @return house city or county
     */
    private String getHouseCityCounty(String houseAddress) {

        return houseAddress.substring(houseAddress.lastIndexOf(",") + 1).trim();
    }


    /**
     * Get house number of beds
     *
     * @param housePage house advertisement webpage
     * @return house number of beds
     */
    private int getHouseBedsNumber(Document housePage) {

        int beds;

        try {
            String bedsStr = housePage.getElementsByClass("TitleBlock__CardInfoItem-sc-1avkvav-9 fgXVWJ").get(0).text();
            if (!bedsStr.contains("Bed")) {
                return -1;
            }

            beds = Integer.parseInt(bedsStr.substring(0, bedsStr.indexOf(" ")));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return -1;
        }
        return beds;
    }


    /**
     * Get runtime value string from start to end
     *
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
     *
     * @return date and time now
     */
    private String getDateTime() {

        Date dateNow = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(dateNow);
    }


}
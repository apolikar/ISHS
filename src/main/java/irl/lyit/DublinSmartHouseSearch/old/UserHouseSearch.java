package irl.lyit.DublinSmartHouseSearch.old;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserHouseSearch {

    private double minPrice;
    private double maxPrice;
    private int minBeds;
    private int maxBeds;


    public UserHouseSearch(double minPrice, double maxPrice, int minBeds, int maxBeds) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minBeds = minBeds;
        this.maxBeds = maxBeds;
    }

    public UserHouseSearch(double maxPrice, int minBeds) {
        this.maxPrice = maxPrice;
        this.minBeds = minBeds;
    }


    public UserHouseSearch() {
    }


    // get time in minutes and seconds
    private static String timeInfo(long ms) {

        long minutes = (ms / 1000) / 60;
        long seconds = (ms / 1000) % 60;

        return minutes + " minutes " + seconds + " seconds.";
    }







    public void searchHouse() throws IOException {


        // measure time
        long start = System.currentTimeMillis();


        int from = 0;
        int count = 0;

        String first = "https://www.daft.ie/property-for-sale/dublin?salePrice_from=" + minPrice +
                "&salePrice_to=" + maxPrice +
                "&numBeds_from=" + minBeds +
                "&numBeds_to=" + maxBeds +
                "&from=" + from;


        Document page = Jsoup.connect(first).get();

        String propertiesForSale = page.getElementsByClass("styles__SearchH1-sc-1t5gb6v-3 bekXMP").text();
        int total = Integer.parseInt(propertiesForSale.substring(0, propertiesForSale.indexOf(" ")).replace(",", ""));
        System.out.println("Total number of houses available for sale in Dublin: " + total);

        // daft.ie has 20 ads per page


        while (from < total) {

            page = Jsoup.connect(first + from).get();


            Elements houses = page.select("#__next > main > div.styles__MainFlexWrapper-sc-1t5gb6v-0.fLpeuJ >" +
                    " div.SearchPage__MainColumn-gg133s-0.jsbRqT > ul > li");

            for (int i = 0; i < houses.size(); i++) {

                // link for each property
                String link = houses.get(i).select("a").attr("abs:href");

                Document property = Jsoup.connect(link).get();

                // handle geolocation
                String geoLocation = property.getElementsByClass("NewButton__StyledButtonHrefLink-yem86a-3 hzIHZl")
                        .select("a").attr("abs:href");
                double lat = Double.parseDouble(geoLocation.substring(geoLocation.lastIndexOf(":") + 1, geoLocation.indexOf("+")));
                double lng = Double.parseDouble(geoLocation.substring(geoLocation.lastIndexOf("-")));

                String priceStr = property.getElementsByClass("TitleBlock__StyledSpan-sc-1avkvav-5 gUFuYZ").text().replace(",", "");
                if (priceStr.isEmpty()) {
                    continue;
                }
                double price = Double.parseDouble(priceStr.substring(priceStr.indexOf("€") + 1));

                String address = property.getElementsByClass("TitleBlock__Address-sc-1avkvav-8 fdWqfc").text();

                String bedsStr = property.getElementsByClass("TitleBlock__CardInfoItem-sc-1avkvav-9 enhPnH").get(0).text();
                int beds = Integer.parseInt(bedsStr.substring(0, bedsStr.indexOf(" ")));

                System.out.println("\n" + link + "\nLatitude: " + lat + "Longtitude: " + lng + "\nPrice: " + price + "\nAddress: " +  address + "\nBeds: " + beds + "\n");

            }
            from += 20;
        }

        long end = System.currentTimeMillis();
        System.out.println("To process " + total + " houses took: " + timeInfo(end - start));

    }



    public void searchHouseInBoundingBox() throws IOException {


        List<String> result = new ArrayList<>();


        // measure time
        long start = System.currentTimeMillis();


        int from = 0;
        int count = 0;

        String first = "https://www.daft.ie/property-for-sale/dublin/houses??salePrice_from=" + minPrice +
                "&salePrice_to=" + maxPrice +
                "&numBeds_from=" + minBeds +
                "&numBeds_to=" + maxBeds +
                "&from=" + from;


        Document page = Jsoup.connect(first).get();

        String propertiesForSale = page.getElementsByClass("styles__SearchH1-sc-1t5gb6v-3 bekXMP").text();
        int total = Integer.parseInt(propertiesForSale.substring(0, propertiesForSale.indexOf(" ")).replace(",", ""));
        System.out.println("Total number of houses available for sale in Dublin: " + total);

        // daft.ie has 20 ads per page


        while (from < total) {

            page = Jsoup.connect(first + from).get();


            Elements houses = page.select("#__next > main > div.styles__MainFlexWrapper-sc-1t5gb6v-0.fLpeuJ >" +
                    " div.SearchPage__MainColumn-gg133s-0.jsbRqT > ul > li");

            for (int i = 0; i < houses.size(); i++) {

                // link for each property
                String link = houses.get(i).select("a").attr("abs:href");

                Document property = Jsoup.connect(link).get();

                // handle geolocation
                String geoLocation = property.getElementsByClass("NewButton__StyledButtonHrefLink-yem86a-3 hzIHZl")
                        .select("a").attr("abs:href");
                double lat = Double.parseDouble(geoLocation.substring(geoLocation.lastIndexOf(":") + 1, geoLocation.indexOf("+")));
                double lng = Double.parseDouble(geoLocation.substring(geoLocation.lastIndexOf("-")));

                String priceStr = property.getElementsByClass("TitleBlock__StyledSpan-sc-1avkvav-5 gUFuYZ").text().replace(",", "");
                if (priceStr.isEmpty()) {
                    continue;
                }
                double price = Double.parseDouble(priceStr.substring(priceStr.indexOf("€") + 1));

                String address = property.getElementsByClass("TitleBlock__Address-sc-1avkvav-8 fdWqfc").text();

                String bedsStr = property.getElementsByClass("TitleBlock__CardInfoItem-sc-1avkvav-9 enhPnH").get(0).text();
                int beds = Integer.parseInt(bedsStr.substring(0, bedsStr.indexOf(" ")));

                String house = "\n" + link + "\nLatitude: " + lat + "Longtitude: " + lng + "\nPrice: " + price + "\nAddress: " +  address + "\nBeds: " + beds + "\n";
                System.out.println(house);

                if( (lat > 53.42693289149682 && lat < 53.46916335929757) &&
                        (lng < -6.188578104972818 && lng > -6.25174220713779)) {
                    System.out.println("Found house");
                    result.add(house);
                }


            }
            from += 20;
        }

        long end = System.currentTimeMillis();
        System.out.println("To process " + total + " houses took: " + timeInfo(end - start));

        for(String s : result)
            System.out.println(s);

    }








    public void searchParallel() throws IOException, InterruptedException {

        // measure time
        long start = System.currentTimeMillis();


        int from = 0;
        int count = 0;

        String first = "https://www.daft.ie/property-for-sale/dublin?salePrice_from=" + minPrice +
                "&salePrice_to=" + maxPrice +
                "&numBeds_from=" + minBeds +
                "&numBeds_to=" + maxBeds +
                "&from=" + from;


        Document page = Jsoup.connect(first).get();

        String propertiesForSale = page.getElementsByClass("styles__SearchH1-sc-1t5gb6v-3 bekXMP").text();
        int total = Integer.parseInt(propertiesForSale.substring(0, propertiesForSale.indexOf(" ")).replace(",", ""));
        System.out.println("Total number of houses available for sale in Dublin: " + total);

        // daft.ie has 20 ads per page


        while (from < total) {

            page = Jsoup.connect(first + from).get();


            Elements houses = page.select("#__next > main > div.styles__MainFlexWrapper-sc-1t5gb6v-0.fLpeuJ >" +
                    " div.SearchPage__MainColumn-gg133s-0.jsbRqT > ul > li");

            System.out.println();


            Thread firstThread = new Thread(
                    () -> {

                        for (int i = 0; i < houses.size() / 2 / 2; i++) {

                            // link for each property
                            String link = houses.get(i).select("a").attr("abs:href");

                            // redirect to property link

                            Document property = null;
                            try {
                                property = Jsoup.connect(link).get();

                                // handle geolocation
                                String geoLocation = property.getElementsByClass("NewButton__StyledButtonHrefLink-yem86a-3 hzIHZl")
                                        .select("a").attr("abs:href");
                                double lat = Double.parseDouble(geoLocation.substring(geoLocation.lastIndexOf(":") + 1, geoLocation.indexOf("+")));
                                double lng = Double.parseDouble(geoLocation.substring(geoLocation.lastIndexOf("-")));

                                String priceStr = property.getElementsByClass("TitleBlock__StyledSpan-sc-1avkvav-5 gUFuYZ").text().replace(",", "");
                                if (priceStr.isEmpty()) {
                                    continue;
                                }
                                double price = Double.parseDouble(priceStr.substring(priceStr.indexOf("€") + 1));

                                String address = property.getElementsByClass("TitleBlock__Address-sc-1avkvav-8 fdWqfc").text();

                                String bedsStr = property.getElementsByClass("TitleBlock__CardInfoItem-sc-1avkvav-9 enhPnH").get(0).text();
                                int beds = Integer.parseInt(bedsStr.substring(0, bedsStr.indexOf(" ")));

                                System.out.println("\n" + link + "\nLatitude: " + lat + "Longtitude: " + lng + "\nPrice: " + price + "\nAddress: " +  address + "\nBeds: " + beds + "\n");

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
            firstThread.start();

            Thread secondThread = new Thread(
                    () -> {


                        for (int i = houses.size() / 2 / 2; i < houses.size() / 2; i++) {

                            // link for each property
                            String link = houses.get(i).select("a").attr("abs:href");

                            Document property = null;
                            try {
                                property = Jsoup.connect(link).get();

                                // handle geolocation
                                String geoLocation = property.getElementsByClass("NewButton__StyledButtonHrefLink-yem86a-3 hzIHZl")
                                        .select("a").attr("abs:href");
                                double lat = Double.parseDouble(geoLocation.substring(geoLocation.lastIndexOf(":") + 1, geoLocation.indexOf("+")));
                                double lng = Double.parseDouble(geoLocation.substring(geoLocation.lastIndexOf("-")));

                                String priceStr = property.getElementsByClass("TitleBlock__StyledSpan-sc-1avkvav-5 gUFuYZ").text().replace(",", "");
                                if (priceStr.isEmpty()) {
                                    continue;
                                }
                                double price = Double.parseDouble(priceStr.substring(priceStr.indexOf("€") + 1));

                                String address = property.getElementsByClass("TitleBlock__Address-sc-1avkvav-8 fdWqfc").text();

                                String bedsStr = property.getElementsByClass("TitleBlock__CardInfoItem-sc-1avkvav-9 enhPnH").get(0).text();
                                int beds = Integer.parseInt(bedsStr.substring(0, bedsStr.indexOf(" ")));

                                System.out.println("\n" + link + "\nLatitude: " + lat + "Longtitude: " + lng + "\nPrice: " + price + "\nAddress: " +  address + "\nBeds: " + beds + "\n");

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });

            secondThread.start();


            Thread thirdThread = new Thread(
                    () -> {

                        for (int i = houses.size() / 2; i < houses.size() / 2 + houses.size() / 2 / 2; i++) {

                            // link for each property
                            String link = houses.get(i).select("a").attr("abs:href");

                            // redirect to property link

                            Document property = null;
                            try {
                                property = Jsoup.connect(link).get();

                                // handle geolocation
                                String geoLocation = property.getElementsByClass("NewButton__StyledButtonHrefLink-yem86a-3 hzIHZl")
                                        .select("a").attr("abs:href");
                                double lat = Double.parseDouble(geoLocation.substring(geoLocation.lastIndexOf(":") + 1, geoLocation.indexOf("+")));
                                double lng = Double.parseDouble(geoLocation.substring(geoLocation.lastIndexOf("-")));

                                String priceStr = property.getElementsByClass("TitleBlock__StyledSpan-sc-1avkvav-5 gUFuYZ").text().replace(",", "");
                                if (priceStr.isEmpty()) {
                                    continue;
                                }
                                double price = Double.parseDouble(priceStr.substring(priceStr.indexOf("€") + 1));

                                String address = property.getElementsByClass("TitleBlock__Address-sc-1avkvav-8 fdWqfc").text();

                                String bedsStr = property.getElementsByClass("TitleBlock__CardInfoItem-sc-1avkvav-9 enhPnH").get(0).text();
                                int beds = Integer.parseInt(bedsStr.substring(0, bedsStr.indexOf(" ")));

                                System.out.println("\n" + link + "\nLatitude: " + lat + "Longtitude: " + lng + "\nPrice: " + price + "\nAddress: " +  address + "\nBeds: " + beds + "\n");

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
            thirdThread.start();

            Thread forthThread = new Thread(
                    () -> {


                        for (int i = houses.size() / 2 + houses.size() / 2 / 2; i < houses.size(); i++) {

                            // link for each property
                            String link = houses.get(i).select("a").attr("abs:href");

                            Document property = null;
                            try {
                                property = Jsoup.connect(link).get();

                                // handle geolocation
                                String geoLocation = property.getElementsByClass("NewButton__StyledButtonHrefLink-yem86a-3 hzIHZl")
                                        .select("a").attr("abs:href");
                                double lat = Double.parseDouble(geoLocation.substring(geoLocation.lastIndexOf(":") + 1, geoLocation.indexOf("+")));
                                double lng = Double.parseDouble(geoLocation.substring(geoLocation.lastIndexOf("-")));

                                String priceStr = property.getElementsByClass("TitleBlock__StyledSpan-sc-1avkvav-5 gUFuYZ").text().replace(",", "");
                                if (priceStr.isEmpty()) {
                                    continue;
                                }
                                double price = Double.parseDouble(priceStr.substring(priceStr.indexOf("€") + 1));

                                String address = property.getElementsByClass("TitleBlock__Address-sc-1avkvav-8 fdWqfc").text();

                                String bedsStr = property.getElementsByClass("TitleBlock__CardInfoItem-sc-1avkvav-9 enhPnH").get(0).text();
                                int beds = Integer.parseInt(bedsStr.substring(0, bedsStr.indexOf(" ")));

                                System.out.println("\n" + link + "\nLatitude: " + lat + "Longtitude: " + lng + "\nPrice: " + price + "\nAddress: " +  address + "\nBeds: " + beds + "\n");

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });

            forthThread.start();

            firstThread.join();
            secondThread.join();
            thirdThread.join();
            forthThread.join();

            from += 20;
        }


        long end = System.currentTimeMillis();
        System.out.println("To process " + total + " houses took: " + timeInfo(end - start));
    }

}



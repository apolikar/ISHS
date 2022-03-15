package irl.lyit.DublinSmartHouseSearch.old;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {

//  public static void main(String[] args) throws IOException {
//    int from = 20;
//    ArrayList<String> hyperLinks = new ArrayList<String>();
//
//    while (from < 1840){
//      Document page = Jsoup.connect("https://www.daft.ie/property-for-sale/ireland/houses?location=dublin&location=dublin-city&from=" + from).get();
//      Elements pageElements = page.select("a[href]");
//
//
////      // iterating and extracting
//      for (Element e:pageElements) {
//        if(e.text().contains("€")){
//          hyperLinks.add("Text: " + e.text());
//          hyperLinks.add("Link: " + "https://www.daft.ie/" + e.attr("href"));
//        }
//      }
//      from += 20;
//
//      for (String s : hyperLinks) {
//        System.out.println(s);
//      }
//    }
//
//  }

   //VERSION 2

//  public static void main(String[] args) throws IOException {
//
//    Document page = Jsoup.connect("https://www.daft.ie/property-for-sale/ireland/houses?location=dublin&location=dublin-city").get();
//
//    // get total number of houses for sale in Dublin
//    String propertiesForSale = page.getElementsByClass("styles__SearchH1-sc-1t5gb6v-3 bekXMP").text();
//    int total = Integer.parseInt(propertiesForSale.substring(0, propertiesForSale.indexOf(" ")).replace(",", ""));
//    int from = 0;
//
//
//    while (from < total){
//
//      page = Jsoup.connect("https://www.daft.ie/property-for-sale/ireland/houses?location=dublin&location=dublin-city&from=" + from).get();
//
//      for (Element card : page.getElementsByClass("SearchPage__SearchResults-gg133s-3 henGdF")){
//        System.out.println(card.getElementsByClass("TitleBlock__StyledSpan-sc-1avkvav-5 gUFuYZ").text());
//        System.out.println(card.getElementsByClass("TitleBlock__Address-sc-1avkvav-8 fdWqdO").text());
//        System.out.println(card.getElementsByClass("TitleBlock__CardInfoItem-sc-1avkvav-9 iKhTdR").text());
//        System.out.println();
//      }
//
//      from += 20;
//    }
//
//  }


  // VERSION 3

  public static void main(String[] args) throws IOException {


    // measure time
    long start = System.currentTimeMillis();

    // home page
    Document page = Jsoup.connect("https://www.daft.ie/property-for-sale/ireland/houses?location=dublin&location=dublin-city").get();

    // get total number of houses for sale in Dublin
    String propertiesForSale = page.getElementsByClass("styles__SearchH1-sc-1t5gb6v-3 bekXMP").text();
    int total = Integer.parseInt(propertiesForSale.substring(0, propertiesForSale.indexOf(" ")).replace(",", ""));
    System.out.println("Total number of houses available for sale in Dublin: " + total);

    // daft.ie has 20 ads per page
    int from = 0;
    int count = 0;

    while (from < total){

      page = Jsoup.connect("https://www.daft.ie/property-for-sale/ireland/houses?location=dublin&location=dublin-city&from=" + from).get();
      Elements pageElements = page.select("a[href]");

//      // iterating and extracting
      for (Element e:pageElements)  {



        if(e.text().contains("€")){

          System.out.println(++count);

          String info =  e.text();
          System.out.println(info);
          String link = "https://www.daft.ie/" + e.attr("href");
          System.out.println(link);


          // get geo-location from embedded Google Maps link to the advertisement
          Document property = Jsoup.connect(link).get();
          String geoLocation = property.select("#__next > main > div.PropertyPage__MainFlexWrapper-sc-14jmnho-0.fdlAwF > "
              + "div.PropertyPage__MainColumn-sc-14jmnho-1.inXeJr > div:nth-child(2) > div.PropertyPage__MapSection-sc-14jmnho-33.jfxvIY > "
              + "div.PropertyPage__MapInfo-sc-14jmnho-34.iJfokh > div > div:nth-child(2) > a").attr("href");


          if(!geoLocation.isEmpty()) {

            // handle geo-location
            String[] locArray =  geoLocation.substring(geoLocation.lastIndexOf("=") + 1).split(",");

            // cast to double
            double lat = Double.parseDouble(locArray[0]);
            double lng = Double.parseDouble(locArray[1]);

            System.out.println("Lat: " + lat + " lng: " + lng);

          }


          System.out.println();
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

    String result = minutes + " minutes " + seconds + " seconds.";
    return result;
  }





}

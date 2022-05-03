package irl.lyit.DublinSmartHouseSearch.service.geoCoordinates;

public class GeoCoordinates {

  private double lat;
  private double lng;

  public GeoCoordinates(double lat, double lng) {
    this.lat = lat;
    this.lng = lng;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLng() {
    return lng;
  }

  public void setLng(double lng) {
    this.lng = lng;
  }

  public GeoCoordinates() {
  }

  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof GeoCoordinates)) {
      return false;
    }
    return lat == ((GeoCoordinates)obj).getLat()
            && lng == ((GeoCoordinates)obj).getLng();
  }

  @Override
  public String toString() {
    return "GeoCoordinates{" +
        "lat=" + lat +
        ", lng=" + lng +
        '}';
  }
}

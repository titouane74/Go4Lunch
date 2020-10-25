package com.fleb.go4lunch.model;


import java.io.Serializable;

/**
 * Created by Florence LE BOURNOT on 16/09/2020
 */
public class Restaurant implements Serializable, Comparable<Restaurant> {

    private String restoPlaceId;
    private String restoName;
    private String restoAddress;
    private String restoPhone;
    private String restoWebSite;
    private String restoDistanceText;
    private int restoNbWorkmates;
    private double restoRating;
    private String restoPhotoUrl;
    private RestaurantPojo.Location restoLocation;
    private RestaurantDetailPojo.OpeningHours restoOpeningHours;
    private int restoDistance;

    public Restaurant () {}

    public Restaurant(String pRestoPlaceId, String pRestoName, String pRestoAddress, String pRestoPhone, String pRestoWebSite,
                      String pRestoDistanceText, int pRestoNbWorkmates, double pRestoRating, String pRestoPhotoUrl,
                      RestaurantPojo.Location pRestoLocation, RestaurantDetailPojo.OpeningHours pRestoOpeningHours,
                      int pRestoDistance) {

        restoPlaceId = pRestoPlaceId;
        restoName = pRestoName;
        restoAddress = pRestoAddress;
        restoPhone = pRestoPhone;
        restoWebSite = pRestoWebSite;
        restoDistanceText = pRestoDistanceText;
        restoNbWorkmates = pRestoNbWorkmates;
        restoRating = pRestoRating;
        restoPhotoUrl = pRestoPhotoUrl;
        restoLocation = pRestoLocation;
        restoOpeningHours = pRestoOpeningHours;
        restoDistance = pRestoDistance;
    }

    public String getRestoPlaceId() { return restoPlaceId; }

    public void setRestoPlaceId(String pRestoPlaceId) { restoPlaceId = pRestoPlaceId; }

    public String getRestoName() { return restoName; }

    public void setRestoName(String pRestoName) { restoName = pRestoName; }

    public String getRestoAddress() { return restoAddress; }

    public void setRestoAddress(String pRestoAddress) { restoAddress = pRestoAddress; }

    public String getRestoPhone() { return restoPhone; }

    public void setRestoPhone(String pRestoPhone) { restoPhone = pRestoPhone; }

    public String getRestoWebSite() { return restoWebSite; }

    public void setRestoWebSite(String pRestoWebSite) { restoWebSite = pRestoWebSite; }

    public String getRestoDistanceText() { return restoDistanceText; }

    public void setRestoDistanceText(String pRestoDistanceText) { restoDistanceText = pRestoDistanceText; }

    public int getRestoNbWorkmates() { return restoNbWorkmates; }

    public void setRestoNbWorkmates(int pRestoNbWorkmates) { restoNbWorkmates = pRestoNbWorkmates; }

    public double getRestoRating() { return restoRating; }

    public void setRestoRating(double pRestoRating) { restoRating = pRestoRating; }

    public String getRestoPhotoUrl() { return restoPhotoUrl; }

    public void setRestoPhotoUrl(String pRestoPhotoUrl) { restoPhotoUrl = pRestoPhotoUrl; }

    public RestaurantPojo.Location getRestoLocation() { return restoLocation; }

    public void setRestoLocation(RestaurantPojo.Location pRestoLocation) { restoLocation = pRestoLocation; }

    public RestaurantDetailPojo.OpeningHours getRestoOpeningHours() { return restoOpeningHours; }

    public void setRestoOpeningHours(RestaurantDetailPojo.OpeningHours pRestoOpeningHours) { restoOpeningHours = pRestoOpeningHours; }

    public int getRestoDistance() { return restoDistance; }

    public void setRestoDistance(int pRestoDistance) { restoDistance = pRestoDistance; }

    @Override
    public int compareTo(Restaurant pRestaurant) {
        return pRestaurant.restoDistance - this.restoDistance;
    }
}

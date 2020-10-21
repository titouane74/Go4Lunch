package com.fleb.go4lunch.model;


import java.io.Serializable;

/**
 * Created by Florence LE BOURNOT on 16/09/2020
 */
public class Restaurant implements Serializable {

    private String restoPlaceId;
    private String restoName;
    private String restoAddress;
    private String restoPhone;
    private String restoWebSite;
    private String restoDistance;
    private int restoNbWorkmates;
    private double restoRating;
    private String restoPhotoUrl;
    private RestaurantPojo.Location restoLocation;
    private RestaurantDetailPojo.OpeningHours restoOpeningHours;

    public Restaurant () {}

    public Restaurant(String pRestoPlaceId, String pRestoName, String pRestoAddress, String pRestoPhone, String pRestoWebSite,
                      String pRestoDistance, int pRestoNbWorkmates, double pRestoRating, String pRestoPhotoUrl,
                      RestaurantPojo.Location pRestoLocation, RestaurantDetailPojo.OpeningHours pRestoOpeningHours) {

        restoPlaceId = pRestoPlaceId;
        restoName = pRestoName;
        restoAddress = pRestoAddress;
        restoPhone = pRestoPhone;
        restoWebSite = pRestoWebSite;
        restoDistance = pRestoDistance;
        restoNbWorkmates = pRestoNbWorkmates;

        restoRating = pRestoRating;
        restoPhotoUrl = pRestoPhotoUrl;
        restoLocation = pRestoLocation;
        restoOpeningHours = pRestoOpeningHours;
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

    public String getRestoDistance() { return restoDistance; }

    public void setRestoDistance(String pRestoDistance) { restoDistance = pRestoDistance; }

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
}

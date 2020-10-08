package com.fleb.go4lunch.model;

import com.google.firebase.firestore.DocumentId;

/**
 * Created by Florence LE BOURNOT on 16/09/2020
 */
public class Restaurant {

    private String restoPlaceId;
    private String restoName;
    private String restoAddress;
    private String restoPhone;
    private String restoWebsite;
    private String restoDistance;
    private int restoNbWorkmates;
    private String restoOpening;
    private double restoRating;
    private String restoPhotoUrl;
    //    private Double restoLat;
//    private Double restoLng;
    private RestaurantPojo.Location restoLocation;


    public Restaurant () {}

/*
    public Restaurant(String pRestoPlaceId, String pRestoName, String pRestoAddress, String pRestoPhone, String pRestoWebsite,
                      String pRestoDistance, int pRestoNbWorkmates, String pRestoOpening, double pRestoRating, String pRestoPhotoUrl,
                      Double pRestoLat, Double pRestoLng) {
*/

    public Restaurant(String pRestoPlaceId, String pRestoName, String pRestoAddress, String pRestoPhone, String pRestoWebsite,
                      String pRestoDistance, int pRestoNbWorkmates, String pRestoOpening, double pRestoRating, String pRestoPhotoUrl,
                      RestaurantPojo.Location pRestoLocation) {
        restoPlaceId = pRestoPlaceId;
        restoName = pRestoName;
        restoAddress = pRestoAddress;
        restoPhone = pRestoPhone;
        restoWebsite = pRestoWebsite;
        restoDistance = pRestoDistance;
        restoNbWorkmates = pRestoNbWorkmates;
        restoOpening = pRestoOpening;
        restoRating = pRestoRating;
        restoPhotoUrl = pRestoPhotoUrl;
/*
        restoLat = pRestoLat;
        restoLng = pRestoLng;
*/
        restoLocation = pRestoLocation;
    }

    public String getRestoPlaceId() { return restoPlaceId; }

    public void setRestoPlaceId(String pRestoPlaceId) { restoPlaceId = pRestoPlaceId; }

    public String getRestoName() { return restoName; }

    public void setRestoName(String pRestoName) { restoName = pRestoName; }

    public String getRestoAddress() { return restoAddress; }

    public void setRestoAddress(String pRestoAddress) { restoAddress = pRestoAddress; }

    public String getRestoPhone() { return restoPhone; }

    public void setRestoPhone(String pRestoPhone) { restoPhone = pRestoPhone; }

    public String getRestoWebsite() { return restoWebsite; }

    public void setRestoWebsite(String pRestoWebsite) { restoWebsite = pRestoWebsite; }

    public String getRestoDistance() { return restoDistance; }

    public void setRestoDistance(String pRestoDistance) { restoDistance = pRestoDistance; }

    public int getRestoNbWorkmates() { return restoNbWorkmates; }

    public void setRestoNbWorkmates(int pRestoNbWorkmates) { restoNbWorkmates = pRestoNbWorkmates; }

    public String getRestoOpening() { return restoOpening; }

    public void setRestoOpening(String pRestoOpening) { restoOpening = pRestoOpening; }

    public double getRestoRating() { return restoRating; }

    public void setRestoRating(double pRestoRating) { restoRating = pRestoRating; }

    public String getRestoPhotoUrl() { return restoPhotoUrl; }

    public void setRestoPhotoUrl(String pRestoPhotoUrl) { restoPhotoUrl = pRestoPhotoUrl; }

/*    public Double getRestoLat() { return restoLat; }

    public void setRestoLat(Double pRestoLat) { restoLat = pRestoLat; }

    public Double getRestoLng() { return restoLng; }

    public void setRestoLng(Double pRestoLng) { restoLng = pRestoLng; }*/

    public RestaurantPojo.Location getRestoLocation() { return restoLocation; }

    public void setRestoLocation(RestaurantPojo.Location pRestoLocation) { restoLocation = pRestoLocation; }
}

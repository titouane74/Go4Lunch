package com.fleb.go4lunch.model;

import java.io.Serializable;
import java.util.List;

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
    private double restoRating;
    private String restoPhotoUrl;
    private RestaurantPojo.Location restoLocation;
    private RestaurantDetailPojo.OpeningHours restoOpeningHours;
    private int restoDistance;
    private List<WorkmatesList> restoWkList;

    public Restaurant () {}

    public Restaurant(String pRestoPlaceId, String pRestoName) {
        restoPlaceId = pRestoPlaceId;
        restoName = pRestoName;
    }

    public Restaurant(String pRestoPlaceId, String pRestoName, String pRestoAddress,
                      String pRestoDistanceText, double pRestoRating, String pRestoPhotoUrl,
                      RestaurantPojo.Location pRestoLocation, RestaurantDetailPojo.OpeningHours pRestoOpeningHours,
                      int pRestoDistance, List<WorkmatesList> pRestoWkList) {
        restoPlaceId = pRestoPlaceId;
        restoName = pRestoName;
        restoAddress = pRestoAddress;
        restoDistanceText = pRestoDistanceText;
        restoRating = pRestoRating;
        restoPhotoUrl = pRestoPhotoUrl;
        restoLocation = pRestoLocation;
        restoOpeningHours = pRestoOpeningHours;
        restoDistance = pRestoDistance;
        restoWkList = pRestoWkList;
    }

    public Restaurant(String pRestoPlaceId, String pRestoName, String pRestoAddress, String pRestoPhone, String pRestoWebSite,
                      String pRestoDistanceText,  double pRestoRating, String pRestoPhotoUrl,
                      RestaurantPojo.Location pRestoLocation, RestaurantDetailPojo.OpeningHours pRestoOpeningHours,
                      int pRestoDistance, List<WorkmatesList> pRestoWkList) {

        restoPlaceId = pRestoPlaceId;
        restoName = pRestoName;
        restoAddress = pRestoAddress;
        restoPhone = pRestoPhone;
        restoWebSite = pRestoWebSite;
        restoDistanceText = pRestoDistanceText;
        restoRating = pRestoRating;
        restoPhotoUrl = pRestoPhotoUrl;
        restoLocation = pRestoLocation;
        restoOpeningHours = pRestoOpeningHours;
        restoDistance = pRestoDistance;
        restoWkList = pRestoWkList;
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

    public List<WorkmatesList> getRestoWkList() {
        return restoWkList;
    }

    public void setRestoWkList(List<WorkmatesList> pRestoWkList) {
        restoWkList = pRestoWkList;
    }

    @Override
    public int compareTo(Restaurant pRestaurant) { return pRestaurant.restoDistance - this.restoDistance; }

    public static class WorkmatesList {
        public String wkId;
        public String wkName;
        public String wkUrl;

        public WorkmatesList() {}

        public WorkmatesList(String pWkId, String pWkName, String pWkUrl) {
            wkId = pWkId;
            wkName = pWkName;
            wkUrl = pWkUrl;
        }

        public String getWkId() {
            return wkId;
        }

        public void setWkId(String pWkId) {
            wkId = pWkId;
        }

        public String getWkName() {
            return wkName;
        }

        public void setWkName(String pWkName) {
            wkName = pWkName;
        }

        public String getWkUrl() {
            return wkUrl;
        }

        public void setWkUrl(String pWkUrl) {
            wkUrl = pWkUrl;
        }
    }

    public enum Fields{
        Restaurant,
        restoPlaceId,
        restoName,
        restoAddress,
        restoPhone,
        restoWebSite,
        restoDistanceText,
        restoRating,
        restoPhotoUrl,
        restoLocation,
        restoOpeningHours,
        restoDistance,
        restoWkList;
    }

}

package com.fleb.go4lunch.utils;

import android.annotation.SuppressLint;
import android.location.Location;
import com.fleb.go4lunch.model.RestaurantDetailPojo;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;

import java.math.RoundingMode;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.fleb.go4lunch.network.JsonRetrofitApi.BASE_URL_GOOGLE;
import static com.fleb.go4lunch.network.JsonRetrofitApi.TXT_KEY_GOOGLE;
import static com.fleb.go4lunch.network.JsonRetrofitApi.TXT_MAX_WIDTH_GOOGLE;
import static com.fleb.go4lunch.network.JsonRetrofitApi.TXT_PHOTO_REF_GOOGLE;

/**
 * Created by Florence LE BOURNOT on 09/10/2020
 */
public class Go4LunchHelper {

    public static final String TXT_PROVIDER = "fusedLocationProvider";
    public static final double MAX_LEVEL_ONE_STAR = 1.67;
    public static final double MAX_LEVEL_TWO_STAR = 3.34;

    /**
     * Indicates the number of star note to display in function of the rating note
     * @param pNote : double : rating note
     * @return int : number of star to display
     */
    public static int ratingNumberOfStarToDisplay(double pNote) {

        int lNbStarToDisplay;

        if (pNote == 0) {
            lNbStarToDisplay = 0;
        } else if (pNote > 0 && pNote <= MAX_LEVEL_ONE_STAR) {
            lNbStarToDisplay = 1;
        } else if (pNote > MAX_LEVEL_ONE_STAR && pNote <= MAX_LEVEL_TWO_STAR) {
            lNbStarToDisplay = 2;
        } else {
            lNbStarToDisplay = 3;
        }
        return lNbStarToDisplay;
    }

    /**
     * Calculate the distance between the current location and the restaurant
     * @param pCurrentLocation : object : current location
     * @param pRestoLocation : object : restaurant location
     * @return int : return the distance
     */
    public static int getRestaurantDistanceToCurrentLocation(Location pCurrentLocation, RestaurantDetailPojo.Location pRestoLocation) {
        Location lRestaurantLocation = new Location(TXT_PROVIDER);
        lRestaurantLocation.setLatitude(pRestoLocation.getLat());
        lRestaurantLocation.setLongitude(pRestoLocation.getLng());
        return (int) pCurrentLocation.distanceTo(lRestaurantLocation);
    }

    /**
     * Convert a latitude and a longitude into a location
     * @param pLat : double : latitude
     * @param pLng: double : longitude
     * @return : object : location
     */
    public static Location setCurrentLocation(Double pLat, Double pLng) {
        Location lFusedLocationProvider = new Location(TXT_PROVIDER);
        lFusedLocationProvider.setLatitude(pLat);
        lFusedLocationProvider.setLongitude(pLng);
        return lFusedLocationProvider;
    }

    /**
     * Convert the distance in a text format for the display
     * @param pDistance : int : distance
     * @return : string : distance with the indicator meters or kilometers
     */
    public static String convertDistance(int pDistance) {
        String lNewDistance = String.valueOf(pDistance);
        double lDistance = pDistance*0.001;

        DecimalFormat lDecimalFormat = new DecimalFormat("##.#");
        lDecimalFormat.setRoundingMode(RoundingMode.UP);

        if(pDistance<1000) {
            lNewDistance = lNewDistance + "m";
        } else {
            lNewDistance = lDecimalFormat.format(lDistance) + "km";
        }

        return lNewDistance;
    }

    /**
     * get the current day of week
     * @return : int : day of week
     */
    public static int getCurrentDayInt() {
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        //TODO only for the change date
        //c.add(Calendar.DATE,1);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Get day in string format
     * @param pDay : int : day
     * @return : string : day
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDayString(int pDay) {
        String[] lShortWeekDay = new DateFormatSymbols().getShortWeekdays();
        String lStringDay = "";

        for(int lIndex = 1; lIndex < lShortWeekDay.length; lIndex++) {
            if (lIndex == pDay+1) {
                lStringDay = lShortWeekDay[lIndex];
            }
        }
        return lStringDay;
    }

    /**
     * Get current time
     * @return : int : time
     */
    public static int getCurrentTime() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        return Integer.parseInt(sdf.format(new Date()));
    }

    /**
     * Get current time with a string format
     * @param pTime : int : time
     * @return : string : time
     */
    public static String getCurrentTimeFormatted(int pTime) {

        @SuppressLint("DefaultLocale")
        String lResult = String.format("%02d:%02d", pTime / 100, pTime % 100);

        return lResult;
    }

    /**
     * Convert time into minutes
     * @param pTime : int : time
     * @return : int : minutes
     */
    public static int convertTimeInMinutes(int pTime) {
        int lHourIntoMin = (pTime/100)*60;
        int lMinutes = pTime % 100;
        return (lHourIntoMin + lMinutes);
    }

    /**
     * Format the address
     * @param pAddress : string : address
     * @return : string : address
     */
    public static String formatAddress(String pAddress) {
        String lAddress = null;
        if(pAddress.indexOf(",")>0) {
            lAddress = pAddress.substring(0, pAddress.indexOf(","));
        }
        return lAddress;
    }

    /**
     * Define a perimeter for the autocomplete prediction request
     * @param center : object : LatLng center of the perimeter
     * @param radiusInMeters : double : radius in meters of the perimeter
     * @return : object : LatLngBounds : return the new position
     */
    public static LatLngBounds toBounds(LatLng center, double radiusInMeters) {
        double distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0);
        LatLng southwestCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0);
        LatLng northeastCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0);
        return new LatLngBounds(southwestCorner, northeastCorner);
    }

    /**
     * Get the photo from Google
     * @param pPhotoReference : string : photo reference of the restaurant
     * @param pMaxWidth : int : max width of the photo
     * @param pKey : string : google key
     * @return : string : the link to the photo
     */
    public static String getPhoto(String pPhotoReference, int pMaxWidth, String pKey) {
        return BASE_URL_GOOGLE + TXT_PHOTO_REF_GOOGLE + pPhotoReference
                + TXT_MAX_WIDTH_GOOGLE + pMaxWidth + TXT_KEY_GOOGLE + pKey;
    }

}

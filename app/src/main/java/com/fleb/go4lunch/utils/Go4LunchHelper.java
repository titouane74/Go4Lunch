package com.fleb.go4lunch.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.RestaurantPojo;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Florence LE BOURNOT on 09/10/2020
 */
public class Go4LunchHelper {

    public static int ratingNumberOfStarToDisplay(Context pContext, double pNote) {

        int lMaxNote = Integer.parseInt(pContext.getResources().getString(R.string.max_level_three_star));
        int lNbNote = Integer.parseInt(pContext.getResources().getString(R.string.nb_star));
        double lMaxLevelOneStar = Double.parseDouble(pContext.getResources().getString(R.string.max_level_one_star));
        double lMaxLevelTwoStar = Double.parseDouble(pContext.getResources().getString(R.string.max_level_two_star));
        double lNote;
        int lNbStarToDisplay;

        lNote = (pNote / lMaxNote) * lNbNote;

        if (lNote == 0) {
            lNbStarToDisplay = 0;
        } else if (lNote > 0 && lNote <= lMaxLevelOneStar) {
            lNbStarToDisplay = 1;
        } else if (lNote > lMaxLevelOneStar && lNote <= lMaxLevelTwoStar) {
            lNbStarToDisplay = 2;
        } else {
            lNbStarToDisplay = 3;
        }
        return lNbStarToDisplay;
    }

    public static int getRestaurantDistanceToCurrentLocation(Location pCurrentLocation, RestaurantPojo.Location pRestoLocation) {
        Location lRestaurantLocation = new Location("fusedLocationProvider");

        lRestaurantLocation.setLatitude(pRestoLocation.getLat());
        lRestaurantLocation.setLongitude(pRestoLocation.getLng());

        return (int) pCurrentLocation.distanceTo(lRestaurantLocation);
    }

    public static Location setCurrentLocation(Double pLat, Double pLng) {
        Location lFusedLocationProvider = new Location("fusedLocationProvider");
        lFusedLocationProvider.setLatitude(pLat);
        lFusedLocationProvider.setLongitude(pLng);
        return lFusedLocationProvider;
    }

    public static String convertDistance(int pDistance) {
        String lNewDistance = String.valueOf(pDistance);
        if(pDistance<1000) {
            lNewDistance = lNewDistance + "m";
        } else {
            lNewDistance = lNewDistance.substring(1) +"." + lNewDistance.substring(2) +"km";
        }
        return lNewDistance;
    }

    public static int getCurrentDayInt() {
        String[] weekdays = new DateFormatSymbols(Locale.FRANCE).getWeekdays();
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        //TODO only for the change date
        //c.add(Calendar.DATE,1);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public static int getCurrentTime() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        return Integer.parseInt(sdf.format(new Date()));
    }

    public static String getCurrentTimeFormatted(int pTime) {

        @SuppressLint("DefaultLocale")
        String lResult = String.format("%02d:%02d", pTime / 100, pTime % 100);

        return lResult;
    }

}

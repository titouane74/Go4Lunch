package com.fleb.go4lunch.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.View;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.RestaurantPojo;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
        String TAG_WEEKDAY = "TAG_WEEKDAY";

        String[] weekdays = new DateFormatSymbols(Locale.FRANCE).getWeekdays();
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        System.out.println(dayOfWeek);
        System.out.println(weekdays[dayOfWeek]);
        Log.d(TAG_WEEKDAY, "getCurrentDay: " + dayOfWeek);

        return dayOfWeek;
    }

    public static String getCurrentDayText() {
        String TAG_WEEKDAY = "TAG_WEEKDAY";

        String[] weekdays = new DateFormatSymbols(Locale.FRANCE).getWeekdays();
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        System.out.println(dayOfWeek);
        System.out.println(weekdays[dayOfWeek]);
        Log.d(TAG_WEEKDAY, "getCurrentDay: " + weekdays[dayOfWeek]);

        return weekdays[dayOfWeek];
    }

    public static int getCurrentTime() {
        String TAG_WEEKDAY = "TAG_WEEKDAY";

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");

        return Integer.parseInt(sdf.format(new Date()));
    }
}

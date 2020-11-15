package com.fleb.go4lunch.utilstest;

import android.content.Context;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.RestaurantDetailPojo;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florence LE BOURNOT on 13/11/2020
 */
public class RestaurantUtils {

    public static Restaurant generateRestaurantName(String pRestoId, String pRestoName) {
        return new Restaurant(pRestoId, pRestoName);
    }

    public static Restaurant generateRestaurantWithoutPhoneAndWebsite(String pRestoId, String pRestoName, int pDistance , double pRating, double pLat, double pLng) {
        RestaurantDetailPojo.Location lLocation = generateLocation(pLat, pLng);
        RestaurantDetailPojo.OpeningHours lOpeningHours = generateOpeningHours();
        List<Restaurant.WorkmatesList> lwkList = generateWorkmatesList();

        return new Restaurant(pRestoId, pRestoName,"resto_address",
                pDistance + "m",pRating,"resto_photo_url", lLocation,
                lOpeningHours,pDistance,lwkList);
    }

    public static Restaurant generateRestaurantAllFields(String pRestoId, String pRestoName, int pDistance , double pRating, double pLat, double pLng) {
        RestaurantDetailPojo.Location lLocation = generateLocation(pLat, pLng);
        RestaurantDetailPojo.OpeningHours lOpeningHours = generateOpeningHours();
        List<Restaurant.WorkmatesList> lwkList = generateWorkmatesList();

        return new Restaurant(pRestoId, pRestoName,"resto_address",
                "01.01.01.01.01", "resto_web_site",pDistance + "m",pRating,
                "resto_photo_url", lLocation,lOpeningHours,pDistance,lwkList);
    }

    public static RestaurantDetailPojo.Location generateLocation(double pLat, double pLng) {
            return new RestaurantDetailPojo.Location(pLat,pLng);
    }

    public static List<Restaurant.WorkmatesList> generateWorkmatesList() {
        List<Restaurant.WorkmatesList> lWkList = new ArrayList<>();
        lWkList.add(new Restaurant.WorkmatesList("workmate1_id", "workmate1_name","workmate1_url"));
        lWkList.add(new Restaurant.WorkmatesList("workmate2_id", "workmate2_name","workmate2_url"));
        lWkList.add(new Restaurant.WorkmatesList("workmate3_id", "workmate3_name","workmate3_url"));

        return lWkList;
    }


    public static RestaurantDetailPojo.OpeningHours generateOpeningHours() {

        RestaurantDetailPojo.OpeningHours lOpeningHours = new RestaurantDetailPojo.OpeningHours();
        lOpeningHours.setOpenNow(true);
        lOpeningHours.setPeriods(generatePeriodsList());
        lOpeningHours.setWeekdayText(generateWeekdayText());

        return lOpeningHours;
    }

    public static List<RestaurantDetailPojo.Period> generatePeriodsList() {

        List<RestaurantDetailPojo.Period> lPeriodList = new ArrayList<>();

        lPeriodList.add(generatePeriod(generateClose(0,"1430"),generateOpen(0,"1200")));
        lPeriodList.add(generatePeriod(generateClose(0,"2230"),generateOpen(0,"1900")));
        lPeriodList.add(generatePeriod(generateClose(1,"2230"),generateOpen(1,"1900")));
        lPeriodList.add(generatePeriod(generateClose(2,"1430"),generateOpen(2,"1200")));
        lPeriodList.add(generatePeriod(generateClose(2,"2230"),generateOpen(2,"1900")));
        lPeriodList.add(generatePeriod(generateClose(3,"1430"),generateOpen(3,"1200")));
        lPeriodList.add(generatePeriod(generateClose(3,"2230"),generateOpen(3,"1900")));
        lPeriodList.add(generatePeriod(generateClose(4,"1430"),generateOpen(4,"1200")));
        lPeriodList.add(generatePeriod(generateClose(4,"2230"),generateOpen(4,"1900")));
        lPeriodList.add(generatePeriod(generateClose(5,"1430"),generateOpen(5,"1200")));
        lPeriodList.add(generatePeriod(generateClose(5,"2230"),generateOpen(5,"1900")));
        lPeriodList.add(generatePeriod(generateClose(6,"1430"),generateOpen(6,"1200")));
        lPeriodList.add(generatePeriod(generateClose(6,"2230"),generateOpen(6,"1900")));

        return lPeriodList;
    }

    public static RestaurantDetailPojo.Period generatePeriod(RestaurantDetailPojo.Close pClose, RestaurantDetailPojo.Open pOpen) {
        RestaurantDetailPojo.Period lPeriod = new RestaurantDetailPojo.Period();
        lPeriod.setClose(pClose);
        lPeriod.setOpen(pOpen);
        return lPeriod;
    }

    public static RestaurantDetailPojo.Close generateClose(int pDay, String pTime) {
        RestaurantDetailPojo.Close lClose = new RestaurantDetailPojo.Close();
        lClose.setDay(pDay);
        lClose.setTime(pTime);
        return lClose;
    }

    public static RestaurantDetailPojo.Open generateOpen(int pDay, String pTime) {
        RestaurantDetailPojo.Open lOpen = new RestaurantDetailPojo.Open();
        lOpen.setDay(pDay);
        lOpen.setTime(pTime);
        return lOpen;
    }

    public static List<String> generateWeekdayText() {
        List<String> lWeekdayText = new ArrayList<>();

        lWeekdayText.add("Monday: 7:00 – 10:30 PM");
        lWeekdayText.add("Tuesday: 12:00 – 2:30 PM, 7:00 – 10:30 PM");
        lWeekdayText.add("Wednesday: 12:00 – 2:30 PM, 7:00 – 10:30 PM");
        lWeekdayText.add("Thursday: 12:00 – 2:30 PM, 7:00 – 10:30 PM");
        lWeekdayText.add("Friday: 12:00 – 2:30 PM, 7:00 – 10:30 PM");
        lWeekdayText.add("Saturday: 12:00 – 2:30 PM, 7:00 – 10:30 PM");
        lWeekdayText.add("Sunday: 12:00 – 2:30 PM, 7:00 – 10:30 PM");

        return lWeekdayText;
    }

}

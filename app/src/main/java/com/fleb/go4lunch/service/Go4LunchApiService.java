package com.fleb.go4lunch.service;

import android.location.Location;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.utils.PreferencesHelper;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Objects;

import static com.fleb.go4lunch.utils.PreferencesHelper.mPreferences;

/**
 * Created by Florence LE BOURNOT on 26/10/2020
 *
 * Go4Lunch API Service
 */
public class Go4LunchApiService implements Go4LunchApi {

    public static final String PREF_KEY_LATITUDE = "PREF_KEY_LATITUDE";
    public static final String PREF_KEY_LONGITUDE = "PREF_KEY_LONGITUDE";

    private Workmate mWorkmate;
    private String mWorkmateId;
    private Restaurant mRestaurant;
    private List<Restaurant> mRestaurantList;
    private Location mLocation;

    @Override
    public void setWorkmateId(String pUserId) {
        mWorkmateId = pUserId;
    }

    @Override
    public String getWorkmateId() {
        return mWorkmateId;
    }

    @Override
    public void setWorkmate(Workmate pWorkmate) {
        mWorkmate = pWorkmate;
    }

    @Override
    public Workmate getWorkmate() {
        return mWorkmate;
    }

    @Override
    public void setRestaurant(Restaurant pRestaurant) {
        mRestaurant = pRestaurant;
    }

    @Override
    public Restaurant getRestaurant() {
        return mRestaurant;
    }

    @Override
    public void setRestaurantList(List<Restaurant> pRestaurantList) { mRestaurantList = pRestaurantList; }

    @Override
    public List<Restaurant> getRestaurantList() { return mRestaurantList; }

    @Override
    public void setLocation(Location pLocation) { mLocation = pLocation; }

    @Override
    public Location getLocation() { return mLocation; }

    public void saveLocationInSharedPreferences(Location pLocation) {
        PreferencesHelper.saveStringPreferences(PREF_KEY_LATITUDE, String.valueOf(pLocation.getLatitude()));
        PreferencesHelper.saveStringPreferences(PREF_KEY_LONGITUDE, String.valueOf(pLocation.getLongitude()));
    }

    @Override
    public double getLocationFromSharedPreferences(String pTypeLocation) {
        return  Double.parseDouble(Objects.requireNonNull(mPreferences.getString(pTypeLocation, "")));
    }
}

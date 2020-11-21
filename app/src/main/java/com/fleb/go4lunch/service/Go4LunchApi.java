package com.fleb.go4lunch.service;

import android.location.Location;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * Created by Florence LE BOURNOT on 26/10/2020
 *
 * Go4Lunch API Service
 */
public interface Go4LunchApi {

    void setWorkmateId(String pUserId) ;

    String getWorkmateId();

    void setWorkmate(Workmate pWorkmate);

    Workmate getWorkmate() ;

    void setRestaurantId(String pRestaurantId);

    String getRestaurantId();

    void setRestaurant(Restaurant pRestaurant);

    Restaurant getRestaurant();

    void setRestaurantList(List<Restaurant> pRestaurantList);

    List<Restaurant> getRestaurantList();

    void setLocation(Location pLocation);

    Location getLocation();

    void saveLocationInSharedPreferences(Location pLocation);

    double getLocationFromSharedPreferences(String pTypeLocation);
}

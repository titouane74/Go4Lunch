package com.fleb.go4lunch.utils;

import com.fleb.go4lunch.model.Restaurant;
import com.google.gson.Gson;


/**
 * Created by Florence LE BOURNOT on 09/10/2020
 */
public class GsonHelper {

    /**
     * Trannsforme un List en String avec la méthode Gson
     * @param pRestaurant : restaurant
     * @return : String : retourne la liste du restaurant au format String
     */
    public static String getGsonString (Restaurant pRestaurant) {
        return new Gson().toJson(pRestaurant);

    }
    /**
     * Transforme un String en object restaurant avec la méthode Gson
     * @param pString : String : châine à transformer en ArrayList
     * @return : object : retourne la chaîne au format ArrayList
     */
    public static Restaurant getGsonRestaurant (String pString) {
        return new Gson().fromJson(pString,Restaurant.class);

    }
}

package com.fleb.go4lunch.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Florence LE BOURNOT on 20/10/2020
 */
public class PreferencesHelper {

    public static SharedPreferences mPreferences;

    /**
     * Upload the SharedPreferences in mPreferences
     */
    public static void  loadPreferences(Context pContext) {
        mPreferences = pContext.getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
    }

    /**
     * Backup a String data type in the SharedPreferences
     * @param key : String : SharedPreferences key
     * @param valueSaved : String : value of the data saved
     */
    public static void saveStringPreferences(String key,String valueSaved) {
        mPreferences.edit().putString(key,valueSaved).apply();
    }

    /**
     * Backup an int data type in the SharedPreferences
     * @param key : String : SharedPreferences key
     * @param valueSaved : int : value of the data saved
     */
    public static void saveIntPreferences(String key, int valueSaved) {
        mPreferences.edit().putInt(key,valueSaved).apply();
    }

}

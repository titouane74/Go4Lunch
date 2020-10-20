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
     * Charge un item de type String depuis SharedPreferences
     * @param key : String : clé de SharedPreferences
     * @return : String : retourne le contenu de la clé
     */
    public static String loadStringItemPreferences (String key) {
        return loadStringItemPreferences(key, null);
    }

    /**
     * Charge un item de type String depuis SharedPreferences
     * @param key : String : clé de SharedPreferences
     * @param defaultValue : String : valeur par défaut retourné si vide dans le fichier
     * @return : String : retourne le contenu de la clé
     */
    public static String loadStringItemPreferences (String key, String defaultValue) {
        return mPreferences.getString(key, defaultValue);
    }

    /**
     * Charge un item de type int depuis SharedPreferences
     * @param key : String : clé de SharedPreferences
     * @param defaultValue : int : valeur par défaut retourné si vide dans le fichier
     * @return : String : retourne le contenu de la clé
     */
    public static int loadIntItemPreferences (String key,int defaultValue) {
        return mPreferences.getInt(key, defaultValue);
    }

    /**
     * Sauvegarde une donnée de type String dans les SharedPreferences
     * @param key : String : clé de SharedPreferences
     * @param valueSaved : String : valeur de la donnée sauvagardée
     */
    public static void saveStringPreferences(String key,String valueSaved) {
        mPreferences.edit().putString(key,valueSaved).apply();
    }

    /**
     * Sauvegarde une donnée de type int dans les SharedPreferences
     * @param key : String : clé de SharedPreferences
     * @param valueSaved : int : valeur de la donnée sauvagardée
     */
    public static void saveIntPreferences(String key, int valueSaved) {
        mPreferences.edit().putInt(key,valueSaved).apply();
    }

}

package com.fleb.go4lunch;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.fleb.go4lunch.di.DI;
import com.fleb.go4lunch.service.Go4LunchApi;
import com.fleb.go4lunch.utils.Go4LunchHelper;
import com.fleb.go4lunch.utils.PreferencesHelper;
import com.fleb.go4lunch.workmanager.WorkerRestaurantChoiceController;

/**
 * Created by Florence LE BOURNOT on 30/10/2020
 */
public class AppGo4Lunch extends MultiDexApplication {
    /**
     * Notifications
     */
    public static final String CHANNEL_1_ID = "channel1";

    /**
     * Shared Preferences
     */
    public static final String PREF_KEY_RADIUS = "PREF_KEY_RADIUS";
    public static final String PREF_KEY_TYPE_GOOGLE_SEARCH = "PREF_KEY_TYPE_GOOGLE_SEARCH";
    public static final String PREF_KEY_PLACE_DETAIL_FIELDS = "PREF_KEY_PLACE_DETAIL_FIELDS";

    public static final String TAG="TAG_NOTIF";

    public static Go4LunchApi sApi = DI.getGo4LunchApiService();
    public static SharedPreferences mPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();

        initializeSharedPreferences();

        removePreviousRestaurantChoice();

    }

    private void removePreviousRestaurantChoice() {
        WorkerRestaurantChoiceController.startWorkerController(this);
    }

    private void initializeSharedPreferences() {

        PreferencesHelper.loadPreferences(this);
        String lValueString;
        int lValueInt;

        if (mPreferences == null) {
            lValueString = getString(R.string.type_search);
            PreferencesHelper.saveStringPreferences(PREF_KEY_TYPE_GOOGLE_SEARCH, lValueString);

            lValueString = getString(R.string.place_detail_fields);
            PreferencesHelper.saveStringPreferences(PREF_KEY_PLACE_DETAIL_FIELDS, lValueString);

            lValueInt = Integer.parseInt(getString(R.string.proximity_radius));
            PreferencesHelper.saveIntPreferences(PREF_KEY_RADIUS, lValueInt);

            double lLat = Double.parseDouble(getString(R.string.default_latitude_position));
            double lLng = Double.parseDouble(getString(R.string.default_longitude_position));
            Location lLocation = Go4LunchHelper.setCurrentLocation(lLat, lLng);
            sApi.setLocation(lLocation);
            sApi.saveLocationInSharedPreferences(lLocation);
        }
    }

    private void createNotificationChannels() {
        Log.d(TAG, "createNotificationChannels: " );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManager lManager = getSystemService(NotificationManager.class);

            NotificationChannel lChannel1 = new NotificationChannel(
                    CHANNEL_1_ID, getString(R.string.notif_hight),
                    NotificationManager.IMPORTANCE_HIGH);
            lChannel1.setDescription(getString(R.string.notif_hight_importance));

            if (lManager != null) {
                lManager.createNotificationChannel(lChannel1);
            }
        }

    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
}


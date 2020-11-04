package com.fleb.go4lunch;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import com.fleb.go4lunch.di.DI;
import com.fleb.go4lunch.notifications.WorkerNotificationController;
import com.fleb.go4lunch.service.Go4LunchApi;
import com.fleb.go4lunch.utils.Go4LunchHelper;
import com.fleb.go4lunch.utils.PreferencesHelper;

/**
 * Created by Florence LE BOURNOT on 30/10/2020
 */
public class AppGo4Lunch extends Application {
    /**
     * Notifications
     */
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    public static final String CHANNEL_3_ID = "channel3";
    public static final String CHANNEL_4_ID = "channel4";

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
        Log.d(TAG, "onCreate: call WorkManagerNotificationController StarRequest" );
        WorkerNotificationController.startWorkRequest(getBaseContext());
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
                    CHANNEL_1_ID, "Pas important",
                    NotificationManager.IMPORTANCE_NONE);
            lChannel1.setDescription("Notification non importante");

            NotificationChannel lChannel2 = new NotificationChannel(
                    CHANNEL_2_ID, "Faible importance",
                    NotificationManager.IMPORTANCE_LOW);
            lChannel2.setDescription("Notification de faible importance");

            NotificationChannel lChannel3 = new NotificationChannel(
                    CHANNEL_3_ID, "Normal",
                    NotificationManager.IMPORTANCE_DEFAULT);
            lChannel3.setDescription("Importance normale");

            NotificationChannel lChannel4 = new NotificationChannel(
                    CHANNEL_4_ID, "Haute importance",
                    NotificationManager.IMPORTANCE_HIGH);
            lChannel4.setDescription("Notification de haute importance");

/*            if (lChannel1.getLightColor()==0) {
                lChannel1.enableLights(true);
            } else {
                lChannel1.enableLights(false);
            }
            if (lChannel2.getLightColor()==0) {
                lChannel2.enableLights(true);
            } else {
                lChannel2.enableLights(false);
            }
            if (lChannel3.getLightColor()==0) {
            lChannel3.enableLights(true);
            } else {
                lChannel3.enableLights(false);
            }
            if (lChannel4.getLightColor()==0) {
            lChannel4.enableLights(true);
            } else {
                lChannel4.enableLights(false);
            }*/

/*            lChannel1.enableVibration(true);
            lChannel2.enableVibration(true);
            lChannel3.enableVibration(true);
            lChannel4.enableVibration(true);*/

            if (lManager != null) {
                lManager.createNotificationChannel(lChannel1);
                lManager.createNotificationChannel(lChannel2);
                lManager.createNotificationChannel(lChannel3);
                lManager.createNotificationChannel(lChannel4);
            }
        }

    }
}


package com.fleb.go4lunch;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/**
 * Created by Florence LE BOURNOT on 30/10/2020
 */
public class AppGo4Lunch extends Application {
    /**
     * Notifications
     */
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";


    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();

    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel lChannel1 = new NotificationChannel(
                    CHANNEL_1_ID, "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH);
            lChannel1.setDescription("This testchannel 1");

            NotificationChannel lChannel2 = new NotificationChannel(
                    CHANNEL_2_ID, "Channel 1",
                    NotificationManager.IMPORTANCE_DEFAULT);
            lChannel2.setDescription("This testchannel 2");

            NotificationManager lManager = getSystemService(NotificationManager.class);
            if (lManager != null) {
                lManager.createNotificationChannel(lChannel1);
                lManager.createNotificationChannel(lChannel2);
            }

        }
    }
}

package com.fleb.go4lunch.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Florence LE BOURNOT on 03/11/2020
 */
public class WorkerRestaurantChoiceController {
    public static final String TAG ="TAG_NOTIF";

    private static final String WORK_REQUEST_NAME = "WORK_REQUEST_NAME_GO4LUNCH_CHOICE";
    private static final String WORK_REQUEST_TAG = "WORK_REQUEST_TAG_GO4LUNCH_CHOICE";
    private static final int NOTIFICATION_HOUR = 14;
    private static final int NOTIFICATION_MINUTE = 00;
    private static final int NOTIFICATION_FREQUENCY_DAY = 1;

    public static void startWorkerController(Context context) {
        Log.d(TAG, "startWorkerController: " );
        PeriodicWorkRequest lWorkRequest = configureRequestPeriod();
        Log.d(TAG, "startWorkerController: enqueue the request");
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(WORK_REQUEST_NAME,
                ExistingPeriodicWorkPolicy.REPLACE, lWorkRequest);
    }

    private static PeriodicWorkRequest configureRequestPeriod() {
        Log.d(TAG, "configureRequestPeriod: " );
        long lSysTime = System.currentTimeMillis();

        Calendar lCalendar = Calendar.getInstance();

        if (lCalendar.get(Calendar.HOUR_OF_DAY) > NOTIFICATION_HOUR
                || (lCalendar.get(Calendar.HOUR_OF_DAY) == NOTIFICATION_HOUR
                && lCalendar.get(Calendar.MINUTE) > NOTIFICATION_MINUTE)){
            lCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        lCalendar.set(Calendar.HOUR_OF_DAY, NOTIFICATION_HOUR);
        lCalendar.set(Calendar.MINUTE, NOTIFICATION_MINUTE);
        lCalendar.set(Calendar.SECOND, 0);
        lCalendar.set(Calendar.MILLISECOND, 0);

        long lStartTime = lCalendar.getTimeInMillis() - lSysTime;

        // Constraints
        final Constraints lConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // PeriodicWorkRequest
        return new PeriodicWorkRequest.Builder(RestaurantChoiceWorker.class,
                NOTIFICATION_FREQUENCY_DAY, TimeUnit.DAYS)
//                .setInitialDelay(lStartTime, TimeUnit.MILLISECONDS)
                .setInitialDelay(20000, TimeUnit.MILLISECONDS)
                .setConstraints(lConstraints)
                .addTag(WORK_REQUEST_TAG)
                .build();
    }
}

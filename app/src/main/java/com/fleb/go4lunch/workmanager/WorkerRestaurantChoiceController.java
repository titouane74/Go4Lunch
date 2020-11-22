package com.fleb.go4lunch.workmanager;

import android.content.Context;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

/**
 * Created by Florence LE BOURNOT on 03/11/2020
 */
public class WorkerRestaurantChoiceController {

    private static final String WORK_REQUEST_NAME = "WORK_REQUEST_NAME_GO4LUNCH_CHOICE";
    private static final String WORK_REQUEST_TAG = "WORK_REQUEST_TAG_GO4LUNCH_CHOICE";
    private static final int NOTIFICATION_FREQUENCY_DAY = 1;

    /**
     * Start the controller for the cleaning restaurant choice
     * @param context : object : context
     */
    public static void startWorkerController(Context context) {

        PeriodicWorkRequest lWorkRequest = configureRequestPeriod();
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(WORK_REQUEST_NAME,
                ExistingPeriodicWorkPolicy.REPLACE, lWorkRequest);
    }

    /**
     * configure the request periodic work
     * @return : object : periodic work request
     */
    private static PeriodicWorkRequest configureRequestPeriod() {

        // Constraints
        final Constraints lConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        long lStartTime = 1000*60;

        // PeriodicWorkRequest
        return new PeriodicWorkRequest.Builder(RestaurantChoiceWorker.class,
                NOTIFICATION_FREQUENCY_DAY, TimeUnit.DAYS)
                .setInitialDelay(lStartTime, TimeUnit.MILLISECONDS)
                .setConstraints(lConstraints)
                .addTag(WORK_REQUEST_TAG)
                .build();
    }
}

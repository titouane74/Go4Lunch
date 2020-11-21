package com.fleb.go4lunch.workmanager;

import android.content.Context;

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
public class WorkerNotificationController {

    private static final String WORK_REQUEST_NAME = "WORK_REQUEST_NAME_GO4LUNCH_NOTIF";
    private static final String WORK_REQUEST_TAG = "WORK_REQUEST_TAG_GO4LUNCH_NOTIF";
    private static final int NOTIFICATION_HOUR = 14;
    private static final int NOTIFICATION_MINUTE = 0;
    private static final int NOTIFICATION_FREQUENCY_DAY = 1;

    /**
     * Start the controller for the generation of notification if they are activated
     * @param context : object : context
     */
    public static void startWorkerController(Context context) {
        PeriodicWorkRequest lWorkRequest = configureRequestPeriod();
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(WORK_REQUEST_NAME,
                ExistingPeriodicWorkPolicy.REPLACE, lWorkRequest);
    }

    /**
     * Stop the controller for the generation of the notification if they are deactivated
     * @param context : object : context
     */
    public static void stopWorkerController(Context context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(WORK_REQUEST_TAG);
    }

    /**
     * configure the request periodic work
     * @return : object : periodic work request
     */
    private static PeriodicWorkRequest configureRequestPeriod() {
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


        // Constraints
        final Constraints lConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        long lStartTime = lCalendar.getTimeInMillis() - lSysTime;
        //TODO to reinitialize
        lStartTime = 20000;

        // PeriodicWorkRequest
        return new PeriodicWorkRequest.Builder(NotifyWorker.class,
                NOTIFICATION_FREQUENCY_DAY, TimeUnit.DAYS)
                .setInitialDelay(lStartTime, TimeUnit.MILLISECONDS)
                .setConstraints(lConstraints)
                .addTag(WORK_REQUEST_TAG)
                .build();
    }
}

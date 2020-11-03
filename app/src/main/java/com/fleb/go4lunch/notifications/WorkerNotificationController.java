package com.fleb.go4lunch.notifications;

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
    private static final String WORK_REQUEST_NAME = "WORK_REQUEST_NAME_GO4LUNCH";
    private static final String WORK_REQUEST_TAG = "WORK_REQUEST_TAG_GO4LUNCH";
    private static final int NOTIFICATION_HOUR = 12;
    private static final int NOTIFICATION_MINUTE = 0;
    private static final int NOTIFICATION_FREQUENCY_DAY = 1;

    public static void startWorkRequest(Context context) {
        PeriodicWorkRequest lWorkRequest = configureWorkRequest();
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(WORK_REQUEST_NAME,
                ExistingPeriodicWorkPolicy.REPLACE, lWorkRequest);
    }

    public static void stopWorkRequest(Context context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(WORK_REQUEST_TAG);
    }

    private static PeriodicWorkRequest configureWorkRequest()
    {
        final Calendar lCalendar = Calendar.getInstance();

        final long lNowTimeInMillis = lCalendar.getTimeInMillis();

        //  NOTIFICATION_HOUR = 12 & NOTIFICATION_MINUTE = 0
        //  if Now >= 12h00 -> + 1 day
        if (lCalendar.get(Calendar.HOUR_OF_DAY) > NOTIFICATION_HOUR
                || (lCalendar.get(Calendar.HOUR_OF_DAY) == NOTIFICATION_HOUR && lCalendar.get(Calendar.MINUTE) > NOTIFICATION_MINUTE))
        {
            lCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        lCalendar.set(Calendar.HOUR_OF_DAY, NOTIFICATION_HOUR);
        lCalendar.set(Calendar.MINUTE, NOTIFICATION_MINUTE);
        lCalendar.set(Calendar.SECOND, 0);
        lCalendar.set(Calendar.MILLISECOND, 0);

        final long lInitialDelay = lCalendar.getTimeInMillis() - lNowTimeInMillis;

        // Constraints
        final Constraints lConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();


        // PeriodicWorkRequest
        return new PeriodicWorkRequest.Builder(NotifyWorker.class,
                NOTIFICATION_FREQUENCY_DAY, TimeUnit.DAYS)
                .setInitialDelay(lInitialDelay, TimeUnit.MILLISECONDS)
                .setConstraints(lConstraints)
                .addTag(WORK_REQUEST_TAG)
                .build();
    }
}

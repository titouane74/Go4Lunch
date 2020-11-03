package com.fleb.go4lunch.notifications;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Workmate;

import java.util.ArrayList;
import java.util.List;

import static com.fleb.go4lunch.AppGo4Lunch.CHANNEL_4_ID;

/**
 * Created by Florence LE BOURNOT on 03/11/2020
 */
public class NotifyWorker extends Worker {

    private Context mContext;
    public NotifyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams)
    {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        createNotification();
        return Result.success();
    }


    private void createNotification() {
        int lNotifPriority=0;

        String lTitle = "Tata eugénie";
        String lMessage = "92bis rue de Charenton";

        List<Workmate> lWorkmateList = new ArrayList<>();
        Workmate lWorkmate1 = new Workmate("id ti put","Roger");
        Workmate lWorkmate2 = new Workmate("id ti put","Albert");
        Workmate lWorkmate3 = new Workmate("id ti put","Titouane");
        Workmate lWorkmate4 = new Workmate("id ti put","Jesse");
        Workmate lWorkmate5 = new Workmate("id ti put","Loki");
        Workmate lWorkmate6 = new Workmate("id ti put","Charlie");
        Workmate lWorkmate7 = new Workmate("id ti put","Budy");
        Workmate lWorkmate8 = new Workmate("id ti put","Madmax");
        lWorkmateList.add(lWorkmate1);
        lWorkmateList.add(lWorkmate2);
        lWorkmateList.add(lWorkmate3);
        lWorkmateList.add(lWorkmate4);
        lWorkmateList.add(lWorkmate5);
        lWorkmateList.add(lWorkmate6);
        lWorkmateList.add(lWorkmate7);
        lWorkmateList.add(lWorkmate8);

        NotificationCompat.Builder lBuilder = new NotificationCompat.Builder(mContext, CHANNEL_4_ID)
                .setSmallIcon(R.drawable.logo_go4lunch_orange)
                .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                .setContentTitle(lTitle)
                .setContentText(lMessage)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);

        NotificationCompat.InboxStyle lInboxStyle = new NotificationCompat.InboxStyle()
                .setBigContentTitle(lTitle)
                .setSummaryText(lTitle);

        // Add each summary line of the new emails, you can add up to 5.
        for (Workmate lWorkmate : lWorkmateList) {
            lInboxStyle.addLine(lWorkmate.getWorkmateName());
        }
        lBuilder.setStyle(lInboxStyle);

        lBuilder.setOnlyAlertOnce(true);

        switch (lNotifPriority) {
            case NotificationCompat.PRIORITY_HIGH:
                lBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
                break;
            case NotificationCompat.PRIORITY_LOW:
                lBuilder.setPriority(NotificationCompat.PRIORITY_LOW);
                break;
            case NotificationCompat.PRIORITY_MAX:
                lBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
                break;
            case NotificationCompat.PRIORITY_MIN:
                lBuilder.setPriority(NotificationCompat.PRIORITY_MIN);
                break;
            default:
                lBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }


        NotificationManagerCompat lNotificationManager = NotificationManagerCompat.from(mContext);
        lNotificationManager.notify(1, lBuilder.build());
    }

}

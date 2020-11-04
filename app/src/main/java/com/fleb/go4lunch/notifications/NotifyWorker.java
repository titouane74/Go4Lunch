package com.fleb.go4lunch.notifications;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;

import static com.fleb.go4lunch.AppGo4Lunch.CHANNEL_4_ID;
import static com.fleb.go4lunch.AppGo4Lunch.sApi;

/**
 * Created by Florence LE BOURNOT on 03/11/2020
 */
public class NotifyWorker extends Worker {

    public static final String TAG = "TAG_NOTIF";
    private Context mContext;
    private RestaurantRepository mRestaurantRepo;
    private Workmate mCurrentWorkmate;

    public NotifyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
        mRestaurantRepo = new RestaurantRepository();
        Log.d(TAG, "NotifyWorker: ");
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: ");

        mCurrentWorkmate = sApi.getWorkmate();

        if ((mCurrentWorkmate != null) && (mCurrentWorkmate.getWorkmateRestoChoosed() != null)) {
            mRestaurantRepo.getRestaurantNotif(mCurrentWorkmate.getWorkmateRestoChoosed().getRestoId());
            prepareNotification();
        }
        return Result.success();
    }

    public void prepareNotification() {
        List<Restaurant> lRestaurantList = sApi.getRestaurantList();
        Log.d(TAG, "prepareNotification: " + lRestaurantList.size());
        for (Restaurant lRestaurant : lRestaurantList) {
            Log.d(TAG, "doWork: " + lRestaurant.getRestoName());
            if ((lRestaurant.getRestoWkList() != null)
                    && (lRestaurant.getRestoPlaceId().equals(mCurrentWorkmate.getWorkmateRestoChoosed().getRestoId()))) {
                Log.d(TAG, "doWork: liste non vide");
                List<Restaurant.WorkmatesList> lWorkmateList = lRestaurant.getRestoWkList();
                List<Workmate> lListWorkmatesComing = new ArrayList<>();
                for (Restaurant.WorkmatesList lWorkmateComing : lWorkmateList) {
                    Log.d(TAG, "doWork: workmate coming " + lWorkmateComing);
                    if (!mCurrentWorkmate.getWorkmateName().equals(lWorkmateComing.getWkName())) {
                        lListWorkmatesComing.add(new Workmate(lWorkmateComing.getWkId(), lWorkmateComing.getWkName()));
                        Log.d(TAG, "doWork: workmate add to list coming : " + lWorkmateComing.getWkName());
                    }
                }
                createNotification(lListWorkmatesComing, lRestaurant);
            }
        }
    }

    private void createNotification(List<Workmate> pWorkmateComing, Restaurant pRestaurant) {
        int lNotifPriority = 0;
        NotificationCompat.Builder lBuilder;

        Log.d(TAG, "createNotification: ");
        String lTitle = pRestaurant.getRestoName();
        String lMessage = pRestaurant.getRestoAddress();
        Log.d(TAG, "createNotification: address" + pRestaurant.getRestoAddress());

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            lBuilder = new NotificationCompat.Builder(mContext, CHANNEL_4_ID)
                    .setSmallIcon(R.drawable.logo_go4lunch_orange)
                    .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                    .setContentTitle(lTitle)
                    .setContentText(lMessage)
                    .setAutoCancel(true)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE);
        } else {
            lBuilder = new NotificationCompat.Builder(mContext, CHANNEL_4_ID)
                    .setSmallIcon(R.drawable.logo_bowl_orange)
                    .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                    .setContentTitle(lTitle)
                    .setContentText(lMessage)
                    .setAutoCancel(true)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE);
        }
        NotificationCompat.InboxStyle lInboxStyle = new NotificationCompat.InboxStyle()
                .setBigContentTitle(lTitle)
                .setSummaryText(lTitle);

        // Add each summary line of the new emails, you can add up to 5.
        for (Workmate lWorkmate : pWorkmateComing) {
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

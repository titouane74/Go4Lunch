package com.fleb.go4lunch.workmanager;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fleb.go4lunch.AppGo4Lunch.CHANNEL_4_ID;
import static com.fleb.go4lunch.AppGo4Lunch.sApi;

/**
 * Created by Florence LE BOURNOT on 03/11/2020
 */
public class NotifyWorker extends Worker {

    /**
     * Firebase declarations
     */
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private final CollectionReference mRestoRef = mDb.collection(String.valueOf(Restaurant.Fields.Restaurant));

    private final Context mContext;
    private Workmate mCurrentWorkmate;

    public NotifyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        mCurrentWorkmate = sApi.getWorkmate();

        if ((mCurrentWorkmate != null) && (mCurrentWorkmate.getWorkmateRestoChosen() != null)) {
            getRestaurantNotif(mCurrentWorkmate.getWorkmateRestoChosen().getRestoId());
        }
        return Result.success();
    }

    /**
     * Get the restaurant for the generation of  the notification
     *
     * @param pRestaurantId : string : restaurant id
     */
    public void getRestaurantNotif(String pRestaurantId) {
        mRestoRef.document(pRestaurantId)
                .get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        Restaurant lRestaurant = (Objects.requireNonNull(pTask.getResult()).toObject(Restaurant.class));
                        if (lRestaurant != null) {
                            prepareNotification(lRestaurant);
                        }
                    }
                });
    }

    /**
     * Prepare the notification for the current user if he choose a restaurant
     */
    public void prepareNotification(Restaurant pRestaurant) {
        List<Restaurant.WorkmatesList> lWorkmateList = pRestaurant.getRestoWkList();
        List<Workmate> lListWorkmatesComing = new ArrayList<>();
        for (Restaurant.WorkmatesList lWorkmateComing : lWorkmateList) {
            if (!mCurrentWorkmate.getWorkmateName().equals(lWorkmateComing.getWkName())) {
                lListWorkmatesComing.add(new Workmate(lWorkmateComing.getWkId(), lWorkmateComing.getWkName()));
            }
        }
        createNotification(lListWorkmatesComing, pRestaurant);
    }

    /**
     * Create the notification
     *
     * @param pWorkmateComing : list object : workmate list coming to the same restaurant
     * @param pRestaurant     : object : restaurant chosen by the workmate
     */
    private void createNotification(List<Workmate> pWorkmateComing, Restaurant pRestaurant) {

        NotificationCompat.Builder lBuilder;

        String lTitle = pRestaurant.getRestoName();
        String lMessage = pRestaurant.getRestoAddress();

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
                    .setSmallIcon(R.drawable.logo_go4lunch_small_png)
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

        NotificationManagerCompat lNotificationManager = NotificationManagerCompat.from(mContext);
        lNotificationManager.notify(1, lBuilder.build());
    }
}
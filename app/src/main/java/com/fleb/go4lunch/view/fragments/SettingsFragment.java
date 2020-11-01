package com.fleb.go4lunch.view.fragments;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.notifications.ReminderBroadcast;
import com.fleb.go4lunch.viewmodel.SettingsViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static com.fleb.go4lunch.AppGo4Lunch.CHANNEL_1_ID;
import static com.fleb.go4lunch.AppGo4Lunch.CHANNEL_2_ID;
import static com.fleb.go4lunch.AppGo4Lunch.CHANNEL_3_ID;
import static com.fleb.go4lunch.AppGo4Lunch.CHANNEL_4_ID;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */
public class SettingsFragment extends BaseFragment {

    public static final String TAG="TAG_NOTIF";

    private static final int NOTIFICATION_HOUR = 11;
    private static final int NOTIFICATION_MINUTE = 38;
    private static final int NOTIFICATION_FREQUENCY_DAY = 1;

    private NotificationManagerCompat mNotificationManager;
    private EditText mEditTextTitle;
    private EditText mEditTextMessage;
    private Context mContext;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void configureFragmentOnCreateView(View pView) {
        mContext = pView.getContext();


        SettingsViewModel lSettingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        final TextView textView = pView.findViewById(R.id.text_settings);
        lSettingsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        mNotificationManager = NotificationManagerCompat.from(mContext);

        boolean lIsNotificationEnable = mNotificationManager.areNotificationsEnabled();

        if (!lIsNotificationEnable) {
            Toast.makeText(mContext, "Vous devez autoriser les notifications pour cette application", Toast.LENGTH_SHORT).show();
            openNotificationSettingsForApp();
        }


        mEditTextTitle = pView.findViewById(R.id.edit_text_title);
        mEditTextMessage = pView.findViewById(R.id.edit_text_message);
        Button lBtnChannel1 = pView.findViewById(R.id.btn_channel1);
        Button lBtnChannel2 = pView.findViewById(R.id.btn_channel2);

        lBtnChannel1.setOnClickListener(v -> sendOnChannel1(pView));
        lBtnChannel2.setOnClickListener(v -> sendOnChannel2(pView));

    }


    public void sendOnChannel1(View pView) {
/*
        String lTitle = mEditTextTitle.getText().toString();
        String lMessage = mEditTextMessage.getText().toString();
*/
/*        String lTitle = "Tata eugénie";
        String lMessage = "92bis rue de Charenton";

        Bitmap lLargeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_go4lunch_orange_png);

        Notification lNotification = new NotificationCompat.Builder(mContext, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.logo_go4lunch_orange)
                .setContentTitle(lTitle)
                .setContentText(lMessage)
                //.setLargeIcon(lLargeIcon)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("Workmate 1")
                        .addLine(("workmate 2")))
                .setContentInfo("Adresse")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.argb(100, 255, 87, 34))
                .build();

        mNotificationManager.notify(1, lNotification);*/

        final Calendar lCalendar = Calendar.getInstance();

        long lSysTime = System.currentTimeMillis();

        Log.d(TAG, "sendOnChannel1: now : " + lSysTime);

        //  NOTIFICATION_HOUR = 12 & NOTIFICATION_MINUTE = 0
        //  if Now >= 12h00 -> + 1 day
        if (lCalendar.get(Calendar.HOUR_OF_DAY) > NOTIFICATION_HOUR
                || (lCalendar.get(Calendar.HOUR_OF_DAY) == NOTIFICATION_HOUR && lCalendar.get(Calendar.MINUTE) > NOTIFICATION_MINUTE))
        {
            Log.d(TAG, "sendOnChannel1: add one day");
            lCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

/*
        lCalendar.set(Calendar.HOUR_OF_DAY, NOTIFICATION_HOUR);
        lCalendar.set(Calendar.MINUTE, NOTIFICATION_MINUTE);
        lCalendar.set(Calendar.SECOND, 0);
        lCalendar.set(Calendar.MILLISECOND, 0);
*/
        lCalendar.set(Calendar.HOUR_OF_DAY, 11);
        lCalendar.set(Calendar.MINUTE, 56);
        lCalendar.set(Calendar.SECOND, 0);
        lCalendar.set(Calendar.MILLISECOND, 0);

        long lTimeSendNotification = lCalendar.getTimeInMillis() - lSysTime;

        Log.d(TAG, "sendOnChannel1: calendartime : " + lCalendar.getTimeInMillis());
        Log.d(TAG, "sendOnChannel1: timetosend (cal-now) : " + lTimeSendNotification);

        Intent lIntent = new Intent(mContext, ReminderBroadcast.class);
        PendingIntent lPendingIntent = PendingIntent.getBroadcast(mContext, 0,lIntent,0);

        AlarmManager lAlarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);

        long lTenSecondsInMillis = 1000 * 120;
        Log.d(TAG, "sendOnChannel1: " + lTenSecondsInMillis);

        if (lAlarmManager != null) {
            lAlarmManager.set(AlarmManager.RTC_WAKEUP,
                    lSysTime + lTenSecondsInMillis,
//                    lTimeSendNotification,
                    lPendingIntent);
        }
    }

    private void scheduleJob(View pView) {

    }

    private void cancelJob(View pView){

    }
    public void sendOnChannel2(View pView) {
/*
        String lTitle = mEditTextTitle.getText().toString();
        String lMessage = mEditTextMessage.getText().toString();
*/

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


        mNotificationManager.notify(1, lBuilder.build());
    }

    private void openNotificationSettingsForApp() {
        // Links to this app's notification settings.
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package", mContext.getPackageName());
        intent.putExtra("app_uid", mContext.getApplicationInfo().uid);

        // for Android 8 and above
        intent.putExtra("android.provider.extra.APP_PACKAGE", mContext.getPackageName());

        startActivity(intent);
    }
}
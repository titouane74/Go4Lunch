package com.fleb.go4lunch.view.fragments;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.view.activities.RestaurantDetailActivity;
import com.fleb.go4lunch.viewmodel.SettingsViewModel;
import com.google.android.gms.maps.model.BitmapDescriptor;

import java.util.ArrayList;
import java.util.List;

import static com.fleb.go4lunch.AppGo4Lunch.CHANNEL_1_ID;
import static com.fleb.go4lunch.AppGo4Lunch.CHANNEL_2_ID;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */
public class SettingsFragment extends BaseFragment {

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
        String lTitle = "Tata eugénie";
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

        mNotificationManager.notify(1, lNotification);
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

        NotificationCompat.Builder lBuilder = new NotificationCompat.Builder(mContext, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.logo_go4lunch_orange)
                .setColor(Color.argb(100, 255, 87, 34))
                .setContentTitle(lTitle)
                .setContentText(lMessage)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);


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

        int lNbWorkmate = 0;
        if (lWorkmateList.size()>0) {
            lNbWorkmate = lWorkmateList.size();
            if (lNbWorkmate>7) {
                lNbWorkmate = 7;
            }
        }
        switch (lNbWorkmate) {
            case 1:
                lBuilder.setStyle(new NotificationCompat.InboxStyle()
                        .addLine(lWorkmateList.get(0).getWorkmateName())
                .setBigContentTitle(lMessage)
                .setSummaryText(lTitle));
                break;
            case 2:
                lBuilder.setStyle(new NotificationCompat.InboxStyle()
                        .addLine(lWorkmateList.get(0).getWorkmateName())
                        .addLine(lWorkmateList.get(1).getWorkmateName())
                        .setBigContentTitle(lMessage)
                        .setSummaryText(lTitle));
                break;
            case 3:
                lBuilder.setStyle(new NotificationCompat.InboxStyle()
                        .addLine(lWorkmateList.get(0).getWorkmateName())
                        .addLine(lWorkmateList.get(1).getWorkmateName())
                        .addLine(lWorkmateList.get(2).getWorkmateName())
                        .setBigContentTitle(lMessage)
                        .setSummaryText(lTitle));
                break;
            case 4:
                lBuilder.setStyle(new NotificationCompat.InboxStyle()
                        .addLine(lWorkmateList.get(0).getWorkmateName())
                        .addLine(lWorkmateList.get(1).getWorkmateName())
                        .addLine(lWorkmateList.get(2).getWorkmateName())
                        .addLine(lWorkmateList.get(3).getWorkmateName())
                        .setBigContentTitle(lMessage)
                        .setSummaryText(lTitle));
                break;
            case 5:
                lBuilder.setStyle(new NotificationCompat.InboxStyle()
                        .addLine(lWorkmateList.get(0).getWorkmateName())
                        .addLine(lWorkmateList.get(1).getWorkmateName())
                        .addLine(lWorkmateList.get(2).getWorkmateName())
                        .addLine(lWorkmateList.get(3).getWorkmateName())
                        .addLine(lWorkmateList.get(4).getWorkmateName())
                        .setBigContentTitle(lMessage)
                        .setSummaryText(lTitle));
                break;
            case 6:
                lBuilder.setStyle(new NotificationCompat.InboxStyle()
                        .addLine(lWorkmateList.get(0).getWorkmateName())
                        .addLine(lWorkmateList.get(1).getWorkmateName())
                        .addLine(lWorkmateList.get(2).getWorkmateName())
                        .addLine(lWorkmateList.get(3).getWorkmateName())
                        .addLine(lWorkmateList.get(4).getWorkmateName())
                        .addLine(lWorkmateList.get(5).getWorkmateName())
                        .setBigContentTitle(lMessage)
                        .setSummaryText(lTitle));
                break;
            case 7:
                lBuilder.setStyle(new NotificationCompat.InboxStyle()
                        .addLine(lWorkmateList.get(0).getWorkmateName())
                        .addLine(lWorkmateList.get(1).getWorkmateName())
                        .addLine(lWorkmateList.get(2).getWorkmateName())
                        .addLine(lWorkmateList.get(3).getWorkmateName())
                        .addLine(lWorkmateList.get(4).getWorkmateName())
                        .addLine(lWorkmateList.get(5).getWorkmateName())
                        .addLine(lWorkmateList.get(6).getWorkmateName())
                        .setBigContentTitle(lMessage)
                        .setSummaryText(lTitle));
                break;
            default:
        }

        mNotificationManager.notify(2, lBuilder.build());
    }
}
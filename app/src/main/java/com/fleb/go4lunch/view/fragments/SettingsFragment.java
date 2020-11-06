package com.fleb.go4lunch.view.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
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
import com.fleb.go4lunch.workmanager.ReminderBroadcast;
import com.fleb.go4lunch.viewmodel.SettingsViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static com.fleb.go4lunch.AppGo4Lunch.CHANNEL_4_ID;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */
public class SettingsFragment extends BaseFragment {

    public static final String TAG="TAG_SETTINGS";


    private Context mContext;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void configureFragmentOnCreateView(View pView) {
        mContext = pView.getContext();

        SettingsViewModel lSettingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        TextView textView = pView.findViewById(R.id.txt_setting_notif_status);
        lSettingsViewModel.getTextNotifStatus(mContext).observe(getViewLifecycleOwner(), textView::setText);

        Button lBtnChangeNotifStatus = pView.findViewById(R.id.btn_change_notif_status);

        lBtnChangeNotifStatus.setOnClickListener(v -> openNotificationSettingsForApp());
    }

    private void openNotificationSettingsForApp() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, mContext.getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", mContext.getPackageName());
            intent.putExtra("app_uid", mContext.getApplicationInfo().uid);
        } else {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        }

        startActivity(intent);
    }
}
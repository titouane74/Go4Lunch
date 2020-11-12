package com.fleb.go4lunch.viewmodel;

import android.content.Context;

import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.R;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */
public class SettingsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SettingsViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    /**
     * Display the right text for the notification activated or not
     * @param pContext : object : context
     * @return : string : text to display
     */
    public LiveData<String> getTextNotifStatus(Context pContext) {
        NotificationManagerCompat lNotificationManager = NotificationManagerCompat.from(pContext);

        boolean lIsNotificationEnable = lNotificationManager.areNotificationsEnabled();

        if (!lIsNotificationEnable) {
            mText.setValue(pContext.getString(R.string.text_setting_notif_deactivated));
        } else {
            mText.setValue(pContext.getString(R.string.text_setting_notif_activated));
        }
    return mText;
    }


}
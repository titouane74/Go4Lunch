package com.fleb.go4lunch.viewmodel;

import android.content.Context;

import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.WorkmateRepository;
import com.fleb.go4lunch.utils.ActionStatus;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */
public class SettingsViewModel extends ViewModel {

    private MutableLiveData<String> mNotificationStatus;

    private WorkmateRepository mWorkmateRepo = new WorkmateRepository();

    public SettingsViewModel() {
        mNotificationStatus = new MutableLiveData<>();
    }

    /**
     * Display the right text for the notification activated or not
     * @param pContext : object : context
     * @return : string : text to display
     */
    public LiveData<String> getNotificationStatus(Context pContext) {
        NotificationManagerCompat lNotificationManager = NotificationManagerCompat.from(pContext);

        boolean lIsNotificationEnable = lNotificationManager.areNotificationsEnabled();

        if (!lIsNotificationEnable) {
            mNotificationStatus.setValue(pContext.getString(R.string.text_setting_notif_deactivated));
        } else {
            mNotificationStatus.setValue(pContext.getString(R.string.text_setting_notif_activated));
        }
    return mNotificationStatus;
    }

    /**
     * Retrieve workmate informations from Firestore
     * @param pWorkmateId : string : workmate id
     * @return : object : workmate
     */
    public LiveData<Workmate> getWorkmateInfos(String pWorkmateId) {
        return mWorkmateRepo.getWorkmateData(pWorkmateId);
    }

    /**
     * Update the workmate user name in Firestore
     * @param pWorkmate : object : workmate
     * @param pNewUserName : string : new user name
     * @return : enum : result of the action
     */
    public LiveData<ActionStatus> updateWorkmateUserName(Workmate pWorkmate, String pNewUserName) {

        return mWorkmateRepo.updateWorkmateUserName(pWorkmate, pNewUserName);
    }

}
package com.fleb.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */
public class LogoutViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LogoutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Déconnexion en cours");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
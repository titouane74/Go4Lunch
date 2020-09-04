package com.fleb.go4lunch.view.start;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StartViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StartViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("HOME SWEET HOME");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

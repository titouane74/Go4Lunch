package com.fleb.go4lunch.view.workmate;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WorkmateListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WorkmateListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is workmate fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

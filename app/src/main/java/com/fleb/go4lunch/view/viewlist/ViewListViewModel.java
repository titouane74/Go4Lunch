package com.fleb.go4lunch.view.viewlist;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ViewListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is view list fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

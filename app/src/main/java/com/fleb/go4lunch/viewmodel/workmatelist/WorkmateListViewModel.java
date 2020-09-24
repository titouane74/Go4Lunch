package com.fleb.go4lunch.viewmodel.workmatelist;


import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.WorkmateRepo;

import java.util.List;


public class WorkmateListViewModel extends ViewModel implements WorkmateRepo.OnFirestoreTaskComplete {

    public static final String TAG_WORKMATE_VM = "TAG_WORKMATE_VM";

    private MutableLiveData<List<Workmate>> mWorkmateList = new MutableLiveData<>();

    public LiveData<List<Workmate>> getWorkmateList() {
        return mWorkmateList;
    }

    //Constructor if fragment is ised for another fragment
    // put mWorkmateRepo.getWorkmateData(); elsewhere
    public WorkmateListViewModel() {
        WorkmateRepo lWorkmateRepo = new WorkmateRepo(this);
        lWorkmateRepo.getWorkmateData();
    }

    @Override
    public void workmateDataLoaded(List<Workmate> pWorkmateList) {
        mWorkmateList.setValue(pWorkmateList);
    }

    @Override
    public void workmateOnError(Exception pE) {

    }
}

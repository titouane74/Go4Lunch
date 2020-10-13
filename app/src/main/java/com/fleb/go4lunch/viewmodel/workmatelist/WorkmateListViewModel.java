package com.fleb.go4lunch.viewmodel.workmatelist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.WorkmateRepository;

import java.util.List;


public class WorkmateListViewModel extends ViewModel {

    private MutableLiveData<List<Workmate>> mLDWorkmateList = new MutableLiveData<>();

    private WorkmateRepository mRepository = new WorkmateRepository();

    public WorkmateListViewModel() {
        mLDWorkmateList = mRepository.getLDWorkmateData();
    }
    public LiveData<List<Workmate>> getWorkmateList() {
        return mLDWorkmateList;
    }

}

package com.fleb.go4lunch.viewmodel.workmatelist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.WorkmateRepository;

import java.util.List;


public class WorkmateListViewModel extends ViewModel {

    private WorkmateRepository mWorkmateRepo = new WorkmateRepository();

    public WorkmateListViewModel() {    }

    public LiveData<List<Workmate>> getWorkmateList() {
        return mWorkmateRepo.getLDWorkmateListData();
    }

}

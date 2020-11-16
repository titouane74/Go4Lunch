package com.fleb.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.WorkmateRepository;

import java.util.List;


public class WorkmateListViewModel extends ViewModel {

    private final WorkmateRepository mWorkmateRepo;

    public WorkmateListViewModel(WorkmateRepository pWorkmateRepository) {
        mWorkmateRepo = pWorkmateRepository;
    }

    /**
     * Get workmate list
     * @return : list object : workmate list
     */
    public LiveData<List<Workmate>> getWorkmateList() {
        return mWorkmateRepo.getLDWorkmateListData();
    }

}

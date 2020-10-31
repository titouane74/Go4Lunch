package com.fleb.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.WorkmateRepository;

/**
 * Created by Florence LE BOURNOT on 26/10/2020
 */
public class MainActivityViewModel extends ViewModel {

    private WorkmateRepository mWorkmateRepo = new WorkmateRepository();

    public LiveData<Workmate> getWorkmateInfos(String pWorkmateId) {
        return mWorkmateRepo.getWorkmateData(pWorkmateId);
    }
}

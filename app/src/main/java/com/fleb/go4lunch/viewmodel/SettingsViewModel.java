package com.fleb.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.repository.WorkmateRepository;
import com.fleb.go4lunch.utils.ActionStatus;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */
public class SettingsViewModel extends ViewModel {

    private final WorkmateRepository mWorkmateRepo ;

    public SettingsViewModel(WorkmateRepository pWorkmateRepository) {
        mWorkmateRepo = pWorkmateRepository;
    }

    /**
     * Retrieve workmate information from Firestore
     * @param pWorkmateId : string : workmate id
     * @return : object : workmate
     */
    public LiveData<Workmate> getWorkmateData(String pWorkmateId) {
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
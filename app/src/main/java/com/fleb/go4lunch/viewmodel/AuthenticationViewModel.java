package com.fleb.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.repository.WorkmateRepository;
import com.fleb.go4lunch.utils.ActionStatus;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Florence LE BOURNOT on 25/10/2020
 */
public class AuthenticationViewModel extends ViewModel {

    private final WorkmateRepository mWorkmateRepo;

    public AuthenticationViewModel(WorkmateRepository pWorkmateRepository) {
        mWorkmateRepo = pWorkmateRepository;
    }

    /**
     * Save the firebase user and workmate in Firestore
     * @param pUser : object : FirebaseUser
     * @return : enum : result of the action
     */
    public LiveData<ActionStatus> saveWorkmateFirebaseProfile(FirebaseUser pUser) {
        return mWorkmateRepo.saveLDWorkmateFirebaseProfile(pUser);
    }

}

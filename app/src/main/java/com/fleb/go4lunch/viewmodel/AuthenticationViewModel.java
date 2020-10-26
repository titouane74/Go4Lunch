package com.fleb.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.fleb.go4lunch.repository.WorkmateRepository;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Florence LE BOURNOT on 25/10/2020
 */
public class AuthenticationViewModel extends ViewModel {

    private WorkmateRepository mWorkmateRepo = new WorkmateRepository();

    public AuthenticationViewModel() { }

    public LiveData<String> saveWorkmateFirebaseProfile(FirebaseUser pUser) {

        return mWorkmateRepo.saveWorkmateFirebaseProfile(pUser);
    }

}

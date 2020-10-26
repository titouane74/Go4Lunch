package com.fleb.go4lunch.service;

import com.fleb.go4lunch.model.Workmate;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Florence LE BOURNOT on 26/10/2020
 */
public interface Go4LunchApi {

    void saveWorkmateId(FirebaseUser pUser) ;

    String getWorkmateId();

    void setWorkmate(Workmate pWorkmate);

    Workmate getWorkmate() ;

}

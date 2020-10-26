package com.fleb.go4lunch.repository;

import androidx.lifecycle.MutableLiveData;

import com.fleb.go4lunch.model.Choice;

import java.util.List;

/**
 * Created by Florence LE BOURNOT on 25/10/2020
 */
public class ChoiceRepository {

    private MutableLiveData<List<Choice>> mLDListWorkmateComing = new MutableLiveData<>();

    public MutableLiveData<List<Choice>> getWorkmateComingInRestaurant (String pRestoId) {
        //TODO get avec whereEqualsTo(restoId=pRestoId)
        return mLDListWorkmateComing;
    }
}

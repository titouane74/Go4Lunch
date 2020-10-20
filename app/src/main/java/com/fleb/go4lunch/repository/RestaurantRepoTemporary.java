package com.fleb.go4lunch.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.fleb.go4lunch.model.Restaurant;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

import java.util.Objects;

/**
 * Created by Florence LE BOURNOT on 18/10/2020
 */
public class RestaurantRepoTemporary {
    /**
     * Firebase declarations
     */
    public static final String RESTO_COLLECTION = "Restaurant";
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference mRestoRef = mDb.collection(RESTO_COLLECTION);

    /**
     * MutableLiveData Declarations
     */
    private MutableLiveData<List<Restaurant>> mLDRestoList = new MutableLiveData<>();

    public MutableLiveData<List<Restaurant>> getLDFirestoreRestaurantList() {
        mRestoRef.get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        mLDRestoList.setValue(Objects.requireNonNull(pTask.getResult()).toObjects(Restaurant.class));
                    } else {
                        Log.d("TAG_REPO_ERROR", "getRestaurantData: " + pTask.getException());
                    }
                });

        return mLDRestoList;
    }
}

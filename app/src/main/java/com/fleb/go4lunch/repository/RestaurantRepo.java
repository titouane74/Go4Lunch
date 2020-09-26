package com.fleb.go4lunch.repository;

import com.fleb.go4lunch.model.Restaurant;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;


/**
 * Created by Florence LE BOURNOT on 26/09/2020
 */
public class RestaurantRepo {

    public interface OnFirestoreTaskComplete {
        void restoDataLoaded(List<Restaurant> pRestiList);
        void restoOnError(Exception pE);
    }

    public static final String RESTO_COLLECTION = "Restaurant";

    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference mRestoRef = mDb.collection(RESTO_COLLECTION);

    private OnFirestoreTaskComplete mOnFirestoreTaskComplete;

    public RestaurantRepo(OnFirestoreTaskComplete pOnFirestoreTaskComplete) {
        this.mOnFirestoreTaskComplete = pOnFirestoreTaskComplete;
    }

    public void getRestoData() {
        mRestoRef.get()
                .addOnCompleteListener(pTask -> {
                   if (pTask.isSuccessful()) {
                        mOnFirestoreTaskComplete.restoDataLoaded(
                                Objects.requireNonNull(pTask.getResult()).toObjects(Restaurant.class)
                        );
                   } else {
                        mOnFirestoreTaskComplete.restoOnError(pTask.getException());
                   }
                });
    }
}

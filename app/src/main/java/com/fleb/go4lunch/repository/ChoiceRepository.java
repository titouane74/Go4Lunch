package com.fleb.go4lunch.repository;

import android.util.Log;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.fleb.go4lunch.AppGo4Lunch.ERROR_ON_FAILURE_LISTENER;
import static com.fleb.go4lunch.AppGo4Lunch.ERROR_UNKNOWN;
import static com.fleb.go4lunch.AppGo4Lunch.sApi;

/**
 * Created by Florence LE BOURNOT on 25/10/2020
 *
 * Repository which manage the cleaning choices
 *
 */

public class ChoiceRepository {

    public static final String TAG = "TAG_CHOICE";

    /**
     * Firebase declarations
     */
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private final CollectionReference mWorkmateRef = mDb.collection(String.valueOf(Workmate.Fields.Workmate));
    private final CollectionReference mRestaurantRef = mDb.collection(String.valueOf(Restaurant.Fields.Restaurant));

    public static final int REFRESH_HOUR = 14;

    private final Date mDate = new Date();

    /**
     * Select all the workmates who have a choice to remove
     */
    public void removePreviousChoice() {
        Calendar lCalNow = Calendar.getInstance();
        lCalNow.setTime(mDate);

        Calendar lCalToday = Calendar.getInstance();;
        lCalToday.setTime(lCalNow.getTime());
        lCalToday.set(Calendar.HOUR_OF_DAY, REFRESH_HOUR);
        lCalToday.set(Calendar.MINUTE, 0);
        lCalToday.set(Calendar.SECOND, 0);

        Calendar lCalYesterday = Calendar.getInstance();
        lCalYesterday.setTime(lCalToday.getTime());
        lCalYesterday.add(Calendar.DAY_OF_MONTH,-1);

        mWorkmateRef.get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        List<Workmate> lWorkmateList =  pTask.getResult().toObjects(Workmate.class);
                        for(Workmate lWorkmate : lWorkmateList) {
                            if(lWorkmate.getWorkmateRestoChosen()!=null) {
                                Date lWorkmateDateChoice = lWorkmate.getWorkmateRestoChosen().getRestoDateChoice().toDate();
                                if (lWorkmateDateChoice.before(lCalYesterday.getTime())) {
                                    removeWorkmateChoice(lWorkmate, lWorkmate.getWorkmateRestoChosen().getRestoId());
                                } else if (lWorkmateDateChoice.before(lCalToday.getTime()) && lCalNow.after(lCalToday)) {
                                    removeWorkmateChoice(lWorkmate, lWorkmate.getWorkmateRestoChosen().getRestoId());
                                }
                            }
                        }
                    }
                });
    }

    /**
     * Remove the previous restaurant choice that the workmate has made
     * @param pWorkmate : object : workmate concerned by the update
     * @param pRestaurantId : string : id of the restaurant chosen by the workmate
     */
    private void removeWorkmateChoice(Workmate pWorkmate, String pRestaurantId) {

        pWorkmate.setWorkmateRestoChosen(null);
        sApi.setWorkmate(pWorkmate);
        mWorkmateRef.document(pWorkmate.getWorkmateId())
                .update(String.valueOf(Workmate.Fields.workmateRestoChosen), FieldValue.delete())
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) removeWorkmateFromRestaurant(pWorkmate, pRestaurantId);
                })
                .addOnFailureListener(pE -> Log.e(TAG, ERROR_ON_FAILURE_LISTENER + pE));
    }

    /**
     * Remove the workmate in the restaurant list of workmate who's coming to eat
     * @param pWorkmate : object : workmate who's gonna be removed
     * @param pRestaurantId : string : id of the restaurant chosen by the workmate
     */
    private void removeWorkmateFromRestaurant(Workmate pWorkmate, String pRestaurantId) {

        Restaurant.WorkmatesList lWorkmatesInRestoList =
                new Restaurant.WorkmatesList(pWorkmate.getWorkmateId(), pWorkmate.getWorkmateName(),
                        pWorkmate.getWorkmatePhotoUrl());

        mRestaurantRef.document(pRestaurantId)
                .update(String.valueOf(Restaurant.Fields.restoWkList), FieldValue.arrayRemove(lWorkmatesInRestoList))
                .addOnCompleteListener(pTask -> {
                    if (!pTask.isSuccessful()) {
                        Log.e(TAG, ERROR_UNKNOWN + " on " + pWorkmate.getWorkmateRestoChosen().getRestoName() );
                    }
                })
                .addOnFailureListener(pE -> Log.e(TAG, ERROR_ON_FAILURE_LISTENER + pE));
    }
}

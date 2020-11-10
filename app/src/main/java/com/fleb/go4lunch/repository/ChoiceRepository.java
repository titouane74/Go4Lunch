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

import static com.fleb.go4lunch.AppGo4Lunch.sApi;

/**
 * Created by Florence LE BOURNOT on 25/10/2020
 */
public class ChoiceRepository {

    public static final String TAG = "TAG_CHOICE";

    /**
     * Firebase declarations
     */
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference mWorkmateRef = mDb.collection(String.valueOf(Workmate.Fields.Workmate));
    private CollectionReference mRestaurantRef = mDb.collection(String.valueOf(Restaurant.Fields.Restaurant));

    public static final int REFRESH_HOUR = 14;

    private Date mDate = new Date();

    public void removePreviousChoice() {
        Calendar lCalendar = Calendar.getInstance();
        lCalendar.setTime(mDate);

        lCalendar.add(Calendar.DAY_OF_MONTH,-1);
        lCalendar.set(Calendar.HOUR_OF_DAY, REFRESH_HOUR);
        lCalendar.set(Calendar.MINUTE, 0);
        lCalendar.set(Calendar.SECOND, 0);

        mWorkmateRef.get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        List<Workmate> lWorkmateList =  pTask.getResult().toObjects(Workmate.class);
                        for(Workmate lWorkmate : lWorkmateList) {
                            if(lWorkmate.getWorkmateRestoChoosed()!=null) {
                                Date lWorkmateDateChoice = lWorkmate.getWorkmateRestoChoosed().getRestoDateChoice().toDate();
                                if (lWorkmateDateChoice.compareTo(lCalendar.getTime())<0) {
                                    removeWorkmateChoice(lWorkmate, lWorkmate.getWorkmateRestoChoosed().getRestoId());
                                }
                            }
                        }
                    }
                });
    }

    private void removeWorkmateChoice(Workmate pWorkmate, String pRestaurantId) {

        pWorkmate.setWorkmateRestoChoosed(null);
        sApi.setWorkmate(pWorkmate);
        mWorkmateRef.document(pWorkmate.getWorkmateId())
                .update(String.valueOf(Workmate.Fields.workmateRestoChoosed), FieldValue.delete())
                .addOnCompleteListener(pTask -> removeWorkmateFromRestaurant(pWorkmate, pRestaurantId))
                .addOnFailureListener(pE -> Log.d(TAG, "removeChoice : restaurant removed Failed"));
    }

    private void removeWorkmateFromRestaurant(Workmate pWorkmate, String pRestaurantId) {

        Restaurant.WorkmatesList lWorkmatesInRestoList =
                new Restaurant.WorkmatesList(pWorkmate.getWorkmateId(), pWorkmate.getWorkmateName(),
                        pWorkmate.getWorkmatePhotoUrl());

        mRestaurantRef.document(pRestaurantId)
                .update(String.valueOf(Restaurant.Fields.restoWkList), FieldValue.arrayRemove(lWorkmatesInRestoList))
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        Log.e(TAG, "removeWorkmateFromRestaurant: remove resto " );
                    } else {
                        Log.e(TAG, "removeWorkmateFromRestaurant: faile on restaurant "
                                + pWorkmate.getWorkmateRestoChoosed().getRestoName() );
                    }
                })
                .addOnFailureListener(pE -> Log.d(TAG, "onFailure: Document not saved", pE));
    }
}

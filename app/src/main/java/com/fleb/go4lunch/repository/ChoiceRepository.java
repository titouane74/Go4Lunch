package com.fleb.go4lunch.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.fleb.go4lunch.model.Choice;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.utils.ActionStatus;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Florence LE BOURNOT on 25/10/2020
 */
public class ChoiceRepository {

    public static final String TAG = "TAG_CHOICE";

    /**
     * Firebase declarations
     */
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference mChoiceRef = mDb.collection(String.valueOf(Choice.Fields.Choice));
    private DocumentReference mChoiceDocRef;

    private MutableLiveData<List<Choice>> mLDListWorkmateComing = new MutableLiveData<>();
    private MutableLiveData<ActionStatus> mLDChoiceStatus = new MutableLiveData<>();

    @SuppressLint("SimpleDateFormat")
    public MutableLiveData<ActionStatus> getOrSaveWorkmateChoiceForRestaurant(Workmate pWorkmate, Restaurant pRestaurant, ActionStatus pActionStatus) {

        Date lDate = new Date();
        SimpleDateFormat lSdf = new SimpleDateFormat("yyyyMMdd");
        String lIdDoc = pWorkmate.getWorkmateId() + pRestaurant.getRestoPlaceId() + lSdf.format(lDate);

        if(pActionStatus.equals(ActionStatus.TO_SEARCH)) {
            getWorkmateChoiceForRestaurant(lIdDoc);
        } else {
            saveChoiceWorkmateRestaurant(lIdDoc, pWorkmate, pRestaurant);
        }

        return mLDChoiceStatus;
    }

    private void getWorkmateChoiceForRestaurant(String pIdDoc) {
        mChoiceRef.document(pIdDoc)
                .get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        if(pTask.getResult().toObject(Choice.class) != null) {
                            mLDChoiceStatus.setValue(ActionStatus.IS_CHOOSED);
                        } else {
                            mLDChoiceStatus.setValue(ActionStatus.NOT_CHOOSED);
                        }
                    } else {
                        Log.d("TAG_REPO_ERROR", "getWorkmateLikeForRestaurant: " + pTask.getException());
                        mLDChoiceStatus.setValue(ActionStatus.ERROR);
                    }
                });

    }

    public MutableLiveData<List<Choice>> getWorkmateComingInRestaurant(String pRestoId) {
        //TODO get avec whereEqualsTo(restoId=pRestoId)
        //call firestore to get the list of the workmate coming in the restaurant
        return mLDListWorkmateComing;
    }

    @SuppressLint("SimpleDateFormat")
    public void saveChoiceWorkmateRestaurant(String pIdDoc,Workmate pWorkmate,
                                                                      Restaurant pRestaurant) {
        Log.d(TAG, "saveChoiceWorkmateRestaurant: " + pRestaurant.getRestoName());
        mChoiceRef.document(pIdDoc)
                .get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        Choice lChoice = pTask.getResult().toObject(Choice.class);
                        if (lChoice != null) {
                            removeRestaurantToChoice(lChoice);
                        } else {
                            addRestaurantToChoice(pIdDoc,pWorkmate, pRestaurant);
                        }
                    } else {
                        Log.d("TAG_REPO_ERROR", "getWorkmateLikeForRestaurant: " + pTask.getException());
                        mLDChoiceStatus.setValue(ActionStatus.ERROR);
                    }
                });
    }

    private void addRestaurantToChoice(String pIdDoc, Workmate pWorkmate, Restaurant pRestaurant) {
        Timestamp lTimestamp = Timestamp.now();
        Log.d(TAG, "addRestaurantToChoice: timestamp : " + lTimestamp);

        Choice lChoice = new Choice(pIdDoc, pWorkmate.getWorkmateId(), pWorkmate.getWorkmateName(),
                pRestaurant.getRestoPlaceId(), pRestaurant.getRestoName(), lTimestamp, lTimestamp);

        Log.d(TAG, "addRestaurantToChoice: lIdDoc " + pIdDoc);
        mChoiceRef.document(pIdDoc)
                .set(lChoice)
                .addOnSuccessListener(pDocumentReference -> {
                    Log.d("TAG_CHOICE", "onSuccess : Document saved ");
                    mLDChoiceStatus.setValue(ActionStatus.ADDED);
                })
                .addOnFailureListener(pE -> {
                    Log.d("TAG_CHOICE", "onFailure: Document not saved", pE);
                    mLDChoiceStatus.setValue(ActionStatus.SAVED_FAILED);
                });
    }

    private void removeRestaurantToChoice(Choice pChoice) {

        String lIdDoc = pChoice.getChChoiceId();
        Log.d(TAG, "removeRestaurantToChoice:  choiceID : " + lIdDoc);

        mChoiceRef.document(lIdDoc)
                .delete()
                .addOnSuccessListener(pDocumentReference -> {
                    Log.d("TAG_CHOICE", "onSuccess : Document saved ");
                    mLDChoiceStatus.setValue(ActionStatus.REMOVED);
                })
                .addOnFailureListener(pE -> {
                    Log.d("TAG_CHOICE", "onFailure: Document not saved", pE);
                    mLDChoiceStatus.setValue(ActionStatus.SAVED_FAILED);
                });
    }

}

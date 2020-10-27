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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


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
    private CollectionReference mWorkmateRef = mDb.collection(String.valueOf(Workmate.Fields.Workmate));

    private MutableLiveData<List<Workmate>> mLDListWorkmateComing = new MutableLiveData<>();
    private MutableLiveData<ActionStatus> mLDChoiceStatus = new MutableLiveData<>();

    private List<Workmate> mWorkmateList = new ArrayList<>();
    private Restaurant mRestaurant;


    @SuppressLint("SimpleDateFormat")
    public MutableLiveData<ActionStatus> getOrSaveWorkmateChoiceForRestaurant(Workmate pWorkmate, Restaurant pRestaurant, ActionStatus pActionStatus) {

        Date lDate = new Date();
        SimpleDateFormat lSdf = new SimpleDateFormat("yyyyMMdd");
        String lDateChoice = lSdf.format(lDate);
        String lIdDoc = pWorkmate.getWorkmateId() + pRestaurant.getRestoPlaceId() + lDateChoice;

        if (pActionStatus.equals(ActionStatus.TO_SEARCH)) {
            getWorkmateChoiceForRestaurant(lIdDoc);
        } else {
            saveChoiceWorkmateRestaurant(lIdDoc, pWorkmate, pRestaurant, lDateChoice);
        }

        return mLDChoiceStatus;
    }

    private void getWorkmateChoiceForRestaurant(String pIdDoc) {
        mChoiceRef.document(pIdDoc)
                .get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        if (pTask.getResult().toObject(Choice.class) != null) {
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

    @SuppressLint("SimpleDateFormat")
    public void saveChoiceWorkmateRestaurant(String pIdDoc, Workmate pWorkmate,
                                             Restaurant pRestaurant, String pDateChoice) {
        Log.d(TAG, "saveChoiceWorkmateRestaurant: " + pRestaurant.getRestoName());
        mChoiceRef.document(pIdDoc)
                .get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        Choice lChoice = pTask.getResult().toObject(Choice.class);
                        if (lChoice != null) {
                            removeRestaurantToChoice(lChoice);
                        } else {
                            addRestaurantToChoice(pIdDoc, pWorkmate, pRestaurant, pDateChoice);
                        }
                    } else {
                        Log.d("TAG_REPO_ERROR", "getWorkmateLikeForRestaurant: " + pTask.getException());
                        mLDChoiceStatus.setValue(ActionStatus.ERROR);
                    }
                });
    }

    private void addRestaurantToChoice(String pIdDoc, Workmate pWorkmate, Restaurant pRestaurant, String pDateChoice) {
        Timestamp lTimestamp = Timestamp.now();
        Log.d(TAG, "addRestaurantToChoice: timestamp : " + lTimestamp);

        Choice lChoice = new Choice(pIdDoc, pWorkmate.getWorkmateId(), pWorkmate.getWorkmateName(),
                pRestaurant.getRestoPlaceId(), pRestaurant.getRestoName(), lTimestamp, pDateChoice);

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

    @SuppressLint("SimpleDateFormat")
    public MutableLiveData<List<Workmate>> getWorkmateComingInRestaurant(Restaurant pRestaurant) {
        //TODO get avec whereEqualsTo(restoId=pRestoId)
        //call firestore to get the list of the workmate coming in the restaurant
        Date lDate = new Date();
        SimpleDateFormat lSdf = new SimpleDateFormat("yyyyMMdd");
        String lDateChoice = lSdf.format(lDate);
        mRestaurant = pRestaurant;

        mChoiceRef.whereEqualTo(String.valueOf(Choice.Fields.chRestoPlaceId), mRestaurant.getRestoPlaceId())
                .whereEqualTo(String.valueOf(Choice.Fields.chChoiceDate), lDateChoice);

        mChoiceRef.get()
                .addOnSuccessListener(pQueryDocumentSnapshots -> {
                    if (!pQueryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> lResult = pQueryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "onSuccess: il y a des données");
                        extractWorkmates(lResult, lDateChoice);
                    } else {
                        Log.d(TAG, "onSuccess: pas de données");
                    }
                });

        return mLDListWorkmateComing;
    }

    private void extractWorkmates(List<DocumentSnapshot> pResult, String pDateChoice) {
        int lWorkmateCount = 0;
        for (DocumentSnapshot lDoc : pResult) {
            lWorkmateCount++;

                if (Objects.requireNonNull(lDoc.get(String.valueOf(Choice.Fields.chRestoName))).equals(mRestaurant.getRestoName())) {
                    Log.d(TAG, "extractWorkmates: bon resto : " + mRestaurant.getRestoName());
                    if (Objects.requireNonNull(lDoc.get(String.valueOf(Choice.Fields.chChoiceDate))).equals(pDateChoice)) {
                        Log.d(TAG, "extractWorkmates: bonne date : " + pDateChoice);
                        String lWorkmateId = String.valueOf(lDoc.get(String.valueOf(Choice.Fields.chWorkmateId)));

                        //MutableLiveData<Workmate> lLDWorkmate =  mWorkmateRepo.getWorkmate(lWorkmateId);
                        addWorkmateToList(lWorkmateId, pResult.size(), lWorkmateCount);

                    } else {
                        Log.d(TAG, "extractWorkmates: pas la bonne date on ne fait rien");
                    }
                } else {
                    Log.d(TAG, "extractWorkmates: pas  le bon resto on ne fait rien");
                }
        }
    }

    public void sendBackToView(int pResultSize,int pWorkmateCount) {
        if (pResultSize == pWorkmateCount) {
            mLDListWorkmateComing.setValue(mWorkmateList);
        }
    }
    public void addWorkmateToList(String pWorkmateId, int pResultSize, int pWorkmateCount) {

        mWorkmateRef.document(pWorkmateId)
                .get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        Workmate lWorkmate = pTask.getResult().toObject(Workmate.class);
                        if (lWorkmate != null) {
                            mWorkmateList.add(new Workmate(pWorkmateId, lWorkmate.getWorkmateName(),
                                    lWorkmate.getWorkmatePhotoUrl(),mRestaurant.getRestoName()));
                            sendBackToView(pResultSize, pWorkmateCount);
                        }
                    } else {
                        Log.d("TAG_REPO_ERROR", "addWorkmateToList: " + pTask.getException());
                    }
                });
    }
}

package com.fleb.go4lunch.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.fleb.go4lunch.model.Choice;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.utils.ActionStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

    private Date mDate = new Date();
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat mSdf = new SimpleDateFormat("yyyyMMdd");
    private String mDateChoice = mSdf.format(mDate);

    private MutableLiveData<Boolean> mLDHasChoosed = new MutableLiveData<>();

    public MutableLiveData<ActionStatus> getOrSaveWorkmateChoiceForRestaurant(Workmate pWorkmate, Restaurant pRestaurant, ActionStatus pActionStatus) {

        String lIdDoc = pWorkmate.getWorkmateId() + pRestaurant.getRestoPlaceId() + mDateChoice;

        if (pActionStatus.equals(ActionStatus.TO_SEARCH)) {
            getWorkmateChoiceForRestaurant(lIdDoc);
        } else {
            saveChoiceWorkmateRestaurant(lIdDoc, pWorkmate, pRestaurant, mDateChoice);
        }
        return mLDChoiceStatus;
    }

    public MutableLiveData<Boolean> hasAlreadyMadeAChoice(Workmate pWorkmate, Restaurant pRestaurant) {
        Log.d(TAG, "workmate : " + pWorkmate.getWorkmateId() + " dateChoice " + mDateChoice);
        mChoiceRef.whereEqualTo(String.valueOf(Choice.Fields.chChoiceDate), mDateChoice)
                .whereEqualTo(String.valueOf(Choice.Fields.chWorkmateId), pWorkmate.getWorkmateId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> pTask) {
                        if (pTask.isSuccessful()) {
                            List<Choice> lChoiceList = pTask.getResult().toObjects(Choice.class);
                            for (Choice lChoice : lChoiceList) {
                                if (!lChoice.getChRestoPlaceId().equals(pRestaurant.getRestoPlaceId())) {
                                    mChoiceRef.document(lChoice.getChChoiceId())
                                            .delete()
                                            .addOnSuccessListener(pDocumentReference -> {
                                                Log.d("TAG_CHOICE", "onSuccess : previous choice removed ");
                                                mLDHasChoosed.setValue(true);
                                            })
                                            .addOnFailureListener(pE -> Log.d("TAG_CHOICE", "onFailure: previous choice error : ", pE));
                                }
                            }
                        } else {
                            mLDHasChoosed.setValue(false);
                        }
                    }
                });
        return mLDHasChoosed;
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

    public void saveChoiceWorkmateRestaurant(String pIdDoc, Workmate pWorkmate,
                                             Restaurant pRestaurant, String pDateChoice) {
        Log.d(TAG, "saveChoiceWorkmateRestaurant: " + pRestaurant.getRestoName());
        mChoiceRef.document(pIdDoc)
                .get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        Choice lChoice = pTask.getResult().toObject(Choice.class);
                        if (lChoice != null) {
                            removeRestaurantFromChoice(lChoice);
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

    private void removeRestaurantFromChoice(Choice pChoice) {

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

    public MutableLiveData<List<Workmate>> getWorkmateComingInRestaurant(Restaurant pRestaurant, String pWorkmateId) {

        mRestaurant = pRestaurant;

/*        Log.d(TAG, " param requête : dateChoice : " + mDateChoice
                + " , restaurant : " + mRestaurant.getRestoName()
                + " / " + mRestaurant.getRestoPlaceId());*/

        mChoiceRef.whereEqualTo(String.valueOf(Choice.Fields.chChoiceDate), mDateChoice)
                .whereEqualTo(String.valueOf(Choice.Fields.chRestoPlaceId), mRestaurant.getRestoPlaceId())
                .get()
                .addOnSuccessListener(pQueryDocumentSnapshots -> {
                    if (!pQueryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> lResult = pQueryDocumentSnapshots.getDocuments();
                        //Log.d(TAG, "onSuccess: il y a des données");
                        extractWorkmates(lResult, mDateChoice, pWorkmateId);
                    } else {
                        Log.d(TAG, "onSuccess: pas de données");
                    }
                });

        return mLDListWorkmateComing;
    }

    private void extractWorkmates(List<DocumentSnapshot> pResult, String pDateChoice, String pWorkmateId) {
        int lWorkmateCount = 0;
        for (DocumentSnapshot lDoc : pResult) {
            lWorkmateCount++;

/*
            Log.d(TAG, "extract workmate : dateChoice : " + mDateChoice
                    + " , restaurant : " + mRestaurant.getRestoName()
                    + " / " + mRestaurant.getRestoPlaceId());
            Log.d(TAG, "result : dateChoice : " +lDoc.get(String.valueOf(Choice.Fields.chChoiceDate))
                    + " , restaurant : " + lDoc.get(String.valueOf(Choice.Fields.chRestoName))
                    + " / " + lDoc.get(String.valueOf(Choice.Fields.chRestoPlaceId)));
*/
            String lWorkmateId = String.valueOf(lDoc.get(String.valueOf(Choice.Fields.chWorkmateId)));
            if (!lWorkmateId.equals(pWorkmateId)) {
                //MutableLiveData<Workmate> lLDWorkmate =  mWorkmateRepo.getWorkmate(lWorkmateId);
                addWorkmateToList(lWorkmateId, pResult.size(), lWorkmateCount);
            }
        }
    }

    public void sendBackToView(int pResultSize, int pWorkmateCount) {
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
                            lWorkmate.setWorkmateRestoChoosed(mRestaurant.getRestoName());
                            mWorkmateList.add(lWorkmate);
                            sendBackToView(pResultSize, pWorkmateCount);
                        }
                    } else {
                        Log.d("TAG_REPO_ERROR", "addWorkmateToList: " + pTask.getException());
                    }
                });
    }
}

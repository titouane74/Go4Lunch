package com.fleb.go4lunch.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.fleb.go4lunch.model.Choice;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.utils.ActionStatus;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Florence LE BOURNOT on 22/09/2020
 */
public class WorkmateRepository {

    private static final String TAG = "TAG_REPO_WORKMTAE";

    /**
     * Firebase declarations
     */
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference mWorkmateRef = mDb.collection(String.valueOf(Workmate.Fields.Workmate));
    private CollectionReference mChoiceRef = mDb.collection(String.valueOf(Choice.Fields.Choice));
    private DocumentReference mWorkmateDocRef;

    private MutableLiveData<List<Workmate>> mLDWorkmateList = new MutableLiveData<>();
    private MutableLiveData<Workmate> mLDWorkmate = new MutableLiveData<>();
    private MutableLiveData<ActionStatus> mLDWorkmateSaved = new MutableLiveData<>();
    private MutableLiveData<ActionStatus> mLDLikeStatus = new MutableLiveData<>();
    private List<Workmate> mWorkmateList = new ArrayList<>();
    private MutableLiveData<String> mLDWorkmateChoice = new MutableLiveData<>();

    public MutableLiveData<List<Workmate>> getLDWorkmateListData() {
        mWorkmateRef
                .get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        mWorkmateList = Objects.requireNonNull(pTask.getResult()).toObjects(Workmate.class);
                        getRestaurantChoice();
                    } else {
                        Log.d("TAG_REPO_ERROR", "getWorkmateData: " + pTask.getException());
                    }
                });
        return mLDWorkmateList;
    }

    private Date mDate = new Date();
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat mSdf = new SimpleDateFormat("yyyyMMdd");
    private String mDateChoice = mSdf.format(mDate);

    private void getRestaurantChoice() {

        for (Workmate lWorkmate : mWorkmateList) {
            mChoiceRef.whereEqualTo(String.valueOf(Choice.Fields.chWorkmateId), lWorkmate.getWorkmateId())
                    .whereEqualTo(String.valueOf(Choice.Fields.chChoiceDate), mDateChoice);
            mChoiceRef.get()
                    .addOnCompleteListener(pTask -> {
                        if (pTask.isSuccessful()) {
                            List<Choice> lChoiceList = pTask.getResult().toObjects(Choice.class);
                            for (Choice lChoice : lChoiceList) {
                                if ((lChoice.getChChoiceDate().equals(mDateChoice))
                                        && (lChoice.getChWorkmateId().equals(lWorkmate.getWorkmateId()))) {
                                    //passer l'objet resto dans table choiceloge
                                    Log.e(TAG, "getRestaurantChoice: " + lChoice.getChRestoName());
                                    lWorkmate.setWorkmateRestoChoosed(lChoice.getChRestoName());
                                }
                            }
                            mLDWorkmateList.postValue(mWorkmateList);
                        }

                    });
        }
            mLDWorkmateList.setValue(mWorkmateList);
    }

    public MutableLiveData<ActionStatus> saveWorkmateFirebaseProfile(FirebaseUser pWorkmate) {
        mWorkmateRef.document(pWorkmate.getUid())
                .get()
                .addOnSuccessListener(pVoid -> {
                    if (!pVoid.exists()) {
                        Map<String, Object> lWorkmate = new HashMap<>();
                        lWorkmate.put(String.valueOf(Workmate.Fields.workmateEmail), pWorkmate.getEmail());
                        lWorkmate.put(String.valueOf(Workmate.Fields.workmateName), pWorkmate.getDisplayName());
                        lWorkmate.put(String.valueOf(Workmate.Fields.workmateId), pWorkmate.getUid());
                        if (pWorkmate.getPhotoUrl() != null) {
                            lWorkmate.put(String.valueOf(Workmate.Fields.workmatePhotoUrl), Objects.requireNonNull(pWorkmate.getPhotoUrl()).toString());
                        }
                        mWorkmateRef.document(pWorkmate.getUid())
                                .set(lWorkmate)
                                .addOnSuccessListener(pDocumentReference -> {
                                    Log.d(TAG, "onSuccess : Document saved ");
                                    mLDWorkmateSaved.setValue(ActionStatus.SAVED);
                                })
                                .addOnFailureListener(pE -> {
                                    Log.d(TAG, "onFailure: Document not saved", pE);
                                    mLDWorkmateSaved.setValue(ActionStatus.SAVED_FAILED);
                                })
                        ;

                    }
                })
                .addOnFailureListener(pE -> Log.d("TAG_AUTH_EXIST", "onFailure Save: Document not saved", pE));
        return mLDWorkmateSaved;
    }

    public MutableLiveData<Workmate> getWorkmate(String pWorkmateId) {
        mWorkmateRef.document(pWorkmateId)
                .get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        Workmate lWorkmate = pTask.getResult().toObject(Workmate.class);
                        mLDWorkmate.setValue(lWorkmate);

                    } else {
                        Log.d("TAG_REPO_ERROR", "getWorkmateData: " + pTask.getException());
                    }
                });
        return mLDWorkmate;
    }

    public MutableLiveData<ActionStatus> getOrSaveWorkmateLikeForRestaurant(Workmate pWorkmate, Restaurant pRestaurant, ActionStatus pActionStatus) {
        if (pActionStatus.equals(ActionStatus.TO_SEARCH)) {
            getWorkmateLikeForRestaurant(pWorkmate, pRestaurant, pActionStatus);
        } else {
            saveLikeRestaurant(pWorkmate, pRestaurant, pActionStatus);
        }
        return mLDLikeStatus;
    }

    public void getWorkmateLikeForRestaurant(Workmate pWorkmate, Restaurant pRestaurant, ActionStatus pActionStatus) {
        mWorkmateRef
                .document(pWorkmate.getWorkmateId())
                .get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        Workmate lWorkmate = pTask.getResult().toObject(Workmate.class);
                        if (lWorkmate != null) {
                            findRestaurantInLikes(lWorkmate, pRestaurant, pActionStatus);
                        } else {
                            Log.d("TAG_REPO_ERROR", "getWorkmateLikeForRestaurant: " + pTask.getException());
                            mLDLikeStatus.setValue(ActionStatus.ERROR);
                        }
                    } else {
                        Log.d("TAG_REPO_ERROR", "getWorkmateLikeForRestaurant: " + pTask.getException());
                        mLDLikeStatus.setValue(ActionStatus.ERROR);
                    }
                });
    }

    public void saveLikeRestaurant(Workmate pWorkmate, Restaurant pRestaurant, ActionStatus pActionStatus) {
        mWorkmateDocRef = mWorkmateRef.document(pWorkmate.getWorkmateId());
        mWorkmateDocRef.get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        Workmate lWorkmate = pTask.getResult().toObject(Workmate.class);
                        if (lWorkmate != null) {
                            findRestaurantInLikes(lWorkmate, pRestaurant, pActionStatus);
                        } else {
                            Log.d("TAG_REPO_ERROR", "getWorkmateData: " + pTask.getException());
                            mLDLikeStatus.setValue(ActionStatus.ERROR);
                        }
                    } else {
                        Log.d("TAG_REPO_ERROR", "getWorkmateData: " + pTask.getException());
                        mLDLikeStatus.setValue(ActionStatus.ERROR);
                    }
                });
    }

    private void findRestaurantInLikes(Workmate pWorkmate, Restaurant pRestaurant, ActionStatus pActionStatus) {
        boolean lIsFound = false;
        Workmate.Likes lRestaurant = new Workmate.Likes(pRestaurant.getRestoPlaceId(), pRestaurant.getRestoName());

        if (pWorkmate.getWorkmateLikes() != null) {
            for (Workmate.Likes lLikes : pWorkmate.getWorkmateLikes()) {
                if (lLikes.restoId.equals(pRestaurant.getRestoPlaceId())) {
                    lIsFound = true;
                    break;
                }
            }
        }

        switch (pActionStatus) {
            case TO_SAVE:
                if (lIsFound) {
                    removeRestaurantFromLikes(lRestaurant);
                } else {
                    addRestaurantToLikes(lRestaurant);
                }
                break;
            case TO_SEARCH:
                if (lIsFound) {
                    mLDLikeStatus.setValue(ActionStatus.IS_CHOOSED);
                } else {
                    mLDLikeStatus.setValue(ActionStatus.NOT_CHOOSED);
                }
                break;
            default:
                mLDLikeStatus.setValue(ActionStatus.ERROR);
        }
    }

    private void addRestaurantToLikes(Workmate.Likes pRestaurant) {

        mWorkmateDocRef.update("workmateLikes", FieldValue.arrayUnion(pRestaurant))
                .addOnSuccessListener(pDocumentReference -> {
                    Log.d(TAG, "onSuccess : Document saved ");
                    mLDLikeStatus.setValue(ActionStatus.ADDED);
                })
                .addOnFailureListener(pE -> {
                    Log.d(TAG, "onFailure: Document not saved", pE);
                    mLDLikeStatus.setValue(ActionStatus.SAVED_FAILED);
                });
    }

    private void removeRestaurantFromLikes(Workmate.Likes pRestaurant) {

        mWorkmateDocRef.update("workmateLikes", FieldValue.arrayRemove(pRestaurant))
                .addOnSuccessListener(pDocumentReference -> {
                    Log.d(TAG, "onSuccess : Document saved ");
                    mLDLikeStatus.setValue(ActionStatus.REMOVED);
                })
                .addOnFailureListener(pE -> {
                    Log.d(TAG, "onFailure: Document not saved", pE);
                    mLDLikeStatus.setValue(ActionStatus.SAVED_FAILED);
                });
    }

    public MutableLiveData<String> getWorkmateRestaurantChoice(Workmate pWorkmate) {

        String pWorkmateId = pWorkmate.getWorkmateId();
/*
        Log.d(TAG, " param requête : dateChoice : " + mDateChoice
                + " , workmate : " + pWorkmate.getWorkmateName()
                + " / " + pWorkmateId);
*/
        mChoiceRef.whereEqualTo(String.valueOf(Choice.Fields.chWorkmateId), pWorkmateId)
                .whereEqualTo(String.valueOf(Choice.Fields.chChoiceDate), mDateChoice)
                .get()
                .addOnSuccessListener(pQueryDocumentSnapshots -> {
                    if (!pQueryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> lResult = pQueryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "getWorkmateRestaurantChoice: " + lResult.size());
                        for (DocumentSnapshot lDoc : lResult) {
/*                            Log.d(TAG, " param requête : dateChoice : " + lDoc.get(String.valueOf(Choice.Fields.chChoiceDate))
                                    + " , workmate : " + lDoc.get(String.valueOf(Choice.Fields.chWorkmateName))
                                    + " / " + lDoc.get(String.valueOf(Choice.Fields.chWorkmateId)));
                                    */
                            String lWorkmateId = String.valueOf(lDoc.get(String.valueOf(Choice.Fields.chWorkmateId)));

                            if (lWorkmateId.equals(pWorkmateId)) {
                                Log.d(TAG, "affected: " + lDoc.getString(String.valueOf(Choice.Fields.chRestoName)));
                                pWorkmate.setWorkmateRestoChoosed(lDoc.getString(String.valueOf(Choice.Fields.chRestoName)));
                                mLDWorkmateChoice.setValue(lDoc.getString(String.valueOf(Choice.Fields.chRestoName)));
                            }
                        }
                    } else {
                        Log.d("TAG_", "getWorkmateRestaurantChoice: pas de données");
                        mLDWorkmateChoice.setValue("");
                    }
                });

        return mLDWorkmateChoice;
    }

}

package com.fleb.go4lunch.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.utils.ActionStatus;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fleb.go4lunch.AppGo4Lunch.ERROR_ON_FAILURE_LISTENER;
import static com.fleb.go4lunch.AppGo4Lunch.sApi;

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
    private CollectionReference mRestaurantRef = mDb.collection(String.valueOf(Restaurant.Fields.Restaurant));
    private DocumentReference mWorkmateDocRef;

    /**
     * MutableLiveData declarations
     */
    private MutableLiveData<List<Workmate>> mLDWorkmateList = new MutableLiveData<>();
    private MutableLiveData<Workmate> mLDWorkmate = new MutableLiveData<>();
    private MutableLiveData<ActionStatus> mLDWorkmateSaved = new MutableLiveData<>();
    private MutableLiveData<ActionStatus> mLDLikeStatus = new MutableLiveData<>();
    private List<Workmate> mWorkmateList = new ArrayList<>();
    private MutableLiveData<ActionStatus> mLDWorkmateChoiceStatus = new MutableLiveData<>();

    /**
     * Get the workmate list
     * @return : mutable live data list object : workmate list
     */
    public MutableLiveData<List<Workmate>> getLDWorkmateListData() {
        mWorkmateRef
                .get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        mWorkmateList = Objects.requireNonNull(pTask.getResult()).toObjects(Workmate.class);
                        mLDWorkmateList.setValue(mWorkmateList);
                    } else {
                        Log.e(TAG, "getWorkmateData: " + pTask.getException());
                    }
                });
        return mLDWorkmateList;
    }

    /**
     * On his first connection, save the user profile in Firestore
     * @param pWorkmate : object: workmate / user
     * @return : mutable live data Enum ActionStatus : information if the backup is done
     * it can be SAVED , EXIST, SAVE_FAILED
     */
    public MutableLiveData<ActionStatus> saveWorkmateFirebaseProfile(FirebaseUser pWorkmate) {

        String lUrl = null;
        if (pWorkmate.getPhotoUrl() != null) {
            lUrl = pWorkmate.getPhotoUrl().toString();
        }

        Workmate lWorkmate = new Workmate(pWorkmate.getUid(), pWorkmate.getDisplayName(),
                pWorkmate.getEmail(), lUrl);

        mWorkmateRef.document(lWorkmate.getWorkmateId())
                .get()
                .addOnSuccessListener(pVoid -> {
                    if (!pVoid.exists()) {
                        mWorkmateRef.document(pWorkmate.getUid())
                                .set(lWorkmate)
                                .addOnSuccessListener(pDocumentReference -> {
                                    sApi.setWorkmate(lWorkmate);
                                    mLDWorkmateSaved.setValue(ActionStatus.SAVED);
                                })
                                .addOnFailureListener(pE -> mLDWorkmateSaved.setValue(ActionStatus.SAVED_FAILED));
                    } else {
                        mLDWorkmateSaved.setValue(ActionStatus.EXIST);
                    }
                })
                .addOnFailureListener(pE -> {
                    Log.e(TAG, ERROR_ON_FAILURE_LISTENER + pE);
                    mLDWorkmateSaved.setValue(ActionStatus.SAVED_FAILED);
                });
        return mLDWorkmateSaved;
    }

    /**
     * Get the workate informations
     * @param pWorkmateId : string : workmate id
     * @return : mutable live data object : workmate
     */
    public MutableLiveData<Workmate> getWorkmateData(String pWorkmateId) {
        mWorkmateRef.document(pWorkmateId)
                .get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        Workmate lWorkmate = pTask.getResult().toObject(Workmate.class);
                        sApi.setWorkmate(lWorkmate);
                        mLDWorkmate.setValue(lWorkmate);
                    } else {
                        Log.e(TAG, "getWorkmateData: " + pTask.getException());
                    }
                });
        return mLDWorkmate;
    }

    /**
     * Manage if the demand is to be get or save
     * @param pWorkmate : object : workmate who likes
     * @param pRestaurant : object : restaurant which is liked
     * @param pActionStatus : enum : status of the action SEARCH or SAVE
     * @return : mutable live data enum actionstatus : result of the action
     */
    public MutableLiveData<ActionStatus> getOrSaveWorkmateLikeForRestaurant(Workmate pWorkmate, Restaurant pRestaurant, ActionStatus pActionStatus) {
        if (pActionStatus.equals(ActionStatus.TO_SEARCH)) {
            getWorkmateLikeForRestaurant(pWorkmate, pRestaurant, pActionStatus);
        } else {
            saveLikeRestaurant(pWorkmate, pRestaurant, pActionStatus);
        }
        return mLDLikeStatus;
    }

    /**
     * For the search case, get workmate likes if the restaurant is in
     * @param pWorkmate : object : workmate who likes
     * @param pRestaurant : object : restaurant which is liked
     * @param pActionStatus : enum : status of the action SEARCH or SAVE
     */
    public void getWorkmateLikeForRestaurant(Workmate pWorkmate, Restaurant pRestaurant, ActionStatus pActionStatus) {
        mWorkmateRef
                .document(pWorkmate.getWorkmateId())
                .get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        Workmate lWorkmate = pTask.getResult().toObject(Workmate.class);
                        if (lWorkmate != null) {
                            sApi.setWorkmate(lWorkmate);
                            findRestaurantInLikes(lWorkmate, pRestaurant, pActionStatus);
                        } else {
                            mLDLikeStatus.setValue(ActionStatus.ERROR);
                        }
                    } else {
                        mLDLikeStatus.setValue(ActionStatus.ERROR);
                    }
                })
                .addOnFailureListener(pE -> mLDLikeStatus.setValue(ActionStatus.ERROR));
    }

    /**
     * Save the workmate like
     * @param pWorkmate : object : workmate who likes
     * @param pRestaurant : object : restaurant which is liked
     * @param pActionStatus : enum : status of the action SEARCH or SAVE
     */
    public void saveLikeRestaurant(Workmate pWorkmate, Restaurant pRestaurant, ActionStatus pActionStatus) {
        mWorkmateDocRef = mWorkmateRef.document(pWorkmate.getWorkmateId());
        mWorkmateDocRef.get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        Workmate lWorkmate = pTask.getResult().toObject(Workmate.class);
                        if (lWorkmate != null) {
                            findRestaurantInLikes(lWorkmate, pRestaurant, pActionStatus);
                        } else {
                            mLDLikeStatus.setValue(ActionStatus.ERROR);
                        }
                    } else {
                        mLDLikeStatus.setValue(ActionStatus.ERROR);
                    }
                })
                .addOnFailureListener(pE -> mLDLikeStatus.setValue(ActionStatus.SAVED_FAILED));
    }

    /**
     * Find in the workmate likes if the restaurant is already in
     * If it's for the search case, mutable live data resturn IS_CHOOSED or NOT_CHOOSED
     * if it's for the save case, the restuarant is added to the list or removed
     * @param pWorkmate : object : workmate who likes
     * @param pRestaurant : object : restaurant which is liked
     * @param pActionStatus : enum : status of the action SEARCH or SAVE
     */
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

    /**
     * Add the restaurant in the like list
     * set the mutable live data to ADDED
     * @param pRestaurant : object : restaurant to add
     */
    private void addRestaurantToLikes(Workmate.Likes pRestaurant) {

        mWorkmateDocRef.update(String.valueOf(Workmate.Fields.workmateLikes), FieldValue.arrayUnion(pRestaurant))
                .addOnSuccessListener(pDocumentReference -> mLDLikeStatus.setValue(ActionStatus.ADDED))
                .addOnFailureListener(pE -> mLDLikeStatus.setValue(ActionStatus.SAVED_FAILED));
    }

    /**
     * Remove the restaurant from the like list
     * set the mutable live data to REMOVED
     * @param pRestaurant : object : Workmate.Likes : restaurant informations
     */
    private void removeRestaurantFromLikes(Workmate.Likes pRestaurant) {

        mWorkmateDocRef.update(String.valueOf(Workmate.Fields.workmateLikes), FieldValue.arrayRemove(pRestaurant))
                .addOnSuccessListener(pDocumentReference -> mLDLikeStatus.setValue(ActionStatus.REMOVED))
                .addOnFailureListener(pE -> mLDLikeStatus.setValue(ActionStatus.SAVED_FAILED));
    }

    /**
     * Manage if the demand is to be get or save
     * @param pRestaurant : object : restaurant which is liked
     * @param pActionStatus : enum : status of the action SEARCH or SAVE
     * @return : mutable live data enum actionstatus : result of the action
     */
    public MutableLiveData<ActionStatus> getOrSaveWorkmateRestaurantChoice(Restaurant pRestaurant, ActionStatus pActionStatus) {

        Workmate lWorkmate = sApi.getWorkmate();
        mWorkmateDocRef = mWorkmateRef.document(lWorkmate.getWorkmateId());

        if (pActionStatus.equals(ActionStatus.TO_SEARCH)) {
            getWorkmateChoice(lWorkmate, pRestaurant);
        } else {
            saveWorkmateChoice(lWorkmate, pRestaurant);
        }
        return mLDWorkmateChoiceStatus;
    }

    /**
     * Save workmate choice
     * @param pWorkmate : object : workmate
     * @param pRestaurant : object : restaurant
     */
    private void saveWorkmateChoice(Workmate pWorkmate, Restaurant pRestaurant) {
        if (pWorkmate.getWorkmateRestoChoosed() == null) {
            addChoice(pWorkmate, pRestaurant);
        } else if (pWorkmate.getWorkmateRestoChoosed().getRestoId().equals(pRestaurant.getRestoPlaceId())){
            removeChoice(pWorkmate, pRestaurant);
        } else {
            removeWorkmateFromPreviousRestaurant(pWorkmate, pRestaurant);
        }
    }

    /**
     * Add workmate choice to his informations
     * @param pWorkmate : object : workmate
     * @param pRestaurant : object : restaurant choosed
     */
    private void addChoice(Workmate pWorkmate, Restaurant pRestaurant) {
        Timestamp lTimestamp = Timestamp.now();

        Workmate.WorkmateRestoChoice lRestaurant = new Workmate.WorkmateRestoChoice(
                pRestaurant.getRestoPlaceId(), pRestaurant.getRestoName(), lTimestamp);

        mWorkmateDocRef.update(String.valueOf(Workmate.Fields.workmateRestoChoosed), lRestaurant)
                .addOnCompleteListener(pTask -> {
                    sApi.setWorkmate(pWorkmate);
                    addWorkmateToRestaurant(pWorkmate, pRestaurant);
                })
                .addOnFailureListener(pE -> mLDWorkmateChoiceStatus.setValue(ActionStatus.SAVED_FAILED));
    }

    /**
     * add the workmate in the restaurant list of workmates who comes to eat
     * @param pWorkmate : object : workmate
     * @param pRestaurant : object : restaurant
     */
    private void addWorkmateToRestaurant(Workmate pWorkmate, Restaurant pRestaurant) {

        Restaurant.WorkmatesList lWorkmatesInRestoList =
                new Restaurant.WorkmatesList(pWorkmate.getWorkmateId(), pWorkmate.getWorkmateName(), pWorkmate.getWorkmatePhotoUrl());

        mRestaurantRef.document(pRestaurant.getRestoPlaceId())
                .update(String.valueOf(Restaurant.Fields.restoWkList), FieldValue.arrayUnion(lWorkmatesInRestoList))
                .addOnSuccessListener(pDocumentReference -> mLDWorkmateChoiceStatus.setValue(ActionStatus.ADDED))
                .addOnFailureListener(pE -> mLDWorkmateChoiceStatus.setValue(ActionStatus.SAVED_FAILED));
    }

    /**
     * Remove the workmate choice
     * @param pWorkmate : object : workate
     * @param pRestaurant : object : restaurant
     */
    private void removeChoice(Workmate pWorkmate, Restaurant pRestaurant) {
        pWorkmate.setWorkmateRestoChoosed(null);
        sApi.setWorkmate(pWorkmate);
        mWorkmateDocRef.update(String.valueOf(Workmate.Fields.workmateRestoChoosed), FieldValue.delete())
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) removeWorkmateFromRestaurant(pWorkmate, pRestaurant);
                })
                .addOnFailureListener(pE -> mLDWorkmateChoiceStatus.setValue(ActionStatus.SAVED_FAILED));
    }

    /**
     * Remove the workmate from the restaurant list of workmates who comes to eat
     * @param pWorkmate : object: workmate
     * @param pRestaurant : object : restaurant
     */
    private void removeWorkmateFromRestaurant(Workmate pWorkmate, Restaurant pRestaurant) {

        Restaurant.WorkmatesList lWorkmatesInRestoList =
                new Restaurant.WorkmatesList(pWorkmate.getWorkmateId(), pWorkmate.getWorkmateName(), pWorkmate.getWorkmatePhotoUrl());

        mRestaurantRef.document(pRestaurant.getRestoPlaceId())
                .update(String.valueOf(Restaurant.Fields.restoWkList), FieldValue.arrayRemove(lWorkmatesInRestoList))
                .addOnSuccessListener(pDocumentReference -> mLDWorkmateChoiceStatus.setValue(ActionStatus.REMOVED))
                .addOnFailureListener(pE -> mLDWorkmateChoiceStatus.setValue(ActionStatus.SAVED_FAILED));
    }

    /**
     * If the workmate has already made a choice which is not this restaurant
     * The workmate is removed from his previous restaurant workmates list
     * @param pWorkmate : object : workmate
     * @param pRestaurant :object : restaurant
     */
    private void removeWorkmateFromPreviousRestaurant(Workmate pWorkmate, Restaurant pRestaurant) {

        Restaurant.WorkmatesList lWorkmatesInRestoList =
                new Restaurant.WorkmatesList(pWorkmate.getWorkmateId(), pWorkmate.getWorkmateName(), pWorkmate.getWorkmatePhotoUrl());

        mRestaurantRef.document(pWorkmate.getWorkmateRestoChoosed().getRestoId())
                .update(String.valueOf(Restaurant.Fields.restoWkList), FieldValue.arrayRemove(lWorkmatesInRestoList))
                .addOnSuccessListener(pDocumentReference -> addChoice(pWorkmate, pRestaurant))
                .addOnFailureListener(pE -> mLDWorkmateChoiceStatus.setValue(ActionStatus.SAVED_FAILED));
    }

    /**
     * Get the workmate choice
     * @param pWorkmate : object : workmate
     * @param pRestaurant : object : restaurant
     */
    private void getWorkmateChoice(Workmate pWorkmate, Restaurant pRestaurant) {
        mWorkmateRef.document(pWorkmate.getWorkmateId())
                .get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        Workmate lWorkmate = pTask.getResult().toObject(Workmate.class);
                        sApi.setWorkmate(lWorkmate);
                        if ((lWorkmate != null) && (lWorkmate.getWorkmateRestoChoosed() != null)
                                && (lWorkmate.getWorkmateRestoChoosed().getRestoId().equals(pRestaurant.getRestoPlaceId()))){
                            mLDWorkmateChoiceStatus.setValue(ActionStatus.IS_CHOOSED);
                        } else{
                            mLDWorkmateChoiceStatus.setValue(ActionStatus.NOT_CHOOSED);
                        }
                    } else {
                        mLDWorkmateChoiceStatus.setValue(ActionStatus.NOT_CHOOSED);
                    }
                })
                .addOnFailureListener(pE -> mLDWorkmateChoiceStatus.setValue(ActionStatus.ERROR));
    }
}

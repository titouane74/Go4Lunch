package com.fleb.go4lunch.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;

import com.fleb.go4lunch.utils.LikeStatus;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Created by Florence LE BOURNOT on 22/09/2020
 */
public class WorkmateRepository {

    public static final String WORKMATE_EMAIL_KEY = "workmateEmail";
    public static final String WORKMATE_NAME_KEY = "workmateName";
    public static final String WORKMATE_PHOTO_URL_KEY = "workmatePhotoUrl";
    public static final String WORKMATE_COLLECTION = "Workmate";
    public static final String WORKMATE_ID_KEY = "workmateId";
    //public static final String WORKMATE_LIKES_KEY = "workmateLikes";

    private static final String TAG_AUTH_SAVE = "TAG_AUTH_SAVE";

    //public static final String TAG = "TAG_WORKMATE_REPO";
    /**
     * Firebase declarations
     */
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference mWorkmateRef = mDb.collection(WORKMATE_COLLECTION);
    private DocumentReference mWorkmateDocRef;

    private MutableLiveData<List<Workmate>> mLDWorkmateList = new MutableLiveData<>();
    private MutableLiveData<Workmate> mLDWorkmate = new MutableLiveData<>();
    private MutableLiveData<String> mLDWorkmateSaved = new MutableLiveData<>();
    private MutableLiveData<LikeStatus> mLDLikeSaved = new MutableLiveData<>();

    public MutableLiveData<List<Workmate>> getLDWorkmateListData() {
        mWorkmateRef
                .get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        mLDWorkmateList.setValue(Objects.requireNonNull(pTask.getResult()).toObjects(Workmate.class));
                    } else {
                        Log.d("TAG_REPO_ERROR", "getWorkmateData: " + pTask.getException());
                    }
                });
        return mLDWorkmateList;
    }

    //TODO add the add user to Firestore called by the auth
    public MutableLiveData<String> saveWorkmateFirebaseProfile(FirebaseUser pWorkmate) {
        mWorkmateRef.document(pWorkmate.getUid())
                .get()
                .addOnSuccessListener(pVoid -> {
                    if (!pVoid.exists()) {
                        Map<String, Object> lWorkmate = new HashMap<>();
                        lWorkmate.put(WORKMATE_EMAIL_KEY, pWorkmate.getEmail());
                        lWorkmate.put(WORKMATE_NAME_KEY, pWorkmate.getDisplayName());
                        lWorkmate.put(WORKMATE_ID_KEY, pWorkmate.getUid());
                        if (pWorkmate.getPhotoUrl() != null) {
                            lWorkmate.put(WORKMATE_PHOTO_URL_KEY, Objects.requireNonNull(pWorkmate.getPhotoUrl()).toString());
                        }
                        mWorkmateRef.document(pWorkmate.getUid())
                                .set(lWorkmate)
                                .addOnSuccessListener(pDocumentReference -> {
                                    Log.d(TAG_AUTH_SAVE, "onSuccess : Document saved ");
                                    mLDWorkmateSaved.setValue("SAVED");
                                })
                                .addOnFailureListener(pE -> {
                                    Log.d(TAG_AUTH_SAVE, "onFailure: Document not saved", pE);
                                    mLDWorkmateSaved.setValue(String.valueOf(pE));
                                })
                        ;

                    }
                })
                .addOnFailureListener(pE -> Log.d("TAG_AUTH_EXIST", "onFailure Save: Document not saved", pE));
        return mLDWorkmateSaved;
    }

    public MutableLiveData<Workmate> getWorkmate(String pWorkmateId) {
        mWorkmateRef
                .document(pWorkmateId)
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

    public MutableLiveData<LikeStatus> saveLikeRestaurant(Workmate pWorkmate,
                                                          Restaurant pRestaurant) {

        mWorkmateDocRef = mWorkmateRef.document(pWorkmate.getWorkmateId());

        mWorkmateDocRef.get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        Workmate lWorkmate = pTask.getResult().toObject(Workmate.class);
                        if (lWorkmate != null) {
                            findRestaurantInLikes(lWorkmate, pRestaurant);
                        }
                    } else {
                        Log.d("TAG_REPO_ERROR", "getWorkmateData: " + pTask.getException());
                    }
                });
        return mLDLikeSaved;
    }

    private void findRestaurantInLikes(Workmate pWorkmate, Restaurant pRestaurant) {
        boolean lIsFound = false;
        Workmate.Likes lRestaurant = new Workmate.Likes(pRestaurant.getRestoPlaceId(), pRestaurant.getRestoName());

        for (Workmate.Likes lLikes : pWorkmate.getWorkmateLikes()) {
            if (lLikes.restoId.equals(pRestaurant.getRestoPlaceId())) {
                lIsFound = true;
                break;
            }
        }

        if (lIsFound) {
            removeRestaurantFromLikes(lRestaurant);
        } else {
            addRestaurantToLikes(lRestaurant);
        }

    }

    private void addRestaurantToLikes(Workmate.Likes pRestaurant) {

        mWorkmateDocRef.update("workmateLikes", FieldValue.arrayUnion(pRestaurant))
                .addOnSuccessListener(pDocumentReference -> {
                    Log.d(TAG_AUTH_SAVE, "onSuccess : Document saved ");
                    mLDLikeSaved.setValue(LikeStatus.ADDED);
                })
                .addOnFailureListener(pE -> {
                    Log.d(TAG_AUTH_SAVE, "onFailure: Document not saved", pE);
                    mLDLikeSaved.setValue(LikeStatus.SAVED_FAILED);
                });
    }

    private void removeRestaurantFromLikes(Workmate.Likes pRestaurant) {

        mWorkmateDocRef.update("workmateLikes", FieldValue.arrayRemove(pRestaurant))
                .addOnSuccessListener(pDocumentReference -> {
                    Log.d(TAG_AUTH_SAVE, "onSuccess : Document saved ");
                    mLDLikeSaved.setValue(LikeStatus.REMOVED);
                })
                .addOnFailureListener(pE -> {
                    Log.d(TAG_AUTH_SAVE, "onFailure: Document not saved", pE);
                    mLDLikeSaved.setValue(LikeStatus.SAVED_FAILED);
                });
    }
}

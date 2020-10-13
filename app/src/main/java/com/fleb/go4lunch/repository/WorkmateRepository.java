package com.fleb.go4lunch.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.fleb.go4lunch.model.Workmate;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

import static com.fleb.go4lunch.view.activities.MainActivity.WORKMATE_COLLECTION;

/**
 * Created by Florence LE BOURNOT on 22/09/2020
 */
public class WorkmateRepository {

    /**
     * Firebase declarations
     */
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference mWorkmateRef = mDb.collection(WORKMATE_COLLECTION);

    private MutableLiveData<List<Workmate>> mLDWorkmateList = new MutableLiveData<>();


    public MutableLiveData<List<Workmate>> getLDWorkmateData() {
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

}

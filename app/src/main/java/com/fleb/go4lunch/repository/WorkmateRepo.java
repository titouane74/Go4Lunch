package com.fleb.go4lunch.repository;

import com.fleb.go4lunch.model.Workmate;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

import static com.fleb.go4lunch.view.activities.MainActivity.WORKMATE_COLLECTION;

/**
 * Created by Florence LE BOURNOT on 22/09/2020
 */
public class WorkmateRepo {

    public interface OnFirestoreTaskComplete {
        void workmateDataLoaded(List<Workmate> pWorkmateList);
        void workmateOnError(Exception pE);
    }

    private OnFirestoreTaskComplete mOnFirestoreTaskComplete;

    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference mWorkmateRef = mDb.collection(WORKMATE_COLLECTION);

    public WorkmateRepo(OnFirestoreTaskComplete pOnFirestoreTaskComplete) {
        this.mOnFirestoreTaskComplete = pOnFirestoreTaskComplete;
    }

    public void getWorkmateData() {
        mWorkmateRef
                .get()
                .addOnCompleteListener(pTask -> {
                    if (pTask.isSuccessful()) {
                        mOnFirestoreTaskComplete.workmateDataLoaded(
                                Objects.requireNonNull(pTask.getResult()).toObjects(Workmate.class));
                    } else {
                        mOnFirestoreTaskComplete.workmateOnError(pTask.getException());
                    }
                });
    }

}

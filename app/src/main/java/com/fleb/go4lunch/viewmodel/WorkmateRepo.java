package com.fleb.go4lunch.viewmodel;

import androidx.annotation.NonNull;

import com.fleb.go4lunch.model.Workmate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * Created by Florence LE BOURNOT on 22/09/2020
 */
public class WorkmateRepo {

    private OnFirestoreTaskComplete mOnFirestoreTaskComplete;

    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference mWorkmateRef = mDb.collection("Workmate");

    public WorkmateRepo(OnFirestoreTaskComplete pOnFirestoreTaskComplete) {
        this.mOnFirestoreTaskComplete = pOnFirestoreTaskComplete;
    }

    public void getWorkmateData() {
        mWorkmateRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> pTask) {
                        if (pTask.isSuccessful()) {
                            mOnFirestoreTaskComplete.workmateDataAdded(pTask.getResult().toObjects(Workmate.class));
                        } else {
                            mOnFirestoreTaskComplete.workmateOnError(pTask.getException());
                        }
                    }
                });
    }

    public interface OnFirestoreTaskComplete {
        void workmateDataAdded(List<Workmate> pWorkmateList);
        void workmateOnError(Exception pE);
    }
}

package com.fleb.go4lunch.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.fleb.go4lunch.model.Choice;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.utils.ActionStatus;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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
    private DocumentReference mChoiceDocRef;

    private MutableLiveData<List<Choice>> mLDListWorkmateComing = new MutableLiveData<>();
    private MutableLiveData<ActionStatus> mLDChoice = new MutableLiveData<>();


    public MutableLiveData<ActionStatus> getWorkmateChoiceForRestaurant(String pWorkmateId, Restaurant pRestaurant) {
        //TODO
        //call firestore
        mChoiceRef.whereEqualTo(String.valueOf(Choice.Fields.chWorkmateId) , pWorkmateId)
                .whereEqualTo(String.valueOf(Choice.Fields.chRestoPlaceId), pRestaurant.getRestoPlaceId());
        //TODO add timestamp quand fonctionnera avec les 2 premiers chammps
        mChoiceRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot pQueryDocumentSnapshots) {
                        if(!pQueryDocumentSnapshots.isEmpty()) {
                            //Vérifier qu'il n'y a un seul enregistrement
                            //pQueryDocumentSnapshots.getDocuments();
                            Log.d(TAG, "onSuccess: il y a des données");
                            mLDChoice.setValue(ActionStatus.IS_CHOOSED);
                        } else {
                            Log.d(TAG, "onSuccess: pas de données");
                            mLDChoice.setValue(ActionStatus.NOT_CHOOSED);
                        }
                    }
                });
        return mLDChoice;
    }


    public MutableLiveData<List<Choice>> getWorkmateComingInRestaurant (String pRestoId) {
        //TODO get avec whereEqualsTo(restoId=pRestoId)
        //call firestore to get the list of the workmate coming in the restaurant
        return mLDListWorkmateComing;
    }
}

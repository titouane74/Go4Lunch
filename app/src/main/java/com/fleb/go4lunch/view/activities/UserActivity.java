package com.fleb.go4lunch.view.activities;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Workmate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fleb.go4lunch.view.activities.MainActivity.WORKMATE_COLLECTION;
//import static com.fleb.go4lunch.view.activities.MainActivity.WORKMATE_EMAIL_KEY;
import static com.fleb.go4lunch.view.activities.MainActivity.WORKMATE_NAME_KEY;
//import static com.fleb.go4lunch.view.activities.MainActivity.WORKMATE_PHOTO_URL_KEY;

public class UserActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG_USER = "TAG_USER";
    private FirebaseUser mCurrentUser;

    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference mWorkmateRef = mDb.collection(WORKMATE_COLLECTION);

    private ImageView mImgUser;
    private TextView mTxtEmail;
    private EditText mTxtName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //public static final String TAG_USER = "TAG_USER";
        FirebaseAuth lAuth = FirebaseAuth.getInstance();
        mCurrentUser = lAuth.getCurrentUser();

        mImgUser = findViewById(R.id.user_img_user);
        mTxtEmail = findViewById(R.id.user_txt_email);
        mTxtName = findViewById(R.id.user_edit_text_username);

        displayWorkmateData(mCurrentUser);

        Button lBtnUpdate = findViewById(R.id.user_btn_update);
        Button lBtnDelete = findViewById(R.id.user_btn_delete);

        lBtnUpdate.setOnClickListener(this);
        lBtnDelete.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_btn_update:
                updateWorkmate(mCurrentUser);
                break;
            case R.id.user_btn_delete:
                signOutFromFirebase();
                deleteWorkmate(mCurrentUser);
                break;
        }
    }

    public void signOutFromFirebase() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> startActivity(new Intent(getApplicationContext(), AuthenticationActivity.class)));
    }

    public void displayWorkmateData(FirebaseUser pCurrentWorkmate) {

        mWorkmateRef.document(Objects.requireNonNull(pCurrentWorkmate.getEmail()))
/*
        mWorkmateRef.document(pCurrentWorkmate.getUid())
                .get()
                .addOnSuccessListener(pDocumentSnapshot -> {
                    if (pDocumentSnapshot.exists()) {
                        Workmate lWorkmate = pDocumentSnapshot.toObject(Workmate.class);
                        String lPhotoUrl = Objects.requireNonNull(lWorkmate).getWorkmatePhotoUrl();
                        if (lPhotoUrl != null) {
                            Glide.with(this).load(lPhotoUrl).apply(RequestOptions.circleCropTransform()).into(mImgUser);
                        }
                        mTxtName.setText(lWorkmate.getWorkmateName());
                        mTxtEmail.setText(lWorkmate.getWorkmateEmail());
                    } else {
                        Toast.makeText(this, "Document does not exist", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(pE -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show());
*/

        //mWorkmateRef.document(pCurrentWorkmate.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                        if (pTask.isSuccessful()) {
                            Log.d(TAG_USER, "onComplete: SUCCESSFUL" );
                            DocumentSnapshot pDocSnap = pTask.getResult();
                            if (Objects.requireNonNull(pDocSnap).exists()) {
                                Log.d(TAG_USER, "onComplete: SUCCESSFUL and EXIST" );
                                Workmate lWorkmate = pDocSnap.toObject(Workmate.class);
                                String lPhotoUrl = Objects.requireNonNull(lWorkmate).getWorkmatePhotoUrl();
                                if (lPhotoUrl != null) {
                                    Glide.with(UserActivity.this).load(lPhotoUrl).apply(RequestOptions.circleCropTransform()).into(mImgUser);
                                }
                                mTxtName.setText(lWorkmate.getWorkmateName());
                                mTxtEmail.setText(lWorkmate.getWorkmateEmail());
                            } else {
                                Log.d(TAG_USER, "onComplete: SUCCESSFUL and NOT EXIST" );
                            }
                        } else {
                            Log.d(TAG_USER, "onComplete: NOT SUCCESSFUL" );
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception pE) {
                        Log.d(TAG_USER, "onComplete: FAILURE" );
                    }
                });

    }

    public void deleteWorkmate(FirebaseUser pCurrentWorkmate) {


        mWorkmateRef.document( Objects.requireNonNull(pCurrentWorkmate.getEmail()))
//        mWorkmateRef.document( pCurrentWorkmate.getUid())
                .delete()
                .addOnSuccessListener(pVoid -> Log.d(TAG_USER, "onSuccess: Document deleted " ))
                .addOnFailureListener(pE -> Log.d(TAG_USER, "onFailure: Document not deleted", pE));
        Toast.makeText(this, "COMPTE SUPPRIME", Toast.LENGTH_LONG).show();
        finish();
    }

    public void updateWorkmate(FirebaseUser pCurrentWorkmate) {

        String lWorkmateName = mTxtName.getText().toString();

        Map<String, Object> lWorkmate = new HashMap<>();
        lWorkmate.put(WORKMATE_NAME_KEY, lWorkmateName);

        mWorkmateRef.document(Objects.requireNonNull(pCurrentWorkmate.getEmail()))
//        mWorkmateRef.document( pCurrentWorkmate.getUid())
                .update(lWorkmate)
                .addOnSuccessListener(pVoid -> Log.d(TAG_USER, "onSuccess: Document updated " ))
                .addOnFailureListener(pE -> Log.d(TAG_USER, "onFailure: Document not updated", pE));
        Toast.makeText(this, "COMPTE MIS A JOUR", Toast.LENGTH_LONG).show();
        finish();
    }

}
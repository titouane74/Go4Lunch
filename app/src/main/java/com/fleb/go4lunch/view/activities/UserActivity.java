package com.fleb.go4lunch.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.fleb.go4lunch.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fleb.go4lunch.view.activities.MainActivity.TAG_FIRESTORE;
import static com.fleb.go4lunch.view.activities.MainActivity.WORKMATE_COLLECTION;
import static com.fleb.go4lunch.view.activities.MainActivity.WORKMATE_EMAIL_KEY;
import static com.fleb.go4lunch.view.activities.MainActivity.WORKMATE_NAME_KEY;
import static com.fleb.go4lunch.view.activities.MainActivity.WORKMATE_PHOTO_URL_KEY;

public class UserActivity extends BaseActivity implements View.OnClickListener{
    //public static final String TAG_USER = "TAG_USER";
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference mWorkmateRef = mDb.collection(WORKMATE_COLLECTION);

    private ImageView mImgUser;
    private TextView mTxtEmail;
    private EditText mTxtName;


    @Override
    protected int getActivityLayout() { return R.layout.activity_user; }

    @Override
    protected void configureActivityOnCreate() {
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mImgUser = findViewById(R.id.user_img_user);
        mTxtEmail = findViewById(R.id.user_txt_email);
        mTxtName = findViewById(R.id.user_edit_text_username);

        displayWorkmateData(mCurrentUser);
/*
        if (mCurrentUser.getPhotoUrl() != null) {
            String lPhotoUrl = Objects.requireNonNull(mCurrentUser.getPhotoUrl()).toString();
            Glide.with(this).load(lPhotoUrl).apply(RequestOptions.circleCropTransform()).into(lImgUser);
        }
        lTxtEmail.setText(mCurrentUser.getEmail());
        mTxtName.setText(mCurrentUser.getDisplayName());
*/

        Button lBtnUpdate = findViewById(R.id.user_btn_update);
        Button lBtnSignOut = findViewById(R.id.user_btn_sign_out);
        Button lBtnDelete = findViewById(R.id.user_btn_delete);

        //TODO : to implement
        lBtnUpdate.setOnClickListener(this);
        lBtnSignOut.setOnClickListener(this);
        lBtnDelete.setOnClickListener(this);

    }

/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mImgUser = findViewById(R.id.user_img_user);
        mTxtEmail = findViewById(R.id.user_txt_email);
        mTxtName = findViewById(R.id.user_edit_text_username);

        displayWorkmateData(mCurrentUser);
*/
/*
        if (mCurrentUser.getPhotoUrl() != null) {
            String lPhotoUrl = Objects.requireNonNull(mCurrentUser.getPhotoUrl()).toString();
            Glide.with(this).load(lPhotoUrl).apply(RequestOptions.circleCropTransform()).into(lImgUser);
        }
        lTxtEmail.setText(mCurrentUser.getEmail());
        mTxtName.setText(mCurrentUser.getDisplayName());
*//*


        Button lBtnUpdate = findViewById(R.id.user_btn_update);
        Button lBtnSignOut = findViewById(R.id.user_btn_sign_out);
        Button lBtnDelete = findViewById(R.id.user_btn_delete);

        //TODO : to implement
        lBtnUpdate.setOnClickListener(this);
        lBtnSignOut.setOnClickListener(this);
        lBtnDelete.setOnClickListener(this);
    }
*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_btn_update:
                updateWorkmate(mCurrentUser);
                Toast.makeText(this, getString(R.string.to_do) + " UPDATE", Toast.LENGTH_SHORT).show();
                break;
            case R.id.user_btn_sign_out:
                signOutFromFirebase();
                break;
            case R.id.user_btn_delete:
                Toast.makeText(this, getString(R.string.to_do) + " DELETE", Toast.LENGTH_SHORT).show();
                deleteWorkmate(mCurrentUser);
                signOutFromFirebase();
                break;
        }
    }

    public void signOutFromFirebase() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    startActivity(new Intent(getApplicationContext(), AuthenticationActivity.class));
                });
    }

    public void displayWorkmateData(FirebaseUser pCurrentWorkmate) {

        mWorkmateRef.document(Objects.requireNonNull(pCurrentWorkmate.getEmail()))
                .get()
                .addOnSuccessListener(pDocumentSnapshot -> {
                    if (pDocumentSnapshot.exists()) {
                        String lEmail = pDocumentSnapshot.getString(WORKMATE_EMAIL_KEY);
                        String lName = pDocumentSnapshot.getString(WORKMATE_NAME_KEY);
                        String lPhotoUrl = pDocumentSnapshot.getString(WORKMATE_PHOTO_URL_KEY);

                        if (lPhotoUrl != null) {
                            Glide.with(this).load(lPhotoUrl).apply(RequestOptions.circleCropTransform()).into(mImgUser);
                        }
                        mTxtName.setText(lName);
                        mTxtEmail.setText(lEmail);
                    } else {
                        Toast.makeText(this, "Document does not exist", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(pE -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show());
    }

    public void deleteWorkmate(FirebaseUser pCurrentWorkmate) {

        Map<String, Object> lWorkmate = new HashMap<>();
        lWorkmate.put(WORKMATE_EMAIL_KEY, pCurrentWorkmate.getEmail());
        lWorkmate.put(WORKMATE_NAME_KEY, pCurrentWorkmate.getDisplayName());
        if (pCurrentWorkmate.getPhotoUrl() != null) {
            lWorkmate.put(WORKMATE_PHOTO_URL_KEY, Objects.requireNonNull(pCurrentWorkmate.getPhotoUrl()).toString());
        }

        mWorkmateRef.document( Objects.requireNonNull(pCurrentWorkmate.getEmail()))
                .delete()
                .addOnSuccessListener(pVoid -> Log.d(TAG_FIRESTORE, "onSuccess: Document deleted " ))
                .addOnFailureListener(pE -> Log.d(TAG_FIRESTORE, "onFailure: Document not deleted", pE));
    }

    public void updateWorkmate(FirebaseUser pCurrentWorkmate) {

        String lWorkmateName = mTxtName.getText().toString();

        Map<String, Object> lWorkmate = new HashMap<>();
        lWorkmate.put(WORKMATE_NAME_KEY, lWorkmateName);

        mWorkmateRef.document(Objects.requireNonNull(pCurrentWorkmate.getEmail()))
                .update(lWorkmate)
                .addOnSuccessListener(pVoid -> Log.d(TAG_FIRESTORE, "onSuccess: Document updated " ))
                .addOnFailureListener(pE -> Log.d(TAG_FIRESTORE, "onFailure: Document not updated", pE));

    }

}
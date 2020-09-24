package com.fleb.go4lunch.view.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Workmate;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.BreakIterator;
import java.util.Objects;

import static com.fleb.go4lunch.view.activities.MainActivity.TAG_FIRESTORE;
import static com.fleb.go4lunch.view.activities.MainActivity.WORKMATE_COLLECTION;
import static com.fleb.go4lunch.view.activities.MainActivity.WORKMATE_EMAIL_KEY;
import static com.fleb.go4lunch.view.activities.MainActivity.WORKMATE_NAME_KEY;
import static com.fleb.go4lunch.view.activities.MainActivity.WORKMATE_PHOTO_URL_KEY;

/**
 * Created by Florence LE BOURNOT on 24/09/2020
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG_BASE_ACTIVITY = "TAG_BASE_ACTIVITY";

    protected abstract int getActivityLayout();

    protected abstract void configureActivityOnCreate();

    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference mWorkmateRef = mDb.collection(WORKMATE_COLLECTION);

    private ImageView mImgUser;
    private TextView mTxtEmail;

    private String mEmail;
    private String mName;
    private String mPhotoUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        this.configureActivityOnCreate();
    }

/*
    public void displayWorkmateData(Context pContext,View pView, FirebaseUser pCurrentWorkmate) {

        Log.d(TAG_BASE_ACTIVITY, "displayWorkmateData: " + pContext.toString());

        mWorkmateRef.document(Objects.requireNonNull(pCurrentWorkmate.getEmail()))
                .get()
                .addOnSuccessListener(pDocumentSnapshot -> {
                    if (pDocumentSnapshot.exists()) {
*/
/*
                        String lEmail = pDocumentSnapshot.getString(WORKMATE_EMAIL_KEY);
                        String lName = pDocumentSnapshot.getString(WORKMATE_NAME_KEY);
                        String lPhotoUrl = pDocumentSnapshot.getString(WORKMATE_PHOTO_URL_KEY);

                        if (lPhotoUrl != null) {
                            Glide.with(this).load(lPhotoUrl).apply(RequestOptions.circleCropTransform()).into(mImgUser);
                        }
                        mTxtName.setText(lName);
                        mTxtEmail.setText(lEmail);
*//*

                        mEmail = pDocumentSnapshot.getString(WORKMATE_EMAIL_KEY);
                        mName = pDocumentSnapshot.getString(WORKMATE_NAME_KEY);
                        mPhotoUrl = pDocumentSnapshot.getString(WORKMATE_PHOTO_URL_KEY);

                    } else {
                        Toast.makeText(this, "Document does not exist", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(pE -> {
                    Toast.makeText(this, "Error " + pE.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG_FIRESTORE, "displayWorkmateData: " + pE.getMessage());
                });


        if (pContext.toString().contains("UserActivity")) {
            mImgUser = findViewById(R.id.user_img_user);
            mTxtEmail = findViewById(R.id.user_txt_email);
            EditText lTxtName = findViewById(R.id.user_edit_text_username);
            lTxtName.setText(mName);
        } else {
            mImgUser = pView.findViewById(R.id.nav_img_user);
            mTxtEmail = pView.findViewById(R.id.nav_txt_user_email);
            TextView lTxtName = pView.findViewById(R.id.nav_txt_user_name);
            lTxtName.setText(mName);
        }

        mTxtEmail.setText(mEmail);


        if (mPhotoUrl != null) {
            Glide.with(this).load(mPhotoUrl).apply(RequestOptions.circleCropTransform()).into(mImgUser);
        }


        //return new Workmate(mEmail, mName, mEmail, mPhotoUrl);
    }
*/

}

package com.fleb.go4lunch.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fleb.go4lunch.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class UserActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG_USER = "TAG_USER";

    private ImageView mImgLogo;
    private ImageView mImgUser;
    private TextView mTxtEmail;
    private TextView mTxtName;
    private Button mBtnUpdate;
    private Button mBtnSignOut;
    private Button mBtnDelete;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mImgUser = findViewById(R.id.prof_user_img_user);
        mImgLogo = findViewById(R.id.prof_user_img_logo);
        mTxtEmail = findViewById(R.id.prof_user_txt_email);
        mTxtName = findViewById(R.id.prof_user_edit_text_username);

        String lPhotoUrl = Objects.requireNonNull(mCurrentUser.getPhotoUrl()).toString();
        Glide.with(this).load(lPhotoUrl).apply(RequestOptions.circleCropTransform()).into(mImgUser);
        mTxtEmail.setText(mCurrentUser.getEmail());
        mTxtName.setText(mCurrentUser.getDisplayName());

        mBtnUpdate = findViewById(R.id.prof_user_btn_update);
        mBtnSignOut = findViewById(R.id.prof_user_btn_sign_out);
        mBtnDelete = findViewById(R.id.prof_user_btn_delete);

        //TODO : to implement
        mBtnUpdate.setOnClickListener(this);
        mBtnSignOut.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prof_user_btn_update:
                Toast.makeText(this, R.string.to_do + "UPDATE", Toast.LENGTH_SHORT).show();
                break;
            case R.id.prof_user_btn_sign_out:
                Toast.makeText(this, R.string.to_do + " SIGN OUT", Toast.LENGTH_SHORT).show();
                break;
            case R.id.prof_user_btn_delete:
                Toast.makeText(this, R.string.to_do + " DELETE", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
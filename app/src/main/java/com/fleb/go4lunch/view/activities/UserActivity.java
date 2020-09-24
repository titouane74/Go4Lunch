package com.fleb.go4lunch.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    //public static final String TAG_USER = "TAG_USER";

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        ImageView lImgUser = findViewById(R.id.prof_user_img_user);
//        ImageView lImgLogo = findViewById(R.id.prof_user_img_logo);
        TextView lTxtEmail = findViewById(R.id.prof_user_txt_email);
        EditText lTxtName = findViewById(R.id.prof_user_edit_text_username);

        String lPhotoUrl = Objects.requireNonNull(mCurrentUser.getPhotoUrl()).toString();
        Glide.with(this).load(lPhotoUrl).apply(RequestOptions.circleCropTransform()).into(lImgUser);
        lTxtEmail.setText(mCurrentUser.getEmail());
        lTxtName.setText(mCurrentUser.getDisplayName());

        Button lBtnUpdate = findViewById(R.id.user_btn_update);
        Button lBtnSignOut = findViewById(R.id.user_btn_sign_out);
        Button lBtnDelete = findViewById(R.id.user_btn_delete);

        //TODO : to implement
        lBtnUpdate.setOnClickListener(this);
        lBtnSignOut.setOnClickListener(this);
        lBtnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_btn_update:
                Toast.makeText(this, R.string.to_do + " UPDATE", Toast.LENGTH_SHORT).show();
                break;
            case R.id.user_btn_sign_out:
                Toast.makeText(this, R.string.to_do + " SIGN OUT", Toast.LENGTH_SHORT).show();
                break;
            case R.id.user_btn_delete:
                Toast.makeText(this, R.string.to_do + " DELETE", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
package com.fleb.go4lunch.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.fleb.go4lunch.R;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    private static final String TAG_START = "TAG_START";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        openActivity();

    }

    protected void openActivity() {
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            Log.d(TAG_START, "onStart: User connecté");
        } else {
            startActivity(new Intent(getApplicationContext(), AuthenticationActivity.class));
            finish();
            Log.d(TAG_START, "onStart: User null");
        }
    }

}
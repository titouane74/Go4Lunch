package com.fleb.go4lunch.view.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fleb.go4lunch.R;
import com.google.firebase.auth.FirebaseAuth;

import static com.fleb.go4lunch.AppGo4Lunch.sApi;

public class StartActivity extends AppCompatActivity {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        openActivity();

    }

    protected void openActivity() {
        if (mAuth.getCurrentUser() != null) {
            sApi.setWorkmateId(mAuth.getUid());
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(getApplicationContext(), AuthenticationActivity.class));
            finish();
        }
    }

}
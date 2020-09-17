package com.fleb.go4lunch.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.base.BaseActivity;

public class StartActivity extends BaseActivity {

    private static final String TAG_START = "TAG_START";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //TODO voir en mentorat si on met un délai
        /*
        final Handler lHandler = new Handler();
        final Runnable lRun = new Runnable() {public void run() { openActivity();}};
        lHandler.postDelayed(lRun, 250);*/
        openActivity();

    }

    protected void openActivity() {
        if (isCurrentUserLogged()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            Log.d(TAG_START, "onStart: User connecté");
        } else {
            startActivity(new Intent(getApplicationContext(), AuthenticationActivity.class));
            Log.d(TAG_START, "onStart: User null");
        }
    }

    @Override
    public int getFragmentLayout() { return R.layout.activity_start; }
}
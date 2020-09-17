package com.fleb.go4lunch.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.fleb.go4lunch.R;

import com.fleb.go4lunch.base.BaseActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AuthenticationActivity extends BaseActivity {
    public static final String TAG_AUTHENTICATION = "TAG_AUTHENTICATION";
    public static final int RC_SIGN_IN = 123;

    Button mBtnLoginGoogle, mBtnLoginFacebook, mBtnLoginEmail, mBtnLoginTwitter;
    FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mBtnLoginGoogle=findViewById(R.id.btn_start_login_google);
        mBtnLoginFacebook=findViewById(R.id.btn_start_login_facebook);
        mBtnLoginEmail=findViewById(R.id.btn_start_login_email);
        mBtnLoginTwitter=findViewById(R.id.btn_start_login_twitter);

        mBtnLoginGoogle.setOnClickListener((v -> { startSignInWith(Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build())); }));
        mBtnLoginFacebook.setOnClickListener((v -> { startSignInWith(Collections.singletonList(new AuthUI.IdpConfig.FacebookBuilder().build())); }));
        mBtnLoginEmail.setOnClickListener((v -> { startSignInWith(Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build()));}));
        mBtnLoginTwitter.setOnClickListener((v -> { startSignInWith(Collections.singletonList(new AuthUI.IdpConfig.TwitterBuilder().build())); }));

    }

    @Override
    protected void onStart() {
        super.onStart();
        mCurrentUser = this.getCurrentUser();
        if (mCurrentUser != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            Toast.makeText(this,  mCurrentUser.getDisplayName() + getString(R.string.user_connected), Toast.LENGTH_SHORT).show();
        } else
            Log.d(TAG_AUTHENTICATION, "onStart: User null");

    }

    protected void startSignInWith(List<AuthUI.IdpConfig> pProvider) {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders( pProvider)
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                updateUI(mCurrentUser);
                Toast.makeText(this, getString(R.string.connection_succeed) + mCurrentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...

                if (response == null) {
                    Toast.makeText(this, getString(R.string.error_authentication_canceled), Toast.LENGTH_SHORT).show();
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, getString(R.string.error_unknown_error), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    protected void updateUI(FirebaseUser pUser) {
        Log.d(TAG_AUTHENTICATION, "updateUI: ");
    }


    @Override public void onBackPressed() { super.onBackPressed(); }

    protected OnFailureListener onFailureListener(){
        return e -> Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
    }

    @Override public void onClick(View v) {    }

    @Override public int getFragmentLayout() { return 0; }
}
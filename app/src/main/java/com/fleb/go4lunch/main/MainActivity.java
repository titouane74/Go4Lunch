package com.fleb.go4lunch.main;
//TODO implement the javadoc
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.Objects;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.fleb.go4lunch.BuildConfig;
import com.fleb.go4lunch.R;
import com.fleb.go4lunch.base.BaseActivity;
import com.fleb.go4lunch.utils.DisplayMessages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends BaseActivity {
    private static final int RC_SIGN_IN = 123;

    @Override
    public int getFragmentLayout() {return R.layout.activity_main;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configureToolbar();
        setContentView(R.layout.activity_main);
        //TODO after : to put in the menu instead of the button call
        Button lSignOut;
        lSignOut = findViewById(R.id.button_signout);
        lSignOut.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //TODO : test if user is logged : yes open map ; no open startSignInActivity ()
        //DisplayMessages.displayShortMessage(this,this.isCurrentUserLogged().toString());
        if (this.isCurrentUserLogged()) {
            DisplayMessages.displayShortMessage(this, getString(R.string.user_connected));
        } else {
            this.startSignInActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this, getString(R.string.connection_succeed), Toast.LENGTH_SHORT).show();
                // ...
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

    //TODO Attention BuildConfig.DEBUG
    protected void startSignInActivity() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                                        new AuthUI.IdpConfig.FacebookBuilder().build(),
                                        new AuthUI.IdpConfig.TwitterBuilder().build()
                                ))
                        .setIsSmartLockEnabled(!BuildConfig.DEBUG, true)
                        .setLogo(R.drawable.ic_go4lunch_logo)
                        .build(),
                RC_SIGN_IN);
    }

    //TODO : temporarily code for connection test
    protected void signOutFromFirebase(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> finish());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_signout) {
            this.signOutFromFirebase();
        }
    }

}
package com.fleb.go4lunch.view.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.fleb.go4lunch.R;
import com.fleb.go4lunch.base.BaseActivity;
import com.fleb.go4lunch.view.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.isCurrentUserLogged()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            //this.startSignInActivity();
        }
    }

    @Override
    public int getFragmentLayout() { return 0; }

    @Override
    public void onClick(View v) {    }

/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
*/


}
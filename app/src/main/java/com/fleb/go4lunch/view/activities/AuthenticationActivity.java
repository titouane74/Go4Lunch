package com.fleb.go4lunch.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.fleb.go4lunch.R;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.List;
import java.util.Objects;

public class AuthenticationActivity extends AppCompatActivity {

    public static final String TAG_AUTHENTICATION = "TAG_AUTHENTICATION";
    public static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

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

/*
        mBtnLoginGoogle.setOnClickListener((v -> { startSignInWith(Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build())); }));
        mBtnLoginFacebook.setOnClickListener((v -> { startSignInWith(Collections.singletonList(new AuthUI.IdpConfig.FacebookBuilder().build())); }));
        mBtnLoginEmail.setOnClickListener((v -> { startSignInWith(Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build()));}));
        mBtnLoginTwitter.setOnClickListener((v -> { startSignInWith(Collections.singletonList(new AuthUI.IdpConfig.TwitterBuilder().build())); }));
*/

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        mAuth = FirebaseAuth.getInstance();

        mBtnLoginGoogle.setOnClickListener((v -> { signInWithGoogle(); }));

    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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

/* prebuild
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG_AUTHENTICATION, "onActivityResult: value requestCode" + requestCode);
        Log.d(TAG_AUTHENTICATION, "onActivityResult: value resultCode" + resultCode);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                mCurrentUser=getCurrentUser();
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
                } else {
                    Log.d(TAG_AUTHENTICATION, "onActivityResult: " + response.getError().getErrorCode()
                            + response.getError().getMessage());
                }
            }
        }
    }
*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Exception lException = task.getException();
            if (task.isSuccessful()) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG_AUTHENTICATION, "firebaseAuthWithGoogle:" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG_AUTHENTICATION, "Google sign in failed", e);
                    // ...
                }
            } else {
                Log.w(TAG_AUTHENTICATION, Objects.requireNonNull(lException).toString());
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_AUTHENTICATION, "signInWithCredential:success");
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_AUTHENTICATION, "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                        }

                    }
                });
    }



    protected void updateUI(FirebaseUser pUser) {

        Log.d(TAG_AUTHENTICATION, "updateUI: ");

        Log.d(TAG_AUTHENTICATION, "currentuser: " + mCurrentUser.getDisplayName());
        Log.d(TAG_AUTHENTICATION, "currentuser: " + mCurrentUser.getEmail());
        Log.d(TAG_AUTHENTICATION, "currentuser: " + mCurrentUser.getPhotoUrl());


    }

    @Override public void onBackPressed() { super.onBackPressed(); }

    protected OnFailureListener onFailureListener(){
        return e -> Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
    }

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }

}
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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.fleb.go4lunch.R;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class AuthenticationActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG_AUTHENTICATION = "TAG_AUTHENTICATION";
    public static final int RC_SIGN_IN = 123;
    public static final int RC_SIGN_IN_GOOGLE = 456;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mFirebaseAuth;

    Button mBtnLoginGoogle, mBtnLoginFacebook, mBtnLoginEmail, mBtnLoginTwitter;
    FirebaseUser mCurrentUser;
    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mBtnLoginGoogle = findViewById(R.id.btn_start_login_google);
        mBtnLoginFacebook = findViewById(R.id.btn_start_login_facebook);
        mBtnLoginEmail = findViewById(R.id.btn_start_login_email);
        mBtnLoginTwitter = findViewById(R.id.btn_start_login_twitter);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Configure Facebook Sign In
        mCallbackManager = CallbackManager.Factory.create();

        //Firebase instantiation
        mFirebaseAuth = FirebaseAuth.getInstance();
/*
        mBtnLoginGoogle.setOnClickListener((v -> { startSignInWith(Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build())); }));
        mBtnLoginFacebook.setOnClickListener((v -> { startSignInWith(Collections.singletonList(new AuthUI.IdpConfig.FacebookBuilder().build())); }));
        mBtnLoginEmail.setOnClickListener((v -> { startSignInWith(Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build()));}));
        mBtnLoginTwitter.setOnClickListener((v -> { startSignInWith(Collections.singletonList(new AuthUI.IdpConfig.TwitterBuilder().build())); }));
*/
        mBtnLoginEmail.setOnClickListener(this);
        mBtnLoginGoogle.setOnClickListener(this);
        mBtnLoginFacebook.setOnClickListener(this);
        mBtnLoginTwitter.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCurrentUser = mFirebaseAuth.getCurrentUser();
        if (mCurrentUser != null) {
            updateUI(mCurrentUser);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_login_email:
                signInWithEmailFirebase();
                break;
            case R.id.btn_start_login_google:
                signInWithGoogle();
                break;
            case R.id.btn_start_login_facebook:
                signInWithFacebook();
                break;
            case R.id.btn_start_login_twitter:
                signInWithTwitter();
                break;
        }
    }

    //EMAIL FIREBASE
    private void signInWithEmailFirebase() {

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);

    }

    //GOOGLE
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_AUTHENTICATION, "signInWithCredential:success");
                            updateUI(mFirebaseAuth.getCurrentUser());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_AUTHENTICATION, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    //FACEBOOK
    private void signInWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Facebook Sign In was successful, authenticate with Firebase
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG_AUTHENTICATION, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG_AUTHENTICATION, "facebook:onError", error);
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {

        Log.d(TAG_AUTHENTICATION, "signInWithFacebookToken: " + accessToken);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {

                    Log.d(TAG_AUTHENTICATION, "Facebook signInWithCredential:onComplete:" + task.isSuccessful());

                    if (!task.isSuccessful()) {
                        Log.w(TAG_AUTHENTICATION, "Facebook signInWithCredential", task.getException());
                        Toast.makeText(getApplicationContext(), getString(R.string.error_msg_email_user_exists), Toast.LENGTH_SHORT).show();
                        Log.d(TAG_AUTHENTICATION, "onFailure: facebook" + Objects.requireNonNull(task.getException()).getMessage());
                    } else
                        updateUI(mFirebaseAuth.getCurrentUser());
                });
    }

    //TWITTER
    private void signInWithTwitter() {

        OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");

        Task<AuthResult> pendingResultTask = mFirebaseAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    updateUI(mFirebaseAuth.getCurrentUser());
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                    Log.d(TAG_AUTHENTICATION, "onFailure: " + e.getMessage());
                                }
                            });
        } else {
            // There's no pending result so you need to start the sign-in flow.
            // See below.

            mFirebaseAuth
                    .startActivityForSignInWithProvider(this, provider.build())
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    updateUI(mFirebaseAuth.getCurrentUser());
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                    Toast.makeText(getApplicationContext(), getString(R.string.error_msg_email_user_exists), Toast.LENGTH_SHORT).show();
                                    Log.d(TAG_AUTHENTICATION, "onFailure: 2" + e.getMessage());
                                }
                            });
        }
    }

    // ALL AUTHENTICATION
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Exception lException = task.getException();
            if (task.isSuccessful()) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG_AUTHENTICATION, "firebaseAuthWithGoogle:" + Objects.requireNonNull(account).getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG_AUTHENTICATION, "Google sign in failed", e);
                    // ...
                }
            } else {
                Log.w(TAG_AUTHENTICATION, Objects.requireNonNull(lException).toString());
            }
        } else if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                updateUI(mFirebaseAuth.getCurrentUser());
                Toast.makeText(this, getString(R.string.connection_succeed) + mCurrentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
            } else {
                if (response == null) {
                    Toast.makeText(this, getString(R.string.error_authentication_canceled), Toast.LENGTH_SHORT).show();
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, getString(R.string.error_unknown_error), Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG_AUTHENTICATION, "onActivityResult: " + response.getError().getErrorCode()
                            + response.getError().getMessage());
                    Toast.makeText(getApplicationContext(), getString(R.string.error_msg_email_user_exists), Toast.LENGTH_SHORT).show();
                    Log.d(TAG_AUTHENTICATION, "onFailure: email" + Objects.requireNonNull(response.getError().getMessage()));
                }
            }
        }
    }

    private void updateUI(FirebaseUser pCurrentUser) {

        if (pCurrentUser != null) {
            Log.d(TAG_AUTHENTICATION, "successLoginGetData: mCurrentUser " + Objects.requireNonNull(pCurrentUser).getDisplayName());
            Log.d(TAG_AUTHENTICATION, "successLoginGetData: mCurrentUser " + pCurrentUser.getEmail());
            Log.d(TAG_AUTHENTICATION, "successLoginGetData: mCurrentUser " + pCurrentUser.getPhotoUrl());

            //TODO Update user in database Firestore if not exist
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            Toast.makeText(this, "Please sign in to continue.", Toast.LENGTH_SHORT).show();
        }
    }
/*    protected void startSignInWith(List<AuthUI.IdpConfig> pProvider) {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders( pProvider)
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }
 //prebuild
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
    public void onBackPressed() {
        super.onBackPressed();
    }

}
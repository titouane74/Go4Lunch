package com.fleb.go4lunch.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

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

import com.fleb.go4lunch.utils.ActionStatus;
import com.fleb.go4lunch.viewmodel.AuthenticationViewModel;
import com.fleb.go4lunch.viewmodel.factory.Go4LunchViewModelFactory;
import com.fleb.go4lunch.viewmodel.injection.Injection;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
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

import static com.fleb.go4lunch.AppGo4Lunch.sApi;


public class AuthenticationActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG_AUTHENTICATION = "TAG_AUTHENTICATION";

    public static final int RC_SIGN_IN = 123;
    public static final int RC_SIGN_IN_GOOGLE = 456;
    private static final String AUTH_PROVIDER = "twitter.com";
    private static final String PERMISSION_PUBLIC_PROFILE = "public_profile";
    private static final String PERMISSION_EMAIL = "email";

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
                        .setTheme(R.style.AuthenticationTheme)
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
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        updateUI(mFirebaseAuth.getCurrentUser());
                    } else {
                        Toast.makeText(this, getString(R.string.error_unknown_error)
                                + ": " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //FACEBOOK
    private void signInWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(PERMISSION_PUBLIC_PROFILE,PERMISSION_EMAIL));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Facebook Sign In was successful, authenticate with Firebase
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), getString(R.string.error_authentication_canceled),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error)
                        + " : " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_msg_email_user_exists), Toast.LENGTH_SHORT).show();
                    } else
                        updateUI(mFirebaseAuth.getCurrentUser());
                });
    }

    //TWITTER
    private void signInWithTwitter() {

        OAuthProvider.Builder provider = OAuthProvider.newBuilder(AUTH_PROVIDER);

        Task<AuthResult> pendingResultTask = mFirebaseAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            pendingResultTask
                    .addOnSuccessListener(
                            authResult -> updateUI(mFirebaseAuth.getCurrentUser()))
                    .addOnFailureListener(e ->
                        Toast.makeText(this, getString(R.string.error_unknown_error)
                                + ": " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            mFirebaseAuth
                    .startActivityForSignInWithProvider(this, provider.build())
                    .addOnSuccessListener(
                            authResult -> updateUI(mFirebaseAuth.getCurrentUser()))
                    .addOnFailureListener(e ->
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.error_msg_email_user_exists), Toast.LENGTH_SHORT).show()
                    );
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
                    firebaseAuthWithGoogle(Objects.requireNonNull(account).getIdToken());
                } catch (ApiException e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error)
                            + " : " + e, Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.w(TAG_AUTHENTICATION, Objects.requireNonNull(lException).toString());
            }
        } else if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                Toast.makeText(this, getString(R.string.connection_succeed) + mCurrentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                updateUI(mFirebaseAuth.getCurrentUser());
            } else {
                if (response == null) {
                    Toast.makeText(this, getString(R.string.error_authentication_canceled), Toast.LENGTH_SHORT).show();
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, getString(R.string.error_unknown_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_msg_email_user_exists), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Update the user interface
     * @param pCurrentUser : object : FirebaseUser
     */
    private void updateUI(FirebaseUser pCurrentUser) {

        if (pCurrentUser != null) {
            Log.d(TAG_AUTHENTICATION, "successLoginGetData: mCurrentUser " + Objects.requireNonNull(pCurrentUser).getDisplayName());
            Log.d(TAG_AUTHENTICATION, "successLoginGetData: mCurrentUser " + pCurrentUser.getEmail());
            Log.d(TAG_AUTHENTICATION, "successLoginGetData: mCurrentUser " + pCurrentUser.getPhotoUrl());

            sApi.setWorkmateId(pCurrentUser.getUid());

            saveWorkmateIfNotExist(pCurrentUser);
        } else {
            Toast.makeText(this, R.string.auth_msg_sign_in_to_continue, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Save workmate information if not exist in Firestore
     * @param pCurrentWorkmate : object : FirebaseUser to save in Firestore
     */
    public void saveWorkmateIfNotExist(FirebaseUser pCurrentWorkmate) {
        Go4LunchViewModelFactory lFactory = Injection.go4LunchViewModelFactory();

        AuthenticationViewModel lAuthViewModel = new ViewModelProvider(this, lFactory).get(AuthenticationViewModel.class);
        lAuthViewModel.saveWorkmateFirebaseProfile(pCurrentWorkmate).observe(this, pWorkmateSaved -> {
            if (pWorkmateSaved.equals(ActionStatus.SAVED)) {
                Toast.makeText(AuthenticationActivity.this, R.string.auth_account_created, Toast.LENGTH_SHORT).show();
                this.finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else if (pWorkmateSaved.equals(ActionStatus.EXIST)) {
                this.finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else {
                Toast.makeText(AuthenticationActivity.this, R.string.auth_account_not_created, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
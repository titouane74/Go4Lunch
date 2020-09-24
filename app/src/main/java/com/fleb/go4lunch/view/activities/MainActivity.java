package com.fleb.go4lunch.view.activities;
//TODO implement the javadoc

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Workmate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG_FIRESTORE_ADD = "TAG_FIRESTORE_ADD";
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
//    private static final String TAG_MAIN = "TAG_MAIN";

    public static final String TAG_FIRESTORE = "TAG_FIRESTORE";
    public static final String WORKMATE_EMAIL_KEY = "workmateEmail";
    public static final String WORKMATE_NAME_KEY = "workmateName";
    public static final String WORKMATE_PHOTO_URL_KEY = "workmatePhotoUrl";
    public static final String WORKMATE_COLLECTION = "Workmate";

    // Access a Cloud Firestore instance from your Activity
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference mWorkmateRef = mDb.collection(WORKMATE_COLLECTION);

    private NavController mNavController;

    private FirebaseUser mCurrentUser;
    private boolean mExistWorkmate = false;
    private Workmate mWorkmate;

    private ImageView mImgUser;
    private TextView mTxtName;
    private TextView mTxtEmail;


    @Override
    protected int getActivityLayout() { return R.layout.activity_main; }

    @Override
    protected void configureActivityOnCreate() {
        FirebaseAuth lAuth = FirebaseAuth.getInstance();
        mCurrentUser = lAuth.getCurrentUser();

        configureToolBar();
        configureDrawerLayoutNavigationView();

        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        //Contient un navigationitemselectedlistener
        NavigationUI.setupWithNavController(mNavigationView, mNavController);

        //Bottom navigation
        BottomNavigationView lBottomNav = findViewById(R.id.nav_bottom);
        //When drawer and bottomnav are identical
        NavigationUI.setupWithNavController(lBottomNav, mNavController);

        //TODO Update user in database Firestore if not exist
        //TODO Add test if user exist or not in Firebase

        if (existWorkmate(mCurrentUser)) {
            saveWorkmate(mCurrentUser);
        }

    }

/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth lAuth = FirebaseAuth.getInstance();
        mCurrentUser = lAuth.getCurrentUser();

        configureToolBar();
        configureDrawerLayoutNavigationView();

        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        //Contient un navigationitemselectedlistener
        NavigationUI.setupWithNavController(mNavigationView, mNavController);

        //Bottom navigation
        BottomNavigationView lBottomNav = findViewById(R.id.nav_bottom);
        //When drawer and bottomnav are identical
        NavigationUI.setupWithNavController(lBottomNav, mNavController);

        //TODO Update user in database Firestore if not exist
        //TODO Add test if user exist or not in Firebase

        if (existWorkmate(mCurrentUser)) {
            saveWorkmate(mCurrentUser);
        }
    }
*/

    /**
     * Suppress the super.onBackPressed because we don't want that the user can press Back
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void signOutFromFirebase() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> startActivity(new Intent(getApplicationContext(), AuthenticationActivity.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(mNavController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void configureToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void configureDrawerLayoutNavigationView() {
        mDrawerLayout = findViewById(R.id.main_activity_drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        View lHeaderView = mNavigationView.getHeaderView(0);
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);

        mImgUser = lHeaderView.findViewById(R.id.nav_img_user);
        mTxtName = lHeaderView.findViewById(R.id.nav_txt_user_name);
        mTxtEmail = lHeaderView.findViewById(R.id.nav_txt_user_email);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_lunch, R.id.nav_logout, R.id.nav_settings, R.id.nav_map,
                R.id.nav_restaurant_list, R.id.nav_workmate)
                .setOpenableLayout(mDrawerLayout)
                .build();

        if (mCurrentUser != null) {
            displayWorkmateData(mCurrentUser);

        }

        mImgUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(MainActivity.this, UserActivity.class));
        }
    }

    //TODO Add test if user exist or not in Firebase
    public void saveWorkmate(FirebaseUser pCurrentWorkmate) {

        Map<String, Object> lWorkmate = new HashMap<>();
        lWorkmate.put(WORKMATE_EMAIL_KEY, pCurrentWorkmate.getEmail());
        lWorkmate.put(WORKMATE_NAME_KEY, pCurrentWorkmate.getDisplayName());
        if (pCurrentWorkmate.getPhotoUrl() != null) {
            lWorkmate.put(WORKMATE_PHOTO_URL_KEY, Objects.requireNonNull(pCurrentWorkmate.getPhotoUrl()).toString());
        }

        mWorkmateRef.document(Objects.requireNonNull(pCurrentWorkmate.getEmail()))
                .set(lWorkmate)
                .addOnSuccessListener(pDocumentReference -> Log.d(TAG_FIRESTORE, "onSuccess: Document saved "))
                .addOnFailureListener(pE -> Log.d(TAG_FIRESTORE, "onFailure: Document not saved", pE));

        Log.d(TAG_FIRESTORE_ADD, "saveWorkmate: " + mWorkmateRef.document().getId());
    }

    public boolean existWorkmate(FirebaseUser pCurrentWorkmate) {

        mWorkmateRef.document(Objects.requireNonNull(pCurrentWorkmate.getEmail()))
                .get()
                .addOnSuccessListener(pVoid -> {
                    Log.d(TAG_FIRESTORE, "onSuccess: Document updated ");
                    mExistWorkmate = true;
                })
                .addOnFailureListener(pE -> Log.d(TAG_FIRESTORE, "onFailure: Document not updated", pE));

        return mExistWorkmate;
    }

    public void displayWorkmateData(FirebaseUser pCurrentWorkmate) {

        mWorkmateRef.document(Objects.requireNonNull(pCurrentWorkmate.getEmail()))
                .get()
                .addOnSuccessListener(pDocumentSnapshot -> {
                    if (pDocumentSnapshot.exists()) {
                        String lEmail = pDocumentSnapshot.getString(WORKMATE_EMAIL_KEY);
                        String lName = pDocumentSnapshot.getString(WORKMATE_NAME_KEY);
                        String lPhotoUrl = pDocumentSnapshot.getString(WORKMATE_PHOTO_URL_KEY);

                        if (lPhotoUrl != null) {
                            Glide.with(this).load(lPhotoUrl).apply(RequestOptions.circleCropTransform()).into(mImgUser);
                        }
                        mTxtName.setText(lName);
                        mTxtEmail.setText(lEmail);

                    } else {
                        Toast.makeText(this, "Document does not exist", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(pE -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show());

    }

}


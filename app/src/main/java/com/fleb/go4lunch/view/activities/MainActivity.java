package com.fleb.go4lunch.view.activities;
//TODO implement the javadoc

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.fleb.go4lunch.di.DI;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.service.Go4LunchApi;
import com.fleb.go4lunch.utils.PreferencesHelper;
import com.fleb.go4lunch.viewmodel.MainActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.fleb.go4lunch.repository.WorkmateRepository.WORKMATE_COLLECTION;
import static com.fleb.go4lunch.repository.WorkmateRepository.WORKMATE_EMAIL_KEY;
import static com.fleb.go4lunch.repository.WorkmateRepository.WORKMATE_NAME_KEY;
import static com.fleb.go4lunch.repository.WorkmateRepository.WORKMATE_PHOTO_URL_KEY;
import static com.fleb.go4lunch.utils.PreferencesHelper.mPreferences;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private static final String TAG = "TAG_MAIN";

    public static final String PREF_KEY_RADIUS = "PREF_KEY_RADIUS";
    public static final String PREF_KEY_TYPE_GOOGLE_SEARCH = "PREF_KEY_TYPE_GOOGLE_SEARCH";
    public static final String PREF_KEY_PLACE_DETAIL_FIELDS = "PREF_KEY_PLACE_DETAIL_FIELDS";

    private Go4LunchApi mApi;

    private NavController mNavController;

    private FirebaseUser mCurrentUser;

    private ImageView mImgUser;
    private TextView mTxtName;
    private TextView mTxtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeSharedPreferences();
        if(mApi == null) {
            mApi = DI.getGo4LunchApiService();
        }

        FirebaseAuth lAuth = FirebaseAuth.getInstance();
        mCurrentUser = lAuth.getCurrentUser();

        mApi.saveWorkmateId(mCurrentUser);
        Log.d(TAG, "onCreate: saveWorkmateID : " + mCurrentUser.getDisplayName() + " - " + mCurrentUser.getUid());
        configureViewModel();

        configureToolBar();

        configureDrawerLayoutNavigationView();

        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        //Contient un navigationitemselectedlistener
        NavigationUI.setupWithNavController(mNavigationView, mNavController);

        //Bottom navigation
        BottomNavigationView lBottomNav = findViewById(R.id.nav_bottom);
        //When drawer and bottomnav are identical
        NavigationUI.setupWithNavController(lBottomNav, mNavController);

    }

    private void configureViewModel() {
        MainActivityViewModel lMainActivityViewModel = new MainActivityViewModel();
        lMainActivityViewModel.getWorkmateInfos(mApi.getWorkmateId()).observe(this,pWorkmate ->
        {
            Log.d(TAG, "configureViewModel: return from repo save workmate in Api : " + pWorkmate.getWorkmateName());
            mApi.setWorkmate(pWorkmate);
            displayDrawerData(pWorkmate);
        });
    }

    private void initializeSharedPreferences() {

        PreferencesHelper.loadPreferences(MainActivity.this);
        String lValueString;
        int lValueInt;

        if(mPreferences.getString(PREF_KEY_TYPE_GOOGLE_SEARCH,null) == null) {
            lValueString = getString(R.string.type_search);
            Log.d("TAG_PREFS", "initializeSharedPreferences: type " + lValueString);
            PreferencesHelper.saveStringPreferences(PREF_KEY_TYPE_GOOGLE_SEARCH,lValueString);
        }

        if(mPreferences.getString(PREF_KEY_PLACE_DETAIL_FIELDS,null) == null) {
            lValueString = getString(R.string.place_detail_fields);
            Log.d("TAG_PREFS", "initializeSharedPreferences: fields " + lValueString);
            PreferencesHelper.saveStringPreferences(PREF_KEY_PLACE_DETAIL_FIELDS,lValueString);
        }

        lValueInt = Integer.parseInt(getString(R.string.proximity_radius));
        Log.d("TAG_PREFS", "initializeSharedPreferences: radius " + lValueInt);
        PreferencesHelper.saveIntPreferences(PREF_KEY_RADIUS,lValueInt);

    }

    /**
     * Suppress the super.onBackPressed because we don't want that the user can press Back
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        configureDrawerLayoutNavigationView();
    }

    public void signOutFromFirebase() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task ->
                {
                    finish();
                    startActivity(new Intent(getApplicationContext(), AuthenticationActivity.class));
                });
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

/*        if (mCurrentUser != null) {
            displayWorkmateData(mCurrentUser);
        }*/
        mImgUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(MainActivity.this, UserActivity.class));
        }
    }

    private void displayDrawerData(Workmate pWorkmate) {

        if (pWorkmate.getWorkmatePhotoUrl() != null) {
            Glide.with(MainActivity.this).load(pWorkmate.getWorkmatePhotoUrl())
                    .apply(RequestOptions.circleCropTransform()).into(mImgUser);
        }
        mTxtName.setText(pWorkmate.getWorkmateName());
        mTxtEmail.setText(pWorkmate.getWorkmateEmail());
    }

}


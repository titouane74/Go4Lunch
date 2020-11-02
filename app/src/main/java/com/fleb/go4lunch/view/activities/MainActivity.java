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
import com.fleb.go4lunch.viewmodel.MainActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.fleb.go4lunch.AppGo4Lunch.sApi;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TAG_MAIN";

    /**
     * Api Service
     */
    //private Go4LunchApi mApi;

    /**
     * Firebase
     */
    private FirebaseUser mCurrentUser;

    /**
     * Design
     */
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout mDrawerLayout;
    private NavController mNavController;
    private ImageView mImgUser;
    private TextView mTxtName;
    private TextView mTxtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        if (mApi == null) {
            mApi = DI.getGo4LunchApiService();
        }*/

        FirebaseAuth lAuth = FirebaseAuth.getInstance();
        mCurrentUser = lAuth.getCurrentUser();

        sApi.setWorkmateId(mCurrentUser);
        Log.d(TAG, "onCreate: saveWorkmateID : " + mCurrentUser.getDisplayName() + " - " + mCurrentUser.getUid());
        configureViewModel();

        configureToolBar();

        configureDrawerLayoutNavigationView();

    }

    private void configureViewModel() {
        MainActivityViewModel lMainActivityViewModel = new MainActivityViewModel();
        lMainActivityViewModel.getWorkmateInfos(mCurrentUser.getUid()).observe(this, pWorkmate ->
        {
            sApi.setWorkmate(pWorkmate);
            displayDrawerData(pWorkmate);
        });
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
        NavigationView lNavigationView = findViewById(R.id.nav_view);
        View lHeaderView = lNavigationView.getHeaderView(0);
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);

        mImgUser = lHeaderView.findViewById(R.id.nav_img_user);
        mTxtName = lHeaderView.findViewById(R.id.nav_txt_user_name);
        mTxtEmail = lHeaderView.findViewById(R.id.nav_txt_user_email);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_lunch, R.id.nav_logout, R.id.nav_settings, R.id.nav_map,
                R.id.nav_restaurant_list, R.id.nav_workmate)
                .setOpenableLayout(mDrawerLayout)
                .build();


        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);

        //Contient un navigationitemselectedlistener
        NavigationUI.setupWithNavController(lNavigationView, mNavController);

        //Bottom navigation
        BottomNavigationView lBottomNav = findViewById(R.id.nav_bottom);
        //When drawer and bottomnav are identical
        NavigationUI.setupWithNavController(lBottomNav, mNavController);

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
package com.fleb.go4lunch.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
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
import com.fleb.go4lunch.workmanager.WorkerNotificationController;
import com.fleb.go4lunch.viewmodel.MainActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TAG_MAIN";

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
    private NavigationView mNavigationView;
    private ImageView mImgUser;
    private TextView mTxtName;
    private TextView mTxtEmail;

    private MainActivityViewModel mMainActivityViewModel;

    private Workmate mWorkmate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseAuth lAuth = FirebaseAuth.getInstance();
        mCurrentUser = lAuth.getCurrentUser();
        Log.e(TAG, "onCreate: " + mCurrentUser.getDisplayName() );
        Log.d(TAG, "onCreate: saveWorkmateID : " + mCurrentUser.getDisplayName() + " - " + mCurrentUser.getUid());
        configureViewModel();

        configureToolBar();

        configureDrawerLayoutNavigationView();

        Log.d(TAG, "onCreate: call WorkManagerNotificationController StarRequest" );

        configureWorkerNotification(getApplicationContext());

    }

    private void configureWorkerNotification(Context pContext){
        NotificationManagerCompat lNotificationManager = NotificationManagerCompat.from(pContext);

        if (lNotificationManager.areNotificationsEnabled()) {
            WorkerNotificationController.startWorkerController(pContext);
        } else {
            WorkerNotificationController.stopWorkerController(pContext);
        }
    }

    private void configureViewModel() {
        mMainActivityViewModel = new MainActivityViewModel();
        mMainActivityViewModel.getWorkmateInfos(mCurrentUser.getUid()).observe(this, pWorkmate ->
        {
            if(pWorkmate!=null) {
                mWorkmate = pWorkmate;
                displayDrawerData(pWorkmate);

                configureMenuYourLunch();
            } else {
                Log.e(TAG, "configureViewModel: user not found" );
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        configureDrawerLayoutNavigationView();

        mMainActivityViewModel.getWorkmateInfos(mWorkmate.getWorkmateId()).observe(this, pWorkmate -> {
            mWorkmate = pWorkmate;
            configureMenuYourLunch();
        });
    }

    private void configureMenuYourLunch() {
        if (mWorkmate.getWorkmateRestoChoosed() != null) {
            Menu lMenu = mNavigationView.getMenu();
            lMenu.getItem(1).setEnabled(true);
        } else {
            Menu lMenu = mNavigationView.getMenu();
            lMenu.getItem(1).setEnabled(false);
            //Toast.makeText(this, getString(R.string.text_lunch_not_choosed), Toast.LENGTH_LONG).show();
        }
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
                R.id.nav_home, R.id.restaurantDetailActivity, R.id.nav_logout, R.id.settingsActivity, R.id.nav_map,
                R.id.nav_restaurant_list, R.id.nav_workmate)
                .setOpenableLayout(mDrawerLayout)
                .build();


        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);

        //Contient un navigationitemselectedlistener
        NavigationUI.setupWithNavController(mNavigationView, mNavController);

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
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
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
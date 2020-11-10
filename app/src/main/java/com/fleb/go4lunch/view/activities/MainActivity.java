package com.fleb.go4lunch.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;

import com.fleb.go4lunch.BuildConfig;
import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.model.Workmate;
import com.fleb.go4lunch.view.fragments.MapsFragment;
import com.fleb.go4lunch.view.fragments.RestaurantListFragment;
import com.fleb.go4lunch.workmanager.WorkerNotificationController;
import com.fleb.go4lunch.viewmodel.MainActivityViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TAG_MAIN";

    private String mKey = BuildConfig.MAPS_API_KEY;

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
        Log.e(TAG, "onCreate: " + Objects.requireNonNull(mCurrentUser).getDisplayName());
        Log.d(TAG, "onCreate: saveWorkmateID : " + mCurrentUser.getDisplayName() + " - " + mCurrentUser.getUid());
        configureViewModel();

        configureToolBar();

        configureDrawerLayoutNavigationView();

        Log.d(TAG, "onCreate: call WorkManagerNotificationController StarRequest");

        configureWorkerNotification(getApplicationContext());

    }

    private void configureWorkerNotification(Context pContext) {
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
            if (pWorkmate != null) {
                mWorkmate = pWorkmate;
                displayDrawerData(pWorkmate);

                configureMenuYourLunch();
            } else {
                Log.e(TAG, "configureViewModel: user not found");
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        Menu lMenu = mNavigationView.getMenu();
        if (mWorkmate.getWorkmateRestoChoosed() != null) {
            lMenu.getItem(1).setEnabled(true);
        } else {
            lMenu.getItem(1).setEnabled(false);
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

        getMenuInflater().inflate(R.menu.main, menu);

        final SearchView lSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        lSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String pString) {
                if (pString.length() > 3) {
                    if (!Places.isInitialized()) {
                        Places.initialize(getApplicationContext(), mKey);
                    }

                    // Create a new Places client instance.
                    PlacesClient lPlacesClient = Places.createClient(getApplicationContext());

                    mMainActivityViewModel.getAutocompleteRestaurantList(lPlacesClient, pString)
                            .observe(MainActivity.this, pRestaurantList -> sendDataToFragment(pRestaurantList));
                } else {
                    mMainActivityViewModel.getRestaurantList().observe(MainActivity.this, pRestaurantList -> {
                        Log.e(TAG, "onQueryTextChange: return observe size : " + pRestaurantList.size() );
                        sendDataToFragment(pRestaurantList);
                    });
                }
                return true;
            }
        });
        lSearchView.setQueryHint(getString(R.string.text_hint_search));
        return super.onCreateOptionsMenu(menu);
    }

    private void sendDataToFragment(List<Restaurant> pRestaurantList) {

        if (mNavController.getCurrentBackStackEntry() != null) {
            if (mNavController.getCurrentBackStackEntry().getDestination().toString().contains("MapsFragment")) {
                //lMapsFragment.setMapMarkers(lRestaurantList);
                //((MapsFragment) fragment).(setMarkers(lRestaurantList));
                MapsFragment lMapsFragment = getMapsFragmentActualInstance();
                lMapsFragment.setMapMarkers(pRestaurantList);
                if (pRestaurantList.size() == 1) {
                    LatLng lLatLng = new LatLng(pRestaurantList.get(0).getRestoLocation().getLat(),
                            pRestaurantList.get(0).getRestoLocation().getLng());
                    lMapsFragment.setCameraOnCurrentLocation(lLatLng, Integer.parseInt(getString(R.string.map_zoom)));
                }
            } else if (mNavController.getCurrentBackStackEntry().getDestination().toString().contains("RestaurantListFragment")) {
                RestaurantListFragment lRestaurantListFragment = getRestaurantListFragmentActualInstance();
                lRestaurantListFragment.changeAndNotifyAdapterChange(pRestaurantList);
            }
        } else {
            Log.e(TAG, "sendDataToFragment: navController.currentBackStackEntry NULL"  );
        }
    }

    private MapsFragment getMapsFragmentActualInstance() {
        MapsFragment resultFragment = null;
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            List<Fragment> fragmentList = navHostFragment.getChildFragmentManager().getFragments();
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof MapsFragment) {
                    resultFragment = (MapsFragment) fragment;
                    break;
                }
            }
        }
        return resultFragment;
    }

    private RestaurantListFragment getRestaurantListFragmentActualInstance() {
        RestaurantListFragment resultFragment = null;
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            List<Fragment> fragmentList = navHostFragment.getChildFragmentManager().getFragments();
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof RestaurantListFragment) {
                    resultFragment = (RestaurantListFragment) fragment;
                    break;
                }
            }
        }
        return resultFragment;
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
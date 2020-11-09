package com.fleb.go4lunch.view.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fleb.go4lunch.AppGo4Lunch.sApi;


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
        Log.e(TAG, "onCreate: " + mCurrentUser.getDisplayName());
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

        final SearchView lSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        lSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @SuppressLint("ResourceType")
            @Override
            public boolean onQueryTextChange(String pString) {
                if (pString.length()>3) {
                    Toast.makeText(MainActivity.this, "TODO", Toast.LENGTH_SHORT).show();
                manageAutocomplete(pString);
                }
                return true;
            }
        });
        lSearchView.setQueryHint(getString(R.string.text_hint_search));
        return super.onCreateOptionsMenu(menu);
    }

    private void manageAutocomplete(String pQuery) {
        double lLat = sApi.getLocation().getLatitude();
        double lLng = sApi.getLocation().getLongitude();

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), mKey);
        }

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        // Create a RectangularBounds object.
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(lLat, lLng),
                new LatLng(48.8236549, 2.4102578));
        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                .setLocationBias(bounds)
                //.setLocationRestriction(bounds)
                .setOrigin(new LatLng(lLat,lLng))
                .setCountries("FR")
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setSessionToken(token)
                .setQuery(pQuery)
                .build();

        List<Restaurant> lRestaurantList = new ArrayList<>();

        Fragment navHostFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
        //Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);

        for (int i=0; 0<navHostFragment.getChildFragmentManager().getFragments().size();i++) {
            Log.d(TAG, "manageAutocomplete: " + navHostFragment.getChildFragmentManager().getFragments().get(i).toString());
        }

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                Log.i(TAG, prediction.getPlaceId());
                Log.i(TAG, prediction.getPrimaryText(null).toString());
                Log.i(TAG, prediction.getPlaceTypes().toString());
                Toast.makeText(MainActivity.this, prediction.getPrimaryText(null) + "-" + prediction.getSecondaryText(null), Toast.LENGTH_SHORT).show();
                Restaurant lRestaurant = new Restaurant(prediction.getPlaceId(), prediction.getPrimaryText(null).toString());
                lRestaurantList.add(lRestaurant);
            }

            Log.d(TAG, "manageAutocomplete: size list : " + lRestaurantList.size());
            if (mNavController.getCurrentBackStackEntry().getDestination().toString().contains("MapsFragment")) {
                Log.d(TAG, "onQueryTextChange: MAPS : " + Objects.requireNonNull(mNavController.getCurrentDestination()).getId());
                //lMapsFragment.setMapMarkers(lRestaurantList);
                //((MapsFragment) fragment).(setMarkers(lRestaurantList));
                MapsFragment lMapsFragment = getMapsFragmentActualInstance();
                lMapsFragment.setMapMarkers(lRestaurantList);

            } else if (mNavController.getCurrentBackStackEntry().getDestination().toString().contains("RestaurantListFragment")) {
                Log.d(TAG, "onQueryTextChange: RESTAURANTLIST : " + Objects.requireNonNull(mNavController.getCurrentDestination()).getId());
//                RestaurantListFragment lRestaurantListFragment = getRestaurantListFragment();
//                lRestaurantListFragment.changeAndNotifyAdapterChange(lRestaurantList);
            }

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
            }
        });

    }

    private MapsFragment getMapsFragmentActualInstance(){
        MapsFragment resultFragment = null;
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if(navHostFragment != null && navHostFragment.getChildFragmentManager() != null) {
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

    private RestaurantListFragment getRestaurantListFragmentActualInstance(){
        RestaurantListFragment resultFragment = null;
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if(navHostFragment != null && navHostFragment.getChildFragmentManager() != null) {
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
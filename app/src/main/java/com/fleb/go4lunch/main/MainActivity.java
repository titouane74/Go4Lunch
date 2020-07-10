package com.fleb.go4lunch.main;
//TODO implement the javadoc
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.Objects;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.fleb.go4lunch.R;
import com.fleb.go4lunch.base.BaseActivity;
import com.fleb.go4lunch.ui.lunch.LunchFragment;
import com.fleb.go4lunch.ui.map.MapFragment;
import com.fleb.go4lunch.ui.settings.SettingsFragment;
import com.fleb.go4lunch.ui.viewlist.ViewListFragment;
import com.fleb.go4lunch.ui.workmates.WorkmatesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends BaseActivity implements MapFragment.OnListenerLogout {
    private static final int RC_SIGN_IN = 123;
    private AppBarConfiguration mAppBarConfiguration;
    private Fragment mSelectedFragment;

    private static final String TAG_MAIN = "MainActivity";

    @Override
    public int getFragmentLayout() {return R.layout.activity_main;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSelectedFragment = null;
        configureAndShowFragment();

        //Navigation for the Drawer
        DrawerLayout drawer = findViewById(R.id.main_activity_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_lunch, R.id.nav_logout, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(navDrawerListener);

        //Bottom navigation
        BottomNavigationView lBottomNav = findViewById(R.id.nav_bottom);
        lBottomNav.setOnNavigationItemSelectedListener(navListener);

    }
    private NavigationView.OnNavigationItemSelectedListener navDrawerListener =
        new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_lunch:
                    mSelectedFragment = new LunchFragment();
                    break;
                case R.id.nav_settings:
                    mSelectedFragment = new SettingsFragment();
                    break;
                case R.id.nav_logout:
                    mSelectedFragment = new MapFragment();
                    signOutFromFirebase();
                    break;
            }
            replaceFragment(mSelectedFragment);
            //getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mSelectedFragment).commit();
            return true;
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_map:
                        mSelectedFragment = new MapFragment();
                        break;
                    case R.id.nav_view_list:
                        mSelectedFragment = new ViewListFragment();
                        break;
                    case R.id.nav_workmates:
                        mSelectedFragment = new WorkmatesFragment();
                        break;
                }
                replaceFragment(mSelectedFragment);

//                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,mSelectedFragment).commit();

                return true;
            }
        };

    @Override
    protected void onStart() {
        super.onStart();
        //TODO : test if user is logged : yes open map ; no open startSignInActivity ()
        //DisplayMessages.displayShortMessage(this,this.isCurrentUserLogged().toString());
        //noinspection StatementWithEmptyBody
        if (this.isCurrentUserLogged()) {
//            DisplayMessages.displayShortMessage(this, getString(R.string.user_connected));
        } else {
            this.startSignInActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
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

    protected void startSignInActivity() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                                        new AuthUI.IdpConfig.FacebookBuilder().build()
                                        ,
                                        new AuthUI.IdpConfig.TwitterBuilder().build()
                                ))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_go4lunch_logo)
                        .build(),
                RC_SIGN_IN);
    }

    protected void signOutFromFirebase(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> this.startSignInActivity());
    }

    @Override
    public void onClick(View v) { }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onButtonLogoutClickLogout() { this.signOutFromFirebase(); }


    /**
     * Initialization and dispay of the map fragment
     */
    private void configureAndShowFragment() {
        mSelectedFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.nav_map);
        if (mSelectedFragment == null) {
            mSelectedFragment = new MapFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.nav_host_fragment, mSelectedFragment)
                    .commit();
        }
    }

    /**
     * Fragment replace method
     * @param pFragment : fragment : fragment to display
     */
    private void replaceFragment(final Fragment pFragment) {
        final FragmentManager lFragmentManager = getSupportFragmentManager();
        final FragmentTransaction lFragmentTransaction = lFragmentManager.beginTransaction();
//        lFragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        lFragmentTransaction.replace(R.id.nav_host_fragment, pFragment);
        lFragmentTransaction.commit();
    }
}
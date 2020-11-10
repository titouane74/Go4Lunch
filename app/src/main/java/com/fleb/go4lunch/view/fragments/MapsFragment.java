package com.fleb.go4lunch.view.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.fleb.go4lunch.BuildConfig;
import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.utils.Go4LunchHelper;
import com.fleb.go4lunch.utils.PermissionUtils;
import com.fleb.go4lunch.view.activities.MainActivity;
import com.fleb.go4lunch.view.activities.RestaurantDetailActivity;
import com.fleb.go4lunch.viewmodel.MapViewModel;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;
import static com.fleb.go4lunch.AppGo4Lunch.sApi;

public class MapsFragment extends Fragment implements LocationListener {

    public static final String TAG = "TAG_";
    private static final int PERMISSION_REQUEST_CODE = 1;

    private String mKey = BuildConfig.MAPS_API_KEY;

    private int mZoom;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private SupportMapFragment mMapFragment;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @SuppressLint("MissingPermission")
        @Override
        public void onMapReady(GoogleMap googleMap) throws Resources.NotFoundException {
            mMap = googleMap;

            if (checkPermissions()) {
                mMap.setMyLocationEnabled(true);
            }

            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.style_json));

            mMap.setOnMarkerClickListener(pMarker -> {
                displayRestaurantDetail(pMarker);
                return false;
            });
        }
    };

    public MapsFragment() {
    }

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View lView = inflater.inflate(R.layout.fragment_maps, container, false);

        mZoom = Integer.parseInt(getString(R.string.map_zoom));
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        if (!checkPermissions()) {
            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                // Display a dialog with rationale.
                PermissionUtils.RationaleDialog.newInstance(PERMISSION_REQUEST_CODE, true)
                        .show(requireActivity().getSupportFragmentManager(), "dialog");
            } else {
                // Location permission has not been granted yet, request it.
                requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CODE);
            }
        } else {
            getCurrentLocation();
        }
        return lView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mMapFragment != null) {
            mMapFragment.getMapAsync(callback);
        }
    }


    public void configureViewModel() {

        MapViewModel lMapViewModel = new ViewModelProvider(mMapFragment).get(MapViewModel.class);

        lMapViewModel.getRestaurantList().observe(getViewLifecycleOwner(), this::setMapMarkers);
    }

    public void setMapMarkers(List<Restaurant> pRestaurants) {
        BitmapDescriptor lIcon;
        Log.d(TAG, "setMapMarkers: ");
        if (mMap != null) {
            mMap.clear();
            for (Restaurant lRestaurant : pRestaurants) {

                String lName = lRestaurant.getRestoName();
                if ((lRestaurant.getRestoWkList() != null) && (lRestaurant.getRestoWkList().size() > 0)) {
                    lIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_green);
                } else {
                    lIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_orange);
                }

                if (lRestaurant.getRestoLocation()!= null) {
                    LatLng latLng = new LatLng(lRestaurant.getRestoLocation().getLat(),
                            lRestaurant.getRestoLocation().getLng());
                    Marker lMarker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(lName)
                            .icon(lIcon));
                    lMarker.setTag(lRestaurant);
                } else {
                    Log.d(TAG, "setMapMarkers: AUTOCOMPLETE LIST");
                }
            }
        } else {
            Log.d(TAG, "setMapMarkers: AUTOCOMPLETE LIST - MAP NULL");
        }
    }

    private void displayRestaurantDetail(Marker pMarker) {
        Context lContext = requireContext();
        Restaurant lRestaurant = (Restaurant) pMarker.getTag();
        if (lRestaurant != null) {
            Intent lIntentRestoDetail = new Intent(lContext, RestaurantDetailActivity.class);
            lIntentRestoDetail.putExtra("placeid", lRestaurant.getRestoPlaceId());
            lContext.startActivity(lIntentRestoDetail);
        }
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {
        Task<Location> task = mFusedLocationClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                saveLocation(location);
            }
        });

        LocationManager lLocationManager = (LocationManager) this.requireContext().getSystemService(Context.LOCATION_SERVICE);
        if (lLocationManager != null) {
            lLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 50, this);
        }
    }

    public void saveLocation(Location pLocation) {
        double lLongitude = pLocation.getLongitude();
        double lLatitude = pLocation.getLatitude();
        LatLng lLatLng = new LatLng(lLatitude, lLongitude);

        sApi.saveLocationInSharedPreferences(pLocation);
        configureViewModel();
        setCameraOnCurrentLocation(lLatLng, mZoom);
    }

    public void setCameraOnCurrentLocation(LatLng latLng, int zoom) {
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        } catch (Exception pE) {
            Log.e(TAG, "setCameraOnCurrentLocation: ");
        }
    }

    /**
     * Called when the location has changed.
     *
     * <p> There are no restrictions on the use of the supplied Location object.
     *
     * @param location The new location, as a Location object.
     */

    @Override
    public void onLocationChanged(Location location) {
        saveLocation(location);
    }


    /**
     * This callback will never be invoked and providers can be considers as always in the
     * {@link Location Provider#AVAILABLE} state.
     *
     * @deprecated This callback will never be invoked.
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    /**
     * Called when the provider is enabled by the user.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderEnabled(String provider) {
    }

    /**
     * Called when the provider is disabled by the user. If requestLocationUpdates
     * is called on an already disabled provider, this method is called
     * immediately.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderDisabled(String provider) {
    }

    /**
     * Manage the permissions access for the location and the camera
     */
    private boolean checkPermissions() {
        int lPermissionLocation = checkSelfPermission(requireActivity(), ACCESS_FINE_LOCATION);

        return lPermissionLocation == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            //IF PERMISSION GRANTED
            //grantResults[0] -> Permission for the location
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    getCurrentLocation();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel(requireContext().getString(R.string.permission_required_toast),
                                    (dialog, which) -> requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                                            PERMISSION_REQUEST_CODE));
                        }
                    }
                }
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(requireContext())
                .setMessage(message)
                .setPositiveButton(R.string.btn_ok, okListener)
                .setNegativeButton(R.string.btn_cancel, null)
                .create()
                .show();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        configureViewModel();
    }
}
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
import android.view.View;
import android.view.ViewGroup;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.di.DI;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.service.Go4LunchApi;
import com.fleb.go4lunch.utils.GsonHelper;
import com.fleb.go4lunch.utils.PermissionUtils;
import com.fleb.go4lunch.view.activities.RestaurantDetailActivity;
import com.fleb.go4lunch.viewmodel.map.MapViewModel;
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

import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class MapsFragment extends Fragment implements LocationListener {

    public static final String TAG = "TAG_";
    private static final int PERMISSION_REQUEST_CODE = 1;

    private Go4LunchApi mApi;
    private int mZoom;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private SupportMapFragment mMapFragment;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
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

        if (mApi == null) {
            mApi = DI.getGo4LunchApiService();
        }

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

    public void configViewModel() {

        MapViewModel lMapViewModel = new ViewModelProvider(mMapFragment).get(MapViewModel.class);

        lMapViewModel.getRestaurantList().observe(getViewLifecycleOwner(), pRestaurantList -> {
            mApi.setRestaurantList(pRestaurantList);
            MapsFragment.this.setMapMarkers(pRestaurantList);
        });
    }

    public void setMapMarkers(List<Restaurant> pRestaurants) {

        BitmapDescriptor lIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_orange);

        Log.d("TAG_GETRESTO", "setMapMarkers: response size" + pRestaurants.size());

        for (Restaurant lRestaurant : pRestaurants) {

            String lName = lRestaurant.getRestoName();

            if ((lRestaurant.getRestoWkList() != null) && (lRestaurant.getRestoWkList().size() > 0)) {
                lIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_green);
            } else {
                lIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_orange);
            }

            LatLng latLng = new LatLng(lRestaurant.getRestoLocation().getLat(),
                    lRestaurant.getRestoLocation().getLng());
            Marker lMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(lName)
                    .icon(lIcon));
            lMarker.setTag(lRestaurant);
        }
    }

    private void displayRestaurantDetail(Marker pMarker) {
        Context lContext = requireContext();
        Restaurant lRestaurant = (Restaurant) pMarker.getTag();
        if (lRestaurant != null) {
            Intent lIntentRestoDetail = new Intent(lContext, RestaurantDetailActivity.class);
            String lJsonRestaurant = GsonHelper.getGsonString(lRestaurant);
            lIntentRestoDetail.putExtra("placeid", lRestaurant.getRestoPlaceId());
            lIntentRestoDetail.putExtra("restaurant", lJsonRestaurant);
            lContext.startActivity(lIntentRestoDetail);
        }
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {
        Task<Location> task = mFusedLocationClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                Log.d(TAG, "getCurrentLocation: " + location);
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

        mApi.saveLocationInSharedPreferences(pLocation);
        configViewModel();
        setCameraOnCurrentLocation(lLatLng, mZoom);
    }

    private void setCameraOnCurrentLocation(LatLng latLng, int zoom) {
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

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
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
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
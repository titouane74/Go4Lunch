package com.fleb.go4lunch.view.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fleb.go4lunch.BuildConfig;
import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.utils.PermissionUtils;
import com.fleb.go4lunch.viewmodel.MapViewModel;
import com.fleb.go4lunch.viewmodel.MapViewModelFactory;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Objects;

public class MapsFragment extends Fragment implements LocationListener {

    public static final String TAG_MAP = "TAG_MAP";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static final String TAG_GETRESTO = "TAG_GETRESTO";

    private LocationManager mLocationManager;
    private GoogleMap mMap;
    private double mLatitude, mLongitude;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private Location mCurrentLocation;
    private Location mCurrentLocation2;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;


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
        public void onMapReady(GoogleMap googleMap) {

            mMap = googleMap;
            Context lContext = requireContext();

            if (checkPermissions()) {
                mMap.setMyLocationEnabled(true);
            }

            try {
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                lContext, R.raw.style_json));
                if (!success) {
                    Log.e(TAG_MAP, "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Log.e(TAG_MAP, "Can't find style. Error: ", e);
            }

            if (mCurrentLocation == null) {
                if (mLastLocation != null) {
                    mCurrentLocation = mLastLocation;
                    Log.d(TAG_MAP, "onMapReady: last " + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude());
                } else {
                    Log.d(TAG_MAP, "onMapReady: current and last location null" );
                }
            } else {
                Log.d(TAG_MAP, "onMapReady: current " + mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude());
            }
            mLatitude = 48.8236549;
            mLongitude = 2.4102578;
            Log.d(TAG_MAP, "onMapReady location by save: " + mCurrentLocation);
            Log.d(TAG_MAP, "onMapReady location2 " + mCurrentLocation2);

            MapViewModelFactory lFactory = new MapViewModelFactory(lContext, mLatitude, mLongitude);

            MapViewModel lMapViewModel = new ViewModelProvider(requireActivity(), lFactory).get(MapViewModel.class);
            lMapViewModel.getRestoList().observe(getViewLifecycleOwner(), pRestaurants -> {
                setMapMarkers(pRestaurants);
            });

            LatLng lMyPosition = new LatLng(mLatitude, mLongitude);
            mMap.addMarker(new MarkerOptions().position(lMyPosition).title("Charenton Le Pont Maison"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lMyPosition, 16));

        }
    };

    public  void saveLocation(Location pLocation) {
        mCurrentLocation = pLocation;
    }

    public void setMapMarkers(List<Restaurant> pRestaurants) {

        BitmapDescriptor lIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_orange);

        Log.d(TAG_GETRESTO, "setMapMarkers: response size" + pRestaurants.size());

        for (int i = 0; i < pRestaurants.size(); i++) {

            String lName = pRestaurants.get(i).getRestoName();
            String lAddress = pRestaurants.get(i).getRestoAddress();

            LatLng latLng = new LatLng(pRestaurants.get(i).getRestoLocation().getLat(),
                    pRestaurants.get(i).getRestoLocation().getLng());
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(lName + " : " + lAddress)
                    .icon(lIcon));
        }
    }


    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View lView = inflater.inflate(R.layout.fragment_maps, container, false);

        if (!checkPermissions()) {
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
            getCurrentLocation();
            Log.d(TAG_MAP, "onCreateView location by save: " + mCurrentLocation);
            Log.d(TAG_MAP, "onCreateView location2 " + mCurrentLocation2);

        }

        return lView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {
        mLocationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        Objects.requireNonNull(mLocationManager).requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, MapsFragment.this);

        Task<Location> lLocationTask = mFusedLocationClient.getLastLocation();
/*
        lLocationTask.addOnSuccessListener(location -> {
            if (location != null) {
                saveLocation(location);
            } else {
                mLocationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
                Objects.requireNonNull(mLocationManager).requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, MapsFragment.this);

                Objects.requireNonNull(mLocationManager).requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        saveLocation(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                });
            }
        });
        */
        lLocationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> pTask) {
                if (pTask.isSuccessful() ) {
                    saveLocation(pTask.getResult());
                    mCurrentLocation2 = pTask.getResult();
                    Log.d(TAG_MAP, "onComplete: " + mCurrentLocation2);
                } else {
                    mLocationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
                    Objects.requireNonNull(mLocationManager).requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, com.fleb.go4lunch.view.fragments.MapsFragment.this);
                }

            }
        });

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
        Log.d("onLocationChanged", "entered");

        Toast.makeText(getContext(), "Lat: " + location.getLatitude() + ", Long: "
                + location.getLongitude(), Toast.LENGTH_SHORT).show();

        Log.d("TAG_onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");

        // Adding colour to the marker
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

        // Adding Marker to the Map
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        Log.d("TAG_onLocationChanged", String.format("latitude:%.3f longitude:%.3f", mLatitude, mLongitude));

        Log.d("TAG_onLocationChanged", "Exit");

    }


    /**
     * This callback will never be invoked and providers can be considers as always in the
     * {@link Location Provider#AVAILABLE} state.
     *
     * @param provider
     * @param status
     * @param extras
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

    @Override
    public void onPause() {
        stopLocationUpdates();
        super.onPause();
    }

    private void stopLocationUpdates() {
//        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
//        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
//            startLocationUpdates();
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else {
//            getLastLocation();
//            startLocationUpdates();
            getCurrentLocation();
        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();

                        } else {
                            Log.w(TAG_MAP, "getLastLocation:exception", task.getException());
                        }
                    }
                });
    }
}
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

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.utils.PermissionUtils;
import com.fleb.go4lunch.viewmodel.MapViewModel;
import com.fleb.go4lunch.viewmodel.MapViewModelFactory;
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

public class MapsFragment extends Fragment implements LocationListener {

    public static final String TAG_MAP = "TAG_MAP";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static final String TAG_GETRESTO = "TAG_GETRESTO";
    private final int mZoom = 16;

    private Marker mCurrLocationMarker;
    private GoogleMap mMap;
    private double mLatitude = 48.8236549;
    private double mLongitude = 2.4102578;
    private Location mCurrentLocation;
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

            //TODO find a solution to remove this code
            //Pour que ça fonctionne sur le téléphone
            configViewModel(requireContext(), mLatitude,mLongitude);
            setCameraOnCurrentLocation(new LatLng(mLatitude, mLongitude),mZoom);

        }
    };

    public void configViewModel(Context pContext, Double pLatitude, Double pLongitude) {

/*
    //TODO to debug return nothing
        MapViewModel lMapViewModel = new MapViewModel();
        lMapViewModel.initViewModel(pContext, pLatitude, pLongitude);
        lMapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);
        lMapViewModel.getRestaurantList().observe(getViewLifecycleOwner(), this::setMapMarkers);

*/

//V1
        MapViewModelFactory lFactory = new MapViewModelFactory(pContext, pLatitude, pLongitude);

        MapViewModel lMapViewModel = new ViewModelProvider(requireActivity(), lFactory).get(MapViewModel.class);
        lMapViewModel.getRestoList().observe(getViewLifecycleOwner(), this::setMapMarkers);

    }

    public  void saveLocation(Location pLocation) {
        mCurrentLocation = pLocation;

        //TODO to active when it's for the emulator
        //configViewModel(requireContext(), mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        //setCameraOnCurrentLocation(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()),mZoom);
    }

    private void setCameraOnCurrentLocation(LatLng latLng, int zoom) {
        Log.d(TAG_MAP, "moveCamera: ");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    public void setMapMarkers(List<Restaurant> pRestaurants) {

        BitmapDescriptor lIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_orange);

        Log.d(TAG_GETRESTO, "setMapMarkers: response size" + pRestaurants.size());

        for (Restaurant restaurant : pRestaurants) {

            String lName = restaurant.getRestoName();
            String lAddress = restaurant.getRestoAddress();

            LatLng latLng = new LatLng(restaurant.getRestoLocation().getLat(),
                    restaurant.getRestoLocation().getLng());
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

        try {
            LocationManager locationManager = (LocationManager) this.requireContext().getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, this);
            }
        } catch (SecurityException securityException) {
            Log.d("TAG", "getCurrentLocation: ", securityException);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            getCurrentLocation();
                            saveLocation(task.getResult());
                        } else {
                            Log.w(TAG_MAP, "getLastLocation:exception", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!checkPermissions()) {
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else {
            //getLastLocation();
            //getCurrentLocation();
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
        Log.d(TAG_MAP, "onLocationChanged: ");
        saveLocation(location);
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        mCurrentLocation = location;
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);
        setCameraOnCurrentLocation(latLng, mZoom);
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

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


}
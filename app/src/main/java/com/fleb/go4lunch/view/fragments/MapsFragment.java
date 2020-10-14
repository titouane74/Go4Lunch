package com.fleb.go4lunch.view.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.Restaurant;
import com.fleb.go4lunch.utils.PermissionUtils;
import com.fleb.go4lunch.viewmodel.map.MapViewModel;
import com.fleb.go4lunch.viewmodel.map.MapViewModelFactory;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class MapsFragment extends Fragment implements LocationListener {

    public static final String TAG_MAP = "TAG_MAP";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static final String TAG_GETRESTO = "TAG_GETRESTO";
    private int mZoom;

    private GoogleMap mMap;
    private double mLatitude = 48.8236549;
    private double mLongitude = 2.4102578;
    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocationKnown;
    private LocationManager mLocationManager;
    private MapViewModel mMapViewModel;

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

//            configViewModel(requireContext(), mLatitude, mLongitude);
            configViewModel(requireContext(), mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            setCameraOnCurrentLocation(new LatLng(mCurrentLocation.getLatitude(),  mCurrentLocation.getLongitude()), mZoom);
        }
    };

    public void configViewModel(Context pContext, Double pLatitude, Double pLongitude) {

        MapViewModelFactory lFactory = new MapViewModelFactory(pContext, pLatitude, pLongitude);

        MapViewModel lMapViewModel = new ViewModelProvider(requireActivity(), lFactory).get(MapViewModel.class);
        lMapViewModel.getRestaurantList().observe(getViewLifecycleOwner(), new Observer<List<Restaurant>>() {
            @Override
            public void onChanged(List<Restaurant> pRestaurantList) {
                if (pRestaurantList != null) Log.d(TAG_MAP, "onChanged:getrestaurantlist " + pRestaurantList.size());

                for (Restaurant pRestaurant : pRestaurantList) {

                    lMapViewModel.restaurantExistInFirestore(pRestaurant).observe(getViewLifecycleOwner(),
                            new Observer<Boolean>() {
                                @Override
                                public void onChanged(Boolean pNotExist) {
                                    if (pNotExist) {
                                        lMapViewModel.getGoogleRestaurantDetail(MapsFragment.this.getContext(), pRestaurant)
                                                .observe(getViewLifecycleOwner(), new Observer<Restaurant>() {
                                                    @Override
                                                    public void onChanged(Restaurant pRestaurant) {
                                                        lMapViewModel.saveFirestoreRestaurant(pRestaurant);
                                                    }
                                                });
                                    }
                                }
                            });
                }

//                restaurantExistInFirestore(pRestaurantList);
                setMapMarkers(pRestaurantList);
            }
        });
    }

    public void restaurantExistInFirestore(List<Restaurant> pRestaurantList) {
        if (pRestaurantList != null) {
            Log.d(TAG_MAP, "restaurantExistInFirestore " + pRestaurantList.size());
            for (Restaurant pRestaurant : pRestaurantList) {
                mMapViewModel.restaurantExistInFirestore(pRestaurant).observe(getViewLifecycleOwner(),
                        new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean pNotExist) {
                                if (pNotExist) {
                                    getGoogleRestaurantDetail(pRestaurant);
                                }
                            }
                        });
            }
        }
    }

    public void getGoogleRestaurantDetail(Restaurant pRestaurant) {
        mMapViewModel.getGoogleRestaurantDetail(MapsFragment.this.getContext(), pRestaurant)
                .observe(getViewLifecycleOwner(), new Observer<Restaurant>() {
                    @Override
                    public void onChanged(Restaurant pRestaurant) {
                        mMapViewModel.saveFirestoreRestaurant(pRestaurant);
                    }
                });

    }

/*    public void saveLocation(Location pLocation) {
        mCurrentLocation = pLocation;

        //TODO to active when it's for the emulator
        //configViewModel(requireContext(), mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        //setCameraOnCurrentLocation(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()),mZoom);
    }*/

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

        mZoom = Integer.parseInt(getString(R.string.map_zoom));

        if (!checkPermissions()) {
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
            getLastLocation();
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
            mLocationManager = (LocationManager) this.requireContext().getSystemService(Context.LOCATION_SERVICE);
            if (mLocationManager != null) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 50, this);
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
//                            saveLocation(task.getResult());
                            mCurrentLocation = task.getResult();
                            Log.d(TAG_MAP, "onComplete:currentlocation " + mCurrentLocation.getLatitude() +","+mCurrentLocation.getLongitude());

                        } else {
                            Log.w(TAG_MAP, "getLastLocation:exception", task.getException());
                        }
                    }
                });

        Log.d(TAG_MAP, "getLastLocation: FusedLocationClient hors oncomplete " + mCurrentLocation);

        LocationManager lLocationManager = (LocationManager) this.requireContext().getSystemService(Context.LOCATION_SERVICE);
        if (lLocationManager != null) {
            lLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 50, this);
            mLastLocationKnown = lLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.d(TAG_MAP, "getLastLocation: " + mLatitude + "," + mLongitude);
            mLatitude = mLastLocationKnown != null ? mLastLocationKnown.getLatitude() : mLatitude;
            mLongitude = mLastLocationKnown != null ? mLastLocationKnown.getLongitude() : mLongitude;
            Log.d(TAG_MAP, "getLastLocation: lastknown" + mLatitude + "," + mLongitude);
            String lText = "Initial location" + mLatitude + "," + mLongitude;
            Toast.makeText(getContext(), lText, Toast.LENGTH_SHORT).show();
            mCurrentLocation = mLastLocationKnown;
            lLocationManager.removeUpdates(this);
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onStart() {
        super.onStart();
        if (!checkPermissions()) {
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else {
            mLastLocationKnown = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(this);
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
//        saveLocation(location);
        mCurrentLocation = location;
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        String lText = "New location " + mLatitude + "," + mLongitude;
        Toast.makeText(getContext(), lText, Toast.LENGTH_SHORT).show();
        LatLng latLng = new LatLng(mLatitude, mLongitude);
        setCameraOnCurrentLocation(latLng, mZoom);
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

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

}
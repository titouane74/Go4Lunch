package com.fleb.go4lunch.view.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.fleb.go4lunch.network.JsonRetrofitApi;
import com.fleb.go4lunch.utils.PermissionUtils;
import com.fleb.go4lunch.viewmodel.MapViewModel;
import com.fleb.go4lunch.viewmodel.MapViewModelFactory;
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

import java.util.List;
import java.util.Objects;

public class MapsFragment extends Fragment implements LocationListener {

    public static final String TAG_MAP = "TAG_MAP";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static final String TAG_GETRESTO = "TAG_GETRESTO";

    LocationManager mLocationManager;
    private GoogleMap mMap;
    double mLatitude,  mLongitude ;
    Location mLastLocation;
    Marker mCurrLocationMarker;

    private JsonRetrofitApi mJsonRetrofitApi;
    private String mKey ;
    private int mProximityRadius;


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

            enableMyLocation(googleMap);
            getCurrentLocation();

            mLatitude = 48.8236549;
            mLongitude = 2.4102578;

            MapViewModelFactory lFactory = new MapViewModelFactory(lContext,mLatitude, mLongitude);

            MapViewModel lMapViewModel = new ViewModelProvider(requireActivity(),lFactory).get(MapViewModel.class);
            lMapViewModel.getRestoList().observe(getViewLifecycleOwner(),pRestaurants -> {
                setMapMarkers(pRestaurants);
            });

            LatLng lMyPosition = new LatLng(mLatitude, mLongitude);
            Log.d(TAG_MAP, "onMapReady: " + mLatitude + " : " + mLongitude);
            googleMap.addMarker(new MarkerOptions().position(lMyPosition).title("Charenton Le Pont Maison"));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lMyPosition, 16));

        }
    };

    public void setMapMarkers(List<Restaurant> pRestaurants) {

        BitmapDescriptor lIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_orange);

        Log.d(TAG_GETRESTO, "setMapMarkers: response size" + pRestaurants.size());

        for (int i = 0; i < pRestaurants.size(); i++) {

            String lName = pRestaurants.get(i).getRestoName();
            String lAddress = pRestaurants.get(i).getRestoAddress();

            LatLng latLng = new LatLng(pRestaurants.get(i).getRestoLat(), pRestaurants.get(i).getRestoLng());
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(lName + " : " + lAddress)
                    .icon(lIcon));
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View lView = inflater.inflate(R.layout.fragment_maps, container, false);

        mKey = BuildConfig.MAPS_API_KEY;
        mProximityRadius = Integer.parseInt(requireContext().getResources().getString(R.string.proximity_radius));
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

    private void enableMyLocation(GoogleMap pMap) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (pMap != null) {
                pMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }

    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {
        try {
            mLocationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
            Objects.requireNonNull(mLocationManager).requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, MapsFragment.this);
        } catch (Exception pE) {
            pE.printStackTrace();
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
        Toast.makeText(getContext(), "Lat: " + location.getLatitude() + ", Long: "
                + location.getLongitude(), Toast.LENGTH_SHORT).show();

        Log.d("onLocationChanged", "entered");

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

        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", mLatitude, mLongitude));

        Log.d("onLocationChanged", "Exit");
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

}
package com.fleb.go4lunch.view.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.fleb.go4lunch.utils.PermissionUtils;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsFragment extends Fragment implements LocationListener {

    public static final String TAG_MAP = "TAG_MAP";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private int mZoom;

    private GoogleMap mMap;
    private double mLatitude;
    private double mLongitude;
    private LatLng mLatLng;
    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager mLocationManager;
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

        }
    };

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
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), ACCESS_FINE_LOCATION)) {
                // Display a dialog with rationale.
                PermissionUtils.RationaleDialog.newInstance(PERMISSION_REQUEST_CODE, true)
                        .show(requireActivity().getSupportFragmentManager(), "dialog");
            } else {
                // Location permission has not been granted yet, request it.
                ActivityCompat.requestPermissions(requireActivity(), new String[]{ACCESS_FINE_LOCATION},
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

    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {

        Task<Location> task = mFusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    saveLocation(location);
                }
            }
        });

        mLocationManager = (LocationManager) this.requireContext().getSystemService(Context.LOCATION_SERVICE);
        if (mLocationManager != null) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 50, this);
        }
    }

    public void saveLocation(Location pLocation) {
        mCurrentLocation = pLocation;
        mLongitude = pLocation.getLongitude();
        mLatitude = pLocation.getLatitude();
        mLatLng = new LatLng(pLocation.getLatitude(), pLocation.getLongitude());

        setCameraOnCurrentLocation(new LatLng(pLocation.getLatitude(), pLocation.getLongitude()), mZoom);
    }

    private void setCameraOnCurrentLocation(LatLng latLng, int zoom) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
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
        int lPermissionLocation = ActivityCompat.checkSelfPermission(requireActivity(), ACCESS_FINE_LOCATION);

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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            //IF PERMISSION GRANTED
            //grantResults[0] -> Permission for the location
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel("You need to allow access the permission",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                                                    PERMISSION_REQUEST_CODE);
                                        }
                                    });

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

}
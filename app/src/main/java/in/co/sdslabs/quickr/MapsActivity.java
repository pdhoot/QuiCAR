package in.co.sdslabs.quickr;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.*;
import com.google.android.gms.maps.model.MarkerOptions;
import com.quinny898.library.persistentsearch.SearchBox;

import org.json.*;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private SearchBox search;
    private String url = " ";
    private boolean mapCameraMovedForCurrentLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        search = (SearchBox) findViewById(R.id.searchbox);
        SearchRequest searchRequest = new SearchRequest(search,
                new SearchResponse.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // GEt the result
                    }
                });

//        Log.d("response" , x.toString());

//        SearchResult searchable = new SearchResult("title", getResources().getDrawable(R.id.cast_notification_id));
//        ArrayList<SearchResult> searchables = new ArrayList<>();
//        searchables.add(searchable);
//        search.setSearchables(searchables);
        AdsCollection collection = new AdsCollection();
        JSONObject obj = new JSONObject(response);
        try {
            JSONArray ads = obj.getJSONArray("ads");
            for (int i = 0; i < ads.length(); i++) {
                Ads ad = new Ads(ads.getJSONObject(i));
                collection.adList.add(ad);
            }
        }
        catch(JSONException  e) {}

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpClustering() {
        // Declare a variable for the cluster manager.
        ClusterManager<MyItem> mClusterManager;

        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyItem>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {

                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                for (MyItem markerItem: cluster.getItems()) {
                    builder.include(markerItem.getPosition());
                }

                LatLngBounds bounds = builder.build();

                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;

                int padding = ((width * 10) / 100); // offset from edges of the map
                // in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,
                        padding);
                mMap.animateCamera(cu);

                return true;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem marker) {
                Log.d("Latitude", Double.toString(marker.getPosition().latitude));
                Log.d("Longitude", Double.toString(marker.getPosition().longitude));
                return true;
            }

        });

        // Set some lat/lng coordinates to start with.
        double lat = 0;
        double lng = 0;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            MyItem offsetItem = new MyItem(lat, lng);
            mClusterManager.addItem(offsetItem);
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        setUpClustering();

        mMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        String locationProvider = LocationManager.GPS_PROVIDER;

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                onLocationChanged(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };


        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if(lastKnownLocation != null) {
            onLocationChanged(lastKnownLocation);
        }

        Log.d("Location", "Unable");

        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-18.142, 178.431), 2));
    }

    private void onLocationChanged(Location location) {
        if(!mapCameraMovedForCurrentLocation) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            LatLng latLng = new LatLng(latitude, longitude);

            Log.d("Latitude", Double.toString(latitude));
            Log.d("Longitude", Double.toString(longitude));

            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6));

            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

            mapCameraMovedForCurrentLocation = !mapCameraMovedForCurrentLocation;
        }
    }

}

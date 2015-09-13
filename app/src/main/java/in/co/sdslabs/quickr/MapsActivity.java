package in.co.sdslabs.quickr;

import android.Manifest;
import android.app.ActionBar;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.ads.AdSize;
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
import com.quinny898.library.persistentsearch.SearchResult;

import org.json.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MapsActivity extends FragmentActivity{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private SearchBox search;
    private String url = " ";
    private final String allAdsURL = "http://vps.rkravi.com:8000/getAds";
    private ClusterManager<MyItem> mClusterManager;
    private boolean mapCameraMovedForCurrentLocation = false;
    private AdsCollection collection = new AdsCollection();
    private SlidingUpPanelLayout slidingPanelLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        search = (SearchBox) findViewById(R.id.searchbox);
        search.setLogoText("Search");

        slidingPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        //slidingPanelLayout.setVisibility(View.INVISIBLE);
        slidingPanelLayout.setPanelHeight(0);

        SearchRequest searchRequest = new SearchRequest(search,
                new SearchResponse.Listener<AdsCollection>() {
                    @Override
                    public void onResponse(AdsCollection ads) {
                        collection = ads;

                        if(mClusterManager!=null) {
                            Map<MyItem, Ads> m = ads.getMarkerAdMapping();

                            Log.d("Collection Size", Integer.toString(m.size()));

                            // clear all existing markers
                            mClusterManager.clearItems();

                            for(Map.Entry<MyItem , Ads> entry : m.entrySet() )
                            {
                                mClusterManager.addItem(entry.getKey());
                            }

                            mMap.animateCamera(CameraUpdateFactory.zoomTo(mMap.getCameraPosition().zoom - 1), 2000, null);
                        }

                    }
                }, slidingPanelLayout);

        ArrayList<SearchResult> searchables = new ArrayList<>();

        for(int i = 0 ; i<SearchItems.brand_name.length ; i++)
        {
            SearchResult searchable = new SearchResult(SearchItems.brand_name[i] , null);
            searchables.add(searchable);
        }

        for(int i = 0 ; i<SearchItems.model_name.length ; i++)
        {
            SearchResult searchable = new SearchResult(SearchItems.model_name[i] , null);
            searchables.add(searchable);
        }

        search.setSearchables(searchables);

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

                int markerCount = cluster.getSize();

                Random rand = new Random();

                int randomMarkerIndex = rand.nextInt(markerCount);

                int index = 0;

                for (MyItem markerItem : cluster.getItems()) {

                    if (index == randomMarkerIndex) {
                        // TODO: uncomment this @nightfury
                        populateTable(markerItem);
                    }

                    builder.include(markerItem.getPosition());
                    index++;
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

                //slidingPanelLayout.setVisibility(View.VISIBLE);
                slidingPanelLayout.setPanelHeight(210);
                populateTable(marker);

                try {

                    Ads ad = collection.getMarkerAdMapping().get(marker);
                    url = ad.getImageUrl();

                    ImageRequest request = new ImageRequest(url,
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap bitmap) {
                                    ImageView mImageView;
                                    mImageView = (ImageView) findViewById(R.id.imageView);
                                    mImageView.setImageBitmap(bitmap);
                                }
                            }, 0, 0, null,
                            new Response.ErrorListener() {
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                    // Access the RequestQueue through your singleton class.
                    VolleySingleton vol = VolleySingleton.getInstance();
                    RequestQueue requestQue = vol.getRequestQueue();
                    requestQue.add(request);
                } catch (Exception e) {
                    System.out.println("error");
                }

                return true;
            }

        });

        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, allAdsURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AdsCollection coll = new AdsCollection();

                        //perform the result
                        try {
                            JSONArray ads = response.getJSONArray("ads");

                            for (int i = 0; i < ads.length(); i++) {
                                Ads ad = new Ads(ads.getJSONObject(i));

                                if(!ad.hasGeoCoord()) {
                                    continue;
                                }

                                double lat = ad.getLatitude();
                                double lng = ad.getLongitude();
                                MyItem offsetItem = new MyItem(lat, lng);

                                coll.addMappedItem(offsetItem, ad);
                            }

                            collection = coll;

                            if(mClusterManager!=null) {
                                Map<MyItem , Ads> m = coll.getMarkerAdMapping();

                                Log.d("Collection Size", Integer.toString(m.size()));

                                // clear all existing markers
                                mClusterManager.clearItems();

                                for(Map.Entry<MyItem , Ads> entry : m.entrySet() )
                                {
                                    mClusterManager.addItem(entry.getKey());
                                }

                                mMap.animateCamera(CameraUpdateFactory.zoomTo(mMap.getCameraPosition().zoom - 1), 2000, null);
                            }
                        }
                        catch(JSONException e) {}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //show error
            }

        });

        requestQueue.add(request);

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
                onLocationsChanged(location);
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
            onLocationsChanged(lastKnownLocation);
        }

        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
    }

    private void onLocationsChanged(Location location) {
        if(!mapCameraMovedForCurrentLocation) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            LatLng latLng = new LatLng(latitude, longitude);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);

            mapCameraMovedForCurrentLocation = !mapCameraMovedForCurrentLocation;
        }
    }

    private void findAllInRadius(AdsCollection collection, Location location)
    {
        Location center = location;
        for (MyItem myItem : collection.getMarkerAdMapping().keySet()) {
            LatLng target = myItem.getPosition() ;
            Location loc2 = new Location(LocationManager.GPS_PROVIDER);
            loc2.setLatitude(target.latitude);
            loc2.setLongitude(target.longitude);
            Float distance = center.distanceTo(loc2);
            Log.d("pdhoot", distance.toString());
        }
    }

    private void populateTable(MyItem marker)
    {
        slidingPanelLayout.setVisibility(View.VISIBLE);

        Ads ad = collection.getMarkerAdMapping().get(marker);

        String attributes[] = ad.getAttributeArray();

        TextView titleTextView = (TextView) findViewById(R.id.title);
        TextView yearTextView = (TextView) findViewById(R.id.year);
        TextView priceTextView = (TextView) findViewById(R.id.price);
        TableLayout details = (TableLayout) findViewById(R.id.details);
        details.removeAllViews();
        details.setStretchAllColumns(true);
        details.bringToFront();

        for (String attribute:attributes) {


            TableRow tr = new TableRow(this);
            tr.setPadding(5, 5, 5, 5);
            TextView c1 = new TextView(this);
            c1.setText(ad.getHumanReadableText(attribute));
            c1.setTextSize(18);
            c1.setTextColor(Color.BLACK);
            tr.addView(c1);
            TextView c2 = new TextView(this);
            try {
                Log.d(ad.getHumanReadableText(attribute), ad.getAttributeValue(attribute));
                c2.setText(ad.getAttributeValue(attribute));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            c2.setTextSize(18);
            c2.setTextColor(Color.BLACK);
            tr.addView(c2);
            details.addView(tr);

        }

        try {
            titleTextView.setText(ad.getTitle());
            yearTextView.setText(ad.getYear());
            priceTextView.setText(ad.getPrice());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

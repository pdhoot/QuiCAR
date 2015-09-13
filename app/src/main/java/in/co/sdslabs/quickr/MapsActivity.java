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
import android.support.v7.app.AppCompatActivity;
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
import com.quinny898.library.persistentsearch.SearchResult;

import org.json.*;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity{

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
        String brand_name[] = { "Ashok Leyland", "Aston Martin", "Audi", "Bentley", "BMW", "Bugatti", "Caterham",
                "Chevrolet", "Conquest", "Daewoo", "Datsun", "DC", "Ferrari", "Fiat", "Force One", "Ford", "Hindustan Motors",
                "Honda", "Hyundai", "ICML", "Isuzu", "Jaguar", "Koenigsegg", "Lamborghini", "Land Rover", "Mahindra", "Maruti Suzuki",
                "Maserati", "Mercedes Benz", "Mini", "Mitsubishi", "Nissan", "Opel", "Others","Porsche", "Premier",
                "Renault", "Rolls Royce", "San Motors", "Skoda", "Tata", "Toyota", "Volkswagen", "Volvo" };
        String model_name[] =  { "7", "1 Series", "3 Door", "3 Series", "370Z", "458 Italia", "458 Speciale", "458 Spider", "5 Door",
               "5 Series", "599 GTB Fiorano", "6 Series", "7 Series", "911 Convertible", "911 Coupe", "A Class", "A Star", "A3",
               "A3 cabriolet", "A4", "A6", "A7", "A8", "A8 L", "Accent", "Accord", "Agera", "Altis", "Alto", "Alto 800", "Alto K10",
               "Amaze", "Ambassador", "Aria", "Armada", "Arnage", "Astra", "Avanti", "Aventador Convertible", "Aventador Coupe", "Aveo",
               "Aveo U-VA", "Avventura", "B Class", "Baleno", "Beat", "Beetle", "Bolero", "Bolt", "Boxster", "Brio",
               "C Class", "California", "Camry", "Captiva", "Cayenne", "Cayman", "Cedia", "Celerio", "Celica", "Ciaz", "Cielo",
               "City", "City ZX", "Civic", "CL Class", "CL Range", "CLA Class", "Classic", "CLK", "CLS", "CLS Class",
               "Contessa", "Continental", "Cooper", "Cooper Convertible", "Corolla", "Corolla Altis", "Corsa",
               "Coupe GT", "Cross Polo", "Cruze", "CRV", "Datsun Go", "DB9", "DBS", "Discover", "Discovery 3", "Discovery 4", "Duster",
               "E Class", "e20", "e2o", "Ecosport", "Eeco", "Elantra", "Elite i20", "Endeavor", "Enjoy", "Eon", "Ertiga", "Estate",
               "Esteem", "Etios", "Etios Cross", "Etios Liva", "Evade", "Evalia", "Extreme", "F Type", "F12 Berlinetta", "F50 Spider",
               "Fabia", "FF", "Fiesta", "Fiesta Classic", "Figo", "Fluence", "Flying Spur", "Force Once EX", "Force Once LX",
               "Fortuner", "Freelander 2", "Fusion", "G Class", "Gallardo", "Getz", "Getz Prime", "Ghost", "GL Class", "GLA Class",
               "GO", "GO Plus", "Gran Cabrio", "Gran Turismo", "Grand i10", "Grand Vitara", "Grande Punto", "Gurkha", "Gypsy",
               "Huracan", "i10", "i20", "i20 Active", "i8", "Ikon", "Indica", "Indica eV2", "Indica V2", "Indica V2 Turbo",
               "Indica V2 Xeta", "Indica Vista", "Indica Xeta", "Indigo", "Indigo CS", "Indigo ECS", "Indigo Marina", "Indigo V Series",
               "Indigo XL", "Innova", "Jazz", "Jeep", "Jetta", "Kizashi", "Koleos", "L & K", "Lancer", "Lancer Evolution X",
               "Land Cruiser", "Laura", "Linea", "Linea Classic", "Lodgy", "Logan", "Logan Edge", "M Class", "M Series",
               "Macan", "Manza", "Marshal DI", "Maruti 1000", "Maruti 800", "Matiz", "Maxx", "Micra", "Micra Active", "Mini Cooper",
               "Mini Cooper Countryman", "MM Range", "Mobilio", "Montero", "Movus", "MU 7", "Mulsanne", "Murcielago", "Nano",
               "Neo Fluidic Elantra", "New City", "New Ikon", "New Yeti", "Nexia", "Octavia", "Omni", "One 77", "One SUV", "Optra",
               "Optra Magnum", "Outlander", "Pajero", "Palio", "Palio Stile", "Palio Stile Multijet", "Panamera", "Passat", "Petra",
               "Phaeton", "Phantom Convertible", "Phantom Coupe", "Phantom Sedan", "Polo", "Prado", "Prius", "Pulse", "Punto", "Punto Adventure",
               "Punto EVO", "Q3", "Q5", "Q7", "Qualis", "Quanto", "Quattroporte", "R Class", "R8", "Range Rover", "Range Rover Evoque",
               "Range Rover Sport", "Rapid", "Rapide", "Reva", "Rexton", "Rhino Rx", "Rio", "Ritz", "RS5", "RS7", "S Class", "S4", "S40", "S6",
               "S60", "S80", "Safari", "Safari Dicor", "Safari Storme", "Sail", "Sail U-VA", "Santa Fe", "Santro", "Santro Xing", "Scala",
               "Scorpio", "Scorpio W", "Siena", "SL Class", "SLK Class", "SLS", "SLS AMG", "Sonata", "Sonata Embera", "Sonata Transform",
               "Spark", "Spark LPG", "SRV", "Stile", "Storm", "Sumo", "Sumo Grande", "Sumo Spacio", "Sumo Victa", "Sunny", "Superb", "Swift",
               "Swift Dzire", "SX4", "Tavera", "Tavera Neo", "Teana", "Terrano", "Thar", "Touareg", "TT", "Tucson", "Uno", "V40 Cross Country",
               "Vanquish", "Vantage", "Vectra", "Vento", "Venture", "Verito", "Verito Vibe", "Verna", "Verna Fluidic", "Versa", "Veyron", "Virage",
               "Vista", "Voyager", "Wagon R", "Wagon R Duo", "Wagon R Stingray", "Winger", "Wraith", "X-Trail", "X1", "X3", "X5", "X6", "XC60",
               "XC90", "Xcent", "Xenon XT", "XF", "XJ", "XK", "XUV 500", "Xylo", "Yeti", "Z4 Convertible", "Z4 Coupe", "Zen", "Zen Estilo", "Zest" };
        ArrayList<SearchResult> searchables = new ArrayList<>();
        for(int i = 0 ; i<brand_name.length ; i++)
        {
            SearchResult searchable = new SearchResult(brand_name[i] , null);
            searchables.add(searchable);
        }

        for(int i = 0 ; i<model_name.length ; i++)
        {
            SearchResult searchable = new SearchResult(model_name[i] , null);
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

        Log.d("Location", "Unable");

        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-18.142, 178.431), 2));
    }

    private void onLocationsChanged(Location location) {
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

    private void findAllInRadius(Location location)
    {
        Location center = location;
        for (MyItem myItem : AdsCollection.adMap.keySet()) {
            LatLng target = myItem.getPosition() ;
            Location loc2 = new Location(LocationManager.GPS_PROVIDER);
            loc2.setLatitude(target.latitude);
            loc2.setLongitude(target.longitude);
            Float distance = center.distanceTo(loc2);
            Log.d("pdhoot", distance.toString());
        }
    }

}

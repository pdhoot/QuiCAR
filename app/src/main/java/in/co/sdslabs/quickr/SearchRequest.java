package in.co.sdslabs.quickr;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.quinny898.library.persistentsearch.SearchBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by arpit on 12/9/15.
 */
public class SearchRequest {

    public SearchRequest(SearchBox search, SearchResponse.Listener<String> listener) {

        search.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                Log.d("search opened", "yeah");
            }


            @Override
            public void onSearchTermChanged() {
                //React to the search term changing
                //Called after it has updated results
                Log.d("search term changed", "yeah");

            }

            @Override
            public void onSearch(String searchTerm) {
                //Call sendGETRequest with url and the search term
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
                        "Vista", "Voyager", "Wagon R", "Wagon R Duo", "Wagon R Stingray", "Winger", "Wraith", "X-Trail", "X1", "X3" , "X5", "X6", "XC60",
                        "XC90", "Xcent", "Xenon XT", "XF", "XJ", "XK", "XUV 500", "Xylo", "Yeti", "Z4 Convertible", "Z4 Coupe", "Zen", "Zen Estilo", "Zest" };
                String url = "";
                for(int i = 0 ;i<brand_name.length ; i++)
                {
                    if(searchTerm.toLowerCase().equals(brand_name[i].toLowerCase()))
                    {
                        url = "http://vps.rkravi.com:8000/getAds?brand_name=" + searchTerm;
                        break;
                    }
                }
                if(!url.equals(""))
                {
                    for(int i = 0 ;i<model_name.length ; i++)
                    {
                        if(searchTerm.toLowerCase().equals(brand_name[i].toLowerCase()))
                        {
                            url = "http://vps.rkravi.com:8000/getAds?model=" + searchTerm;
                            break;
                        }
                    }
                }

                sendGETRequest(url);
                Log.d("onsearch", "yeah");

            }

            @Override
            public void onSearchClosed() {
                Log.d("search closed", "yeah");

                //Use this to un-tint the screen
            }

            @Override
            public void onSearchCleared() {
                Log.d("search cleared", "yeah");

            }
        });
    }

    // Modify this function according to backend to fetch ads for given brand name or car name
    private void sendGETRequest(String url) {
        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //perform the result
                        AdsCollection collection = new AdsCollection();
                        try {
                            JSONArray ads = response.getJSONArray("ads");
                            for (int i = 0; i < ads.length(); i++) {
                                Ads ad = new Ads(ads.getJSONObject(i));
                                double lat = ad.getLatitude();
                                double lng = ad.getLongitude();
                                MyItem offsetItem = new MyItem(lat, lng);
                                //mclusetermanager.additem(offsetitem) to be implemented
                                collection.adMap.put(offsetItem , ad);
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
}

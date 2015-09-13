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
                String url = "vps.rkravi.com:8000/getAds?model=" + searchTerm;
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

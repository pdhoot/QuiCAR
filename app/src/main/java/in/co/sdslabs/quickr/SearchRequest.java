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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by arpit on 12/9/15.
 */
public class SearchRequest {
    private SearchResponse.Listener<AdsCollection> listener;

    public SearchRequest(final SearchBox search, SearchResponse.Listener<AdsCollection> listener) {
        this.listener = listener;

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
                String url = "";

                for(int i = 0 ;i<SearchItems.brand_name.length ; i++)
                {
                    if(searchTerm.toLowerCase().equals(SearchItems.brand_name[i].toLowerCase()))
                    {
                        try{
                            url = "http://vps.rkravi.com:8000/getAds?brand_name=" + URLEncoder.encode(searchTerm, "UTF-8");
                        } catch (UnsupportedEncodingException exp) {
                            Log.d("Exception", exp.getMessage());
                        }
                        break;
                    }
                }
                if(url.equals(""))
                {
                    for(int i = 0 ;i<SearchItems.model_name.length ; i++)
                    {
                        Log.d("Checking", SearchItems.model_name[i]);
                        if(searchTerm.toLowerCase().equals(SearchItems.model_name[i].toLowerCase()))
                        {
                            try{
                                url = "http://vps.rkravi.com:8000/getAds?model=" + URLEncoder.encode(searchTerm, "UTF-8");
                            } catch (UnsupportedEncodingException exp) {
                                Log.d("Exception", exp.getMessage());
                            }
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

        Log.d("Request URL", url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        AdsCollection collection = new AdsCollection();

                        Log.d("JSON Response", response.toString());

                        //perform the result
                        try {
                            JSONArray ads = response.getJSONArray("ads");
                            Log.d("Ads count", Integer.toString(ads.length()));
                            for (int i = 0; i < ads.length(); i++) {
                                Ads ad = new Ads(ads.getJSONObject(i));

                                if(!ad.hasGeoCoord()) {
                                    continue;
                                }

                                double lat = ad.getLatitude();
                                double lng = ad.getLongitude();
                                MyItem offsetItem = new MyItem(lat, lng);

                                Log.d("Lat", Double.toString(lat));

                                collection.addMappedItem(offsetItem, ad);
                            }

                            listener.onResponse(collection);
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

package in.co.sdslabs.quickr;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.quinny898.library.persistentsearch.SearchBox;

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

    private void sendGETRequest(String url) {
        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //perform the result
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

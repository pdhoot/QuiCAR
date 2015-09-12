package in.co.sdslabs.quickr;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by arpit on 12/9/15.
 */
public class HTTPRequestHelper extends Activity{
    private static String result = "" ;
    private static JSONArray jsonResult ;
    private static Map<String, String> mparams ;

    public static String sendGETRequest(String url, Map<String, String> params ) {

        mparams = params ;

        url = BuildUrl(url, params);

        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        result = response ;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                result = "Error in getting response" ;
            }
        });

        requestQueue.add(request);
        return result ;
    }

    public static String sendGETRequest(String url) {

        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        result = response ;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                result = "Error in getting response" ;
            }
        });

        requestQueue.add(request);
        return result ;

    }

    public static String sendPOSTRequest(String url, Map<String, String> params) {

        mparams = params ;

        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        result = response ;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                result = "error" ;
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return mparams ;
            }
        };

        requestQueue.add(request);

        return result;

    }


    public static JSONArray getJSONResponse(String url, Map<String, String> params ) {

        mparams = params ;

        url = BuildUrl(url, params);

        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        jsonResult = response ;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                jsonResult = null;
            }

        });

        requestQueue.add(request);

        return jsonResult ;

    }



    private static String BuildUrl(String url, Map<String, String> params) {


        StringBuilder builder = new StringBuilder();

        for (String key : params.keySet())
        {
            Object value = params.get(key);
            if (value != null)
            {
                if (builder.length() > 0)
                    builder.append("&");
                builder.append(key).append("=").append(value);
            }
        }

        url += "?" + builder.toString();

        return url ;
    }

}

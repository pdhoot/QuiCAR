package in.co.sdslabs.quickr;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by arpit on 12/9/15.
 */
public class VolleySingleton {

    private static VolleySingleton sInstance = null;

    private RequestQueue mRequestQueue = null;

    private VolleySingleton(){
        mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext()) ;
    }

    public static VolleySingleton getInstance() {
        if (sInstance == null)
        {
            sInstance = new VolleySingleton() ;
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue ;
    }


}

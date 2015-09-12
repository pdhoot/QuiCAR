package in.co.sdslabs.quickr;

import android.app.Application;
import android.content.Context;

/**
 * Created by arpit on 12/9/15.
 */
public class MyApplication extends Application {

    private static MyApplication sInstance;

    @Override

    public void onCreate() {

        super.onCreate();

        sInstance = this;
    }

    public static MyApplication getsInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}

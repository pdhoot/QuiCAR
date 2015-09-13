package in.co.sdslabs.quickr;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.*;

/**
 * Created by vampire on 13/9/15.
 */
public class MyItem implements ClusterItem {
    private final LatLng mPosition;

    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public LatLng getPosition() {
        return mPosition;
    }
}

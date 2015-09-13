package in.co.sdslabs.quickr;
import java.util.*;

/**
 * Created by pdh1596 on 13/9/15.
 */
public class AdsCollection {
    private Map<MyItem, Ads> adMap;

    public AdsCollection() {
        adMap = new HashMap<MyItem , Ads>();
    }

    public void addMappedItem(MyItem markerItem, Ads ad) {
        adMap.put(markerItem, ad);
    }

    public Map<MyItem, Ads> getMarkerAdMapping() {
        return adMap;
    }

}

package in.co.sdslabs.quickr;
import org.json.*;

/**
 * Created by arpit on 13/9/15.
 */
public class Ads {
    private JSONObject jsonObject;
    public Ads(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getLocality() throws JSONException
    {
        return jsonObject.getString("ad_locality");
    }

    public String getTitle() throws JSONException
    {
        return jsonObject.getString("title");
    }

    public String getKmsDriven() throws JSONException
    {
        return jsonObject.getString("kms_Driven");
    }

    public String getPrice() throws JSONException
    {
        return jsonObject.getString("attribute_price");
    }

    public String getYear() throws JSONException
    {
        return jsonObject.getString("year");
    }

    public String getStateName() throws JSONException
    {
        return jsonObject.getString("stateName");
    }

    public JSONArray getImages() throws JSONException
    {
        return jsonObject.getJSONArray("images");
    }

    public String getFuelType() throws JSONException
    {
        return jsonObject.getString("attribute_Fuel_Type");
    }

    public double getLatitude() throws JSONException
    {
        String latlon = jsonObject.getString("geo_pin");
        String coords[] = latlon.split(",");
        double lat = Double.parseDouble(coords[0]);
        return lat;
    }

    public double getLongitude() throws JSONException
    {
        String latlon = jsonObject.getString("geo_pin");
        String coords[] = latlon.split(",");
        double lon = Double.parseDouble(coords[1]);
        return lon;
    }

    public boolean hasGeoCoord()
    {
        try {
            jsonObject.getString("geo_pin");
            return true;
        } catch (JSONException ex) {
            return false;
        }
    }

}

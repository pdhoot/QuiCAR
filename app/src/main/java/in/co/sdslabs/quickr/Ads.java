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

}

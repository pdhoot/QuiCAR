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

    public String getLocality()
    {
        return jsonObject.getString("ad_locality");
    }

    public String getTitle()
    {
        return jsonObject.getString("title");
    }

    public String getKmsDriven()
    {
        return jsonObject.getString("kms_Driven");
    }

    public String getPrice()
    {
        return jsonObject.getString("attribute_price");
    }

    public String getYear()
    {
        return jsonObject.getString("year");
    }

    public String getStateName()
    {
        return jsonObject.getString("stateName");
    }

    public JSONArray getImages()
    {
        return jsonObject.getJSONArray("images");
    }

    public String getFuelType()
    {
        return jsonObject.getString("attribute_Fuel_Type");
    }
    
}

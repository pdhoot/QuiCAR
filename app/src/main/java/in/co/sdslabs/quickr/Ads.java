package in.co.sdslabs.quickr;
import android.util.Log;

import org.json.*;

/**
 * Created by arpit on 13/9/15.
 */
public class Ads {
    private JSONObject jsonObject;
    private final String possibleKeys[] = {"attribute_Condition", "attribute_Color", "ad_quality_score", "attribute_Fuel_Type", "year", "ad_locality", "attribute_No_of_owners"};
    private final String humanReadAbleText[] = {"Condition", "Color", "Ad Quality Score", "Fuel Type", "Year", "Ad Locality", "No Of Owners"};

    public Ads(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String[] getAttributeArray() {
        return possibleKeys;
    }

    public String getAttributeValue(String attribute) throws JSONException{
        return jsonObject.getString(attribute);
    }

    public String getHumanReadableText(String attribute) {
        int index = -1;
        for (int i=0;i<possibleKeys.length;i++) {
            if (possibleKeys[i].equals(attribute)) {
                index = i;
                break;
            }
        }

        return humanReadAbleText[index];
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
        return jsonObject.getString("attribute_Price");
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

    public String getImageUrl() throws JSONException
    {
        String url = "http://orig11.deviantart.net/0f16/f/2012/283/f/5/car_vector_by_vectorportal-d5hda7s.jpg";

        int images = Integer.parseInt(jsonObject.getString("image_count"));

        if(images == 1) {
            url = jsonObject.getString("images");
        } else if(images > 1) {
            Log.d("IMAGES_ARRAY", jsonObject.getJSONArray("images").toString());

            url = jsonObject.getJSONArray("images").getString(0);

            Log.d("bbbc",url);
        }

        return url;
    }

}

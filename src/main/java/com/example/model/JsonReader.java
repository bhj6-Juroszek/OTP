package com.example.model;

import com.example.daoLayer.daos.CitiesDAO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;

/**
 * Created by Bartek on 2017-05-03.
 */
public class JsonReader {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public double distance(Place place1, Place place2 )
    {
        double lat1=place1.getLat();
        double lon1=place1.getLng();
        double lat2=place2.getLat();
        double lon2=place2.getLng();
        double RADIUS = 6378.16;
        double dlon = Radians(lon2 - lon1);
        double dlat = Radians(lat2 - lat1);

        double a = (Math.sin(dlat / 2) * Math.sin(dlat / 2)) + Math.cos(Radians(lat1)) * Math.cos(Radians(lat2)) * (Math.sin(dlon / 2) * Math.sin(dlon / 2));
        double angle = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (angle * RADIUS);
    }
    public static double Radians(double x)
    {
        double PIx = 3.141592653589793;
        return x * PIx / 180;
    }

    public static Place getCity(String url) throws  JSONException {
        url=String.format("http://maps.google.com/maps/api/geocode/json?address=%s&sensor=false&region=pl",url);
        CitiesDAO dao=CitiesDAO.getInstance();
        InputStream is=null;
        Place result=new Place();
        try {
             is= new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            JSONArray results = (JSONArray) json.get("results");
            JSONObject object=(JSONObject)results.getJSONObject(0);
            JSONArray array=object.getJSONArray("address_components");
            result.setName(array.getJSONObject(0).getString("long_name"));
            object=object.getJSONObject("geometry");
            object=object.getJSONObject("location");
            result.setLat((object.getDouble("lat")));
            result.setLng((object.getDouble("lng")));
            }catch(Exception ex)
        {
            return null;
        } finally {
            if(is!=null)
            {
                try {
                    is.close();
                } catch (IOException e) {
                    return null;
                }
            }

        }
        return result;
    }


}

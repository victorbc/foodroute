package br.edu.ufcg.empsoft.foodroute.utilis;

import android.os.AsyncTask;
import android.text.Html;
import android.util.JsonReader;
import android.util.Log;

/*import com.moolajoo.joao.popularmoviesstage2.BuildConfig;
import com.moolajoo.joao.popularmoviesstage2.Constants;
import com.moolajoo.joao.popularmoviesstage2.MovieDetail_Activity;
import com.moolajoo.joao.popularmoviesstage2.MovieFragment;*/

import org.json.JSONException;
import org.json.JSONObject;
import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class API {

    public static String request(String path) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String jsonStr = null;
        try {
            ///String path = "https://maps.googleapis.com/maps/api/directions/json?origin=-7.217017,%20-35.908118&destination=-7.217017,%20-35.908118&waypoints=optimize:true%7C-7.236044,%20-35.896738%7C-7.220680,%20-35.888814%7C-7.223283,%20-35.876996%7C-7.213034,%20-35.883710";

            URL url = new URL(path);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            jsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e("LOG_TAG", "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("LOG_TAG", "Error closing stream", e);
                }
            }
        }

        return jsonStr;
    }

    private static String gpsCoordinateToURL(ArrayList<Coordinate> coordinates) {
        String url = "";
        if (coordinates.size() >= 1) {
            url = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                    coordinates.get(0).toString() +
                    "&destination=" +
                    coordinates.get(0).toString() +
                    "&waypoints=optimize:true";
        }

        for (int i = 1; i < coordinates.size(); i++) {
            url += "|" + coordinates.get(i).toString();
        }
        return url;
    }

    public static void main(String[] args) {
        try{

            ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
            coords.add(new Coordinate(-7.217017,-35.908118));
            coords.add(new Coordinate(-7.236044,-35.896738));
            coords.add(new Coordinate(-7.220680,-35.888814));
            coords.add(new Coordinate(-7.223283,-35.876996));
            coords.add(new Coordinate(-7.213034,-35.883710));

            String URLPath = gpsCoordinateToURL(coords);

            System.out.println(URLPath);

            System.out.println(request(URLPath));
        } catch (Exception e) {
            Log.e("LOG", e.getMessage());
        }
    }
}
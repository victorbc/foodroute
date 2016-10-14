package br.edu.ufcg.empsoft.foodroute.utilis;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;

public class API {
    private static String request(String path) {
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

    private static String locationsToURL(ArrayList<String> locations) {
        String url = "";
        if (locations.size() >= 1) {
            url = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                    locations.get(0).replace(" ", "+") +
                    "&destination=" +
                    locations.get(0).replace(" ", "+") +
                    "&waypoints=optimize:true";
        }

        for (int i = 1; i < locations.size(); i++) {
            url += "|" + locations.get(i).replace(" ", "+");
        }
        return url;
    }

    private static String orderLocationsToUrl(ArrayList<String> locations, JSONArray order) {
        String url = "";
        if (locations.size() >= 1) {
            url = "https://www.google.com.br/maps/dir/" +
                    locations.get(0).replace(" ", "+");
        }

        for (int i = 1; i < locations.size(); i++) {
            url += "/" + locations.get(i).replace(" ", "+");
        }

        if (locations.size() >= 1) {
            url += "/" + locations.get(0).replace(" ", "+");
        }
        return url;
    }

    public static String locationsToGoogleMaps(ArrayList<String> locations) {
        try {
            // Get URL Path
            String URLPath = locationsToURL(locations);
            System.out.println(URLPath);

            // Make Request
            String jsonString = request(URLPath);
            System.out.println(jsonString);

            // Cast JSON String do Object
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
            JSONArray jsonArray = (JSONArray) ((JSONObject)(((JSONArray) jsonObject.get("routes")).get(0))).get("waypoint_order");
            System.out.println(jsonArray.toString());

            // Return Google Map URL
            return orderLocationsToUrl(locations, jsonArray);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        ArrayList<String> locations = new ArrayList<String>();
        locations.add("Rua Aprígio Veloso, 161, Campina Grande");
        locations.add("Avenida Almirante Barroso, 641, Campina Grande");
        locations.add("Rua Sebastião Donato, 15, Campina Grande");
        locations.add("Rua João Florentino de Carvalho, 1872, Campina Grande");
        locations.add("Rua Janúncio Ferreira, 230, Campina Grande");

        System.out.println(locationsToGoogleMaps(locations));
    }
}
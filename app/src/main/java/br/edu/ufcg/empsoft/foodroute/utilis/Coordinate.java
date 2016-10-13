package br.edu.ufcg.empsoft.foodroute.utilis;

/**
 * Created by orion on 13/10/16.
 */
public class Coordinate {

    private double lat, lon;

    public Coordinate(double lat, double lon) {
        setLat(lat);
        setLon(lon);
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return getLat() + "," + getLon();
    }
}

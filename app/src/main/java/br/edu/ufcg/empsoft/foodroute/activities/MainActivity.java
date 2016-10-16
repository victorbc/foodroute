package br.edu.ufcg.empsoft.foodroute.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;

import br.edu.ufcg.empsoft.foodroute.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback  {

    private GoogleMap mMap;

    //MenuItem mi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*mi = (MenuItem) findViewById(R.id.nav_route);

        mi.setOnMenuItemClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com.br/maps/dir/Rua+Aprígio+Veloso,+161,+Campina+Grande/Avenida+Almirante+Barroso,+641,+Campina+Grande/Rua+Sebastião+Donato,+15,+Campina+Grande/Rua+João+Florentino+de+Carvalho,+1872,+Campina+Grande/Rua+Janúncio+Ferreira,+230,+Campina+Grande/Rua+Aprígio+Veloso,+161,+Campina+Grande")
                );
                startActivity(intent);
            }
        });*/

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_analytics) {
            // Handle the camera action
        } else if (id == R.id.nav_conf) {

        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_fav) {

        } else if (id == R.id.nav_route) {

            /*ArrayList<String> locations = new ArrayList<String>();
            locations.add("Rua Aprígio Veloso, 161, Campina Grande");
            locations.add("Avenida Almirante Barroso, 641, Campina Grande");
            locations.add("Rua Sebastião Donato, 15, Campina Grande");
            locations.add("Rua João Florentino de Carvalho, 1872, Campina Grande");
            locations.add("Rua Janúncio Ferreira, 230, Campina Grande");

            String mapsPath = locationsToGoogleMaps(locations);*/

            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    //Uri.parse("mapsPath");
                    Uri.parse("https://www.google.com.br/maps/dir/Rua+Aprígio+Veloso,+161,+Campina+Grande/Avenida+Almirante+Barroso,+641,+Campina+Grande/Rua+Sebastião+Donato,+15,+Campina+Grande/Rua+João+Florentino+de+Carvalho,+1872,+Campina+Grande/Rua+Janúncio+Ferreira,+230,+Campina+Grande/Rua+Aprígio+Veloso,+161,+Campina+Grande")
            );
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_history) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    /*View.OnClickListener myOnlyhandler = new View.OnClickListener() {
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.nav_route:
                    // it was the first button
                    break;
            }
        }
    };*/
}

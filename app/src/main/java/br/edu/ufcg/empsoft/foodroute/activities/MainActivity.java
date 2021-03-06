package br.edu.ufcg.empsoft.foodroute.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.empsoft.foodroute.R;
import br.edu.ufcg.empsoft.foodroute.utilis.API;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private List<Marker> markers;
    private View btGo;
    private View btExpand;
    private View btColapse;
    private View gradient;
    private Bitmap.Config conf;
    private Bitmap bmp;
    private Canvas canvas1;
    private Paint color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        markers = new ArrayList<>();

        gradient = findViewById(R.id.gradient);

        btExpand = findViewById(R.id.bt_expand);
        btColapse = findViewById(R.id.bt_colapse);

        btExpand.setOnClickListener(expandColapse);
        btColapse.setOnClickListener(expandColapse);

        btGo = findViewById(R.id.bt_go);
        btGo.setVisibility(View.INVISIBLE);
        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> locations = new ArrayList<>();
                LatLng tempLatLng;


                btGo.setVisibility(View.INVISIBLE);
                for (Marker marker : markers) {
                    tempLatLng = marker.getPosition();
                    locations.add(tempLatLng.latitude + "," + tempLatLng.longitude);
                }
                String url = API.locationsToIntentURL(locations);

                Uri gmmIntentUri = Uri.parse(url);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                }
                btGo.setVisibility(View.VISIBLE);


            }
        });

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        conf = Bitmap.Config.ARGB_8888;
        bmp = Bitmap.createBitmap(100, 150, conf);
        canvas1 = new Canvas(bmp);

        // paint defines the text color, stroke width and size
        color = new Paint();
        color.setTextSize(30);
        color.setColor(Color.BLACK);
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

        mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                markers.remove(marker);
                mMap.clear();
                for (int i = 0; i < markers.size(); i++) {
                    addMaker(markers.get(i).getPosition(), false, i + 1);
                }
                return false;
            }
        });

        // Add a marker in Sydney and move the camera
        LatLng ufcg = new LatLng(-14.495849, -51.328125);
        //mMap.addMarker(new MarkerOptions().position(ufcg).title("UFCG"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ufcg));


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.setOnMapClickListener(mapClickListener);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.setOnMapClickListener(mapClickListener);
        }


    }


    private GoogleMap.OnMapClickListener mapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            if (gradient.getVisibility() != View.VISIBLE && markers.size() < 20) {
                addMaker(latLng, true, 0);

            }
        }
    };

    private void addMaker(LatLng latLng, boolean save, int pos) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        // markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
        //markerOptions.title("Parada numero: " + (markers.size() + 1));


        // modify canvas
        canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.pinpoint), 0, 0, color);

        if (save)
            canvas1.drawText((markers.size() + 1) + "", 25, 45, color);
        else
            canvas1.drawText(pos + "", 25, 45, color);

        // bmp = Bitmap.createScaledBitmap(bmp, 100, 200, false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bmp));
        // Specifies the anchor to be at a particular point in the marker image.
        markerOptions.anchor(0.5f, 0.5f);

        if (save)
            markers.add(mMap.addMarker(markerOptions));
        else
            mMap.addMarker(markerOptions);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Posição atual");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permissão negada", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            //You can add here other case statements according to your requirement.
        }
    }


    View.OnClickListener expandColapse = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.bt_expand) {
                view.setVisibility(View.INVISIBLE);
                btColapse.setVisibility(View.VISIBLE);
                gradient.setVisibility(View.VISIBLE);
                btGo.setVisibility(View.INVISIBLE);

            } else if (view.getId() == R.id.bt_colapse) {
                view.setVisibility(View.INVISIBLE);
                btExpand.setVisibility(View.VISIBLE);
                gradient.setVisibility(View.INVISIBLE);
                btGo.setVisibility(View.VISIBLE);
            }
        }

    };

}

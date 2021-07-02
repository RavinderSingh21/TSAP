package com.example.newtsap;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;


public class MapsActivity  extends BaseActivity
        implements OnMapReadyCallback, GeoQueryEventListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Firebase_User";
    private GoogleMap mMap;
    SearchView searchView;
    SupportMapFragment mapFragment;
    private LocationRequest locationRequest ;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker currentUser;
    private DatabaseReference myLocationRef;
    private GeoFire geoFire;
    private List<LatLng> Dangerous_Areas;
    private GoogleSignInClient signInClient;
    private GoogleApiClient googleApiClient;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle Toggle;
    Geocoder geocoder;
    List<Address> addresses;
    LocationManager locationManager;
    LocationListener locationListener;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String > adapter;
    TextView textView;
    ImageView imageView;
    String username;
    String useremail;
    String userphoto;
    TextView name;
    TextView email;
    ImageView photo;
    View headerview;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient apiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        drawer =findViewById(R.id.drawer_layout);
        navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerview = navigationView.getHeaderView(0);
        name =headerview.findViewById(R.id.username);
        email =headerview.findViewById(R.id.useremail);
        photo =headerview.findViewById(R.id.userimage);

        //       name.setText(username);
        //       name.setTextSize(TypedValue.COMPLEX_UNIT_SP,12f);
        //       email.setText(useremail);
//        userphoto =userphoto + "?type=small";
//        Picasso.get().load(userphoto).into(photo);


        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MapsActivity.this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        if(savedInstanceState == null){
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            navigationView.setCheckedItem(R.id.nav_audit);}





        Dexter.withActivity(this)
                .withPermission( Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

                        buildLocationRequest();
                        buildLocationCallback();
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(MapsActivity.this);

                        initArea();

                        settingGeoFire();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {




                        Toast.makeText(MapsActivity.this, "You must enable permission", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest
                                                                           permission, PermissionToken token) {

                    }
                }).check();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_audit:
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                Intent intent_a = new Intent(this,AuditActivity.class);

                startActivity(intent_a);
                break;

            case R.id.nav_measures:


                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                Intent intent_m = new Intent(this,MeasuresActivity.class);

                startActivity(intent_m);
                break;

            case R.id.nav_sos:


                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                Intent intents = new Intent(this,SOSActivity.class);
                startActivity(intents);
                break;

            case R.id.pre_fir:

                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                Intent intent_p = new Intent(this,PreFIRActivity.class);
                startActivity(intent_p);
                break;
            case R.id.share:

                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            case R.id.sign_out:
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                assert user != null;
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Intent intent = new Intent(MapsActivity.this, MainActivity.class);

                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }
                        });




        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }




    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        else  {

            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }






    public void onCall(View view) {
        if (isPermissionGranted()) {
            call_action();
        }

    }

    public void call_action() {
        String phnum = "7355272686";
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phnum));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }

    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }



            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void checkPermission(String permission, int requestCode)
    {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                MapsActivity.this,
                permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions(
                            MapsActivity.this,
                            new String[] { permission },
                            requestCode);
        }
        else {
            Toast
                    .makeText(MapsActivity.this,
                            "Permission already granted",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }


    private void initArea() {
        Dangerous_Areas=new ArrayList<>();

        Dangerous_Areas.add(new LatLng(25.480581,      80.3500689 ));
        Dangerous_Areas.add(new LatLng(22.7196,      75.8577 ));
        Dangerous_Areas.add(new LatLng(22.741354,    75.7569164));
        Dangerous_Areas.add(new LatLng(22.6723516,	75.9216458));
        Dangerous_Areas.add(new LatLng(22.628392,	75.849819));
        Dangerous_Areas.add(new LatLng(22.6828392,	75.8549819));
        Dangerous_Areas.add(new LatLng(22.754501,	75.9035451));
        Dangerous_Areas.add(new LatLng(22.7168359,	75.9383404));
        Dangerous_Areas.add(new LatLng(22.4888705,	75.9564843));
        Dangerous_Areas.add(new LatLng(37.0399419,	-95.6213999));
        Dangerous_Areas.add(new LatLng(22.7410937,	75.8512167));
        Dangerous_Areas.add(new LatLng(22.709828,	75.829729));
        Dangerous_Areas.add(new LatLng(22.6923209,	75.9039237));
        Dangerous_Areas.add(new LatLng(22.7053882,	75.8436644));
        Dangerous_Areas.add(new LatLng(22.7053882,	75.8436644));
        Dangerous_Areas.add(new LatLng(22.71958,	75.8875248));
        Dangerous_Areas.add(new LatLng(22.4295855,	75.6193539));
        Dangerous_Areas.add(new LatLng(22.8167246,	75.9255585));
        Dangerous_Areas.add(new LatLng(22.7214957,75.9292902));
        Dangerous_Areas.add(new LatLng( 22.7530847,75.8868403));
        Dangerous_Areas.add(new LatLng(22.7238346,	75.8557557));
        Dangerous_Areas.add(new LatLng(22.6737314,75.8800208));
        Dangerous_Areas.add(new LatLng(22.7269846,75.8412484));
        Dangerous_Areas.add(new LatLng(22.754613,75.90736));
        Dangerous_Areas.add(new LatLng(22.7197123,75.8758371));
        Dangerous_Areas.add(new LatLng(23.838805,	78.7378068));
        Dangerous_Areas.add(new LatLng(26.0538287,	77.3690496));
        Dangerous_Areas.add(new LatLng(22.7532848,	75.8936962));
        Dangerous_Areas.add(new LatLng(22.6478133,	75.8951875));
        Dangerous_Areas.add(new LatLng(22.7008289,	75.8490379));
        Dangerous_Areas.add(new LatLng(22.7184536,	75.8434775));
        Dangerous_Areas.add(new LatLng(22.7657262,	75.8859572));
        Dangerous_Areas.add(new LatLng(22.7207859,	75.8989599));
        Dangerous_Areas.add(new LatLng(22.7552578,	75.8967492));
        Dangerous_Areas.add(new LatLng(22.7047976,	75.8526692));
        Dangerous_Areas.add(new LatLng(22.746148,	75.8682035));
        Dangerous_Areas.add(new LatLng(22.768761,	75.9116443));
        Dangerous_Areas.add(new LatLng(22.7352891,	75.8609845));
        Dangerous_Areas.add(new LatLng(22.724355,	75.8838944));
        Dangerous_Areas.add(new LatLng(22.6908779,	75.8599315));
        Dangerous_Areas.add(new LatLng(22.781292,	75.8001162));
        Dangerous_Areas.add(new LatLng(22.7354613,	75.90736));
        Dangerous_Areas.add(new LatLng(22.7454613,	75.90736));
        Dangerous_Areas.add(new LatLng(22.6838333,	75.8292959));
        Dangerous_Areas.add(new LatLng(23.6838333,	75.8292959));
        Dangerous_Areas.add(new LatLng(22.6838333,	75.8292959));
        Dangerous_Areas.add(new LatLng(22.722231,	75.882036));
        Dangerous_Areas.add(new LatLng(22.7128323,	75.8409869));
        Dangerous_Areas.add(new LatLng(22.6905323,	75.819062));
        Dangerous_Areas.add(new LatLng(22.9416461,	76.0630381));



    }


    private void settingGeoFire() {

        myLocationRef = FirebaseDatabase.getInstance().getReference("MyLocation");
        geoFire = new GeoFire(myLocationRef);


    }

    private void buildLocationCallback() {
        locationCallback =new LocationCallback(){
            @Override
            public void onLocationResult(final LocationResult locationResult) {
                if (mMap !=null){

                    geoFire.setLocation("You", new GeoLocation(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()), new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {
                            if (currentUser != null) currentUser.remove();
                            currentUser = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude()))
                                    .title("You"));

                            //After add marker move camera
                            mMap.animateCamera(CameraUpdateFactory
                                    .newLatLngZoom(currentUser.getPosition(),12.0f));
                            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                            try {
                                addresses = geocoder.getFromLocation(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude(),1);

                                if (addresses !=null && addresses.size() > 0 ){

                                    Log.i("PlaceInfo",addresses.get(0).toString());

                                    String address ="";

                                    if(addresses.get(0).getAddressLine(0) !=null){
                                        address = addresses.get(0).getAddressLine(0);
                                    }
                                    Toast.makeText(MapsActivity.this, address, Toast.LENGTH_SHORT).show();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    });

                }
            }
        };
    }

    private void buildLocationRequest() {

        locationRequest=new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setSmallestDisplacement(10f);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getMaxZoomLevel();


        if (fusedLocationProviderClient != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        // add circles for dangerous areas
        for (LatLng latLng : Dangerous_Areas)
        {
            mMap.addCircle(new CircleOptions().center(latLng)
                    .radius(500)     // radius
                    .strokeColor(Color.BLUE)
                    .fillColor(0x280000FF)   // 22 is transparent code
                    .strokeWidth(5.0f)

            ) ;

            // Create GeoQuery when user in dangerous location
            GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latLng.latitude,latLng.longitude),0.5f) ;// 500m
            geoQuery.addGeoQueryEventListener(MapsActivity.this);
        }

    }

    @Override
    protected void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {
        sendNotification("You",String.format("%s Entered Dangerous area",key));

    }

    @Override
    public void onKeyExited(String key) {

        sendNotification("You",String.format("%s Leaved Dangerous area",key));

    }



    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        sendNotification("You",String.format("%s Move within  Dangerous area",key));

    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {

        Toast.makeText(this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

    }

    private void sendNotification(String title, String content) {

        Toast.makeText(this, ""+content, Toast.LENGTH_SHORT).show();

        String NOTIFICATION_CHANNEL_ID = "User_Multiple_Location";
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"My Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            //config
            notificationChannel.setDescription("Channel Description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.WHITE);
            //notificationChannel.setVibrationPattern(new long[] {0,1000,500,1000});
            //notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));

        Notification notification = builder.build();
        notificationManager.notify(new Random().nextInt(),notification);


    }
    private void DisplayMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }




}
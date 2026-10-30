package com.example.kayabeapp;

import static com.android.volley.VolleyLog.TAG;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FloatingActionButton floatingActionButton;

    private FloatingActionButton tourist_btn;

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private SupportMapFragment mapFragment;
    private LatLng currentLocation;
    private Marker currentLocationMarker;
    private Button profileButton;


    private CardView cardView;
    LatLng mlocation;

    private int currentRouteIndex = 0;

    private List<AlertDialog> openDialogs = new ArrayList<>();



    private LinearLayout sidebarLayout;
    private ImageView hamburgerButton;
    private Button logoutButton;

    private boolean isSidebarVisible = false;

    private Polyline currentPolyline;

    private Polyline PolylineView;




    private final LatLng marisolTerminal = new LatLng(15.152045348364277, 120.59173085894363);
    private final LatLng villaTerminal = new LatLng(15.126836302016194, 120.59737597163085);
    private final LatLng holyHighwayTerminal = new LatLng(15.16917850721998, 120.58724259425236);
    private final LatLng balibagoHighwayTerminal = new LatLng(15.168295506732766, 120.58236906591743);
    private final LatLng sapangbatoTerminal = new LatLng(15.141837318558377, 120.58779385421435);
    private final LatLng pandanTerminal = new LatLng(15.16230084497119, 120.60826193927403);

    private final LatLng mainGateFriendshipTerminal = new LatLng(15.166493711024625, 120.58283235457135);

    private final LatLng capayaAngelesTerminal = new LatLng(15.138171791178188, 120.59034221276481);

    private final LatLng sunsetNepoTerminal = new LatLng(15.1353769, 120.5861382);

    private final LatLng checkpointHensonvilleTerminal = new LatLng(15.166512, 120.582715);


    private Marker markerMarisolTerm;
    private Marker markerVillaTerminal;
    private Marker markerCheckpointTerminal;

    private Marker markerBalibagoHighwayTerminal;

    private Marker markerSapangbatoTerminal;

    private Marker markerPandanTerminal;

    private Marker markerMainGateFriendshipTerminal;

    private Marker markerCapayaAngelesTerminal;

    private Marker markerSunsetNepoTerminal;

    private Marker markerCheckpointHensonvilleTerminal;

    private Marker markerSelectedLoc;

    Button reach;

    private enum Terminal {
        MARISOL_PAMPANG,
        VILLA_PAMPANG,
        HOLY_HIGHWAY,
        BALIBAGO_HIGHWAY,
        SAPANG_BATO,
        PANDAN_PAMPANG,
        MAINGATE_FRIENDSHIP,
        CAPAYA_ANGELES,
        SUNSET_NEPO,
        CHECKPOINT_HENSONVILLE
    }

    private Terminal selectedTerminal;


    private boolean locationAnimationShown = false;
    double thresholdDistance = 300.0;
    private final LatLng school = new LatLng(15.166320281756615, 120.59012627821096);


    private final LatLng HolyRosarioChurch = new LatLng(15.166320281756615, 120.59012627821096);
    private final LatLng MuseoNingAngeles = new LatLng(15.166320281756615, 120.59012627821096);
    private final LatLng PuningHotspring = new LatLng(15.166320281756615, 120.59012627821096);
    private final LatLng WalkingStreet = new LatLng(15.166320281756615, 120.59012627821096);
    private final LatLng SmC = new LatLng(15.166320281756615, 120.59012627821096);
    private final LatLng MarqueeMall = new LatLng(15.166320281756615, 120.59012627821096);
    private final LatLng ApongMamacalulu = new LatLng(15.166320281756615, 120.59012627821096);
    private final LatLng NepoMall = new LatLng(15.166320281756615, 120.59012627821096);
    private final LatLng PamintuanMansion = new LatLng(15.166320281756615, 120.59012627821096);
    private final LatLng Center = new LatLng(15.166320281756615, 120.59012627821096);

    List<LatLng> customRouteForVillaPampang = CustomRoutes.getCustomRouteForVillaPampang();
    List<LatLng> customRouteForMarisolPampang = CustomRoutes.getCustomRouteForMarisolPampang();
    List<LatLng> customRouteForBalibagoHighway = CustomRoutes.getcustomRouteForBalibagoHighway();
    List<LatLng> customRouteForBalibagoHolyHighway = CustomRoutes.getcustomRouteForBalibagoHolyHighway();

    List<LatLng> customRouteForSapangBato = CustomRoutes.getcustomRouteForSapangBato();

    List<LatLng> customRouteForPandan = CustomRoutes.getcustomRouteForPandan();

    List<LatLng> customRouteForMaingate = CustomRoutes.getcustomRouteForMaingate();

    List<LatLng> customRouteForCapaya = CustomRoutes.getcustomRouteForCapaya();

    List<LatLng> customRouteForSunset = CustomRoutes.getcustomRouteForSunset();

    List<LatLng> customRouteForHenson = CustomRoutes.getcustomRouteForHenson();



    private TextView countdownTextView;
    private CountDownTimer countDownTimer;

    private AlertDialog countdownDialog;

    private String number1;

    private String number2;



    FirebaseUser user;

    Button SOS;

    Button report;

    Button matrix;

    private DatabaseReference reference;
    private String userID;

    public  int Marisolfare;

    public  int Balibagofare ;
    public  int Hensonfare ;
    public  int Pandanfare  ;
    public  int VillaPampangfare;
    public  int SapangBatofare;

    public  int BalibagoHolyHighwayfare;
    public  int Sunsetfare;
    public  int Maingatefare;
    public  int Capayafare;

    private double distanceForMarisolPampang;
    private double distanceForBalibagoHighway;
    private double distanceForHenson;
    private double distanceForPandan;
    private double distanceForVillaPampang;
    private double distanceForSapangBato;
    private double distanceForBalibagoHolyHighway;
    private double distanceRouteForSunset;
    private double distanceRouteForMaingate;

    private double distanceRouteForCapaya;

    private int discount = -2;

    private boolean discountApplied = false;
    List<List<LatLng>> allCustomRoutes = new ArrayList<>();

    List<String> nearCustomRoutes = new ArrayList<>(); // List to store names of near custom routes

    int jeep_blue;
    int jeep_yellow;
    int jeep_orange;
    int jeep_grey;
    int jeep_purple;
    int jeep_pink;
    int jeep_white;
    int jeep_brown;
    private LottieAnimationView loadingAnimationView;
    private LottieAnimationView locationfound;
    private View overlayView;

    LatLng selectedPlaceLatLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

         jeep_blue = ContextCompat.getColor(this, R.color.jeep_blue);
         jeep_yellow = ContextCompat.getColor(this, R.color.jeep_yellow);
         jeep_orange = ContextCompat.getColor(this, R.color.jeep_orange);
         jeep_grey = ContextCompat.getColor(this, R.color.jeep_grey);
         jeep_purple = ContextCompat.getColor(this, R.color.jeep_purple);
         jeep_pink = ContextCompat.getColor(this, R.color.jeep_pink);
         jeep_white = ContextCompat.getColor(this, R.color.jeep_white);
         jeep_brown = ContextCompat.getColor(this, R.color.jeep_brown);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);


        sidebarLayout = findViewById(R.id.sidebarLayout);
        hamburgerButton = findViewById(R.id.hamburger_button);
        logoutButton = findViewById(R.id.sidebar_logout_button);
        cardView = findViewById(R.id.cardView);
        report = findViewById(R.id.report);
        floatingActionButton = findViewById(R.id.fab_btn);
        tourist_btn = findViewById(R.id.tourist_btn);
        profileButton = findViewById(R.id.btn_profile);
        SOS = findViewById(R.id.SOS);
        matrix = findViewById(R.id.matrix);
        ImageView imageView = findViewById(R.id.imageView);

        loadingAnimationView = findViewById(R.id.loadingAnimationView);
        locationfound = findViewById(R.id.locationfound);
        overlayView = findViewById(R.id.overlayView);




        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users/Customers");
        userID = user.getUid();


        allCustomRoutes.add(customRouteForVillaPampang);
        allCustomRoutes.add(customRouteForMarisolPampang);
        allCustomRoutes.add(customRouteForBalibagoHighway);
        allCustomRoutes.add(customRouteForBalibagoHolyHighway);
        allCustomRoutes.add(customRouteForSapangBato);
        allCustomRoutes.add(customRouteForPandan);
        allCustomRoutes.add(customRouteForMaingate);
        allCustomRoutes.add(customRouteForSunset);
        allCustomRoutes.add(customRouteForHenson);
        allCustomRoutes.add(customRouteForCapaya);




        Map<LatLng, String> coordinateToCustomRoute = new HashMap<>();


        for (LatLng latLng : customRouteForVillaPampang) {
            coordinateToCustomRoute.put(latLng, "customRouteForVillaPampang");
        }


        for (LatLng latLng : customRouteForMarisolPampang) {
            coordinateToCustomRoute.put(latLng, "customRouteForMarisolPampang");
        }


        for (LatLng latLng : customRouteForBalibagoHighway) {
            coordinateToCustomRoute.put(latLng, "customRouteForBalibagoHighway");
        }


        for (LatLng latLng : customRouteForBalibagoHolyHighway) {
            coordinateToCustomRoute.put(latLng, "customRouteForBalibagoHolyHighway");
        }


        for (LatLng latLng : customRouteForSapangBato) {
            coordinateToCustomRoute.put(latLng, "customRouteForSapangBato");
        }

        for (LatLng latLng : customRouteForPandan) {
            coordinateToCustomRoute.put(latLng, "customRouteForPandan");
        }

        for (LatLng latLng : customRouteForMaingate) {
            coordinateToCustomRoute.put(latLng, "customRouteForMaingate");
        }


        for (LatLng latLng : customRouteForSunset) {
            coordinateToCustomRoute.put(latLng, "customRouteForSunset");
        }


        for (LatLng latLng : customRouteForHenson) {
            coordinateToCustomRoute.put(latLng, "customRouteForHenson");
        }

        for (LatLng latLng : customRouteForCapaya) {
            coordinateToCustomRoute.put(latLng, "customRouteForCapaya");
        }





        DatabaseReference marisolFareRef = FirebaseDatabase.getInstance().getReference().child("Fare").child("Marisolfare");

        marisolFareRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Marisolfare = dataSnapshot.getValue(Integer.class);

                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur during the database operation
            }
        });

        DatabaseReference balibagofare = FirebaseDatabase.getInstance().getReference().child("Fare").child("Balibagofare");

        balibagofare.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Balibagofare = dataSnapshot.getValue(Integer.class);
                    // Now, you have the value of "Marisolfare" in the marisolFare variable
                    // You can use it as needed in your code
                } else {
                    // Handle the case where "Marisolfare" does not exist in the database
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur during the database operation
            }
        });

        DatabaseReference hensonfare = FirebaseDatabase.getInstance().getReference().child("Fare").child("Hensonfare");

        hensonfare.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Hensonfare = dataSnapshot.getValue(Integer.class);
                    // Now, you have the value of "Marisolfare" in the marisolFare variable
                    // You can use it as needed in your code
                } else {
                    // Handle the case where "Marisolfare" does not exist in the database
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur during the database operation
            }
        });

        DatabaseReference pandanfare = FirebaseDatabase.getInstance().getReference().child("Fare").child("Pandanfare");

        pandanfare.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Pandanfare = dataSnapshot.getValue(Integer.class);
                    // Now, you have the value of "Marisolfare" in the marisolFare variable
                    // You can use it as needed in your code
                } else {
                    // Handle the case where "Marisolfare" does not exist in the database
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur during the database operation
            }
        });

        DatabaseReference sapangBatofare = FirebaseDatabase.getInstance().getReference().child("Fare").child("SapangBatofare");

        sapangBatofare.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    SapangBatofare = dataSnapshot.getValue(Integer.class);
                    // Now, you have the value of "Marisolfare" in the marisolFare variable
                    // You can use it as needed in your code
                } else {
                    // Handle the case where "Marisolfare" does not exist in the database
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur during the database operation
            }
        });


        DatabaseReference balibagoHolyHighwayfare = FirebaseDatabase.getInstance().getReference().child("Fare").child("BalibagoHolyHighwayfare");

        balibagoHolyHighwayfare.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    BalibagoHolyHighwayfare = dataSnapshot.getValue(Integer.class);
                    // Now, you have the value of "Marisolfare" in the marisolFare variable
                    // You can use it as needed in your code
                } else {
                    // Handle the case where "Marisolfare" does not exist in the database
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur during the database operation
            }
        });


        DatabaseReference sunsetfare = FirebaseDatabase.getInstance().getReference().child("Fare").child("Sunsetfare");

        sunsetfare.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Sunsetfare = dataSnapshot.getValue(Integer.class);
                    // Now, you have the value of "Marisolfare" in the marisolFare variable
                    // You can use it as needed in your code
                } else {
                    // Handle the case where "Marisolfare" does not exist in the database
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur during the database operation
            }
        });


        DatabaseReference maingatefare = FirebaseDatabase.getInstance().getReference().child("Fare").child("Maingatefare");

        maingatefare.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Maingatefare = dataSnapshot.getValue(Integer.class);
                    // Now, you have the value of "Marisolfare" in the marisolFare variable
                    // You can use it as needed in your code
                } else {
                    // Handle the case where "Marisolfare" does not exist in the database
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur during the database operation
            }
        });


        DatabaseReference capayafare = FirebaseDatabase.getInstance().getReference().child("Fare").child("Capayafare");

        capayafare.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Capayafare = dataSnapshot.getValue(Integer.class);
                    // Now, you have the value of "Marisolfare" in the marisolFare variable
                    // You can use it as needed in your code
                } else {
                    // Handle the case where "Marisolfare" does not exist in the database
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur during the database operation
            }
        });













        // Reference to the "trustedContacts" child
        DatabaseReference trustedContactsRef = reference.child(userID).child("trustedContacts");

        trustedContactsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int contactCount = 0;
                    for (DataSnapshot contactSnapshot : dataSnapshot.getChildren()) {
                        if (contactCount == 0) {
                            number1 = contactSnapshot.getValue(String.class);

                        } else if (contactCount == 1) {
                            number2 = contactSnapshot.getValue(String.class);

                        }
                        contactCount++;
                    }
                } else {
                    Log.d("Firebase Data", "No trusted contacts found in Firebase.");
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occurred
                Log.e("Firebase Error", "Failed to read value.", databaseError.toException());
            }
        });


        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MapActivity.this, Reporting.class);
                startActivity(intent);

            }
        });


        // Set a click listener for the hamburger button
        hamburgerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSidebar();


                if (isSidebarVisible == false) {
                    cardView.setVisibility(View.GONE); // Make the cardView visible
                } else {
                    cardView.setVisibility(View.VISIBLE); // Hide the cardView
                }
            }
        });
        // Set a click listener for the logout button in the sidebar
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the signOut method to sign out the user
                signOut();
            }
        });

        matrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity here
                Intent intent = new Intent(MapActivity.this, Matrix.class);
                startActivity(intent);

            }
        });


        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity here
                Intent intent = new Intent(MapActivity.this, UserProfile.class);
                startActivity(intent);

            }
        });


        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCountdownDialog(5);

            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });

        tourist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });





        String apiKey = getString(R.string.places_api_key);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        LatLng angelesCityLatLng = new LatLng(15.1445, 120.5887);

        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(angelesCityLatLng.latitude - 0.1, angelesCityLatLng.longitude - 0.1),
                new LatLng(angelesCityLatLng.latitude + 0.1, angelesCityLatLng.longitude + 0.1)

        ));

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());


                if (!discountApplied) {
                    // Discount calculation code
                    DatabaseReference discountRef = FirebaseDatabase.getInstance().getReference("Users/Customers/" + userID + "/selected_discounts");

                    // Query the database to get the selected_discounts
                    discountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                List<String> selectedDiscounts = (List<String>) dataSnapshot.getValue(); // Retrieve the selected discounts from the database

                                // Check if any of the discounts are present
                                if (selectedDiscounts != null &&
                                        (selectedDiscounts.contains("Person with Disability") ||
                                                selectedDiscounts.contains("Senior Citizen") ||
                                                selectedDiscounts.contains("Student"))) {
                                    // If any of the discounts is present, subtract 2 from the fare
                                    Balibagofare += discount;
                                    BalibagoHolyHighwayfare += discount;
                                    VillaPampangfare += discount;
                                    Pandanfare += discount;
                                    Maingatefare += discount;
                                    Sunsetfare += discount;
                                    Hensonfare += discount;
                                    Marisolfare += discount;
                                    SapangBatofare += discount;
                                    discountApplied = true; // Mark the discount as applied
                                }

                                // Now you can display the updated fares in your dialog or other parts of your code
                                // ...
                            } else {
                                Log.d("Firebase Data", "No selected_discounts found in Firebase for the current user.");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle any errors that occurred
                            Log.e("Firebase Error", "Failed to read value.", databaseError.toException());
                        }
                    });
                }


                // Get the LatLng of the selected place
                 selectedPlaceLatLng = place.getLatLng();

                // Define a new CameraPosition with desired zoom level
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(selectedPlaceLatLng) // Sets the center of the map to the selected place's LatLng
                        .zoom(20) // Desired zoom level
                        .build();

                // Move the camera to the new position
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                // Remove the previous marker if it exists
                if (markerSelectedLoc != null) {
                    markerSelectedLoc.remove();
                }

                // Create a new marker for the selected location
                markerSelectedLoc = mMap.addMarker(new MarkerOptions()
                        .position(selectedPlaceLatLng)
                        .flat(true));

                checkDistanceToCustomRoutes(selectedPlaceLatLng, school);


                ////PARA SA PICTURE ETO
                // Define a Place ID for the selected place.
                final String placeId = place.getId();

                // Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
                final List<Place.Field> fields = Collections.singletonList(Place.Field.PHOTO_METADATAS);

                // Get a Place object (this example uses fetchPlace(), but you can also use findCurrentPlace())
                final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, fields);

                placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
                    final Place placeWithPhoto = response.getPlace();

                    // Get the photo metadata.
                    final List<PhotoMetadata> metadata = placeWithPhoto.getPhotoMetadatas();
                    if (metadata == null || metadata.isEmpty()) {
                        Log.w(TAG, "No photo metadata.");
                        return;
                    }
                    final PhotoMetadata photoMetadata = metadata.get(0);

                    // Create a FetchPhotoRequest.
                    final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                            .setMaxWidth(800) // Optional.
                            .setMaxHeight(600) // Optional.
                            .build();

                    placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                        Bitmap bitmap = fetchPhotoResponse.getBitmap();
                        // Display the fetched photo in your ImageView (assuming imageView is the ID of your ImageView).
                        imageView.setImageBitmap(bitmap);

                        // Find the Button by its ID
                        Button hideImageButton = findViewById(R.id.hideImageButton);

                        hideImageButton.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);

                        hideImageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Make the ImageView invisible when the button is clicked
                                imageView.setVisibility(View.INVISIBLE);
                                hideImageButton.setVisibility(View.INVISIBLE);
                            }
                        });
                    }).addOnFailureListener((exception) -> {
                        if (exception instanceof ApiException) {
                            final ApiException apiException = (ApiException) exception;
                            Log.e(TAG, "Place not found: " + exception.getMessage());
                            final int statusCode = apiException.getStatusCode();
                            // TODO: Handle error with the given status code.
                        }

                    });
                });

                Map<String, Double> routeDistances = calculateDistancesOnAllCustomRoutes(school, selectedPlaceLatLng, allCustomRoutes);

                // Assign values to the class-level variables
                distanceForMarisolPampang = routeDistances.get("RouteForMarisolPampang");
                distanceForBalibagoHighway = routeDistances.get("RouteForBalibagoHighway");
                distanceForHenson = routeDistances.get("RouteForHenson");
                distanceForPandan = routeDistances.get("RouteForPandan");
                distanceForVillaPampang = routeDistances.get("RouteForVillaPampang");
                distanceForSapangBato = routeDistances.get("RouteForSapangBato");
                distanceForBalibagoHolyHighway = routeDistances.get("RouteForBalibagoHolyHighway");
                distanceRouteForSunset = routeDistances.get("RouteForSunset");
                distanceRouteForMaingate = routeDistances.get("RouteForMaingate");
                distanceRouteForCapaya = routeDistances.get("RouteForCapaya");


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                DatabaseReference selectedPlacesRef = databaseReference.child("SelectedPlaces");


                String uniqueKey = selectedPlacesRef.push().getKey();

                // Create a map to hold the place data
                Map<String, Object> placeData = new HashMap<>();
                placeData.put("name", place.getName());
                placeData.put("latlng", place.getLatLng());


            // Push the place data to the database under the unique key
                selectedPlacesRef.child(uniqueKey).setValue(placeData);

            }


            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }




            public boolean isPlaceNearCustomRoute(LatLng selectedPlaceLatLng, List<LatLng> customRoute, double thresholdDistance) {
                for (LatLng latLng : customRoute) {
                    float[] results = new float[1];
                    Location.distanceBetween(
                            selectedPlaceLatLng.latitude,
                            selectedPlaceLatLng.longitude,
                            latLng.latitude,
                            latLng.longitude,
                            results
                    );

                    float distance = results[0];

                    if (distance <= thresholdDistance) {


                        return true;
                    }
                }
                return false;
            }




            public void checkDistanceToCustomRoutes(LatLng selectedPlaceLatLng, LatLng userLocation) {

                // Check the distance from the user's location to custom routes
                for (int i = 0; i < allCustomRoutes.size(); i++) {
                    List<LatLng> customRoute = allCustomRoutes.get(i);
                    if (isPlaceNearCustomRoute(mlocation, customRoute, thresholdDistance)) {
                        String customRouteLabel = getCustomRouteLabel(customRoute);
                        if (customRouteLabel != null) {
                            nearCustomRoutes.add(customRouteLabel); // Add the label to the list
                            Log.i(TAG, "User's current location is near the route: " + customRouteLabel);

                        } else {
                            Log.i(TAG, "User's current location is near a coordinate, but the custom route label is not found.");
                        }
                    }
                }




                // Check the distance from the selected place to custom routes
                for (int i = 0; i < allCustomRoutes.size(); i++) {
                    List<LatLng> customRoute = allCustomRoutes.get(i);
                    if (isPlaceNearCustomRoute(selectedPlaceLatLng, customRoute, thresholdDistance)) {
                        String customRouteLabel = getCustomRouteLabel(customRoute);
                        if (customRouteLabel != null) {
                            nearCustomRoutes.add(customRouteLabel); // Add the label to the list
                            Log.i(TAG, "Selected place is near the route: " + customRouteLabel);
                        } else {
                            Log.i(TAG, "Selected place is near a coordinate, but the custom route label is not found.");
                        }
                    }
                }

                if (!nearCustomRoutes.isEmpty()) {
                    // Log the names of the routes that are near the selected place or the user's current location
                    Log.i(TAG, "Selected place or user's current location is near the following routes: " + TextUtils.join(", ", nearCustomRoutes));
                    showCustomDialogBox(currentRouteIndex); // Show the dialog for the current route

                } else {
                    Log.i(TAG, "Selected place and user's current location are not near any custom route.");
                }



            }




            public String getCustomRouteLabel(List<LatLng> customRoute) {
                if (customRoute == customRouteForVillaPampang) {
                    return "RouteForVillaPampang";
                } else if (customRoute == customRouteForMarisolPampang) {
                    return "RouteForMarisolPampang";
                } else if (customRoute == customRouteForBalibagoHighway) {
                    return "RouteForBalibagoHighway";
                } else if (customRoute == customRouteForBalibagoHolyHighway) {
                    return "RouteForBalibagoHolyHighway";
                } else if (customRoute == customRouteForSapangBato) {
                    return "RouteForSapangBato";
                } else if (customRoute == customRouteForPandan) {
                    return "RouteForPandan";
                } else if (customRoute == customRouteForMaingate) {
                    return "RouteForMaingate";
                } else if (customRoute == customRouteForSunset) {
                    return "RouteForSunset";
                } else if (customRoute == customRouteForHenson) {
                    return "RouteForHenson";
                } else if (customRoute == customRouteForCapaya) {
                    return "RouteForCapaya";

                } else {
                    return null;
                }
            }


            @SuppressLint("MissingInflatedId")
            public void showCustomDialogBox(int routeIndex) {
                // Determine the layout resource based on the custom route index
                int dialogLayoutResId = 0;
                String customRouteLabel = nearCustomRoutes.get(routeIndex);
                if ("RouteForBalibagoHighway".equals(customRouteLabel)) {
                    dialogLayoutResId = R.layout.grey; // Use the appropriate layout for this route
                } else if ("RouteForBalibagoHolyHighway".equals(customRouteLabel)) {
                    dialogLayoutResId = R.layout.balibagoterminal; // Use the appropriate layout for this route
                } else if ("RouteForVillaPampang".equals(customRouteLabel)) {
                    dialogLayoutResId = R.layout.villapampang; // Use the appropriate layout for this route
                } else if ("RouteForSapangBato".equals(customRouteLabel)) {
                    dialogLayoutResId = R.layout.sapangbato; // Use the appropriate layout for this route
                } else if ("RouteForPandan".equals(customRouteLabel)) {
                    dialogLayoutResId = R.layout.pandan; // Use the appropriate layout for this route
                } else if ("RouteForMaingate".equals(customRouteLabel)) {
                    dialogLayoutResId = R.layout.friendship; // Use the appropriate layout for this route
                } else if ("RouteForSunset".equals(customRouteLabel)) {
                    dialogLayoutResId = R.layout.sunset; // Use the appropriate layout for this route
                } else if ("RouteForHenson".equals(customRouteLabel)) {
                    dialogLayoutResId = R.layout.henson; // Use the appropriate layout for this route
                }else if ("RouteForMarisolPampang".equals(customRouteLabel)) {
                    dialogLayoutResId = R.layout.marisol; // Use the appropriate layout for this route
                }else if ("RouteForCapaya".equals(customRouteLabel)) {
                    dialogLayoutResId = R.layout.capaya; // Use the appropriate layout for this route
                }


                // Check if a valid layout resource was found
                if (dialogLayoutResId != 0) {

                    // Inflate the dialog layout
                    View dialogView = getLayoutInflater().inflate(dialogLayoutResId, null);

                    // Create an AlertDialog builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                    builder.setView(dialogView);


                    TextView distanceBalibagoHighway = null; // Initialize to null
                    TextView distanceBalibagoHolyHighway = null; // Initialize to null
                    TextView distanceVillaPampang = null; // Initialize to null
                    TextView distanceSapangBato = null; // Initialize to null
                    TextView distancePandan = null; // Initialize to null
                    TextView distanceMaingate = null; // Initialize to null
                    TextView distanceSunset = null; // Initialize to null
                    TextView distanceHenson = null; // Initialize to null
                    TextView distanceMarisolPampang = null; // Initialize to null
                    TextView distanceCapaya = null; // Initialize to null

                    // Set the distanceTextView based on the custom route label
                    if ("RouteForBalibagoHighway".equals(customRouteLabel)) {
                        distanceBalibagoHighway = dialogView.findViewById(R.id.Faregrey);
                    } else if ("RouteForBalibagoHolyHighway".equals(customRouteLabel)) {
                        distanceBalibagoHolyHighway = dialogView.findViewById(R.id.Fareholy);
                    } else if ("RouteForVillaPampang".equals(customRouteLabel)) {
                        distanceVillaPampang = dialogView.findViewById(R.id.Farevilla);
                    } else if ("RouteForSapangBato".equals(customRouteLabel)) {
                        distanceSapangBato = dialogView.findViewById(R.id.Faresapangbato);
                    } else if ("RouteForPandan".equals(customRouteLabel)) {
                        distancePandan = dialogView.findViewById(R.id.Farepandan);
                    } else if ("RouteForMaingate".equals(customRouteLabel)) {
                        distanceMaingate = dialogView.findViewById(R.id.Farefriendship);
                    } else if ("RouteForSunset".equals(customRouteLabel)) {
                        distanceSunset = dialogView.findViewById(R.id.Faresunset);
                    } else if ("RouteForHenson".equals(customRouteLabel)) {
                        distanceHenson = dialogView.findViewById(R.id.Farehenson);
                    } else if ("RouteForMarisolPampang".equals(customRouteLabel)) {
                        distanceMarisolPampang = dialogView.findViewById(R.id.Faremarisol);
                    }else if ("RouteForCapaya".equals(customRouteLabel)) {
                        distanceCapaya = dialogView.findViewById(R.id.Farecapaya);
                    }

                    // Check if a valid TextView was found
                    if (distanceBalibagoHighway != null) {
                        distanceBalibagoHighway.setText("Fare is : " + Balibagofare );
                    }

                    // Check if a valid TextView was found
                    if (distanceBalibagoHolyHighway != null) {
                        distanceBalibagoHolyHighway.setText("Fare is : " + BalibagoHolyHighwayfare );
                    }

                    // Check if a valid TextView was found
                    if (distanceVillaPampang != null) {
                        distanceVillaPampang.setText("Fare is : " + VillaPampangfare );
                    }

                    // Check if a valid TextView was found
                    if (distancePandan != null) {
                        distancePandan.setText("Fare is : " + Pandanfare );
                    }

                    // Check if a valid TextView was found
                    if (distanceMaingate != null) {
                        distanceMaingate.setText("Fare is : " + Maingatefare );
                    }

                    // Check if a valid TextView was found
                    if (distanceSunset != null) {
                        distanceSunset.setText("Fare is : " + Sunsetfare );
                    }

                    // Check if a valid TextView was found
                    if (distanceHenson != null) {
                        distanceHenson.setText("Fare is : " + Hensonfare );
                    }

                    // Check if a valid TextView was found
                    if (distanceMarisolPampang != null) {
                        distanceMarisolPampang.setText("Fare is : " + Marisolfare );
                    }

                    // Check if a valid TextView was found
                    if (distanceSapangBato != null) {
                        distanceSapangBato.setText("Fare is : " + SapangBatofare );
                    }

                    if (distanceCapaya != null) {
                        distanceCapaya.setText("Fare is : " + Capayafare );
                    }




                    // Create and show the AlertDialog
                    final AlertDialog dialog = builder.create();
                    openDialogs.add(dialog); // Add the dialog to the list
                    dialog.show();

                    Button buttonNext = dialog.findViewById(R.id.next);
                    buttonNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (currentRouteIndex < nearCustomRoutes.size() - 1) {
                                currentRouteIndex++;
                                showCustomDialogBox(currentRouteIndex);
                            }
                        }
                    });

                    Button buttonPrev = dialog.findViewById(R.id.previous);
                    buttonPrev.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (currentRouteIndex > 0) {
                                currentRouteIndex--;
                                showCustomDialogBox(currentRouteIndex);
                            }
                        }
                    });





                    Button buttonOkay = dialogView.findViewById(R.id.button);
                    buttonOkay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (AlertDialog openDialog : openDialogs) {
                                if ("RouteForBalibagoHighway".equals(customRouteLabel)) {
                                    // Handle "Okay" button click for RouteForBalibagoHighway
                                    PolylineOptions polylineOptions = new PolylineOptions()
                                            .addAll(customRouteForBalibagoHighway)
                                            .width(8)
                                            .color(jeep_grey);
                                    PolylineView = mMap.addPolyline(polylineOptions);
                                } else if ("RouteForBalibagoHolyHighway".equals(customRouteLabel)) {
                                    // Handle "Okay" button click for RouteForBalibagoHolyHighway
                                    PolylineOptions polylineOptions = new PolylineOptions()
                                            .addAll(customRouteForBalibagoHolyHighway)
                                            .width(8)
                                            .color(jeep_purple);
                                    PolylineView = mMap.addPolyline(polylineOptions);
                                } else if ("RouteForVillaPampang".equals(customRouteLabel)) {
                                    // Handle "Okay" button click for RouteForVillaPampang
                                    PolylineOptions polylineOptions = new PolylineOptions()
                                            .addAll(customRouteForVillaPampang)
                                            .width(8)
                                            .color(jeep_yellow);
                                    PolylineView = mMap.addPolyline(polylineOptions);
                                } else if ("RouteForSapangBato".equals(customRouteLabel)) {
                                    // Handle "Okay" button click for RouteForVillaPampang
                                    PolylineOptions polylineOptions = new PolylineOptions()
                                            .addAll(customRouteForSapangBato)
                                            .width(8)
                                            .color(Color.RED);
                                    PolylineView = mMap.addPolyline(polylineOptions);
                                }else if ("RouteForPandan".equals(customRouteLabel)) {
                                    // Handle "Okay" button click for RouteForVillaPampang
                                    PolylineOptions polylineOptions = new PolylineOptions()
                                            .addAll(customRouteForPandan)
                                            .width(8)
                                            .color(Color.BLUE);
                                    PolylineView = mMap.addPolyline(polylineOptions);
                                }else if ("RouteForMaingate".equals(customRouteLabel)) {
                                    // Handle "Okay" button click for RouteForVillaPampang
                                    PolylineOptions polylineOptions = new PolylineOptions()
                                            .addAll(customRouteForMaingate)
                                            .width(8)
                                            .color(jeep_brown);
                                    PolylineView = mMap.addPolyline(polylineOptions);
                                }else if ("RouteForSunset".equals(customRouteLabel)) {
                                    // Handle "Okay" button click for RouteForVillaPampang
                                    PolylineOptions polylineOptions = new PolylineOptions()
                                            .addAll(customRouteForSunset)
                                            .width(8)
                                            .color(jeep_orange);
                                    PolylineView = mMap.addPolyline(polylineOptions);
                                }else if ("RouteForHenson".equals(customRouteLabel)) {
                                    // Handle "Okay" button click for RouteForVillaPampang
                                    PolylineOptions polylineOptions = new PolylineOptions()
                                            .addAll(customRouteForHenson)
                                            .width(8)
                                            .color(jeep_white);
                                    PolylineView = mMap.addPolyline(polylineOptions);
                                }else if ("RouteForMarisolPampang".equals(customRouteLabel)) {
                                    // Handle "Okay" button click for RouteForVillaPampang
                                    PolylineOptions polylineOptions = new PolylineOptions()
                                            .addAll(customRouteForMarisolPampang)
                                            .width(8)
                                            .color(Color.GREEN);
                                    PolylineView = mMap.addPolyline(polylineOptions);
                                }else if ("RouteForCapaya".equals(customRouteLabel)) {
                                    // Handle "Okay" button click for RouteForVillaPampang
                                    PolylineOptions polylineOptions = new PolylineOptions()
                                            .addAll(customRouteForCapaya)
                                            .width(8)
                                            .color(jeep_pink);
                                    PolylineView = mMap.addPolyline(polylineOptions);
                                }


                                discountApplied = false; // Mark the discount as applied
                                openDialog.dismiss();
                            }
                        }
                    });






                } else {
                    Log.e(TAG, "No layout resource found for custom route: " + customRouteLabel);
                }
            }





            public double calculateDistanceBetweenUserLocationAndPlace(LatLng userLocation, LatLng selectedPlaceLatLng, List<LatLng> customRoute) {
                double shortestDistance = Double.MAX_VALUE;

                for (LatLng latLng : customRoute) {
                    float[] results = new float[1];
                    Location.distanceBetween(
                            userLocation.latitude,
                            userLocation.longitude,
                            latLng.latitude,
                            latLng.longitude,
                            results
                    );

                    float distance = results[0];

                    // Check if the calculated distance is shorter than the current shortest distance
                    if (distance < shortestDistance) {
                        shortestDistance = distance;
                    }
                }

                // The shortestDistance variable will now contain the shortest distance between the user's location and the selected place along the custom route.
                // You can convert this distance to kilometers or any desired unit as needed.
                double shortestDistanceInKilometers = shortestDistance / 1000.0;

                return shortestDistanceInKilometers;
            }



            public Map<String, Double> calculateDistancesOnAllCustomRoutes(LatLng userLocation, LatLng selectedPlaceLatLng, List<List<LatLng>> allCustomRoutes) {
                Map<String, Double> routeDistances = new HashMap<>();

                for (int i = 0; i < allCustomRoutes.size(); i++) {
                    List<LatLng> customRoute = allCustomRoutes.get(i);
                    double distance = calculateDistanceBetweenUserLocationAndPlace(userLocation, selectedPlaceLatLng, customRoute);

                    String routeName = getCustomRouteLabel(customRoute);
                    routeDistances.put(routeName, distance);
                }

                // Now, the `routeDistances` map contains the distances between the user's location and the selected place for each custom route.
                // You can access these distances using the route names as keys.

                // Example: Printing the distances
                for (Map.Entry<String, Double> entry : routeDistances.entrySet()) {
                    String routeName = entry.getKey();
                    double distance = entry.getValue();
                    Log.i(TAG, "Distance on route " + routeName + ": " + distance + " KM");


                    // Calculate the fare based on the distance
                    int fare = Marisolfare; // Initialize with the base fare
                    if (distance > 2.8) {
                        // Calculate the additional fare based on 1.8 km increments
                        double additionalDistance = distance - 2.8;
                        if (additionalDistance > 0) {
                            int additionalFare = (int) Math.ceil(additionalDistance / 1.8);
                            fare += additionalFare;
                        }
                    }

                    // Update the fare variables for the respective routes
                    if ("RouteForMarisolPampang".equals(routeName)) {
                        Marisolfare = fare;
                    } else if ("RouteForBalibagoHighway".equals(routeName)) {
                        Balibagofare = fare;
                    } else if ("RouteForHenson".equals(routeName)) {
                        Hensonfare = fare;
                    } else if ("RouteForPandan".equals(routeName)) {
                        Pandanfare = fare;
                    } else if ("RouteForVillaPampang".equals(routeName)) {
                        VillaPampangfare = fare;
                    } else if ("RouteForSapangBato".equals(routeName)) {
                        SapangBatofare = fare;
                    } else if ("RouteForBalibagoHolyHighway".equals(routeName)) {
                        BalibagoHolyHighwayfare = fare;
                    } else if ("RouteForSunset".equals(routeName)) {
                        Sunsetfare = fare;
                    } else if ("RouteForMaingate".equals(routeName)) {
                        Maingatefare = fare;
                    }else if ("RouteForCapaya".equals(routeName)) {
                        Capayafare = fare;
                    }

                }

                return routeDistances; // Return the map of route distances
            }




        });



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 100)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(2000)
                .setMaxUpdateDelayMillis(100)
                .build();
        // Create the location callback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    updateLocationOnMap(location);
                }
            }
        };


    }

    public void touristDialog() {
        // Inflate the dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.tourist, null);

        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Create and show the AlertDialog
        final AlertDialog dialog = builder.create();
        dialog.show();



        CardView buttonOption1 = dialogView.findViewById(R.id.buttonOption1);
        buttonOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        CardView buttonOption2 = dialogView.findViewById(R.id.buttonOption2);
        buttonOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // button 2 clicked
            }
        });

        CardView buttonOption3 = dialogView.findViewById(R.id.buttonOption3);
        buttonOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // button 3 clicked
            }
        });

        CardView buttonOption4 = dialogView.findViewById(R.id.buttonOption4);
        buttonOption4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // button 4 clicked
            }
        });

        CardView buttonOption5 = dialogView.findViewById(R.id.buttonOption5);
        buttonOption5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // button 5 clicked
            }
        });

        CardView buttonOption6 = dialogView.findViewById(R.id.buttonOption6);
        buttonOption6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // button 6 clicked
            }
        });

        CardView buttonOption7 = dialogView.findViewById(R.id.buttonOption7);
        buttonOption7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // button 7 clicked
            }
        });

        CardView buttonOption8 = dialogView.findViewById(R.id.buttonOption8);
        buttonOption8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // button 8 clicked
            }
        });

        CardView buttonOption9 = dialogView.findViewById(R.id.buttonOption9);
        buttonOption9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // button 9 clicked
            }
        });

        CardView buttonOption10 = dialogView.findViewById(R.id.buttonOption10);
        buttonOption10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // button 10 clicked
            }
        });
    }




    private void showCustomDialog() {
        // Inflate the dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.jeepneyterminals, null);

        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Create and show the AlertDialog
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Set click listeners for the buttons inside the dialog
        CardView buttonOption1 = dialogView.findViewById(R.id.buttonOption1);
        buttonOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LatLng current = mlocation;
                if (mMap != null) {
                    markerMarisolTerm.setVisible(true); // Show the marker on the map
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerMarisolTerm.getPosition(), 18f));

                }
                selectedTerminal = Terminal.MARISOL_PAMPANG;

                // Close the dialog
                dialog.dismiss();

                // Check if the selected terminal marker exists and is visible
                if (markerMarisolTerm != null && markerMarisolTerm.isVisible()) {
                    // Zoom in on the selected marker
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerMarisolTerm.getPosition(), 18f));
                }

                // Remove the current polyline if it exists
                if (currentPolyline != null) {
                    currentPolyline.remove();
                }

                // Draw the custom polyline for VILLA-PAMPANG
                if (selectedTerminal == Terminal.MARISOL_PAMPANG) {
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(customRouteForMarisolPampang)
                            .width(8) // Set the width of the polyline
                            .color(Color.GREEN); // Set the color of the polyline

                    // Add the polyline to the map
                    currentPolyline = mMap.addPolyline(polylineOptions);


                }
            }
        });

        CardView buttonOption2 = dialogView.findViewById(R.id.buttonOption2);
        buttonOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap != null) {
                    markerVillaTerminal.setVisible(true); // Show the marker on the map
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerVillaTerminal.getPosition(), 18f));
                }
                // Set a variable to indicate that VILLA-PAMPANG has been selected
                selectedTerminal = Terminal.VILLA_PAMPANG;

                // Close the dialog
                dialog.dismiss();

                // Check if the selected terminal marker exists and is visible
                if (markerVillaTerminal != null && markerVillaTerminal.isVisible()) {
                    // Zoom in on the selected marker
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerVillaTerminal.getPosition(), 18f));
                }
                // Remove the current polyline if it exists
                if (currentPolyline != null) {
                    currentPolyline.remove();
                }

                // Draw the custom polyline for VILLA-PAMPANG
                if (selectedTerminal == Terminal.VILLA_PAMPANG) {
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(customRouteForVillaPampang)
                            .width(8) // Set the width of the polyline
                            .color(jeep_yellow); // Set the color of the polyline

                    currentPolyline = mMap.addPolyline(polylineOptions);
                }
            }

        });

        CardView buttonOption3 = dialogView.findViewById(R.id.buttonOption3);
        buttonOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap != null) {
                    markerCheckpointTerminal.setVisible(true); // Show the marker on the map
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerCheckpointTerminal.getPosition(), 18f));
                }
                // Set a variable to indicate that VILLA-PAMPANG has been selected
                selectedTerminal = Terminal.HOLY_HIGHWAY;

                // Close the dialog
                dialog.dismiss();

                // Check if the selected terminal marker exists and is visible
                if (markerCheckpointTerminal != null && markerCheckpointTerminal.isVisible()) {
                    // Zoom in on the selected marker
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerCheckpointTerminal.getPosition(), 18f));
                }

                // Remove the current polyline if it exists
                if (currentPolyline != null) {
                    currentPolyline.remove();
                }

                // Draw the custom polyline for VILLA-PAMPANG
                if (selectedTerminal == Terminal.HOLY_HIGHWAY) {
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(customRouteForBalibagoHolyHighway)
                            .width(8) // Set the width of the polyline
                            .color(jeep_purple); // Set the color of the polyline

                    currentPolyline = mMap.addPolyline(polylineOptions);
                }
            }
        });

        CardView buttonOption4 = dialogView.findViewById(R.id.buttonOption4);
        buttonOption4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap != null) {
                    markerBalibagoHighwayTerminal.setVisible(true); // Show the marker on the map
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerBalibagoHighwayTerminal.getPosition(), 18f));
                }
                // Set a variable to indicate that VILLA-PAMPANG has been selected
                selectedTerminal = Terminal.BALIBAGO_HIGHWAY;



                // Close the dialog
                dialog.dismiss();


                // Remove the current polyline if it exists
                if (currentPolyline != null) {
                    currentPolyline.remove();
                }

                // Check if the selected terminal marker exists and is visible
                if (markerBalibagoHighwayTerminal != null && markerBalibagoHighwayTerminal.isVisible()) {
                    // Zoom in on the selected marker
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerBalibagoHighwayTerminal.getPosition(), 18f));
                }

                // Draw the custom polyline for VILLA-PAMPANG
                if (selectedTerminal == Terminal.BALIBAGO_HIGHWAY) {
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(customRouteForBalibagoHighway)
                            .width(8) // Set the width of the polyline
                            .color(jeep_grey); // Set the color of the polyline

                    currentPolyline = mMap.addPolyline(polylineOptions);
                }
            }
        });

        CardView buttonOption5 = dialogView.findViewById(R.id.buttonOption5);
        buttonOption5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap != null) {
                    markerSapangbatoTerminal.setVisible(true); // Show the marker on the map
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerSapangbatoTerminal.getPosition(), 18f));
                }

                selectedTerminal = Terminal.SAPANG_BATO;

                dialog.dismiss();
                if (markerSapangbatoTerminal != null && markerSapangbatoTerminal.isVisible()) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerSapangbatoTerminal.getPosition(), 18f));
                }

                // Remove the current polyline if it exists
                if (currentPolyline != null) {
                    currentPolyline.remove();
                }

                if (selectedTerminal == Terminal.SAPANG_BATO) {
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(customRouteForSapangBato)
                            .width(8)
                            .color(Color.RED);

                    currentPolyline = mMap.addPolyline(polylineOptions);
                }
            }
        });
        CardView buttonOption6 = dialogView.findViewById(R.id.buttonOption6);
        buttonOption6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap != null) {
                    markerPandanTerminal.setVisible(true); // Show the marker on the map
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPandanTerminal.getPosition(), 18f));
                }
                selectedTerminal = Terminal.PANDAN_PAMPANG;

                dialog.dismiss();
                if (markerPandanTerminal != null && markerPandanTerminal.isVisible()) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPandanTerminal.getPosition(), 18f));
                }

                // Remove the current polyline if it exists
                if (currentPolyline != null) {
                    currentPolyline.remove();
                }

                if (selectedTerminal == Terminal.PANDAN_PAMPANG) {
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(customRouteForPandan)
                            .width(8)
                            .color(jeep_blue);

                    currentPolyline = mMap.addPolyline(polylineOptions);
                }
            }
        });
        CardView buttonOption7 = dialogView.findViewById(R.id.buttonOption7);
        buttonOption7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap != null) {
                    markerMainGateFriendshipTerminal.setVisible(true); // Show the marker on the map
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerMainGateFriendshipTerminal.getPosition(), 18f));
                }
                selectedTerminal = Terminal.MAINGATE_FRIENDSHIP;

                dialog.dismiss();
                if (markerMainGateFriendshipTerminal != null && markerMainGateFriendshipTerminal.isVisible()) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerMainGateFriendshipTerminal.getPosition(), 18f));
                }
                // Remove the current polyline if it exists
                if (currentPolyline != null) {
                    currentPolyline.remove();
                }


                if (selectedTerminal == Terminal.MAINGATE_FRIENDSHIP) {
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(customRouteForMaingate)
                            .width(8)
                            .color(jeep_brown);

                    currentPolyline = mMap.addPolyline(polylineOptions);
                }
            }
        });

        CardView buttonOption8 = dialogView.findViewById(R.id.buttonOption8);
        buttonOption8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap != null) {
                    markerCapayaAngelesTerminal.setVisible(true); // Show the marker on the map
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerCapayaAngelesTerminal.getPosition(), 18f));
                }
                selectedTerminal = Terminal.CAPAYA_ANGELES;

                dialog.dismiss();
                if (markerCapayaAngelesTerminal != null && markerCapayaAngelesTerminal.isVisible()) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerCapayaAngelesTerminal.getPosition(), 18f));
                }
                // Remove the current polyline if it exists
                if (currentPolyline != null) {
                    currentPolyline.remove();
                }


                if (selectedTerminal == Terminal.CAPAYA_ANGELES) {
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(customRouteForCapaya)
                            .width(8)
                            .color(jeep_pink);

                    currentPolyline = mMap.addPolyline(polylineOptions);
                }
            }
        });

        CardView buttonOption9 = dialogView.findViewById(R.id.buttonOption9);
        buttonOption9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap != null) {
                    markerSunsetNepoTerminal.setVisible(true); // Show the marker on the map
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerSunsetNepoTerminal.getPosition(), 18f));
                }
                selectedTerminal = Terminal.SUNSET_NEPO;

                dialog.dismiss();
                if (markerSunsetNepoTerminal != null && markerSunsetNepoTerminal.isVisible()) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerSunsetNepoTerminal.getPosition(), 18f));
                }

                // Remove the current polyline if it exists
                if (currentPolyline != null) {
                    currentPolyline.remove();
                }

                if (selectedTerminal == Terminal.SUNSET_NEPO) {
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(customRouteForSunset)
                            .width(8)
                            .color(jeep_orange);

                    currentPolyline = mMap.addPolyline(polylineOptions);
                }
            }
        });

        CardView buttonOption10 = dialogView.findViewById(R.id.buttonOption10);
        buttonOption10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap != null) {
                    markerCheckpointHensonvilleTerminal.setVisible(true); // Show the marker on the map
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerCheckpointHensonvilleTerminal.getPosition(), 18f));
                }
                selectedTerminal = Terminal.CHECKPOINT_HENSONVILLE;

                dialog.dismiss();
                if (markerCheckpointHensonvilleTerminal != null && markerCheckpointHensonvilleTerminal.isVisible()) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerCheckpointHensonvilleTerminal.getPosition(), 18f));
                }

                // Remove the current polyline if it exists
                if (currentPolyline != null) {
                    currentPolyline.remove();
                }

                if (selectedTerminal == Terminal.CHECKPOINT_HENSONVILLE) {
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(customRouteForHenson)
                            .width(8)
                            .color(jeep_white);

                    currentPolyline = mMap.addPolyline(polylineOptions);
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onMapReady(GoogleMap  googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);


        // Enable the My Location button
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                // Move the camera to the current location
                if (currentLocation != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                }
                return true;
            }
        });


        //NOTIFICATION TO OPEN LOCATION
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Location Services Required");
            alertDialogBuilder.setMessage("Please enable location services to use this feature.");
            alertDialogBuilder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        // Start location updates
        startLocationUpdates();

        // Add some markers to the map, and add a data object to each marker.
        markerMarisolTerm = mMap.addMarker(new MarkerOptions()
                .position(marisolTerminal)
                .title("Marisol Terminal")
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marisol)));
        markerMarisolTerm.setTag(0);

        markerVillaTerminal = mMap.addMarker(new MarkerOptions()
                .position(villaTerminal)
                .title("Villa Terminal")
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.villa)));
        markerVillaTerminal.setTag(0);

        markerCheckpointTerminal = mMap.addMarker(new MarkerOptions()
                .position(holyHighwayTerminal)
                .title("Holy Highway Terminal")
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.checkpoint)));
        markerCheckpointTerminal.setTag(0);

        markerBalibagoHighwayTerminal = mMap.addMarker(new MarkerOptions()
                .position(balibagoHighwayTerminal)
                .title("Balibago-Highway Terminal")
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.balibagohway)));
        markerBalibagoHighwayTerminal.setTag(0);

        markerSapangbatoTerminal = mMap.addMarker(new MarkerOptions()
                .position(sapangbatoTerminal)
                .title("Sapangbato Terminal")
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.sapangbato)));
        markerSapangbatoTerminal.setTag(0);


        markerPandanTerminal = mMap.addMarker(new MarkerOptions()
                .position(pandanTerminal)
                .title("Pandan Terminal")
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pandan)));
        markerPandanTerminal.setTag(0);

        markerMainGateFriendshipTerminal = mMap.addMarker(new MarkerOptions()
                .position(mainGateFriendshipTerminal)
                .title("MainGate-Friendship Terminal")
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.maingatefriendship)));
        markerMainGateFriendshipTerminal.setTag(0);


        markerCapayaAngelesTerminal = mMap.addMarker(new MarkerOptions()
                .position(capayaAngelesTerminal)
                .title("Capaya-Angeles Terminal")
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.capaya)));
        markerCapayaAngelesTerminal.setTag(0);

        markerSunsetNepoTerminal = mMap.addMarker(new MarkerOptions()
                .position(sunsetNepoTerminal)
                .title("Sunset-Nepo Terminal")
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.sunset)));
        markerSunsetNepoTerminal.setTag(0);

        markerCheckpointHensonvilleTerminal = mMap.addMarker(new MarkerOptions()
                .position(checkpointHensonvilleTerminal)
                .title("Checkpoint-Hensonville Terminal")
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.hensonville)));
        markerCheckpointHensonvilleTerminal.setTag(0);


    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    100);
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

        loadingAnimationView.setVisibility(View.VISIBLE);
        overlayView.setVisibility(View.VISIBLE);
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    public void ReachDestination() {
        View dialogView = getLayoutInflater().inflate(R.layout.here, null);

        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
        builder.setView(dialogView);

        // Create and show the AlertDialog
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Set click listeners for the buttons inside the dialog
        Button ButtonOk = dialogView.findViewById(R.id.ok_button);
        ButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void updateLocationOnMap(Location location) {



        loadingAnimationView.setVisibility(View.GONE);
        overlayView.setVisibility(View.GONE);


        if (!locationAnimationShown) {
            // Show the location found animation only if it hasn't been shown already
            locationfound.setVisibility(View.VISIBLE);

            locationfound.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    // Animation started
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    // Animation ended
                    locationfound.setVisibility(View.GONE); // Hide the animation view
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    // Animation canceled
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    // Animation repeated
                }
            });

            locationAnimationShown = true; // Set the flag to true to indicate the animation has been shown
        }



        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());


        mlocation = currentLocation;

        if (currentLocationMarker == null) {
            // Get the user's chosen avatar from Firebase Realtime Database
            DatabaseReference avatarRef = FirebaseDatabase.getInstance().getReference("Users/Customers/" + userID + "/avatar");
            avatarRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Integer selectedAvatarResourceId = snapshot.getValue(Integer.class);

                    if (selectedAvatarResourceId != null) {
                        // Convert the selected avatar resource ID to a Drawable
                        Drawable avatarDrawable = getResources().getDrawable(selectedAvatarResourceId);

                        // Define the desired dimensions for the marker
                        int markerWidth = 140;  // Set the width as needed
                        int markerHeight = 140; // Set the height as needed

                        // Create a new Bitmap with the desired dimensions
                        Bitmap avatarBitmap = Bitmap.createBitmap(markerWidth, markerHeight, Bitmap.Config.ARGB_8888);

                        // Create a Canvas to draw the avatar onto the new Bitmap
                        Canvas canvas = new Canvas(avatarBitmap);

                        // Set bounds for the avatar drawable
                        avatarDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());

                        // Draw the avatar onto the new Bitmap
                        avatarDrawable.draw(canvas);

                        // Set the user's chosen avatar as the marker icon
                        currentLocationMarker = mMap.addMarker(new MarkerOptions()
                                .position(currentLocation)
                                .title("You are here")
                                .icon(BitmapDescriptorFactory.fromBitmap(avatarBitmap))); // Use the resized avatarBitmap as the marker icon

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                    } else {
                        // Default behavior when no avatar is chosen
                        currentLocationMarker = mMap.addMarker(new MarkerOptions()
                                .position(currentLocation)
                                .title("You are here"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                    }


                    boolean isOutsideAngelesCity = !isLocationWithinAngelesCity(mlocation);

                    if (isOutsideAngelesCity) {
                        // Suggest the nearest terminal
                        suggestNearestTerminal(mlocation);
                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle errors, if any
                    Toast.makeText(MapActivity.this, "Error reading avatar data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            currentLocationMarker.setPosition(currentLocation);
        }




    }


    public void Nearna(){
        // Calculate the distance between the user's location and the selected place
        float[] results = new float[1];
        Location.distanceBetween(
                currentLocation.latitude,
                currentLocation.longitude,
                selectedPlaceLatLng.latitude,
                selectedPlaceLatLng.longitude,
                results
        );
        float distanceToSelectedPlace = results[0];

        if (distanceToSelectedPlace <= thresholdDistance) {
        }
    }
    private void showCountdownDialog(int seconds) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.countdown, null);
        builder.setView(dialogView);

        final TextView countdownText = dialogView.findViewById(R.id.countdown_text);
        final Button cancelButton = dialogView.findViewById(R.id.cancel_button);

        countdownDialog = builder.create();
        countdownDialog.setCancelable(false);

        countDownTimer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownText.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                countdownDialog.dismiss();

                String phoneNumber = "+639954933152"; // Replace with the recipient's phone number
                String message = "SOS! Emergency at my location! https://maps.google.com/maps?q=" + currentLocation.latitude + "," + currentLocation.longitude;
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number1, null, message, null, null);
                smsManager.sendTextMessage(number2, null, message, null, null);

                // Create an Intent to initiate a phone call
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));

                // Check for CALL_PHONE permission before making the call
                if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // Request the permission if it's not granted
                    ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    return;
                }

                // Start the phone call
                startActivity(callIntent);
            }
        };

        countDownTimer.start();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                countdownDialog.dismiss();
            }
        });

        countdownDialog.show();
    }

    // Check if a location is within Angeles City
    private boolean isLocationWithinAngelesCity(LatLng location) {
        // Define the bounds of Angeles City
        LatLngBounds angelesCityBounds = new LatLngBounds(
                new LatLng(15.1266, 120.5662),  // Southwest corner of the bounds
                new LatLng(15.1807, 120.6203)   // Northeast corner of the bounds
        );

        // Check if the location is within the bounds of Angeles City
        return angelesCityBounds.contains(location);
    }

    private void suggestNearestTerminal(LatLng userLocation) {
        // Calculate the distance to each terminal marker and find the nearest one
        double nearestTerminalDistance = Double.MAX_VALUE;
        Marker nearestTerminalMarker = null;

        List<Marker> terminalMarkers = Arrays.asList(
                markerMarisolTerm,
                markerVillaTerminal,
                markerBalibagoHighwayTerminal,
                markerCheckpointTerminal,
                markerSapangbatoTerminal,
                markerPandanTerminal,
                markerMainGateFriendshipTerminal,
                markerCapayaAngelesTerminal,
                markerSunsetNepoTerminal,
                markerCheckpointHensonvilleTerminal
        );

        for (Marker marker : terminalMarkers) {
            if (marker != null) {
                // Get the LatLng position of the terminal marker
                LatLng terminalLocation = marker.getPosition();

                // Calculate the distance between userLocation and terminalLocation
                float[] results = new float[1];
                Location.distanceBetween(
                        userLocation.latitude,
                        userLocation.longitude,
                        terminalLocation.latitude,
                        terminalLocation.longitude,
                        results
                );

                float distance = results[0];

                // Check if this terminal marker is closer than the current nearest terminal
                if (distance < nearestTerminalDistance) {
                    nearestTerminalDistance = distance;
                    nearestTerminalMarker = marker;
                }
            }
        }
// Show a suggestion to the user
        if (nearestTerminalMarker != null) {
            if ("Holy Highway Terminal".equals(nearestTerminalMarker.getTitle())) {
                // Show a special dialog for "Holy Highway"
                nterminalbalibago(nearestTerminalMarker);
            } else if  ("Marisol Terminal".equals(nearestTerminalMarker.getTitle())) {
                nterminalmarisol(nearestTerminalMarker);

            }else if  ("Villa Terminal".equals(nearestTerminalMarker.getTitle())) {
                nterminalvillapampang(nearestTerminalMarker);

            }else if  ("Balibago-Highway Terminal".equals(nearestTerminalMarker.getTitle())) {
                nterminalgrey(nearestTerminalMarker);
            }else if  ("Sapangbato Terminal".equals(nearestTerminalMarker.getTitle())) {
                nterminalsapangbato(nearestTerminalMarker);
            }else if  ("Pandan Terminal".equals(nearestTerminalMarker.getTitle())) {
                nterminalpandan(nearestTerminalMarker);
            }else if  ("Checkpoint-Hensonville Terminal".equals(nearestTerminalMarker.getTitle())) {
                nterminalhenson(nearestTerminalMarker);
            }else if  ("MainGate-Friendship Terminal".equals(nearestTerminalMarker.getTitle())) {
                nterminalfriendship(nearestTerminalMarker);
            }else if  ("Capaya-Angeles Terminal".equals(nearestTerminalMarker.getTitle())) {
                nterminalcapaya(nearestTerminalMarker);
            }else if  ("Sunset-Nepo Terminal".equals(nearestTerminalMarker.getTitle())) {
                nterminalsunset(nearestTerminalMarker);
            }
        }
    }


    private void nterminalbalibago(Marker terminalMarker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customDialogView = getLayoutInflater().inflate(R.layout.nterminalbalibago, null);
        builder.setView(customDialogView);

        Button buttonBalibago = customDialogView.findViewById(R.id.buttonBalibago);

        AlertDialog dialog = builder.create();
        dialog.show();

        buttonBalibago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the user clicks the middle button
                dialog.dismiss();
            }
        });
    }


    private void nterminalfriendship(Marker terminalMarker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customDialogView = getLayoutInflater().inflate(R.layout.nterminalfriendship, null);
        builder.setView(customDialogView);

        Button button = customDialogView.findViewById(R.id.button);

        AlertDialog dialog = builder.create();
        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the user clicks the middle button
                dialog.dismiss();
            }
        });
    }


    private void nterminalcapaya(Marker terminalMarker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customDialogView = getLayoutInflater().inflate(R.layout.nterminalcapaya, null);
        builder.setView(customDialogView);

        Button button = customDialogView.findViewById(R.id.button);

        AlertDialog dialog = builder.create();
        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the user clicks the middle button
                dialog.dismiss();
            }
        });
    }


    private void nterminalgrey(Marker terminalMarker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customDialogView = getLayoutInflater().inflate(R.layout.nterminalgrey, null);
        builder.setView(customDialogView);

        Button button = customDialogView.findViewById(R.id.button);

        AlertDialog dialog = builder.create();
        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the user clicks the middle button
                dialog.dismiss();
            }
        });
    }


    private void nterminalmarisol(Marker terminalMarker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customDialogView = getLayoutInflater().inflate(R.layout.nterminalmarisol, null);
        builder.setView(customDialogView);

        Button button = customDialogView.findViewById(R.id.button);

        AlertDialog dialog = builder.create();
        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the user clicks the middle button
                dialog.dismiss();
            }
        });
    }


    private void nterminalhenson(Marker terminalMarker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customDialogView = getLayoutInflater().inflate(R.layout.nterminalhenson, null);
        builder.setView(customDialogView);

        Button button = customDialogView.findViewById(R.id.button);

        AlertDialog dialog = builder.create();
        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the user clicks the middle button
                dialog.dismiss();
            }
        });
    }

    private void nterminalpandan(Marker terminalMarker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customDialogView = getLayoutInflater().inflate(R.layout.nterminalpandan, null);
        builder.setView(customDialogView);

        Button button = customDialogView.findViewById(R.id.button);

        AlertDialog dialog = builder.create();
        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the user clicks the middle button
                dialog.dismiss();
            }
        });
    }


    private void nterminalsapangbato(Marker terminalMarker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customDialogView = getLayoutInflater().inflate(R.layout.nterminalsapangbato, null);
        builder.setView(customDialogView);

        Button button = customDialogView.findViewById(R.id.button);

        AlertDialog dialog = builder.create();
        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the user clicks the middle button
                dialog.dismiss();
            }
        });
    }

    private void nterminalsunset(Marker terminalMarker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customDialogView = getLayoutInflater().inflate(R.layout.nterminalsunset, null);
        builder.setView(customDialogView);

        Button button = customDialogView.findViewById(R.id.button);

        AlertDialog dialog = builder.create();
        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the user clicks the middle button
                dialog.dismiss();
            }
        });
    }


    private void nterminalvillapampang(Marker terminalMarker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customDialogView = getLayoutInflater().inflate(R.layout.nterminalvillapampang, null);
        builder.setView(customDialogView);

        Button button = customDialogView.findViewById(R.id.button);

        AlertDialog dialog = builder.create();
        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the user clicks the middle button
                dialog.dismiss();
            }
        });
    }


    private void toggleSidebar() {
        if (isSidebarVisible) {
            hideSidebar();
        } else {
            showSidebar();
        }
    }

    private void showSidebar() {
        int sidebarWidth = sidebarLayout.getWidth();
        sidebarLayout.animate()
                .translationX(0)
                .setDuration(500)
                .withStartAction(() -> {
                    sidebarLayout.setVisibility(View.VISIBLE);
                    sidebarLayout.requestFocus();
                })
                .withEndAction(() -> isSidebarVisible = true)
                .start();
    }


    private void hideSidebar() {
        int sidebarWidth = sidebarLayout.getWidth();
        sidebarLayout.animate()
                .translationX(-sidebarWidth)
                .setDuration(500)
                .withEndAction(() -> {
                    sidebarLayout.setVisibility(View.GONE);
                    hamburgerButton.requestFocus();
                    isSidebarVisible = false;
                })
                .start();
    }

    private void signOut() {
        // Perform sign out operations
        FirebaseAuth.getInstance().signOut();

        // Start the login activity
        Intent intent = new Intent(MapActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }





}


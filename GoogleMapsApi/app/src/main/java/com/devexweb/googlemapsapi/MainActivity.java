package com.devexweb.googlemapsapi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.devexweb.googlemapsapi.Adapters.PagerAdapter;
import com.devexweb.googlemapsapi.lib.BottomSheetBehaviorGoogleMapsLike;
import com.devexweb.googlemapsapi.lib.MergedAppBarLayoutBehavior;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    GoogleMap googleMapFinal;

    int[] mDrawables = {
            R.drawable.dummyimage,
            R.drawable.dummyimage_white,
            R.drawable.dummyimage
    };

    LatLng[] firstLine = {new LatLng(20.729948, -103.432988), new LatLng(20.729923, -103.433366), new LatLng(20.729429, -103.435992), new LatLng(20.730197, -103.443179), new LatLng(20.730719, -103.447672)};
    LatLng[] secondLine = {new LatLng(20.72376561460881, -103.43017727136612), new LatLng(20.72202960990537, -103.43024164438248), new LatLng(20.72186905355178, -103.43023627996445), new LatLng(20.72165832307937, -103.43019872903824), new LatLng(20.721482714128395, -103.43006998300552), new LatLng(20.721297070158823, -103.4299573302269), new LatLng(20.721256930892253, -103.42987686395645), new LatLng(20.721141530441564, -103.42974811792374), new LatLng(20.721086338890597, -103.42966765165329), new LatLng(20.721026129902956, -103.42948526144028), new LatLng(20.72093079895692, -103.42929750680923), new LatLng(20.720865572485586, -103.42900782823563), new LatLng(20.720865572485586, -103.42871278524399), new LatLng(20.720875607329162, -103.4282997250557), new LatLng(20.72090069443518, -103.42674940824509), new LatLng(20.720925781537044, -103.42545658349991), new LatLng(20.720935816376624, -103.42423349618912), new LatLng(20.72113149561561, -103.42369168996811), new LatLng(20.72136731384975, -103.4234556555748), new LatLng(20.72155797513232, -103.42317134141922), new LatLng(20.721568009930024, -103.42286556959152), new LatLng(20.721648288287664, -103.42256516218185), new LatLng(20.72166334047498, -103.42240422964096), new LatLng(20.722370791591633, -103.42169344425201), new LatLng(20.722792250260564, -103.42115700244904), new LatLng(20.723414401390148, -103.42053472995758), new LatLng(20.72387599573788, -103.41988027095795), new LatLng(20.724327554064462, -103.41951549053192), new LatLng(20.72479918021242, -103.4189361333847), new LatLng(20.725140355659143, -103.41861426830292)};
    LatLng[] thirdLine = {new LatLng(20.713309146559133, -103.3765760064125), new LatLng(20.71376575404382, -103.37611466646194), new LatLng(20.71451338310817, -103.37532609701157), new LatLng(20.715020163127747, -103.37500423192978), new LatLng(20.715466730254864, -103.37477892637253), new LatLng(20.715722627678293, -103.37472528219223), new LatLng(20.715978524669485, -103.37466090917587), new LatLng(20.71625950910313, -103.37463408708572), new LatLng(20.716565580839628, -103.37460726499557), new LatLng(20.71671861647597, -103.37460927665234), new LatLng(20.71689924850357, -103.37459184229374), new LatLng(20.717040994318857, -103.37458781898022), new LatLng(20.717624283224897, -103.37454490363598), new LatLng(20.71807586019037, -103.37451405823231), new LatLng(20.721698462239573, -103.37424248456955), new LatLng(20.723148482268485, -103.3741083741188), new LatLng(20.72411180969863, -103.37408155202866), new LatLng(20.72419208670787, -103.37532073259354), new LatLng(20.724287415601072, -103.37665110826492), new LatLng(20.724437934783932, -103.37915629148483), new LatLng(20.72482928395923, -103.38362887501717), new LatLng(20.725067605076898, -103.38564857840538), new LatLng(20.725195545732706, -103.3867509663105), new LatLng(20.725208088928444, -103.38679924607277), new LatLng(20.725524177117897, -103.38669866323471), new LatLng(20.726389653307567, -103.38646396994591), new LatLng(20.727142237277487, -103.38626012206078)};

    FloatingActionButton viewRoute;
    @BindView(R.id.bottom_sheet_title)
    TextView bottomSheetTextView;
    BottomSheetBehaviorGoogleMapsLike behavior;

    private static final long DISTANCE_CHANGE_UPDATES = 1;
    private static final long TIME_UPDATES = 1;
    Location locationUpdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                googleMapFinal  = googleMap;
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);

                LocationManager  locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_CHANGE_UPDATES, new android.location.LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        locationUpdated = location;
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                });

                PolylineOptions lineOptions = new PolylineOptions()
                        .addAll(Arrays.asList(firstLine))
                        .color(Color.parseColor("#3399ff"));
                // Closes the polyline.
                Polyline polyline = googleMap.addPolyline(lineOptions);
                polyline.setClickable(true);
                final String PolyId = polyline.getId();

                PolylineOptions RouteLineOptions = new PolylineOptions()
                        .addAll(Arrays.asList(secondLine))
                        .color(Color.parseColor("#F76C60"));
                Polyline RouteLine = googleMap.addPolyline(RouteLineOptions);
                RouteLine.setClickable(true);
                final String RouteLineId = RouteLine.getId();

                PolylineOptions RouteLineOptions2 = new PolylineOptions()
                        .addAll(Arrays.asList(thirdLine))
                        .color(Color.parseColor("#81B71A"));
                Polyline RouteLine2 = googleMap.addPolyline(RouteLineOptions2);
                RouteLine2.setClickable(true);
                final String RouteLineId2 = RouteLine2.getId();

                googleMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
                    @Override
                    public void onPolylineClick(Polyline polyline) {
                        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT);
                    }
                });
            }
        });

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehaviorGoogleMapsLike.from(bottomSheet);

        final AppBarLayout mergedAppBarLayout = (AppBarLayout) findViewById(R.id.merged_appbarlayout);
        MergedAppBarLayoutBehavior mergedAppBarLayoutBehavior = MergedAppBarLayoutBehavior.from(mergedAppBarLayout);
        mergedAppBarLayoutBehavior.setToolbarTitle("Ruta");

        behavior.addBottomSheetCallback(new BottomSheetBehaviorGoogleMapsLike.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED:
                        //Log.d("bottomsheet-", "STATE_COLLAPSED");
                        bottomSheet.scrollTo(0, 0);
                        bottomSheet.scrollTo(0, 0);
                        mergedAppBarLayout.setBackgroundColor(Color.TRANSPARENT);
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_DRAGGING:
                        //Log.d("bottomsheet-", "STATE_DRAGGING");
                        bottomSheet.scrollTo(0, 0);
                        mergedAppBarLayout.setBackgroundColor(Color.TRANSPARENT);
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_EXPANDED:
                        //Log.d("bottomsheet-", "STATE_EXPANDED");
                        mergedAppBarLayout.setBackgroundColor(Color.parseColor("#3F51B5"));
                        mergedAppBarLayout.setElevation(8);
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT:
                        //Log.d("bottomsheet-", "STATE_ANCHOR_POINT");
                        bottomSheet.scrollTo(0, 0);
                        mergedAppBarLayout.setBackgroundColor(Color.TRANSPARENT);
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN:
                        //Log.d("bottomsheet-", "STATE_HIDDEN");
                        bottomSheet.scrollTo(0, 0);
                        break;
                    default:
                        //Log.d("bottomsheet-", "STATE_SETTLING");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        viewRoute = (FloatingActionButton) findViewById(R.id.viewRoute);
        viewRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT);
                upDateMapLocation(googleMapFinal, locationUpdated);
            }
        });

        mergedAppBarLayoutBehavior.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);
            }
        });

        PagerAdapter adapter = new PagerAdapter(this, mDrawables);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
    }

    public void upDateMapLocation(GoogleMap googleMap, Location location){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));

    }

    @Override
    public void onBackPressed() {
        if (behavior.getState() == BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED) {
            super.onBackPressed();
        } else {
            behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}
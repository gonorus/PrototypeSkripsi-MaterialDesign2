package com.prototypeskripsi_materialdesign2.UserInterface;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.prototypeskripsi_materialdesign2.CustomActionEvent.OnInfoWindowElemTouchListener;
import com.prototypeskripsi_materialdesign2.CustomLayout.CustomFloatingActionButton;
import com.prototypeskripsi_materialdesign2.CustomLayout.MapWrapperLayout;
import com.prototypeskripsi_materialdesign2.DataControl.ActivityMaps_RetrieveData;
import com.prototypeskripsi_materialdesign2.R;

public class ActivityMaps extends AppCompatActivity {

    private View screen_overlay, screen_sheet;
    private FragmentMapsGraph mapsGraph;
    private MaterialSheetFab materialSheetFab;
    private ViewGroup infoWindow1, infoWindow2;
    private OnInfoWindowElemTouchListener infoWindowTouchListener1, infoWindowTouchListener2;
    private MapWrapperLayout mapWrapperLayout1, mapWrapperLayout2;
    private MapFragment myMapFragment1, myMapFragment2;

    public static GoogleMap myMap1, myMap2;
    public static String source = "DaftarCurahHujanBoyolali2001-2013";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        source = getIntent().getStringExtra("fileDir");

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FragmentNavigationDrawer navigationDrawerFragment = (FragmentNavigationDrawer) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        navigationDrawerFragment.setup(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        mapsGraph = (FragmentMapsGraph) getFragmentManager().findFragmentById(R.id.sheet_fragment);
        screen_overlay = findViewById(R.id.screen_overlay1);
        screen_overlay.setBackgroundColor(Color.BLACK);
        screen_overlay.setAlpha(0.5f);
        screen_sheet = findViewById(R.id.screen_sheet);
        screen_sheet.setVisibility(View.INVISIBLE);
        screen_overlay.setVisibility(View.GONE);

        infoWindow1 = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_custom_marker, null);
        Button infoButton1 = (Button) infoWindow1.findViewById(R.id.buttonDetails);
        infoWindowTouchListener1 =
                new OnInfoWindowElemTouchListener(
                        infoButton1,
                        getResources().getDrawable(R.drawable.layout_custom_maps_data_item_default),
                        getResources().getDrawable(R.drawable.layout_custom_maps_data_item_pressed)
                ) {
                    @Override
                    protected void onClickConfirmed(View v, Marker marker) {
                        mapsGraph.changeDataset(marker.getTitle(), "Aktual", source);
                        showGraph();
                    }
                };
        infoButton1.setOnTouchListener(infoWindowTouchListener1);

        infoWindow2 = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_custom_marker, null);
        Button infoButton2 = (Button) infoWindow2.findViewById(R.id.buttonDetails);
        infoWindowTouchListener2 =
                new OnInfoWindowElemTouchListener(
                        infoButton2,
                        getResources().getDrawable(R.drawable.layout_custom_maps_data_item_default),
                        getResources().getDrawable(R.drawable.layout_custom_maps_data_item_pressed)
                ) {
                    @Override
                    protected void onClickConfirmed(View v, Marker marker) {
                        mapsGraph.changeDataset(marker.getTitle(), "Prediksi", source);
                        showGraph();
                    }
                };
        infoButton2.setOnTouchListener(infoWindowTouchListener2);

        myMapFragment1 = (MapFragment) getFragmentManager().findFragmentById(R.id.map1);
        myMapFragment2 = (MapFragment) getFragmentManager().findFragmentById(R.id.map2);

        myMapFragment1.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                myMap1 = googleMap;
                createMapView(myMap1);
                myMap1.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        //mapsGraph.changeDataset(1);
                        //showGraph(myMap1);
                    }
                });
                myMap1.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        myMap1.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 10.5f));
                        myMap1.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                mapWrapperLayout1 = (MapWrapperLayout) findViewById(R.id.map_relative_layout1);
                                mapWrapperLayout1.init(myMap1, getPixelsFromDp(ActivityMaps.this, 39 + 20));
                                infoWindowTouchListener1.setMarker(marker);
                                mapWrapperLayout1.setMarkerWithInfoWindow(marker, infoWindow1);
                                return infoWindow1;
                            }
                        });
                        marker.showInfoWindow();
                        return true;
                    }
                });
            }
        });
        myMapFragment2.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                myMap2 = googleMap;
                createMapView(myMap2);
                myMap2.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        //mapsGraph.changeDataset(2);
                        //showGraph(myMap2);
                    }
                });
                myMap2.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        myMap2.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 10.5f));
                        myMap2.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                mapWrapperLayout2 = (MapWrapperLayout) findViewById(R.id.map_relative_layout2);
                                mapWrapperLayout2.init(myMap2, getPixelsFromDp(ActivityMaps.this, 39 + 20));
                                infoWindowTouchListener2.setMarker(marker);
                                mapWrapperLayout2.setMarkerWithInfoWindow(marker, infoWindow2);
                                return infoWindow2;
                            }
                        });
                        marker.showInfoWindow();
                        return true;
                    }
                });
            }
        });
        setupFab();
        setupMap();
    }

    @Override
    public void onBackPressed() {
        if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();
        } else if (screen_overlay.isShown()) {
            screen_sheet.setVisibility(View.INVISIBLE);
            screen_overlay.setVisibility(View.GONE);
            materialSheetFab.showFab();
            myMap1.getUiSettings().setScrollGesturesEnabled(true);
            myMap2.getUiSettings().setScrollGesturesEnabled(true);
        } else {
            super.onBackPressed();
            Intent intent = new Intent(ActivityMaps.this, ActivityMapsChooser.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.translate_back_in, R.anim.translate_back_out);
        }
    }

    private void createMapView(GoogleMap googleMap) {
        LatLng Boyolali = new LatLng(-7.382324, 110.61245);
        googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(Boyolali, 10.5f)
        );
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
    }

    private void setupFab() {
        CustomFloatingActionButton fab = (CustomFloatingActionButton) findViewById(R.id.button1);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.screen_overlay2);
        int sheetColor = Color.BLACK;
        int fabColor = Color.BLUE;

        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);
    }

    public void setupMap(){
        new ActivityMaps_RetrieveData(source, getApplication(), new ProgressDialog(ActivityMaps.this)).execute();
    }

    private void showGraph() {
        materialSheetFab.hideSheetThenFab();
        screen_overlay.setVisibility(View.VISIBLE);
        screen_sheet.setVisibility(View.VISIBLE);
        myMap1.getUiSettings().setScrollGesturesEnabled(false);
        myMap2.getUiSettings().setScrollGesturesEnabled(false);
        if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();
        }
    }

    private int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}

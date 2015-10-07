package com.prototypeskripsi_materialdesign2.DataControl;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.prototypeskripsi_materialdesign2.DataAccessObject.ObjectMaps;
import com.prototypeskripsi_materialdesign2.UserInterface.ActivityMaps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ActivityMaps_RetrieveData extends AsyncTask<Void, String, Boolean> {
    public List<ObjectMaps> dataWilayah = new ArrayList<>();
    public List<ObjectMaps> dataKecamatan = new ArrayList<>();
    public List<PolygonOptions> boundaryMapKabupaten = new ArrayList<>();
    public List<PolygonOptions> boundaryMapKecamatan = new ArrayList<>();

    private List<Double> resultHasil = new ArrayList<>();
    private List<LatLng> sangatKering1 = new ArrayList<>();
    private List<LatLng> kering1 = new ArrayList<>();
    private List<LatLng> agakKering1 = new ArrayList<>();
    private List<LatLng> normal1 = new ArrayList<>();
    private List<LatLng> agakBasah1 = new ArrayList<>();
    private List<LatLng> basah1 = new ArrayList<>();
    private List<LatLng> sangatBasah1 = new ArrayList<>();
    private List<LatLng> sangatKering2 = new ArrayList<>();
    private List<LatLng> kering2 = new ArrayList<>();
    private List<LatLng> agakKering2 = new ArrayList<>();
    private List<LatLng> normal2 = new ArrayList<>();
    private List<LatLng> agakBasah2 = new ArrayList<>();
    private List<LatLng> basah2 = new ArrayList<>();
    private List<LatLng> sangatBasah2 = new ArrayList<>();

    private Application application;
    private ProgressDialog dialog;
    private InputStream inputStream;
    private String source = "";
    private String directory = Environment.getExternalStorageDirectory() + "/PrototypeSkripsi4";

    public ActivityMaps_RetrieveData(String source, Application application, ProgressDialog dialog) {
        this.source = source;
        this.application = application;
        this.dialog = dialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Proses Membaca Data Peta Sedang Berlangsung");
        dialog.setTitle("Menyiapkan Peta");
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        ReadBoundaryKabupaten();
        ReadBoundaryKecamatan();

        publishProgress("Proses Menggambar Batas Wilayah");
        publishProgress("boundary");

        publishProgress("Proses Menggambar Peta Aktual");
        ReadMapsData();
        publishProgress("map1");

        publishProgress("Proses Menggambar Peta Prediksi");
        ReadMapsDataPrediksi();
        publishProgress("map2");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Log.e("ERROR", "INTERRUPTED EXCEPTION ACTIVITY-MAPS-RETRIEVE-DATA-PROGRESS");
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if (values[0].equalsIgnoreCase("boundary")) {
            DrawBoundary();
        } else if (values[0].equalsIgnoreCase("map1")) {
            DrawHeatMap(sangatKering1, kering1, agakKering1, normal1, agakBasah1, basah1, sangatBasah1, ActivityMaps.myMap1);
        } else if (values[0].equalsIgnoreCase("map2")) {
            DrawHeatMap(sangatKering2, kering2, agakKering2, normal2, agakBasah2, basah2, sangatBasah2, ActivityMaps.myMap2);
        } else {
            dialog.setMessage(values[0]);
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean) {
            CaptureMapScreen(ActivityMaps.myMap1, "screenshot1");
            CaptureMapScreen(ActivityMaps.myMap2, "screenshot2");
            dialog.cancel();
        } else {
            dialog.cancel();
            Log.e("ERROR", "UNABLE TO READ THE DATA ERROR ACTIVITY-MAPS-RETRIEVE-DATA-POST-EXECUTE");
            Toast.makeText(application.getApplicationContext(), "Unable to read the data", Toast.LENGTH_SHORT).show();
        }
    }

    private void ReadBoundaryKabupaten() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset("kml_kota.json"));
            JSONArray data1 = obj.getJSONArray("result");
            for (int i = 0; i < data1.length(); i++) {
                JSONObject temp = data1.getJSONObject(i);
                ObjectMaps wilayah = new ObjectMaps();

                //NAMA KOTA
                wilayah.setNamaKota(temp.getString("kota"));

                //KOORDINAT
                wilayah.setLongitude(Double.parseDouble(temp.getString("koordinatLongitude")));
                wilayah.setLatitude(Double.parseDouble(temp.getString("koordinatLatitude")));

                //BOUNDARY
                List<String> templatitude = new ArrayList<>();
                List<String> templongitude = new ArrayList<>();
                JSONArray jsonBoundaryLatitude = temp.getJSONArray("boundaryLatitude");
                for (int j = 0; j < jsonBoundaryLatitude.length(); j++) {
                    templatitude.add(jsonBoundaryLatitude.getString(j));
                }
                JSONArray jsonBoundaryLongitude = temp.getJSONArray("boundaryLongitude");
                for (int j = 0; j < jsonBoundaryLongitude.length(); j++) {
                    templongitude.add(jsonBoundaryLongitude.getString(j));
                }
                wilayah.setBoundaryLatitude(templatitude);
                wilayah.setBoundaryLongitude(templongitude);

                dataWilayah.add(wilayah);
            }

            List<List<LatLng>> tempBoundary1 = new ArrayList<>();
            for (ObjectMaps wilayah : dataWilayah) {
                List<LatLng> temp = new ArrayList<>();
                for (int i = 0; i < wilayah.getBoundaryLatitude().size(); i++) {
                    float lati = Float.parseFloat(wilayah.getBoundaryLatitude().get(i));
                    float longi = Float.parseFloat(wilayah.getBoundaryLongitude().get(i));
                    temp.add(new LatLng(lati, longi));
                }
                tempBoundary1.add(temp);
            }
            for (List<LatLng> titik : tempBoundary1) {
                PolygonOptions polygonOptions = new PolygonOptions();
                polygonOptions.addAll(titik);
                polygonOptions.strokeWidth(2);
                boundaryMapKabupaten.add(polygonOptions);
            }
        } catch (JSONException e) {
            Log.e("ERROR", "JSON EXCEPTION ACTIVITY-MAPS-RETRIEVE-DATA-READ-BOUNDARY-KABUPATEN");
        }
    }

    private void ReadBoundaryKecamatan() {
        try {
            JSONArray data1 = new JSONArray(loadJSONFromAsset("kml_kecamatan.json"));
            for (int i = 0; i < data1.length(); i++) {
                JSONObject temp = data1.getJSONObject(i);
                ObjectMaps wilayah = new ObjectMaps();

                //NAMA KOTA
                wilayah.setNamaKota(temp.getString("kota"));

                //BOUNDARY
                List<String> templatitude = new ArrayList<>();
                List<String> templongitude = new ArrayList<>();
                JSONArray jsonBoundaryLatitude = temp.getJSONArray("boundaryLatitude");
                for (int j = 0; j < jsonBoundaryLatitude.length(); j++) {
                    templatitude.add(jsonBoundaryLatitude.getString(j));
                }
                JSONArray jsonBoundaryLongitude = temp.getJSONArray("boundaryLongitude");
                for (int j = 0; j < jsonBoundaryLongitude.length(); j++) {
                    templongitude.add(jsonBoundaryLongitude.getString(j));
                }
                wilayah.setBoundaryLatitude(templatitude);
                wilayah.setBoundaryLongitude(templongitude);

                dataKecamatan.add(wilayah);
            }

            List<List<LatLng>> tempBoundary2 = new ArrayList<>();
            for (ObjectMaps wilayah : dataKecamatan) {
                List<LatLng> temp = new ArrayList<>();
                for (int i = 0; i < wilayah.getBoundaryLatitude().size(); i++) {
                    float lati = Float.parseFloat(wilayah.getBoundaryLatitude().get(i));
                    float longi = Float.parseFloat(wilayah.getBoundaryLongitude().get(i));
                    temp.add(new LatLng(lati, longi));
                }
                tempBoundary2.add(temp);
            }
            for (List<LatLng> titik : tempBoundary2) {
                PolygonOptions polygonOptions = new PolygonOptions();
                polygonOptions.addAll(titik);
                polygonOptions.strokeWidth(2);
                boundaryMapKecamatan.add(polygonOptions);
            }
        } catch (JSONException e) {
            Log.e("ERROR", "JSON EXCEPTION ACTIVITY-MAPS-RETRIEVE-DATA-READ-BOUNDARY-KECAMATAN");
        }
    }

    private String loadJSONFromAsset(String file) {
        String json;
        try {
            AssetManager assetManager = application.getAssets();
            InputStream is = assetManager.open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            Log.e("ERROR", "IO EXCEPTION ACTIVITY-MAPS-RETRIEVE-DATA-LOAD-JSON-FROM-ASSET");
            return null;
        }
        return json;
    }

    private void DrawBoundary() {
        for (PolygonOptions options : boundaryMapKabupaten) {
            ActivityMaps.myMap1.addPolygon(options);
            ActivityMaps.myMap2.addPolygon(options);
        }

        for (PolygonOptions options : boundaryMapKecamatan) {
            ActivityMaps.myMap1.addPolygon(options);
            ActivityMaps.myMap2.addPolygon(options);
        }

        for (int i = 0; i < dataKecamatan.size(); i++) {
            LatLng pos = getPolygonCenterPoint((ArrayList<LatLng>) boundaryMapKecamatan.get(i).getPoints());
            String[] tempString = dataKecamatan.get(i).getNamaKota().split(" ");
            addText(application.getApplicationContext(), ActivityMaps.myMap1, pos, tempString[1], 8, 16, "petaSPI");
            addText(application.getApplicationContext(), ActivityMaps.myMap2, pos, tempString[1], 8, 16, "petaPrediksi");
        }
    }

    private LatLng getPolygonCenterPoint(ArrayList<LatLng> polygonPointsList) {
        LatLng centerLatLng;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < polygonPointsList.size(); i++) {
            builder.include(polygonPointsList.get(i));
        }
        LatLngBounds bounds = builder.build();
        centerLatLng = bounds.getCenter();

        return centerLatLng;
    }

    private Marker addText(final Context context, final GoogleMap map,
                           final LatLng location, final String text, final int padding,
                           final int fontSize, final String jenisPeta) {
        Marker marker = null;

        if (context == null || map == null || location == null || text == null
                || fontSize <= 0) {
            return marker;
        }

        final TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextSize(fontSize);

        final Paint paintText = textView.getPaint();

        final Rect boundsText = new Rect();
        paintText.getTextBounds(text, 0, textView.length(), boundsText);
        paintText.setTextAlign(Paint.Align.CENTER);

        final Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        final Bitmap bmpText = Bitmap.createBitmap(boundsText.width() + 2
                * padding, boundsText.height() + 2 * padding, conf);

        final Canvas canvasText = new Canvas(bmpText);
        paintText.setColor(Color.BLACK);

        canvasText.drawText(text, canvasText.getWidth() / 2,
                canvasText.getHeight() - padding, paintText);

        if (jenisPeta.equalsIgnoreCase("petaSPI")) {
            final MarkerOptions markerOptions = new MarkerOptions()
                    .position(location)
                    .icon(BitmapDescriptorFactory.fromBitmap(bmpText))
                    .anchor(0.5f, 1)
                    .title(text);
            marker = map.addMarker(markerOptions);
        } else {
            final MarkerOptions markerOptions = new MarkerOptions()
                    .position(location)
                    .icon(BitmapDescriptorFactory.fromBitmap(bmpText))
                    .anchor(0.5f, 1)
                    .title(text);
            marker = map.addMarker(markerOptions);
            //dataMarker2.add(marker);
        }

        return marker;
    }

    private void ReadMapsData() {
        try {
            inputStream = new FileInputStream(new File(directory + "/" + source + "/asli" + source + ".csv"));
            List result = read();
            for (int i = 1; i < result.size(); i++) {
                String[] temp = (String[]) result.get(i);

                for (int j = 0; j < boundaryMapKabupaten.size(); j++) {
                    if (PolyUtil.containsLocation(
                            new LatLng(
                                    Double.parseDouble(temp[2]),
                                    Double.parseDouble(temp[1])
                            ), boundaryMapKabupaten.get(j).getPoints(), true
                    )) {
                        resultHasil.add(
                                Double.parseDouble(temp[3])
                        );
                        if (resultHasil.get(resultHasil.size() - 1) <= -2.0) {
                            sangatKering1.add(new LatLng(
                                    Double.parseDouble(temp[2]),
                                    Double.parseDouble(temp[1])
                            ));
                        } else if (resultHasil.get(resultHasil.size() - 1) <= -1.5 && resultHasil.get(resultHasil.size() - 1) > -2.0) {
                            kering1.add(new LatLng(
                                    Double.parseDouble(temp[2]),
                                    Double.parseDouble(temp[1])
                            ));
                        } else if (resultHasil.get(resultHasil.size() - 1) <= -1.0 && resultHasil.get(resultHasil.size() - 1) > -1.5) {
                            agakKering1.add(new LatLng(
                                    Double.parseDouble(temp[2]),
                                    Double.parseDouble(temp[1])
                            ));
                        } else if (resultHasil.get(resultHasil.size() - 1) < 1.0 && resultHasil.get(resultHasil.size() - 1) > -1.0) {
                            normal1.add(new LatLng(
                                    Double.parseDouble(temp[2]),
                                    Double.parseDouble(temp[1])
                            ));
                        } else if (resultHasil.get(resultHasil.size() - 1) >= 1.0 && resultHasil.get(resultHasil.size() - 1) < 1.5) {
                            agakBasah1.add(new LatLng(
                                    Double.parseDouble(temp[2]),
                                    Double.parseDouble(temp[1])
                            ));
                        } else if (resultHasil.get(resultHasil.size() - 1) >= 1.5 && resultHasil.get(resultHasil.size() - 1) < 2) {
                            basah1.add(new LatLng(
                                    Double.parseDouble(temp[2]),
                                    Double.parseDouble(temp[1])
                            ));
                        } else if (resultHasil.get(resultHasil.size() - 1) >= 2) {
                            sangatBasah1.add(new LatLng(
                                    Double.parseDouble(temp[2]),
                                    Double.parseDouble(temp[1])
                            ));
                        }
                    }
                }

            }
        } catch (FileNotFoundException e) {
            Log.e("ERROR", "FILE NOT FOUND EXCEPTION ACTIVITY-MAPS-RETRIEVE-DATA-READ-MAPS-DATA");
        }
    }

    private void ReadMapsDataPrediksi() {
        try {
            inputStream = new FileInputStream(new File(directory + "/" + source + "/prediksi" + source + ".csv"));
            List result = read();
            for (int i = 1; i < result.size(); i++) {
                String[] temp = (String[]) result.get(i);

                for (int j = 0; j < boundaryMapKabupaten.size(); j++) {
                    if (PolyUtil.containsLocation(
                            new LatLng(
                                    Double.parseDouble(temp[2]),
                                    Double.parseDouble(temp[1])
                            ), boundaryMapKabupaten.get(j).getPoints(), true
                    )) {
                        resultHasil.add(
                                Double.parseDouble(temp[3])
                        );
                        if (resultHasil.get(resultHasil.size() - 1) <= -2.0) {
                            sangatKering2.add(new LatLng(
                                    Double.parseDouble(temp[2]),
                                    Double.parseDouble(temp[1])
                            ));
                        } else if (resultHasil.get(resultHasil.size() - 1) <= -1.5 && resultHasil.get(resultHasil.size() - 1) > -2.0) {
                            kering2.add(new LatLng(
                                    Double.parseDouble(temp[2]),
                                    Double.parseDouble(temp[1])
                            ));
                        } else if (resultHasil.get(resultHasil.size() - 1) <= -1.0 && resultHasil.get(resultHasil.size() - 1) > -1.5) {
                            agakKering2.add(new LatLng(
                                    Double.parseDouble(temp[2]),
                                    Double.parseDouble(temp[1])
                            ));
                        } else if (resultHasil.get(resultHasil.size() - 1) < 1.0 && resultHasil.get(resultHasil.size() - 1) > -1.0) {
                            normal2.add(new LatLng(
                                    Double.parseDouble(temp[2]),
                                    Double.parseDouble(temp[1])
                            ));
                        } else if (resultHasil.get(resultHasil.size() - 1) >= 1.0 && resultHasil.get(resultHasil.size() - 1) < 1.5) {
                            agakBasah2.add(new LatLng(
                                    Double.parseDouble(temp[2]),
                                    Double.parseDouble(temp[1])
                            ));
                        } else if (resultHasil.get(resultHasil.size() - 1) >= 1.5 && resultHasil.get(resultHasil.size() - 1) < 2) {
                            basah2.add(new LatLng(
                                    Double.parseDouble(temp[2]),
                                    Double.parseDouble(temp[1])
                            ));
                        } else if (resultHasil.get(resultHasil.size() - 1) >= 2) {
                            sangatBasah2.add(new LatLng(
                                    Double.parseDouble(temp[2]),
                                    Double.parseDouble(temp[1])
                            ));
                        }
                    }
                }

            }
        } catch (FileNotFoundException e) {
            Log.e("ERROR", "FILE NOT FOUND EXCEPTION ACTIVITY-MAPS-RETRIEVE-DATA-READ-MAPS-DATA-PREDIKSI");
        }
    }

    private List read() {
        List resultList = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                resultList.add(row);
            }
        } catch (IOException ex) {
            Log.e("ERROR", "IO EXCEPTION ACTIVITY-MAPS-RETRIEVE-DATA-READ");
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e("ERROR", "IO EXCEPTION ACTIVITY-MAPS-RETRIEVE-DATA-READ");
            }
        }
        return resultList;
    }

    private void DrawHeatMap(
            List<LatLng> data1, List<LatLng> data2, List<LatLng> data3, List<LatLng> data4, List<LatLng> data5, List<LatLng> data6, List<LatLng> data7, GoogleMap googleMap
    ) {
        int radius = 65;
        if (!data1.isEmpty()) {
            int[] colors1 = {
                    Color.rgb(255, 0, 0),
                    Color.rgb(255, 0, 0)
            };
            float[] startPoints1 = {
                    0.5f, 1f
            };
            Gradient gradient1 = new Gradient(colors1, startPoints1);
            HeatmapTileProvider mProvider1 = new HeatmapTileProvider.Builder()
                    .data(data1)
                    .gradient(gradient1)
                    .build();
            mProvider1.setRadius(radius);
            mProvider1.setOpacity(0.5);
            googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider1));
        }

        if (!data2.isEmpty()) {
            int[] colors2 = {
                    Color.rgb(255, 140, 0),
                    Color.rgb(255, 140, 0)
            };
            float[] startPoints2 = {
                    0.5f, 1f
            };
            Gradient gradient2 = new Gradient(colors2, startPoints2);
            HeatmapTileProvider mProvider2 = new HeatmapTileProvider.Builder()
                    .data(data2)
                    .gradient(gradient2)
                    .build();
            mProvider2.setRadius(radius);
            mProvider2.setOpacity(0.5);
            googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider2));
        }

        if (!data3.isEmpty()) {
            int[] colors3 = {
                    Color.rgb(255, 255, 0),
                    Color.rgb(255, 255, 0)
            };
            float[] startPoints3 = {
                    0.5f, 1f
            };
            Gradient gradient3 = new Gradient(colors3, startPoints3);
            HeatmapTileProvider mProvider3 = new HeatmapTileProvider.Builder()
                    .data(data3)
                    .gradient(gradient3)
                    .build();
            mProvider3.setRadius(radius);
            mProvider3.setOpacity(0.5);
            googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider3));
        }

        if (!data4.isEmpty()) {
            int[] colors4 = {
                    Color.rgb(255, 255, 255),
                    Color.rgb(255, 255, 255)
            };
            float[] startPoints4 = {
                    0.5f, 1f
            };
            Gradient gradient4 = new Gradient(colors4, startPoints4);
            HeatmapTileProvider mProvider4 = new HeatmapTileProvider.Builder()
                    .data(data4)
                    .gradient(gradient4)
                    .build();
            mProvider4.setRadius(radius);
            mProvider4.setOpacity(0.5);
            googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider4));
        }

        if (!data5.isEmpty()) {
            int[] colors5 = {
                    Color.rgb(152, 251, 152),
                    Color.rgb(152, 251, 152)
            };
            float[] startPoints5 = {
                    0.5f, 1f
            };
            Gradient gradient5 = new Gradient(colors5, startPoints5);
            HeatmapTileProvider mProvider5 = new HeatmapTileProvider.Builder()
                    .data(data5)
                    .gradient(gradient5)
                    .build();
            mProvider5.setRadius(radius);
            mProvider5.setOpacity(0.5);
            googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider5));
        }

        if (!data6.isEmpty()) {
            int[] colors6 = {
                    Color.rgb(34, 139, 34),
                    Color.rgb(34, 139, 34)
            };
            float[] startPoints6 = {
                    0.5f, 1f
            };
            Gradient gradient6 = new Gradient(colors6, startPoints6);
            HeatmapTileProvider mProvider6 = new HeatmapTileProvider.Builder()
                    .data(data6)
                    .gradient(gradient6)
                    .build();
            mProvider6.setRadius(radius);
            mProvider6.setOpacity(0.5);
            googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider6));
        }

        if (!data7.isEmpty()) {
            int[] colors7 = {
                    Color.rgb(0, 0, 255),
                    Color.rgb(0, 0, 255)
            };
            float[] startPoints7 = {
                    0.5f, 1f
            };
            Gradient gradient7 = new Gradient(colors7, startPoints7);
            HeatmapTileProvider mProvider7 = new HeatmapTileProvider.Builder()
                    .data(data7)
                    .gradient(gradient7)
                    .build();
            mProvider7.setRadius(radius);
            mProvider7.setOpacity(0.5);
            googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider7));
        }
    }

    private void CaptureMapScreen(GoogleMap googleMap, final String name) {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                bitmap = snapshot;
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/PrototypeSkripsi4/" + source + "/" + name + ".png");
                } catch (FileNotFoundException e) {
                    Log.e("ERROR", "FILE NOT FOUND EXCEPTION ACTIVITY-MAPS-RETRIEVE-DATA-CAPTURE-MAP-SCREEN");
                }
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            }
        };
        googleMap.snapshot(callback);
    }
}
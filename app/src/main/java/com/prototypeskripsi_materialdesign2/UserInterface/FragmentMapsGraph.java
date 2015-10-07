package com.prototypeskripsi_materialdesign2.UserInterface;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.prototypeskripsi_materialdesign2.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMapsGraph extends Fragment {

    private LineChart chart1, chart2;
    private LineDataSet setComp1, setComp1a, setComp2, setComp2a;
    private LineData data1, data2, data3;

    private String[] tempData3;
    private ArrayList<Entry> valsComp1 = new ArrayList<>(), valsComp2 = new ArrayList<>();
    private ArrayList<String> xVals = new ArrayList<>();

    private ImageView imagePreview;
    private TextView namaKecamatan, nilaiHujan, prediksiHujan, nilaiSPI, prediksiSPI, kategoriSPIaktual, kategoriSPIprediksi, nilaiError;

    private InputStream inputStream;
    private int indeks = 1;
    private String query;
    private String tipeGraph;
    private String source = "";
    private String directory = Environment.getExternalStorageDirectory() + "/PrototypeSkripsi4";
    private int count = 0;

    public FragmentMapsGraph() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imagePreview = (ImageView) getActivity().findViewById(R.id.imagePreview);
        namaKecamatan = (TextView) getActivity().findViewById(R.id.namaKecamatan);
        nilaiHujan = (TextView) getActivity().findViewById(R.id.nilaiHujan);
        prediksiHujan = (TextView) getActivity().findViewById(R.id.prediksiHujan);
        nilaiSPI = (TextView) getActivity().findViewById(R.id.nilaiSPI);
        prediksiSPI = (TextView) getActivity().findViewById(R.id.prediksiSPI);
        kategoriSPIaktual = (TextView) getActivity().findViewById(R.id.kategoriSPIaktual);
        kategoriSPIprediksi = (TextView) getActivity().findViewById(R.id.kategoriSPIprediksi);
        nilaiError = (TextView) getActivity().findViewById(R.id.error);

        chart1 = (LineChart) getActivity().findViewById(R.id.chart1);
        chart2 = (LineChart) getActivity().findViewById(R.id.chart2);
        //chart3 = (LineChart) getActivity().findViewById(R.id.chart3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_maps_graph, container, false);
    }

    public void changeDataset(String query, String tipeGraph, String source) {
        this.query = query;
        this.source = source;
        this.tipeGraph = tipeGraph;
        drawGraph();
    }

    private void drawGraph() {
        try {
            valsComp1.clear();
            valsComp2.clear();
            xVals.clear();
        } catch (Exception e) {
            Log.e("ERROR", "EXCEPTION FRAGMENT-MAPS-GRAPH-DRAW-GRAPH");
        }

        initializeDataLabel();
        initializeDataDraph1();
        initializeDataDraph2();
        initilizeLabelgraph();
        setLineComponent1();
        setLineComponent2();
        setDataset();
        prepareChart();
    }

    private void initializeDataLabel() {
        try {
            inputStream = new FileInputStream(new File(directory + "/" + source + "/hasil" + source + ".csv"));
            List result = read();
            for (int i = 1; i < result.size(); i++) {
                String[] tempData1 = (String[]) result.get(i);
                if (tempData1[3].equalsIgnoreCase(query) || tempData1[3].contains(query)) {
                    indeks = i;

                    namaKecamatan.setText(query);
                    nilaiSPI.setText(setDecimal(tempData1[7]));
                    prediksiSPI.setText(setDecimal(tempData1[8]));
                    kategoriSPIaktual.setText(getKategoriSPI(tempData1[7]));
                    kategoriSPIprediksi.setText(getKategoriSPI(tempData1[8]));
                    nilaiError.setText(setDecimal(tempData1[9]));
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            Log.e("ERROR", "FILE NOT FOUND EXCEPTION FRAGMENT-MAPS-GRAPH-INITIALIZE-DATA-LABEL");
        }
    }

    private void initializeDataDraph1() {
        try {
            inputStream = new FileInputStream(new File(directory + "/" + source + "/" + source + ".csv"));
            List result1 = read();
            String[] tempData2 = (String[]) result1.get(indeks);
            count = 0;
            for (int i = 5; i < tempData2.length; i++) {
                Entry tempEntry = new Entry(Float.parseFloat(tempData2[i]), count);
                valsComp1.add(tempEntry);
                count++;
            }
            nilaiHujan.setText(setDecimal(tempData2[tempData2.length - 1]) + " mm");
        } catch (FileNotFoundException e) {
            Log.e("ERROR", "FILE NOT FOUND EXCEPTION FRAGMENT-MAPS-GRAPH-INITIALIZE-DATA-GRAPH1");
        }
    }

    private void initializeDataDraph2() {
        try {
            inputStream = new FileInputStream(new File(directory + "/" + source + "/hasilPrediksi" + source + ".csv"));
            List result2 = read();
            tempData3 = (String[]) result2.get(indeks);
            count = 12;
            for (int i = 1; i < tempData3.length; i++) {
                Entry tempEntry = new Entry(Float.parseFloat(tempData3[i]), count);
                valsComp2.add(tempEntry);
                count++;
            }
            prediksiHujan.setText(setDecimal(tempData3[tempData3.length - 1]) + " mm");
        } catch (FileNotFoundException e) {
            Log.e("ERROR", "FILE NOT FOUND EXCEPTION FRAGMENT-MAPS-GRAPH-INITIALIZE-DATA-GRAPH2");
        }
    }

    private void initilizeLabelgraph() {
        int period = 1;
        int j = 1;
        for (int i = 1; i < tempData3.length + 12; i++) {
            if (j == 1) {
                xVals.add(Integer.toString(period) + " Jan");
                j++;
            } else if (j == 2) {
                xVals.add(Integer.toString(period) + " Feb");
                j++;
            } else if (j == 3) {
                xVals.add(Integer.toString(period) + " Mar");
                j++;
            } else if (j == 4) {
                xVals.add(Integer.toString(period) + " Apr");
                j++;
            } else if (j == 5) {
                xVals.add(Integer.toString(period) + " May");
                j++;
            } else if (j == 6) {
                xVals.add(Integer.toString(period) + " Jun");
                j++;
            } else if (j == 7) {
                xVals.add(Integer.toString(period) + " Jul");
                j++;
            } else if (j == 8) {
                xVals.add(Integer.toString(period) + " Agt");
                j++;
            } else if (j == 9) {
                xVals.add(Integer.toString(period) + " Sep");
                j++;
            } else if (j == 10) {
                xVals.add(Integer.toString(period) + " Okt");
                j++;
            } else if (j == 11) {
                xVals.add(Integer.toString(period) + " Nov");
                j++;
            } else if (j == 12) {
                xVals.add(Integer.toString(period) + " Des");
                j = 1;
                period++;
            }
        }
    }

    private void setLineComponent1() {
        setComp1 = new LineDataSet(valsComp1, "Data Hujan");
        setComp1.setAxisDependency(YAxis.AxisDependency.RIGHT);
        setComp1.setColor(Color.RED);
        setComp1.setCircleColor(Color.RED);
        setComp1.setCircleSize(3);
        setComp1.setCircleColorHole(Color.RED);
        setComp1.setDrawValues(true);
        setComp1.setDrawFilled(true);
        setComp1.setHighlightEnabled(false);

        setComp1a = new LineDataSet(valsComp1, "Data Hujan");
        setComp1a.setAxisDependency(YAxis.AxisDependency.RIGHT);
        setComp1a.setColor(Color.RED);
        setComp1a.setCircleColor(Color.RED);
        setComp1a.setCircleSize(3);
        setComp1a.setCircleColorHole(Color.RED);
        setComp1a.setDrawValues(true);
        setComp1a.setDrawFilled(false);
        setComp1a.setHighlightEnabled(false);
    }

    private void setLineComponent2() {
        setComp2 = new LineDataSet(valsComp2, "Data Prediksi Hujan");
        setComp2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        setComp2.setColor(Color.BLUE);
        setComp2.setCircleColor(Color.BLUE);
        setComp2.setCircleSize(3);
        setComp2.setCircleColorHole(Color.BLUE);
        setComp2.setDrawValues(true);
        setComp2.setDrawFilled(true);
        setComp2.setHighlightEnabled(false);

        setComp2a = new LineDataSet(valsComp2, "Data Prediksi Hujan");
        setComp2a.setAxisDependency(YAxis.AxisDependency.RIGHT);
        setComp2a.setColor(Color.BLUE);
        setComp2a.setCircleColor(Color.BLUE);
        setComp2a.setCircleSize(3);
        setComp2a.setCircleColorHole(Color.BLUE);
        setComp2a.setDrawValues(true);
        setComp2a.setDrawFilled(false);
        setComp2a.setHighlightEnabled(false);
    }

    private void setDataset() {
        ArrayList<LineDataSet> dataSets1 = new ArrayList<>();
        dataSets1.add(setComp1);
        ArrayList<LineDataSet> dataSets2 = new ArrayList<>();
        dataSets2.add(setComp2);
        ArrayList<LineDataSet> dataSets3 = new ArrayList<>();
        dataSets3.add(setComp1a);
        dataSets3.add(setComp2a);

        data1 = new LineData(xVals, dataSets1);
        data2 = new LineData(xVals, dataSets2);
        data3 = new LineData(xVals, dataSets3);
    }

    private void prepareChart() {
        YAxis yAxis1a = chart1.getAxisLeft();
        yAxis1a.setEnabled(false);
        yAxis1a.setStartAtZero(false);
        YAxis yAxis1b = chart1.getAxisRight();
        yAxis1b.setEnabled(true);
        yAxis1b.setStartAtZero(false);

        YAxis yAxis2a = chart2.getAxisLeft();
        yAxis2a.setEnabled(false);
        yAxis2a.setStartAtZero(false);
        YAxis yAxis2b = chart2.getAxisRight();
        yAxis2b.setEnabled(true);
        yAxis2b.setStartAtZero(false);

        if (tipeGraph.equalsIgnoreCase("Aktual")) {
            chart1.setData(data1);
            Drawable drawable = Drawable.createFromPath(directory + "/" + source + "/screenshot1.png");
            imagePreview.setBackground(drawable);
        } else {
            chart1.setData(data2);
            Drawable drawable = Drawable.createFromPath(directory + "/" + source + "/screenshot2.png");
            imagePreview.setBackground(drawable);
        }
        chart1.setDescription("");
        chart1.setVisibleXRangeMaximum(12);
        chart1.animateXY(2500, 2500);
        chart1.invalidate();

        chart2.setData(data3);
        chart2.setDescription("");
        chart2.setVisibleXRangeMaximum(12);
        chart2.animateXY(2500, 2500);
        chart2.invalidate();
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
            Log.e("ERROR", "IO EXCEPTION FRAGMENT-MAPS-GRAPH-READ");
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e("ERROR", "IO EXCEPTION FRAGMENT-MAPS-GRAPH-READ");
            }
        }
        return resultList;
    }

    private String setDecimal(String nilai) {
        String result = "";
        try {
            double temp = Double.parseDouble(nilai);
            DecimalFormat df = new DecimalFormat("#.##");
            result = df.format(temp);
        } catch (NumberFormatException e) {
            result = "NA";
            Log.e("ERROR", "NUMBER FORMAT EXCEPTION FRAGMENT-MAPS-GRAPH-SET-DECIMAL");
        }
        return result;
    }

    private String getKategoriSPI(String nilai) {
        String result = "";
        try {
            double temp = Double.parseDouble(nilai);

            if (temp <= -2.0) {
                result = "Sangat Kering";
            } else if (temp <= -1.5 && temp > -2.0) {
                result = "Kering";
            } else if (temp <= -1.0 && temp > -1.5) {
                result = "Agak Kering";
            } else if (temp < 1.0 && temp > -1.0) {
                result = "Normal";
            } else if (temp >= 1.0 && temp < 1.5) {
                result = "Agak Basah";
            } else if (temp >= 1.5 && temp < 2) {
                result = "Basah";
            } else if (temp >= 2) {
                result = "Sangat Basah";
            }
        } catch (NumberFormatException nfe) {
            result = "Tidak Teridentifikasi";
            Log.e("ERROR", "NUMBER FORMAT EXCEPTION FRAGMENT-MAPS-GRAPH-GET-KATEGORI-SPI");
        }
        return result;
    }
}

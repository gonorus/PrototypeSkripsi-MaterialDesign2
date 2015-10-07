package com.prototypeskripsi_materialdesign2.DataControl;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.WindowManager;

import com.prototypeskripsi_materialdesign2.DataAccessObject.ObjectMapsData;
import com.prototypeskripsi_materialdesign2.R;
import com.prototypeskripsi_materialdesign2.UserInterface.ActivityMaps;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class FragmentTabDownloadData_RetrieveData extends AsyncTask<ObjectMapsData, String, ObjectMapsData> {
    private ProgressDialog dialog;
    private FragmentActivity fragmentActivity;
    private String host = "http://gonorus.ddns.net/skripsi3/RData";
    private String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrototypeSkripsi4";

    public FragmentTabDownloadData_RetrieveData(String fileName, ProgressDialog dialog, FragmentActivity fragmentActivity) {
        File tempFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrototypeSkripsi4");
        if (!tempFile.exists()) {
            tempFile.mkdir();
        }
        tempFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrototypeSkripsi4/" + fileName);
        if (!tempFile.exists()) {
            if (!tempFile.exists()) {
                tempFile.mkdir();
            }
        }
        this.dialog = dialog;
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Mencoba Koneksi Dengan Server");
        dialog.setTitle("Menyiapkan Data Peta");
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected ObjectMapsData doInBackground(ObjectMapsData... params) {
        ObjectMapsData o = params[0];
        publishProgress("Memulai Proses Download Data Peta");
        DownloadFile(
                host + "/Dataset/" + o.getName() + ".csv",
                o.getName(),
                dir + "/" + o.getName());
        DownloadFile(
                host + "/DataHasil/" + o.getName() + "/asli" + o.getName() + ".csv",
                "asli" + o.getName(),
                dir + "/" + o.getName());
        DownloadFile(
                host + "/DataHasil/" + o.getName() + "/hasil" + o.getName() + ".csv",
                "hasil" + o.getName(),
                dir + "/" + o.getName());
        DownloadFile(
                host + "/DataHasil/" + o.getName() + "/hasilPrediksi" + o.getName() + ".csv",
                "hasilPrediksi" + o.getName(),
                dir + "/" + o.getName());
        DownloadFile(
                host + "/DataHasil/" + o.getName() + "/prediksi" + o.getName() + ".csv",
                "prediksi" + o.getName(),
                dir + "/" + o.getName());
        return o;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values[0]);
        dialog.setMessage(values[0]);
    }

    @Override
    protected void onPostExecute(ObjectMapsData result) {
        super.onPostExecute(result);
        dialog.cancel();
        Intent intent = new Intent(fragmentActivity, ActivityMaps.class);
        intent.putExtra("fileDir", result.getName());
        fragmentActivity.finish();
        fragmentActivity.startActivity(intent);
        fragmentActivity.overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
    }

    private void DownloadFile(String host, String fileName, String path) {
        int count;
        try {
            URL url = new URL(host);
            URLConnection conection = url.openConnection();
            conection.connect();
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            OutputStream output = new FileOutputStream(path + "/" + fileName + ".csv");
            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();
        } catch (MalformedURLException e) {
            Log.e("ERROR", "MALFORMED URL EXCEPTION FRAGMENT-TAB-DOWNLOAD-DATA-DOWNLOAD-FILE");
        } catch (FileNotFoundException e) {
            Log.e("ERROR", "FILE NOT FOUND EXCEPTION FRAGMENT-TAB-DOWNLOAD-DATA-DOWNLOAD-FILE");
        } catch (IOException e) {
            Log.e("ERROR", "IO EXCEPTION FRAGMENT-TAB-DOWNLOAD-DATA-DOWNLOAD-FILE");
        }
    }
}

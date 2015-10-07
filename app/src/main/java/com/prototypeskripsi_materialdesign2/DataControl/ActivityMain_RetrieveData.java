package com.prototypeskripsi_materialdesign2.DataControl;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.prototypeskripsi_materialdesign2.DataAccessObject.ObjectMapsData;
import com.prototypeskripsi_materialdesign2.R;
import com.prototypeskripsi_materialdesign2.UserInterface.ActivityMain;
import com.prototypeskripsi_materialdesign2.UserInterface.ActivityMapsChooser;
import com.prototypeskripsi_materialdesign2.UserInterface.FragmentTabNewData;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ActivityMain_RetrieveData extends AsyncTask<String, Void, List<ObjectMapsData>> {
    private ProgressDialog dialog;

    public ActivityMain_RetrieveData(ProgressDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("CEK KONEKSI DENGAN SERVER");
        dialog.setTitle("MENYIAPKAN APLIKASI");
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected List<ObjectMapsData> doInBackground(String... params) {
        String content = loadFromNet(params[0]);

        try {
            JSONObject jsonObject = new JSONObject(content);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject temp = (JSONObject) jsonArray.get(i);
                ActivityMain.mapsListServer.add(new ObjectMapsData(temp.getString("data"), temp.getString("date"), temp.getString("data")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ActivityMain.mapsListServer;
    }

    @Override
    protected void onPostExecute(List<ObjectMapsData> objectMapsDatas) {
        super.onPostExecute(objectMapsDatas);
        dialog.cancel();
    }

    private String loadFromNet(String url) {
        String result = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            HttpResponse response = client.execute(post);

            HttpEntity entity = response.getEntity();
            InputStream entityStream = entity.getContent();
            StringBuilder entityStringBuilder = new StringBuilder();
            byte[] buffer = new byte[1024];
            int bytesReadCount;
            while ((bytesReadCount = entityStream.read(buffer)) > 0) {
                entityStringBuilder.append(new String(buffer, 0, bytesReadCount));
            }
            result = entityStringBuilder.toString();
        } catch (ClientProtocolException e) {
            ActivityMain.connectionResult = false;
            Log.e("ERROR", "CLIENT PROTOCOL EXCEPTION ACTIVITY-MAIN-LOAD-FROM-NET");
        } catch (IOException e) {
            ActivityMain.connectionResult = false;
            Log.e("ERROR", "IO EXCEPTION ACTIVITY-MAIN-LOAD-FROM-NET");
        }
        return result;
    }
}
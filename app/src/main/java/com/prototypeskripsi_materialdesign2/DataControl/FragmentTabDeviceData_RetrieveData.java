package com.prototypeskripsi_materialdesign2.DataControl;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;

import com.prototypeskripsi_materialdesign2.DataAccessObject.ObjectMapsData;
import com.prototypeskripsi_materialdesign2.UserInterface.ActivityMapsChooser;
import com.prototypeskripsi_materialdesign2.UserInterface.FragmentLostConnection;
import com.prototypeskripsi_materialdesign2.UserInterface.FragmentTabDeviceData;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentTabDeviceData_RetrieveData extends AsyncTask<Void, Void, List<ObjectMapsData>> {
    private File currentDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrototypeSkripsi4");

    @Override
    protected List<ObjectMapsData> doInBackground(Void... params) {
        if (!currentDir.exists()) {
            currentDir.mkdir();
            FragmentTabDeviceData.listMaps = retrieveDeviceData(currentDir);
        } else {
            FragmentTabDeviceData.listMaps = retrieveDeviceData(currentDir);
        }
        return FragmentTabDeviceData.listMaps;
    }

    @Override
    protected void onPostExecute(List<ObjectMapsData> result) {
        super.onPostExecute(result);

        //if (!result.isEmpty()) {
            RecyclerView.Adapter mAdapter = new FragmentTabDeviceData_Adapter(FragmentTabDeviceData.listMaps);
            FragmentTabDeviceData.mRecyclerView.setAdapter(mAdapter);
        //}
    }

    private List<ObjectMapsData> retrieveDeviceData(File f) {
        File[] dirs = f.listFiles();
        List<ObjectMapsData> dir = new ArrayList<>();

        for (File ff : dirs) {
            if (ff.isDirectory()) {
                File[] tempFile = ff.listFiles();
                int condition = 0;

                for (File tempFile1 : tempFile) {
                    String[] fileType = tempFile1.getName().split(ff.getName());
                    switch (fileType[0]) {
                        case "asli":
                            condition++;
                            break;
                        case "prediksi":
                            condition++;
                            break;
                        case "hasil":
                            condition++;
                            break;
                        case "hasilPrediksi":
                            condition++;
                            break;
                        default:
                            break;
                    }
                }

                if (condition == 4) {
                    String sdf = new SimpleDateFormat("yyyy-MM-dd").format(
                            new Date(ff.lastModified())
                    );
                    dir.add(new ObjectMapsData(ff.getName(), sdf, ff.getAbsolutePath()));
                }
            }
        }
        return dir;
    }
}

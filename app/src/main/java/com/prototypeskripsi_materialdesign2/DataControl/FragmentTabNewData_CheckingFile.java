package com.prototypeskripsi_materialdesign2.DataControl;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.prototypeskripsi_materialdesign2.R;
import com.prototypeskripsi_materialdesign2.UserInterface.ActivityMaps;
import com.prototypeskripsi_materialdesign2.UserInterface.FragmentTabNewData;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class FragmentTabNewData_CheckingFile extends AsyncTask<String, String, Boolean> {

    private String host = "http://gonorus.ddns.net/skripsi3/";
    private String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrototypeSkripsi4";
    private InputStream inputStream;
    private ProgressDialog dialog;
    private Dialog msgDialog;
    private int serverResponseCode;
    private String resultName;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(FragmentTabNewData.fragmentActivity);
        dialog.setMessage("Membaca Data Input");
        dialog.setTitle("Menyiapkan Data Peta");
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        File file = new File(params[0]);
        boolean result = CheckDataSource(file);
        if (result) {
            String fileName = file.getName().substring(0, file.getName().length() - 4);
            resultName = fileName;

            publishProgress("Proses Upload Data Ke Server");
            UploadData(params[0], host);

            publishProgress("Memproses Data");
            ExecuteRscript(host + "Rscript.php?function=" + file.getName());

            publishProgress("Memulai Proses Download Data Peta");
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
            DownloadFile(
                    host + "RData/Dataset/" + fileName + ".csv",
                    fileName,
                    dir + "/" + fileName);
            DownloadFile(
                    host + "RData/DataHasil/" + fileName + "/asli" + fileName + ".csv",
                    "asli" + fileName,
                    dir + "/" + fileName);
            DownloadFile(
                    host + "RData/DataHasil/" + fileName + "/hasil" + fileName + ".csv",
                    "hasil" + fileName,
                    dir + "/" + fileName);
            DownloadFile(
                    host + "RData/DataHasil/" + fileName + "/hasilPrediksi" + fileName + ".csv",
                    "hasilPrediksi" + fileName,
                    dir + "/" + fileName);
            DownloadFile(
                    host + "RData/DataHasil/" + fileName + "/prediksi" + fileName + ".csv",
                    "prediksi" + fileName,
                    dir + "/" + fileName);
        }

        return result;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        dialog.setMessage(values[0]);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean) {
            dialog.cancel();
            Intent intent = new Intent(FragmentTabNewData.fragmentActivity, ActivityMaps.class);
            intent.putExtra("fileDir", resultName);
            FragmentTabNewData.fragmentActivity.finish();
            FragmentTabNewData.fragmentActivity.startActivity(intent);
            FragmentTabNewData.fragmentActivity.overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
        } else {
            dialog.cancel();
            msgDialog = new Dialog(FragmentTabNewData.fragmentActivity);
            msgDialog.setTitle("ERROR");
            msgDialog.setContentView(R.layout.layout_dialog);
            TextView textView = (TextView) msgDialog.findViewById(R.id.message);
            Button button = (Button) msgDialog.findViewById(R.id.dialogButtonOK);
            textView.setText("Terjadi Kesalahan Pada Inputan, Periksa Data Inputan Kembali dan Koneksi Internet !!!");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msgDialog.dismiss();
                }
            });
            msgDialog.show();
        }
    }

    private boolean CheckDataSource(File file) {
        boolean result = false;
        try {
            inputStream = new FileInputStream(file);
            List data = read();

            boolean tempBoolean = true;
            String[] stringJudul = (String[]) data.get(0);
            if (!stringJudul[1].equalsIgnoreCase("KABUPATEN")) {
                tempBoolean = false;
                Log.d("WRONG", "KECAMATAN DATA");
            }
            if (!stringJudul[2].equalsIgnoreCase("KECAMATAN")) {
                tempBoolean = false;
                Log.d("WRONG", "KABUPATEN DATA");
            }
            if (!stringJudul[3].equals("LAT")) {
                tempBoolean = false;
                Log.d("WRONG", "LAT DATA");
            }
            if (!stringJudul[4].equals("LON")) {
                tempBoolean = false;
                Log.d("WRONG", "LON DATA");
            }

            if (tempBoolean) {
                for (int i = 1; i < data.size(); i++) {
                    String[] tempString = (String[]) data.get(i);
                    try {
                        int parseInt = Integer.parseInt(tempString[0]);
                    } catch (NumberFormatException nfe) {
                        Log.e("ERROR", "NUMBER FORMAT EXCEPTION FRAGMENT-TAB-NEW-DATA-CHECKING-FILE-READ-DATA");
                        result = false;
                        break;
                    }
                    try {
                        double parseDouble = Double.parseDouble(tempString[4]);
                    } catch (NumberFormatException nfe) {
                        Log.e("ERROR", "NUMBER FORMAT EXCEPTION FRAGMENT-TAB-NEW-DATA-CHECKING-FILE-READ-DATA");
                        result = false;
                        break;
                    }
                    try {
                        double parseDouble = Double.parseDouble(tempString[5]);
                    } catch (NumberFormatException nfe) {
                        Log.e("ERROR", "NUMBER FORMAT EXCEPTION FRAGMENT-TAB-NEW-DATA-CHECKING-FILE-READ-DATA");
                        result = false;
                        break;
                    }
                    int count = 0;
                    for (int j = 6; j < tempString.length; j++) {
                        try {
                            double parseDouble = Double.parseDouble(tempString[j]);
                            count++;
                        } catch (NumberFormatException nfe) {
                            Log.e("ERROR", "NUMBER FORMAT EXCEPTION FRAGMENT-TAB-NEW-DATA-CHECKING-FILE-READ-DATA");
                            result = false;
                            break;
                        }
                    }
                    if (count < 24) {
                        result = false;
                    }
                    result = true;
                }
            } else {
                result = false;
            }
        } catch (FileNotFoundException e) {
            Log.e("ERROR", "FILE NOT FOUND EXCEPTION FRAGMENT-TAB-NEW-DATA-CHECKING-FILE-READ-DATA");
            result = false;
        }
        return result;
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
            Log.e("ERROR", "IO EXCEPTION FRAGMENT-TAB-NEW-DATA-CHECKING-FILE-READ");
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e("ERROR", "IO EXCEPTION FRAGMENT-TAB-NEW-DATA-CHECKING-FILE-READ");
            }
        }
        return resultList;
    }

    public int UploadData(String sourceFileUri, String host) {
        String upLoadServerUri = host + "upload.php";
        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
        if (!sourceFile.isFile()) {
            return 0;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(upLoadServerUri);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", fileName);

            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            serverResponseCode = conn.getResponseCode();

            fileInputStream.close();
            dos.flush();
            dos.close();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Log.e("ERROR", "MALFORMED URL EXCEPTION FRAGMENT-TAB-NEW-DATA-CHECKING-FILE-UPLOAD-DATA");
        } catch (FileNotFoundException e) {
            Log.e("ERROR", "FILE NOT FOUND EXCEPTION FRAGMENT-TAB-NEW-DATA-CHECKING-FILE-UPLOAD-DATA");
        } catch (ProtocolException e) {
            Log.e("ERROR", "PROTOCOL EXCEPTION FRAGMENT-TAB-NEW-DATA-CHECKING-FILE-UPLOAD-DATA");
        } catch (IOException e) {
            Log.e("ERROR", "IO EXCEPTION FRAGMENT-TAB-NEW-DATA-CHECKING-FILE-UPLOAD-DATA");
        }
        return serverResponseCode;
    }

    public boolean ExecuteRscript(String url) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            client.execute(post);
        } catch (ClientProtocolException e) {
            Log.e("ERROR", "CLIENT PROTOCOL EXCEPTION FRAGMENT-TAB-NEW-DATA-CHECKING-FILE-EXECUTE-RSCRIPT");
            return false;
        } catch (IOException e) {
            Log.e("ERROR", "IO EXCEPTION FRAGMENT-TAB-NEW-DATA-CHECKING-FILE-EXECUTE-RSCRIPT");
            return false;
        }
        return true;
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
            Log.e("ERROR", "MALFORMED URL EXCEPTION FRAGMENT-TAB-NEW-DATA-CHECKING-FILE-DOWNLOAD-FILE");
        } catch (FileNotFoundException e) {
            Log.e("ERROR", "FILE NOT FOUND EXCEPTION FRAGMENT-TAB-NEW-DATA-CHECKING-FILE-DOWNLOAD-FILE");
        } catch (IOException e) {
            Log.e("ERROR", "IO EXCEPTION FRAGMENT-TAB-NEW-DATA-CHECKING-FILE-DOWNLOAD-FILE");
        }
    }
}
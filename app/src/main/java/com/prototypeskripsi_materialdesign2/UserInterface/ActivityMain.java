package com.prototypeskripsi_materialdesign2.UserInterface;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.prototypeskripsi_materialdesign2.DataAccessObject.ObjectMapsData;
import com.prototypeskripsi_materialdesign2.DataControl.ActivityMain_RetrieveData;
import com.prototypeskripsi_materialdesign2.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity {

    public static boolean start = false;
    public static List<ObjectMapsData> mapsListServer;
    public static boolean connectionResult = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = (WebView) findViewById(R.id.userAgreement);
        webView.loadUrl("file:///android_asset/user_guide.html");

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMain.this, ActivityMapsChooser.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
            }
        });
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder messageBuilder = new AlertDialog.Builder(ActivityMain.this);
                messageBuilder.setIcon(R.drawable.ic_info);
                messageBuilder.setMessage("" +
                                "Author" + "\t" + ": Dwi Anggono W.S.P." + "\n" +
                                "Email" + "\t" + ": 672011122@student.uksw.edu" + "\n" +
                                "Phone" + "\t" + ": +6285640138804"
                )
                        .setTitle("Author");
                messageBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog temp = messageBuilder.create();
                temp.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!start) {
            start = true;
            mapsListServer = new ArrayList<>();
            new ActivityMain_RetrieveData(new ProgressDialog(ActivityMain.this)).execute("http://gonorus.ddns.net/skripsi3/checkMap.php");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}

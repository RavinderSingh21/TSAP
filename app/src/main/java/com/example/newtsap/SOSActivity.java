package com.example.newtsap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

public class SOSActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_s_o_s);
        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        setTitle(R.string.sos);
        Button button1= findViewById(R.id.ff1);
        Button button2= findViewById(R.id.ff2);
        Button button3= findViewById(R.id.ff3);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intents = new Intent(SOSActivity.this,MapsActivity.class);
                startActivity(intents);
                finish();

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CallContact1();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CallContact2();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CallContact3();
            }
        });


    }
    private void CallContact1() {

        Intent call = new Intent(Intent.ACTION_CALL);
        call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        call.setData(Uri.parse("tel:" + "7355272686"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(call);
    }

    private void CallContact2() {

        Intent call1 = new Intent(Intent.ACTION_CALL);
        call1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        call1.setData(Uri.parse("tel:" + "7355272686"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startActivity(call1);
    }

    private void CallContact3() {

        Intent call2 = new Intent(Intent.ACTION_CALL);
        call2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        call2.setData(Uri.parse("tel:" + "7355272686"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            return;

        }
        startActivity(call2);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
        finish();
    }
}
package com.example.newtsap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

public class PreFIRActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_fir_new);
        Toolbar toolbar = findViewById(R.id.toolbarp);
        setSupportActionBar(toolbar);
        setTitle(R.string.pre_fir_D);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_pr = new Intent(PreFIRActivity.this,MapsActivity.class);
                startActivity(intent_pr);
                finish();

            }
        });
        Button submit =findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(PreFIRActivity.this,"PRE - FIR SUBMITTED",Toast.LENGTH_LONG).show();
            }
        });


    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);

    }
}
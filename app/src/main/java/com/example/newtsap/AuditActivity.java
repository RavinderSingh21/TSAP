package com.example.newtsap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

public class AuditActivity extends BaseActivity {
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_audit);
        Toolbar toolbar = findViewById(R.id.toolbara);
        setSupportActionBar(toolbar);
        setTitle(R.string.do_a_safety_audit);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_24);
        Button submit =findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(AuditActivity.this,"Thanks for filling Audit",Toast.LENGTH_LONG).show();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_au = new Intent(AuditActivity.this,MapsActivity.class);
                startActivity(intent_au);
                finish();

            }
        });
        radioGroup = findViewById(R.id.groupradio1);
        radioGroup = findViewById(R.id.groupradio2);
        radioGroup = findViewById(R.id.groupradio3);
        radioGroup = findViewById(R.id.groupradio4);
        radioGroup = findViewById(R.id.groupradio5);
        radioGroup = findViewById(R.id.groupradio6);
        radioGroup = findViewById(R.id.groupradio7);
        radioGroup.clearCheck();




        radioGroup.setOnCheckedChangeListener(
                new RadioGroup
                        .OnCheckedChangeListener() {
                    @Override

                    // The flow will come here when
                    // any of the radio buttons in the radioGroup
                    // has been clicked

                    // Check which radio button has been clicked
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId)
                    {

                        // Get the selected Radio Button
                        RadioButton
                                radioButton
                                = group
                                .findViewById(checkedId);
                    }
                });



    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
        finish();

    }

}
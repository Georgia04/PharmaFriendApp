package com.georgia.gk.pharmafriendapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

public class LocationActivity extends AppCompatActivity {
    private ConstraintLayout enterPostcodeBtn;
    EditText edTextPostalAddress;
    ProgressBar progressBar3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        edTextPostalAddress = findViewById(R.id.editTextTextPostalAddress);
        enterPostcodeBtn = findViewById(R.id.enterPostcodeBtn);
        progressBar3 = findViewById(R.id.progressBar3);



        enterPostcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postcodeAddress = edTextPostalAddress.getText().toString().trim();

                if (TextUtils.isEmpty(postcodeAddress)) {
                    edTextPostalAddress.setError("Please enter your valid postcode address!");
                    return;
                }


                progressBar3.setVisibility(View.VISIBLE);
            }

        });
    }

}
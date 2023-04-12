package com.georgia.gk.pharmafriendapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LocationActivity extends AppCompatActivity {
    private ConstraintLayout enterPostcodeBtn;
    EditText edTextPostalAddress;
    ProgressBar progressBar3;
    FirebaseAuth firebaseAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        edTextPostalAddress = findViewById(R.id.editTextTextPostalAddress);
        enterPostcodeBtn = findViewById(R.id.enterPostcodeBtn);
        progressBar3 = findViewById(R.id.progressBar3);

        firebaseAuth = FirebaseAuth.getInstance();




        enterPostcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postcodeAddress = edTextPostalAddress.getText().toString().trim();

                if (TextUtils.isEmpty(postcodeAddress)) {
                    edTextPostalAddress.setError("Please enter your valid postcode address!");
                    return;
                }else{
                    Intent intent = new Intent(LocationActivity.this, ConfirmLocationActivity.class);
                    startActivity(intent);
                }

                progressBar3.setVisibility(View.VISIBLE);
            }

        });
    }

}
package com.georgia.gk.pharmafriendapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ConfirmLocationActivity extends AppCompatActivity {

    TextView positiveTextLinkMessage, negativeTextLinkMessage;
    ProgressBar progressBar4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_location);


        positiveTextLinkMessage = findViewById(R.id.textViewYes);
        negativeTextLinkMessage = findViewById(R.id.textViewNo);
        progressBar4 = findViewById(R.id.progressBar4);



        positiveTextLinkMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmLocationActivity.this, HomeActivity.class));
            }


        });

        progressBar4.setVisibility(View.GONE);


        negativeTextLinkMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmLocationActivity.this, LocationActivity.class));
            }
        });

    }
}
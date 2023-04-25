package com.georgia.gk.pharmafriendapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.georgia.gk.pharmafriendapp.DTOs.Medications;
import com.georgia.gk.pharmafriendapp.databinding.ActivityMedicationBinding;

import java.io.IOException;
import java.net.URL;

public class MedicationActivity extends AppCompatActivity {

    private ActivityMedicationBinding binding;
    private Thread imageDataThread;
    private static int _counter = 1;
    private String _stringVal;
    private Handler repeatUpdateHandler = new Handler();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMedicationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        Medications med = (Medications) extras.getSerializable("object");

        TextView name = findViewById(R.id.medication_Name_opened);
        TextView price = findViewById(R.id.medication_Price_opened);
        TextView description = findViewById(R.id.medication_Description_opened);
        ImageView image = findViewById(R.id.medication_Image_opened);
        ImageView plus = findViewById(R.id.label_plus);
        ImageView minus = findViewById(R.id.label_minus);
        TextView qty = findViewById(R.id.label_qty);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _counter--;
                _stringVal = Integer.toString(_counter);
                qty.setText(_stringVal);
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // int a =1;
                _counter++;
                _stringVal = Integer.toString(_counter);

                    qty.setText(_stringVal);


            }
        });

        name.setText(med.medication_Name);
        price.setText(med.medication_Price);
        description.setText(med.medication_Description);

        binding.topNavigationView2.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home2:
                    Intent intent = new Intent(MedicationActivity.this, HomeActivity.class);
                    startActivity(intent);
                    break;
            }
            return true;
        });

        imageDataThread = new Thread(() -> {
            try {
                URL tUrl = new URL(med.medication_Image);
                Bitmap imageBitmap = BitmapFactory.decodeStream(tUrl.openConnection().getInputStream());
                runOnUiThread(() -> image.setImageBitmap(imageBitmap));
            } catch(IOException pExc) {
                pExc.printStackTrace();
            }
        });
        imageDataThread.start();
    }
}

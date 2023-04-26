package com.georgia.gk.pharmafriendapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.georgia.gk.pharmafriendapp.DTOs.Medications;
import com.georgia.gk.pharmafriendapp.databinding.ActivityMedicationBinding;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;

import io.reactivex.rxjava3.internal.operators.maybe.MaybeDoOnTerminate;

public class MedicationActivity extends AppCompatActivity {

    Spinner spinner;
    String[]dosage = {"100mg","250mg", "400mg"};
    private DatePickerDialog datePickerDialog;
    private Button buttonDate;
    private ConstraintLayout orderButton;
    CheckBox checkBox;

    private ActivityMedicationBinding binding;
    private Thread imageDataThread;
    private static int _counter = 1;
    private String _stringVal;
    private Handler repeatUpdateHandler = new Handler();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMedicationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initDatePicker();
        buttonDate = findViewById(R.id.dateButton);
        buttonDate.setText(getTodayDate());
        orderButton = findViewById(R.id.orderButton);
        checkBox = findViewById(R.id.checkPayment);
        spinner = findViewById(R.id.dossage_spinner);

        ArrayAdapter<String>adapter=new ArrayAdapter<String>(MedicationActivity.this, android.R.layout.simple_spinner_item,dosage);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        /*spinner.setOnClickListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int posistion, long id) {
                String value = parent.getItemAtPosition(posistion).toString();
                Toast.makeText(MedicationActivity.this, value, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });*/





        Bundle extras = getIntent().getExtras();
        Medications med = (Medications) extras.getSerializable("object");

        TextView name = findViewById(R.id.medication_Name_opened);
        TextView price = findViewById(R.id.medication_Price_opened);
        TextView description = findViewById(R.id.medication_Description_opened);
        ImageView image = findViewById(R.id.medication_Image_opened);
        TextView restrictions = findViewById(R.id.medication_Restrictions_opened);

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

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MedicationActivity.this,PaymentActivityNumber.class));
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MedicationActivity.this, ClickAndCollectActivityNumber.class));
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
        restrictions.setText(med.medication_Restrictions);

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

    private String getTodayDate()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(dayOfMonth, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                month = month+1;
                String date = makeDateString(dayOfMonth, month, year);
                buttonDate.setText(dayOfMonth);


            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, dayOfMonth);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int dayOfMonth, int month, int year)
    {
        return getMonthFormat (month)+ "" + dayOfMonth + "" + year;
    }

    private String getMonthFormat(int month)
    {
        if (month==1)
            return "JAN";
        if (month==2)
            return "FEB";
        if (month==3)
            return "MAR";
        if (month==4)
            return "APR";
        if (month==5)
            return "MAY";
        if (month==6)
            return "JUN";
        if (month==7)
            return "JUL";
        if (month==8)
            return "AUG";
        if (month==9)
            return "SEP";
        if (month==10)
            return "OCT";
        if (month==11)
            return "NOV";
        if (month==12)
            return "DEC";

        return "JAN";
    }

    public void openDate(View view)
    {
      datePickerDialog.show();
    }
}

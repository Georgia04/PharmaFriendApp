package com.georgia.gk.pharmafriendapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PaymentOrderDetails extends AppCompatActivity {

    private ConstraintLayout confirmOrderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_order_details);

        confirmOrderButton = findViewById(R.id.confirmOrderButton);


        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentOrderDetails.this, ClickAndCollectActivityNumber.class));
            }
        });
    }
}
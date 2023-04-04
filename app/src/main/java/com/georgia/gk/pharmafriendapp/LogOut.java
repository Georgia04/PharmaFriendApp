package com.georgia.gk.pharmafriendapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class LogOut extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut(); //User signs out from the account
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}
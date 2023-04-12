package com.georgia.gk.pharmafriendapp;

import static android.content.ContentValues.TAG;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private ConstraintLayout registerButton;
    EditText edPersonName, edEmailAddress, edLoginPassword, edLoginConfirmPassword, edPhoneNumber, edSecureCode;
    TextView textViewLink;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    boolean loginPasswordVisible;
    FirebaseFirestore firestore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edPersonName = findViewById(R.id.editTextPersonName);
        edEmailAddress = findViewById(R.id.editTextEmailAddress);
        edLoginPassword = findViewById(R.id.editTextLoginPassword);
        edLoginConfirmPassword = findViewById(R.id.editTextLoginConfirmPassword);
        edPhoneNumber = findViewById(R.id.editPhoneNumber);
        edSecureCode = findViewById(R.id.editTextSecureCode);
        textViewLink = findViewById(R.id.textViewUser);
        registerButton = findViewById(R.id.registerButton);

        edLoginPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int Right = 2;
                if (event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX()>=edLoginPassword.getRight()-edLoginPassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=edLoginPassword.getSelectionEnd();
                        if (loginPasswordVisible){
                            edLoginPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_off_24, 0);

                            edLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            loginPasswordVisible = false;

                        }else{
                            edLoginPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_24, 0);

                            edLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            loginPasswordVisible = true;
                        }

                        edLoginPassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        edLoginConfirmPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int Right = 2;
                if (event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX()>=edLoginConfirmPassword.getRight()-edLoginConfirmPassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=edLoginConfirmPassword.getSelectionEnd();
                        if (loginPasswordVisible){
                            edLoginConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_off_24, 0);

                            edLoginConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            loginPasswordVisible = false;

                        }else{
                            edLoginConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_24, 0);

                            edLoginConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            loginPasswordVisible = true;
                        }

                        edLoginConfirmPassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        firestore = FirebaseFirestore.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), LogOut.class));
            finish();
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String personName = edPersonName.getText().toString();
                String emailAddress = edEmailAddress.getText().toString().trim();
                String loginPassword = edLoginPassword.getText().toString().trim();
                String loginConfirmPassword = edLoginConfirmPassword.getText().toString().trim();
                String secureCode = edSecureCode.getText().toString();
                String phoneNumber = edPhoneNumber.getText().toString();


                if (personName.length()==0 || emailAddress.length()==0 || loginPassword.length()==0 || loginConfirmPassword.length()==0) {
                    Toast.makeText(getApplicationContext(), "Please fill all the details!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (loginConfirmPassword.compareTo(loginPassword)==0){
                       if (isValid(loginPassword)) {

                           Toast.makeText(getApplicationContext(), "Input has been accepted", Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(RegisterActivity.this,LogOut.class));
                       }
                       /*else{
                           Toast.makeText(getApplicationContext(), "Password must contain more than 8 characters!", Toast.LENGTH_SHORT).show();
                           progressBar.setVisibility(View.GONE);
                       }*/
                    }else{
                        Toast.makeText(getApplicationContext(), "Password does NOT match. Try again!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }

                if (TextUtils.isEmpty(emailAddress)) {
                edEmailAddress.setError("Please enter your valid email address!");
                return;
                }
                if (TextUtils.isEmpty(loginPassword)) {
                    edLoginPassword.setError("Please enter your password!");
                    return;
                }

                if (TextUtils.isEmpty(personName)) {
                    edPersonName.setError("Please enter your full name!");
                    return;
                }


                if (loginPassword.length() < 8) {
                    edLoginPassword.setError("Password must be > = 8 characters");
                    return;
                }

                if (phoneNumber.length() < 11) {
                    edPhoneNumber.setError("The Phone Number has to be of 11 characters");
                    return;
                }

                if (secureCode.length()< 8) {
                    edSecureCode.setError("Secure Code must contain a min of 8 characters! ");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //Register user in firebase

                firebaseAuth.createUserWithEmailAndPassword(emailAddress, loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            //email verification

                            FirebaseUser users = firebaseAuth.getCurrentUser();
                            users.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterActivity.this, "Verification emails has been sent. Please check your email", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Something went wrong. Email NOT sent! " + e.getMessage());
                                }
                            });

                            Toast.makeText(RegisterActivity.this, "You have successfully registered!", Toast.LENGTH_SHORT).show();
                            userID = firebaseAuth.getCurrentUser().getUid();
                            DocumentReference docReference  = firestore.collection("Users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("Full Name",personName);
                            user.put("Email Address", emailAddress);
                            user.put("Phone Number", phoneNumber);
                            user.put("Security Code", secureCode);

                            docReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "onSuccess: A new profile is created for this user ID" + userID );
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), LocationActivity.class));
                        }else{
                            Toast.makeText(RegisterActivity.this, "Error, please try again!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }

        });

        textViewLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    public static boolean isValid(String passwordhere) {
        int f1 = 0, f2 = 0, f3 = 0;
        if (passwordhere.length() < 8) {
            return false;
        } else {
            for (int password = 0; password < passwordhere.length(); password++) {
                if (Character.isLetter(passwordhere.charAt(password))) {
                    f1 = 1;
                }
            }
            for (int r = 0; r < passwordhere.length(); r++) {
                if (Character.isDigit(passwordhere.charAt(r))) {
                    f2 = 1;
                }
            }
            for (int s = 0; s < passwordhere.length(); s++) {
                char c = passwordhere.charAt(s);
                if (c>=33&&c<=46||c==64){
                    f3 = 1;
                }
            }
            if (f1==1 && f2==1 && f3==1)
                return  true;
            return false;

        }
    }
}

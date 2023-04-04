package com.georgia.gk.pharmafriendapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

public class Login extends AppCompatActivity {
    private ConstraintLayout loginButton;
    EditText edLoginEmailAddress, edPassword;
    TextView textLink, forgotPasswordLink;
    ProgressBar progressBar2;
    FirebaseAuth firebaseAuth;
    boolean loginPasswordVisible;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edLoginEmailAddress = findViewById(R.id.editTextLoginEmailAddress);
        edPassword = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.loginButton);
        textLink = findViewById(R.id.textViewNewUser);
        progressBar2 = findViewById(R.id.progressBar2);
        firebaseAuth = FirebaseAuth.getInstance();
        forgotPasswordLink = findViewById(R.id.forgotPassword);

        edPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int Right = 2;
                if (event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX()>=edPassword.getRight()-edPassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=edPassword.getSelectionEnd();
                        if (loginPasswordVisible){
                            edPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_off_24, 0);

                            edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            loginPasswordVisible = false;

                        }else{
                            edPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_24, 0);

                            edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            loginPasswordVisible = true;
                        }

                        edPassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginEmailAddress = edLoginEmailAddress.getText().toString().trim();
                String password = edPassword.getText().toString().trim();
                if (TextUtils.isEmpty(loginEmailAddress)) {
                    edLoginEmailAddress.setError("Please enter your valid email address!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    edPassword.setError("Please enter your password!");
                    return;
                }

                if (password.length() < 8) {
                    edPassword.setError("Password must be > = 8 characters");
                    return;
                }

                progressBar2.setVisibility(View.VISIBLE);

                //Authenticate the user when login

                firebaseAuth.signInWithEmailAndPassword(loginEmailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "You have successfully logged in to your account!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }else{
                            Toast.makeText(Login.this, "Error, please try again!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar2.setVisibility(View.GONE);

                        }
                    }
                });

            }
        });

        textLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetEmail = new EditText(v.getContext());
                final AlertDialog.Builder resetPasswordDialog = new AlertDialog.Builder(v.getContext());
                resetPasswordDialog.setTitle("Do you want to reset your password?");
                resetPasswordDialog.setMessage("Please enter your email address to reset your password");
                resetPasswordDialog.setView(resetEmail);

                resetPasswordDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //provide email address to receive the reset link of the password
                           String email = resetEmail.getText().toString();
                           firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   Toast.makeText(Login.this, "Please check your email for the link", Toast.LENGTH_SHORT).show();
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Toast.makeText(Login.this, "Something went wrong! Reset link is NOT sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           });
                    }
                });

                resetPasswordDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     //user clicks no and goes back to the login page
                    }
                });

                resetPasswordDialog.create().show();
            }
        });

    }
}
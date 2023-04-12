package com.georgia.gk.pharmafriendapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPVerificationCode extends AppCompatActivity {

    private ConstraintLayout otpCodeButton;
    private ConstraintLayout otpVerificationButton;
    EditText phoneNumber, otpCode;

    FirebaseAuth firebaseAuth;
    String verificationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification_code);

        phoneNumber = findViewById(R.id.phoneNumber);
        otpCode = findViewById(R.id.otpCode);
        otpCodeButton = findViewById(R.id.otpCodeButton);
        otpVerificationButton = findViewById(R.id.otpVerificationButton);
        firebaseAuth = FirebaseAuth.getInstance();


        otpCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(phoneNumber.getText().toString()))
                {
                    Toast.makeText(OTPVerificationCode.this, "Please Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String number = phoneNumber.getText().toString();
                    sendotpcode(number);
                }
            }
        });

        otpVerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (TextUtils.isEmpty(otpCode.getText().toString()))
                {
                    Toast.makeText(OTPVerificationCode.this, "OTP Code is wrong!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    verifyotpcode(otpCode.getText().toString());
                }

            }
        });
    }

    private void sendotpcode(String phoneNumber)
    {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber("+44" + phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential)
        {
           final String code = credential.getSmsCode();
           if (code !=null)
           {
             verifyotpcode(code);
           }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(OTPVerificationCode.this, "Verification Failed!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s,
                @NonNull PhoneAuthProvider.ForceResendingToken token)
        {
            super.onCodeSent(s,token);
            verificationID = s;
        }
    };

    private void verifyotpcode(String Code)
    {
     PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, Code);
     signinbyCredentials(credential);
    }

    private void signinbyCredentials(PhoneAuthCredential credential)
    {
        FirebaseAuth firebaseAuth1 = FirebaseAuth.getInstance();
        firebaseAuth1.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(OTPVerificationCode.this, "You have logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(OTPVerificationCode.this, HomeActivity.class));
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser !=null)
        {
            startActivity(new Intent(OTPVerificationCode.this, HomeActivity.class));
            finish();
        }
    }
}
package com.georgia.gk.pharmafriendapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationCodeActivity extends AppCompatActivity {

    private ConstraintLayout getOTPCodeButton;
    private ConstraintLayout verifyOTPCodeButton;
    EditText phoneNumber, otpCode;

    FirebaseAuth firebaseAuth;
    String verificationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);

        phoneNumber = findViewById(R.id.phoneNumber);
        otpCode = findViewById(R.id.otpCode);
        getOTPCodeButton = findViewById(R.id.getOTPCodeButton);
        verifyOTPCodeButton = findViewById(R.id.verifyOTPCodeButton);
        firebaseAuth = FirebaseAuth.getInstance();


        getOTPCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(phoneNumber.getText().toString()))
                {
                    Toast.makeText(VerificationCodeActivity.this, "Please Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String number = phoneNumber.getText().toString();
                    sendotpcode(number);
                }
            }
        });

        verifyOTPCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (TextUtils.isEmpty(otpCode.getText().toString()))
                {
                    Toast.makeText(VerificationCodeActivity.this, "OTP Code is wrong!", Toast.LENGTH_SHORT).show();
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
        public void onVerificationCompleted(PhoneAuthCredential credential)
        {
            final String code = credential.getSmsCode();
            if (code !=null)
            {
                verifyotpcode(code);
            }
        }

        @Override
        public void onVerificationFailed( FirebaseException e) {

            Toast.makeText(VerificationCodeActivity.this, "Verification Failed!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent( String s,
                                PhoneAuthProvider.ForceResendingToken token)
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
                    public void onComplete(Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(VerificationCodeActivity.this, "You have logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(VerificationCodeActivity.this, HomeActivity.class));
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
            startActivity(new Intent(VerificationCodeActivity.this, HomeActivity.class));
            finish();
        }
    }
}
package com.swe.cpms;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class AskPhoneNumber extends AppCompatActivity {


    private EditText editTextMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_phone_number);

        editTextMobile = findViewById(R.id.editTextMobile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {// This will automatically take the signed in users to the dashboard
            // User is signed in
            String name=user.getDisplayName(),email=user.getEmail();

            if(name.equals("")||email.equals("")) // profile has not been made yet
            {
                Intent intent = new Intent(AskPhoneNumber.this, MakeProfile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else { // profile has been made
                Intent intent = new Intent(AskPhoneNumber.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

        }
        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = editTextMobile.getText().toString().trim();

                if(mobile.isEmpty() || mobile.length() < 10){
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }

                Intent intent = new Intent(AskPhoneNumber.this, VerifyPhone.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
            }
        });
    }

}
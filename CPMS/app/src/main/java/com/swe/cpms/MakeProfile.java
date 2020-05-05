package com.swe.cpms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MakeProfile extends AppCompatActivity {
    EditText name,email;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_profile);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        btn = (Button) findViewById(R.id.submit_profile);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String user_name = name.getText().toString();
                String user_email= email.getText().toString();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(user_name)
                        //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                        .build();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast msg = Toast.makeText(getBaseContext(),"Name Registered!"+ user_name,Toast.LENGTH_LONG);
                                    msg.show();
                                }
                                else
                                {
                                    Toast msg = Toast.makeText(getBaseContext(),"Name registration unsucessful!"+ user_name,Toast.LENGTH_LONG);
                                    msg.show();
                                }
                            }
                        });
                int flag=0;
                user.updateEmail(user_email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast msg = Toast.makeText(getBaseContext(),"Email updated!"+ user_name,Toast.LENGTH_LONG);
                                    msg.show();
                                    emailVerification();

                                }
                                else
                                {
                                    Toast msg = Toast.makeText(getBaseContext(),"Email resistration unsucessful"+ user_name,Toast.LENGTH_LONG);
                                    msg.show();
                                }
                            }
                        });
            }
        });
    }

    protected  void emailVerification()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast msg = Toast.makeText(getBaseContext(),"Verification Email sent!",Toast.LENGTH_LONG);
                            msg.show();
                            Intent intent = new Intent(MakeProfile.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                });
    }
}

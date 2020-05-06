package com.swe.cpms;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class ViewProfile extends AppCompatActivity {

    TextView name,age,email,dl_no,v_no,gender,rating;
    Button mUpdateProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        mUpdateProfile= (Button) findViewById(R.id.btn_update_profile) ;
        name= (TextView) findViewById(R.id.view_name);
        age=(TextView) findViewById(R.id.view_Age);
        email=(TextView) findViewById(R.id.view_email);
        dl_no=(TextView) findViewById(R.id.view_dl_no);
        v_no=(TextView) findViewById(R.id.view_v_no);
        gender=(TextView) findViewById(R.id.view_gender);
        rating=(TextView) findViewById(R.id.view_rating);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        document.get("name");
                        Map<String,Object> data = document.getData();
                        name.setText(data.getOrDefault("name","").toString());
                        email.setText(data.getOrDefault("email","").toString());
                        age.setText(data.getOrDefault("age","").toString());
                        dl_no.setText(data.getOrDefault("dl_no","").toString());
                        v_no.setText(data.getOrDefault("v_no","").toString());
                        gender.setText(data.getOrDefault("gender","").toString());
                        rating.setText(data.getOrDefault("avg_rating","").toString());
                        Log.d("hakuna", "DocumentSnapshot data: " + data);
                    } else {
                        Toast.makeText(ViewProfile.this,"No such document",Toast.LENGTH_SHORT).show();
                        Log.d("hakuna", "No such document");
                    }
                } else {
                    Toast.makeText(ViewProfile.this,"Feting data failed",Toast.LENGTH_SHORT).show();
                    Log.d("hakuna", "Database interaction failed", task.getException());
                }
            }
        });
        mUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfile.this, MakeProfile.class);
                startActivity(intent);
            }
        });
    }
}

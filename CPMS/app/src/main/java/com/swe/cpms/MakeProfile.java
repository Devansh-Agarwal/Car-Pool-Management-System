package com.swe.cpms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MakeProfile extends AppCompatActivity {
    EditText name,email,e_age,e_dl_no,e_v_no ;
    Button btn,btn_cancel;
    RadioButton genderradioButton;
    RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_profile);

        Intent intent = getIntent();
        String Flag_check = intent.getStringExtra("Flag_check");
        TextView textView = (TextView) findViewById(R.id.view_Flag_check);
        textView.setText(Flag_check);


        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        e_age=(EditText) findViewById(R.id.age);
        e_dl_no=(EditText) findViewById(R.id.driving_license);
        e_v_no=(EditText) findViewById(R.id.vehicle_no);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        btn = (Button) findViewById(R.id.submit_profile);
        btn_cancel=(Button) findViewById(R.id.cancel_update_profile);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeProfile.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String user_name = name.getText().toString();
                if(TextUtils.isEmpty(user_name)) {
                    name.setError("Enter a Valid name");
                    return;
                }

                final String user_email= email.getText().toString();
                if(TextUtils.isEmpty(user_email)) {
                    email.setError("Enter a Valid email id");
                    return;
                }

                String age_string= e_age.getText().toString();
                if(TextUtils.isEmpty(age_string)) {
                    e_age.setError("Enter a Valid age");
                    return;
                }

                int selectedId = radioGroup.getCheckedRadioButtonId();
                if(selectedId==-1)
                {
                    Toast.makeText(MakeProfile.this,"Select valid Gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                int age= Integer.parseInt(age_string);
                String dl_no= e_dl_no.getText().toString();
                String v_no= e_v_no.getText().toString();
                genderradioButton = (RadioButton) findViewById(selectedId);
                String gender = genderradioButton.getText().toString();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(user_name)
                        //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                        .build();

                final FirebaseUser user_auth = FirebaseAuth.getInstance().getCurrentUser();
                user_auth.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast msg = Toast.makeText(getBaseContext(),"Name Registered!"+ user_name,Toast.LENGTH_SHORT);
                                    msg.show();
                                }
                                else
                                {
                                    Toast msg = Toast.makeText(getBaseContext(),"Name registration unsucessful!"+ user_name,Toast.LENGTH_SHORT);
                                    msg.show();
                                }
                            }
                        });

                user_auth.updateEmail(user_email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast msg = Toast.makeText(getBaseContext(),"Email updated!"+ user_email,Toast.LENGTH_SHORT);
                                    msg.show();
                                    emailVerification();

                                }
                                else
                                {
                                    Toast msg = Toast.makeText(getBaseContext(),"Email resistration unsucessful"+ user_email,Toast.LENGTH_SHORT);
                                    msg.show();
                                }
                            }
                        });
                // Updload other details to the database
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> user = new HashMap<>();
                user.put("name", user_name);
                user.put("email", user_email);
                user.put("age", age);
                user.put("dl_no", dl_no);
                user.put("v_no",v_no);
                user.put("gender",gender);
                user.put("uid",user_auth.getUid());
                user.put("avg_rating",0);
                user.put("number_of_ratings",0);


// Add a new document with a generated ID
                db.collection("users")
                        .document(user_auth.getUid())
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("hakuna", "DocumentSnapshot successfully written!");
                                Toast.makeText(MakeProfile.this,"Profile sucessfully stored in database", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("hakuna", "Error writing document", e);
                                Toast.makeText(MakeProfile.this,"Profile storage unsucessful", Toast.LENGTH_SHORT).show();
                            }
                        });
                Intent intent = new Intent(MakeProfile.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
        };
    });
    }

    protected  void emailVerification()
    {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast msg = Toast.makeText(getBaseContext(),"Verification Email sent to "+user.getEmail(),Toast.LENGTH_SHORT);
                            msg.show();
                            Intent intent = new Intent(MakeProfile.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                });
    }
}

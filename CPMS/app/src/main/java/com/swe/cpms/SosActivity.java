package com.swe.cpms;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;

import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class SosActivity extends Activity {


    EditText number_1,number_2;
    Button submit_number,cancel_number,load_number,send_sos,remove_number;


    private static final String FILE_NAME = "sos_numbers.txt";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos_activity);

        number_1 = (EditText) findViewById(R.id.number_1);
        number_2 = (EditText) findViewById(R.id.number_2);

        submit_number = (Button) findViewById(R.id.submit_number);
        cancel_number = (Button) findViewById(R.id.cancel_number);
        load_number = (Button) findViewById(R.id.load_number);
        send_sos = (Button) findViewById(R.id.send_sos);
        remove_number = (Button) findViewById(R.id.remove_number);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(SosActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);


        cancel_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SosActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        submit_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile1 = number_1.getText().toString();
                String mobile2 = number_2.getText().toString();
                if(mobile1.isEmpty() || mobile1.length() < 10){
                    number_1.setError("Enter a valid mobile");
                    number_1.requestFocus();
                    return;
                }
                if(mobile2.isEmpty() || mobile2.length() < 10){
                    number_2.setError("Enter a valid mobile");
                    number_2.requestFocus();
                    return;
                }
                FileOutputStream fos = null;
                try
                {
                    fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                    fos.write("".getBytes());
                    fos.write("f".getBytes());
                    fos.write(mobile1.getBytes());
                    fos.write(mobile2.getBytes());
                    number_1.getText().clear();
                    number_2.getText().clear();

                }catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        load_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileInputStream fis = null;

                try {
                    fis = openFileInput(FILE_NAME);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);
                    String text;
                    String temp_1 = "";
                    String temp_2 = "";
                    int i = 0;
                    boolean numberinfile = true;
                    while (i<=20) {
                        if(i == 0)
                        {
                            char flag_exist = (char)br.read();
                            if(flag_exist == 't')
                            {
                                numberinfile = false;
                                break;
                            }
                        }
                        else if(i<=10)
                        {
                            temp_1 +=(char)br.read();
                        }
                        else if(i>10)
                        {
                            temp_2 +=(char)br.read();
                        }

                        i++;
                    }
                    if(numberinfile == true) {
                        number_1.setText(temp_1.toString());
                        number_2.setText(temp_2.toString());
                    }
                    else{
                        number_1.setError("No number in storage, enter number to be stored");
                        number_2.setError("No number in storage, enter number to be stored");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });
        remove_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileOutputStream fos = null;
                try {
                    fos = openFileOutput("sos_numbers.txt", MODE_PRIVATE);
                    fos.write("".getBytes());
                    fos.write("t".getBytes());
                    number_1.setError("No number in storage, enter number to be stored");
                    number_2.setError("No number in storage, enter number to be stored");
                    number_1.setText("");
                    number_2.setText("");

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

            }
        });
        send_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GpsTracker gt = new GpsTracker(getApplicationContext());
                Location l = gt.getLocation();

                if( l == null){
                    Toast.makeText(getApplicationContext(),"GPS unable to get Value",Toast.LENGTH_SHORT).show();
                }else {
                    double lat = l.getLatitude();
                    double lon = l.getLongitude();

                    FileInputStream fis = null;

                    try {
                        fis = openFileInput(FILE_NAME);
                        InputStreamReader isr = new InputStreamReader(fis);
                        BufferedReader br = new BufferedReader(isr);
                        String text;
                        String temp_1 = "";
                        String temp_2 = "";
                        int i = 0;
                        boolean numbernotinfile = false;
                        while (i<=20) {
                            if(i == 0)
                            {
                                char flag_exist = (char)br.read();
                                if(flag_exist == 't')
                                {
                                    numbernotinfile = true;
                                    break;
                                }
                            }
                            else if(i<=10)
                            {
                                temp_1 +=(char)br.read();
                            }
                            else if(i>10)
                            {
                                temp_2 +=(char)br.read();
                            }

                            i++;
                        }
                        if(numbernotinfile == false){
                            number_1.setText(temp_1.toString());
                            number_2.setText(temp_2.toString());

                            String phoneNumber_1 = "+91" + temp_1;
                            String phoneNumber_2 = "+91" + temp_2;
                            String message = "Hey!! I am currently in CPMS ride and sensing danger, My current location is\n"+"http://www.google.com/maps/place/" + lat + "," + lon;
                            SmsManager smsManager = SmsManager.getDefault();
                            Toast.makeText(getApplicationContext(),"GPS Lat = "+lat+"\n lon = "+lon,Toast.LENGTH_SHORT).show();
                            smsManager.sendTextMessage(phoneNumber_1,null,message,null,null);
                            smsManager.sendTextMessage(phoneNumber_2,null,message,null,null);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "No Number is stored in the directory", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
        });

    }
}


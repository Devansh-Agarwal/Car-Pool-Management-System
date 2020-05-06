package com.swe.cpms;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class MyRides extends AppCompatActivity{
    LinearLayout myLinearLayout, outerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rides);
        myLinearLayout = (LinearLayout) findViewById(R.id.frameMyRides);
        final int N = 20; // total number of textviews to add

        Context context;
        CardView cardview;
        TextView textview1, textview2, textview3, textview4, textview5, textview6;
        Button button;
        LinearLayout innerLayout1,innerLayout2;

        for (int i = 0; i < N; i++) {
            context = getApplicationContext();
            cardview = new CardView(context);

            cardview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            cardview.setRadius(15);
            cardview.setPadding(25, 25, 25, 25);
            cardview.setCardBackgroundColor(Color.WHITE);
            cardview.setMaxCardElevation(30);
            cardview.setMaxCardElevation(6);
            cardview.setUseCompatPadding(true);

            outerLayout = new LinearLayout(context);
            outerLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            outerLayout.setOrientation(LinearLayout.VERTICAL);

            innerLayout1 = new LinearLayout(context);
            innerLayout1.setOrientation(LinearLayout.HORIZONTAL);

            textview1 = new TextView(context);
            textview1.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 4));
            textview1.setText("name");

            textview2 = new TextView(context);
            textview2.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 2));
            textview2.setText("rating");

            innerLayout1.addView(textview1);
            innerLayout1.addView(textview2);

            outerLayout.addView(innerLayout1);

            textview3 = new TextView(context);
            textview3.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            textview3.setText("source");

            textview4 = new TextView(context);
            textview4.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            textview4.setText("destination");

            outerLayout.addView(textview3);
            outerLayout.addView((textview4));

            innerLayout2 = new LinearLayout(context);
            innerLayout2.setOrientation(LinearLayout.HORIZONTAL);

            textview5 = new TextView(context);
            textview5.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 2));
            textview5.setText("time");

            textview6 = new TextView(context);
            textview6.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 2));
            textview6.setText("seats");

            button = new Button(context);
            button.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 3));
            button.setText("Request ride");

            innerLayout2.addView(textview5);
            innerLayout2.addView(textview6);
            innerLayout2.addView(button);

            outerLayout.addView(innerLayout2);
            cardview.addView(outerLayout);
            myLinearLayout.addView(cardview);
        }
    }
}

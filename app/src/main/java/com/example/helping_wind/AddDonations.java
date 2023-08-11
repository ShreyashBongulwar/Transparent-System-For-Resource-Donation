package com.example.helping_wind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddDonations extends AppCompatActivity {


    private Toolbar toolbar;
    private TextView donation1,donation2,donation3,donation4,donation5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donations);

        toolbar=findViewById(R.id.toolbarr);
        donation1=findViewById(R.id.d1);
        donation2=findViewById(R.id.d2);
        donation3=findViewById(R.id.d3);
        donation4=findViewById(R.id.d4);
        donation5=findViewById(R.id.d5);



        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Donations");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().
                child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("donations");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {
                        donation1.setText(snapshot.child("food").getValue().toString());
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    try {
                        donation2.setText(snapshot.child("cloth").getValue().toString());
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    try {
                        donation3.setText(snapshot.child("education").getValue().toString());
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    try {
                        donation4.setText(snapshot.child("medicine").getValue().toString());
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    try {
                        donation5.setText(snapshot.child("shelter").getValue().toString());
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
package com.example.helping_wind;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectRegistrationActivity extends AppCompatActivity {

    Button donorregistration,recepientregistration;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_registration);
        donorregistration=findViewById(R.id.donorregistration);
        recepientregistration=findViewById(R.id.recepientregistration);
        donorregistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(SelectRegistrationActivity.this,DonorRegistrationActivity.class);
                startActivity(intent1);
            }
        });
        recepientregistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent2=new Intent(SelectRegistrationActivity.this,RecepientRegistrationActivity.class);
                    startActivity(intent2);
            }
        });
    }
}
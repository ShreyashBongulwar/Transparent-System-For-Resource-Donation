package com.example.helping_wind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView type,name,email,phonenumber,id;
    private CircleImageView profileImage;
    private Button backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar=findViewById(R.id.toolbarr);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        profileImage=findViewById(R.id.profile_user_image);
        type=findViewById(R.id.profile_type);
        name=findViewById(R.id.profile_name);
        email=findViewById(R.id.profile_email);
        phonenumber=findViewById(R.id.profile_contact);
        id=findViewById(R.id.profile_id);
        backbutton=findViewById(R.id.profile_backbutton);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().
                child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    type.setText(snapshot.child("type").getValue().toString());
                    name.setText(snapshot.child("name").getValue().toString());
                    id.setText(snapshot.child("idnumber").getValue().toString());
                    phonenumber.setText(snapshot.child("phonenumber").getValue().toString());
                    email.setText(snapshot.child("email").getValue().toString());
                    profileImage.setImageResource(R.drawable.profileimage);
//                    Glide.with(getApplicationContext()).load(snapshot.child("profilepictureurl").getValue().toString()).into(profileImage);
//                    Glide.with(getApplicationContext()).load(snapshot.child("picturesqueness").getValue().toString()).into(profileImage);
//                    if (snapshot.hasChild("profilepictureurl")) {
//                        String imageUrl = snapshot.child("profilepictureurl").getValue().toString();
//                        Glide.with(getApplicationContext()).load(imageUrl).into(profileImage);
//                    } else {
//                        profileImage.setImageResource(R.drawable.profileimage);
//                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
package com.example.helping_wind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.helping_wind.Adapter.UserAdapter;
import com.example.helping_wind.Model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private CircleImageView nav_profile_image;
    private TextView nav_fullname, nav_email, nav_contact, nav_type,nav_idNumber;
    private DatabaseReference userRef;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<User> userList;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("HelpingWind");
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (MainActivity.this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        progressBar=findViewById(R.id.progressbar);
        recyclerView=findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        userList=new ArrayList<>();
        userAdapter=new UserAdapter(MainActivity.this,userList);
        recyclerView.setAdapter(userAdapter);


        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String type=snapshot.child("type").getValue().toString();
                if(type.equals("donor")){
                    readRecipients();
                }
                else
                {
                    readDonors();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        nav_profile_image = navigationView.getHeaderView(0).findViewById(R.id.navigation_user_image);
        nav_fullname = navigationView.getHeaderView(0).findViewById(R.id.navigation_full_name);
        nav_email = navigationView.getHeaderView(0).findViewById(R.id.navigation_user_email);
        nav_contact = navigationView.getHeaderView(0).findViewById(R.id.navigation_user_contact);
        nav_type = navigationView.getHeaderView(0).findViewById(R.id.navigation_user_type);
//        nav_idNumber=navigationView.getHeaderView(0).findViewById(R.id.navigation_user_idNumber);


        userRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue().toString();
                    nav_fullname.setText(name);

                    String email = snapshot.child("email").getValue().toString();
                    nav_email.setText(email);

                    String contact = snapshot.child("phonenumber").getValue().toString();
                    nav_contact.setText(contact);

                    String type = snapshot.child("type").getValue().toString();
                    nav_type.setText(type);

//                    String idNum = snapshot.child("idnumber").getValue().toString();
//                    nav_type.setText(type);
                    nav_profile_image.setImageResource(R.drawable.profileimage);

//                    if (snapshot.hasChild("profilepictureurl")) {
//                        String imageUrl = snapshot.child("profilepictureurl").getValue().toString();
//                        Glide.with(getApplicationContext()).load(imageUrl).into(nav_profile_image);
//                    } else {
//                        nav_profile_image.setImageResource(R.drawable.profileimage);
//                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void readDonors() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users");
        Query query = reference.orderByChild("type").equalTo("donor");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if (userList.isEmpty()){
                    Toast.makeText(MainActivity.this, "No Donors", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void readRecipients() {
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference()
                .child("users");
        Query query=reference.orderByChild("type").equalTo("recipient");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if (userList.isEmpty()){
                    Toast.makeText(MainActivity.this, "No recipients", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.addDonations:
                Intent intent9 = new Intent(MainActivity.this, AddDonations.class);
                startActivity(intent9);
                break;
            case R.id.profile:
                Intent intent2 = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent2);
                break;
            case R.id.food:
                Intent intent3 = new Intent(MainActivity.this, FoodActivity.class);
                intent3.putExtra("group", "food");
                startActivity(intent3);
                break;
            case R.id.clothes:
                Intent intent4 = new Intent(MainActivity.this, ClothActivity.class);
                intent4.putExtra("group", "clothes");
                startActivity(intent4);
                break;
            case R.id.educational:
                Intent intent5 = new Intent(MainActivity.this, EducationActivity.class);
                intent5.putExtra("group", "educational");
                startActivity(intent5);
                break;
            case R.id.medication:
                Intent intent6 = new Intent(MainActivity.this, MedicationActivity.class);
                intent6.putExtra("group", "medication");
                startActivity(intent6);
                break;
            case R.id.shelter:
                Intent intent7 = new Intent(MainActivity.this, ShelterActivity.class);
                intent7.putExtra("group", "shelter");
                startActivity(intent7);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent8 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent8);
                break;
            case R.id.sendEmail:
                Intent intent10 = new Intent(Intent.ACTION_SENDTO);
                Uri.fromParts("mailto","abc@email.com",null);
                startActivity(Intent.createChooser(intent10,"send mail...."));
                break;

            case R.id.search:
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=Ngo near me");
                Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
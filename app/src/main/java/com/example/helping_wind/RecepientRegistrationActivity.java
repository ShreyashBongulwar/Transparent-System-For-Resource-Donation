package com.example.helping_wind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecepientRegistrationActivity extends AppCompatActivity {

    private TextView recepient_alreadyUser_button;
    private CircleImageView recepient_profileimage;
    private EditText recepient_name,recepient_phone,recepient_Email,recepient_Password,recepient_address,recepient_id;
    private Button recepient_register_button;
    private Uri resultUri;
    private ProgressDialog loader;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;
    CheckBox c1,c2,c3,c4,c5;
    Donations donations;
    int i=0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepient_registration);

        recepient_alreadyUser_button = findViewById(R.id.recepient_alreadyUser_button);
        recepient_alreadyUser_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecepientRegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        donations = new Donations();
        recepient_profileimage = findViewById(R.id.recepient_profileimage);
        recepient_name = findViewById(R.id.recepient_name);
        recepient_phone = findViewById(R.id.recepient_phone);
        recepient_Email = findViewById(R.id.recepient_email);
        recepient_address = findViewById(R.id.recepient_address);
        recepient_Password = findViewById(R.id.recepient_password);
        recepient_id = findViewById(R.id.recepient_id);
        recepient_register_button = findViewById(R.id.recepient_register_button);
        loader = new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        c1=findViewById(R.id.food);
        c2=findViewById(R.id.clothes);
        c3=findViewById(R.id.educational);
        c4=findViewById(R.id.medication);
        c5=findViewById(R.id.shelter);

        recepient_profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        recepient_register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = recepient_Email.getText().toString().trim();
                final String password = recepient_Password.getText().toString().trim();
                final String fullName = recepient_name.getText().toString().trim();
                final String phoneNumber = recepient_phone.getText().toString().trim();
                final String address = recepient_address.getText().toString().trim();
                final String idNumber=recepient_id.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    recepient_Email.setError("Email is required!");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    recepient_Password.setError("Password is required!");
                    return;
                }
                if (TextUtils.isEmpty(fullName)){
                    recepient_name.setError("Full name is required is required!");
                    return;
                }
                if (TextUtils.isEmpty(phoneNumber)){
                    recepient_phone.setError("Phone Number is required!");
                    return;
                }
                if (TextUtils.isEmpty(idNumber)){
                    recepient_id.setError("Id Number is required!");
                    return;
                }
                if (TextUtils.isEmpty(address)){
                    recepient_Password.setError("Address is required!");
                    return;
                }
                else {
                    loader.setMessage("Registering you...");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()){
                                String error = task.getException().toString();
                                Toast.makeText(RecepientRegistrationActivity.this, "Error" + error, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                String currentUserId = mAuth.getCurrentUser().getUid();

                                userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("donations");
                                if(c1.isChecked())
                                {
                                    donations.setFood("food");
                                    userDatabaseRef.setValue(donations);
                                }
                                if(c2.isChecked())
                                {
                                    donations.setCloth("clothes");
                                    userDatabaseRef.setValue(donations);
                                }
                                if(c3.isChecked())
                                {
                                    donations.setEducation("educational");
                                    userDatabaseRef.setValue(donations);
                                }
                                if(c4.isChecked())
                                {
                                    donations.setMedicine("medication");
                                    userDatabaseRef.setValue(donations);
                                }
                                if(c5.isChecked())
                                {
                                    donations.setShelter("shelter");
                                    userDatabaseRef.setValue(donations);
                                }


                                userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
                                HashMap userInfo = new HashMap();
                                userInfo.put("id", currentUserId);
                                userInfo.put("name", fullName);
                                userInfo.put("email", email);
                                userInfo.put("idnumber", idNumber);
                                userInfo.put("phonenumber", phoneNumber);
                                userInfo.put("address",address);
                                userInfo.put("type", "recipient");



                                userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(RecepientRegistrationActivity.this, "Data set Successfully", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(RecepientRegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        finish();
                                        //loader.dismiss();
                                    }
                                });

                                if (resultUri !=null){
                                    final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                                            .child("profile images").child(currentUserId);
                                    Bitmap bitmap = null;

                                    try {
                                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                                    byte[] data  = byteArrayOutputStream.toByteArray();
                                    UploadTask uploadTask = filePath.putBytes(data);

                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RecepientRegistrationActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            if (taskSnapshot.getMetadata() !=null && taskSnapshot.getMetadata().getReference() !=null){
                                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String imageUrl = uri.toString();
                                                        Map newImageMap = new HashMap();
                                                        newImageMap.put("profilepictureurl", imageUrl);

                                                        userDatabaseRef.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if (task.isSuccessful()){
                                                                    Toast.makeText(RecepientRegistrationActivity.this, "Image url added to database successfully", Toast.LENGTH_SHORT).show();
                                                                }else {
                                                                    Toast.makeText(RecepientRegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                        finish();
                                                    }
                                                });
                                            }

                                        }
                                    });

                                    Intent intent = new Intent(RecepientRegistrationActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    loader.dismiss();
                                }
                            }

                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1 && resultCode == RESULT_OK && data !=null){
            resultUri = data.getData();
            recepient_profileimage.setImageURI(resultUri);
        }
    }
}
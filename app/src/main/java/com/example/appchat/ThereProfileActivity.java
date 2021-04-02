package com.example.appchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appchat.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThereProfileActivity extends AppCompatActivity {
    CircleImageView image_profile;
    TextView username, age, sdt;
    Button lien_he;

    DatabaseReference reference;
    FirebaseUser fuser;
    String uid;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_there_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        image_profile = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        age = findViewById(R.id.age);
        sdt = findViewById(R.id.sdt);
        lien_he = findViewById(R.id.lien_he);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("id").equalTo(uid);
//        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
//        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String name = ""+ ds.child("username").getValue();
                    String tuoi = ""+ ds.child("age").getValue();
                    String SĐT = ""+ ds.child("sdt").getValue();
                    String img = ""+ ds.child("imageURL").getValue();


                    username.setText(name.toString());
                    age.setText("age: "+ tuoi.toString()+ " tuổi");
                    sdt.setText("SĐT: "+ SĐT.toString());
                    if (img.equals("default")) {
                        image_profile.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Glide.with(getApplicationContext()).load(img).into(image_profile);
                    }


//                    User user = dataSnapshot.getValue(User.class);
//                    usename.setText(user.getUsername());
//                    age.setText("age: "+user.getAge()+" tuổi");
//                    sdt.setText("SĐT: " +user.getSdt());
//                    if (user.getImageURL().equals("default")) {
//                        image_profile.setImageResource(R.mipmap.ic_launcher);
//                    } else {
//                        Glide.with(getApplicationContext()).load(user.getImageURL()).into(image_profile);
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        lien_he.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + sdt.getText().toString()));

                startActivity(callIntent);
            }
        });


    }
}

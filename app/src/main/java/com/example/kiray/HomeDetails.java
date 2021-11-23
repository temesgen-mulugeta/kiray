package com.example.kiray;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import Model.HomeInFeedModel;

public class HomeDetails extends AppCompatActivity {
    private Home home;
    private String homeId;
    private TextView HDhomeName,HDsub,HDworeda,HDrooms,HDprice,HDphone,HDdesc;
    private ImageView HDhomePic;
    private DatabaseReference mbase;
    private Button contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_details);

        homeId=getIntent().getExtras().get("homeId").toString();

        HDhomeName =(TextView)findViewById(R.id.HDhomeName);
        HDsub=(TextView)findViewById(R.id.HDsub);
        HDworeda=(TextView)findViewById(R.id.HDworeda);
        HDrooms=(TextView)findViewById(R.id.HDrooms);
        HDprice=(TextView)findViewById(R.id.HDprice);
        HDphone=(TextView)findViewById(R.id.HDphone);
        HDdesc=(TextView)findViewById(R.id.HDdesc);
        HDhomePic=(ImageView) findViewById(R.id.HDhomePic);
        mbase=  FirebaseDatabase.getInstance().getReference("Rooms");
        contact =findViewById(R.id.contact);


        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseRef;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseRef= firebaseDatabase.getReference().child("Rooms").child(homeId);
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 home = dataSnapshot.getValue(Home.class);
                HDhomeName.setText(home.getTitle());
                HDprice.setText(Double.toString(home.getPrice()));
                HDsub.setText(home.getSubCity());
                HDworeda.setText(Integer.toString(home.getWoreda()));
                HDrooms.setText(Integer.toString(home.getRooms()));
                HDphone.setText(home.getPhone());
                HDdesc.setText(home.getDescription());

                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(home.getPhoto());


                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri downloadUrl)
                    {
                        System.out.println(downloadUrl.toString());
                        Picasso.get()
                                .load(downloadUrl)
                                .placeholder(R.drawable.ic_home)
                                .error(R.drawable.ic_close)
                                .into(HDhomePic);
                        //do something with downloadurl
                    }
                });

                contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + home.getPhone()));
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("xx", "listener canceled", databaseError.toException());
            }
        });




    }

}
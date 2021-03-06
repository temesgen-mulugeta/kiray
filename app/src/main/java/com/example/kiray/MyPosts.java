package com.example.kiray;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiray.Adapter.HomeAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MyPosts extends AppCompatActivity {
    // private Button nevigation;



    RecyclerView rv;
    HomeAdapter adapter;




    private DatabaseReference mbase;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);
        auth = FirebaseAuth.getInstance();
        String uid =auth.getCurrentUser().getUid();

        mbase= FirebaseDatabase.getInstance().getReference("Rooms");
        Query ref= mbase.orderByChild("userId").equalTo(uid);
        rv= (RecyclerView) findViewById(R.id.rec);
        rv.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Home> options
                = new FirebaseRecyclerOptions.Builder<Home>()
                .setQuery(ref, Home.class)
                .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new HomeAdapter(options,MyPosts.this);
        // Connecting Adapter class with the Recycler view*/
        rv.setAdapter(adapter);



    }
    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stoping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }


}


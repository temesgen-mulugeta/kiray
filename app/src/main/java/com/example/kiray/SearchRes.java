package com.example.kiray;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.kiray.Adapter.HomeAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchRes extends AppCompatActivity {

    RecyclerView rv;
    HomeAdapter adapter;


    String subCity;

    private DatabaseReference mbase;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_res);



        subCity=getIntent().getExtras().get("sub").toString();

        mbase= FirebaseDatabase.getInstance().getReference("Rooms");
        Query ref= mbase.orderByChild("subCity").equalTo(subCity);
        rv= (RecyclerView) findViewById(R.id.recycler_menu2);
        rv.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Home> options
                = new FirebaseRecyclerOptions.Builder<Home>()
                .setQuery(ref, Home.class)
                .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new HomeAdapter(options,SearchRes.this);
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
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import Model.HomeInFeedModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomePage extends AppCompatActivity {
    // private Button nevigation;

    NavigationView sidenav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    FirebaseAuth mAuth;

    DatabaseReference db;
    FirebaseHelper helper;
    RecyclerView rv;
    EditText nameEditTxt;
    HomeAdapter adapter;

    private DatabaseReference HomeRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    BottomNavigationView bottomNavigationView;

    CircleImageView SNpropic;

    private FirebaseAuth auth;
    private DatabaseReference mbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        bottomNavigationView =findViewById(R.id.bottom_navigation);
        mbase=  FirebaseDatabase.getInstance().getReference("Rooms");

        rv= (RecyclerView) findViewById(R.id.homeRV);
        rv.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Home> options
                = new FirebaseRecyclerOptions.Builder<Home>()
                .setQuery(mbase, Home.class)
                .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new HomeAdapter(options,HomePage.this);
        // Connecting Adapter class with the Recycler view*/
        rv.setAdapter(adapter);

        navigation();

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

    private void navigation() {

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.homePage:
                        return true;

                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), search.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.postAHome:
                        startActivity(new Intent(getApplicationContext(), PostAHome.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });

        Toolbar toolbar2;
        toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        sidenav = (NavigationView) findViewById(R.id.sidenavmenu);
        SNpropic = (CircleImageView) sidenav.getHeaderView(0).findViewById(R.id.profile_pic_SN);
        // sn_name=(NavigationView)findViewById(R.id.username_sn);
        //View headerView= NavigationView;
        // view
        drawerLayout = (DrawerLayout) findViewById(R.id.draw);
        toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar2,
                R.string.open,
                R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        sidenav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.profileSN:
                        //Toast.makeText(getApplicationContext(), "Profile will Open", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent = new Intent(HomePage.this, Profile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        break;
                    case R.id.mypostsSN:
                        //Toast.makeText(getApplicationContext(), "Myposts will Open", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent1 = new Intent(HomePage.this, MyPosts.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);

                        break;
                    case R.id.notificationSN:
                        //Toast.makeText(getApplicationContext(), "Notifications will Open", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent2 = new Intent(HomePage.this, Notifications.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);

                        break;
                    case R.id.settingsSN:
                        //Toast.makeText(getApplicationContext(), "Settings will Open", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent3 = new Intent(HomePage.this, Settings.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent3);

                        break;
                    case R.id.exitSN:
                        Toast.makeText(getApplicationContext(), "Exit", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        FirebaseAuth.getInstance().signOut();

                        Intent intent7 = new Intent(HomePage.this, MainActivity.class);
                        startActivity(intent7);
                        break;
                    case R.id.logoutSN:
                        Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        FirebaseAuth.getInstance().signOut();

                        Intent intent5 = new Intent(HomePage.this, Login.class);
                        startActivity(intent5);
                        break;
                    case R.id.aboutusSN:
                        // Toast.makeText(getApplicationContext(), "About Us will Open", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent4 = new Intent(HomePage.this, AboutUs.class);
                        intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent4);

                        break;

                }
                return true;
            }
        });

    }
}


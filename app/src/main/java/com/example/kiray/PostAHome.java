package com.example.kiray;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAHome extends AppCompatActivity {
    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
// ...


    FirebaseStorage storage;
    StorageReference storageReference;

    Home newHome;
    String uid;

    NavigationView sidenav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;


    CircleImageView SNpropic;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;


    private Button upldBtn;
    private Uri filePath;
    private ImageView homePhoto;
    private final int PICK_IMAGE_REQUEST = 22;
    private String url;


    private TextInputLayout pstTitle,subCity,woreda,phone,price,rooms,description;
    private TextView phtoReq;



    private Button btnPost;
    private boolean imgUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        imgUpload=true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_a_home);
        mAuth = FirebaseAuth.getInstance();


        newHome= new Home();
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        navigation();
        uploadImage();
        initializeFields();

        btnPost =findViewById(R.id.btnPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { if(isAllFieldsChecked()){
                uploadImageToFirebase();

            }
            }
        });





    }
    private void initializeFields(){
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        pstTitle = findViewById(R.id.pstTitle);
        subCity = findViewById(R.id.subCity);
        woreda = findViewById(R.id.woreda);
        phone= findViewById(R.id.phone);
        price=findViewById(R.id.price);
        rooms=findViewById(R.id.rooms);
        description=findViewById(R.id.description);
        phtoReq =findViewById(R.id.phtoReq);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray( R.array.SubCityString ) );
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.ddSubCity);
        textView.setAdapter(adapter);
    }

    private boolean isAllFieldsChecked(){

        if(filePath==null) {phtoReq.setVisibility(View.VISIBLE); return false;} else phtoReq.setVisibility(View.GONE);
        if(TextUtils.isEmpty(pstTitle.getEditText().getText().toString())){ pstTitle.setError("Title is Required"); return false;} else pstTitle.setError(null);
        if(TextUtils.isEmpty(subCity.getEditText().getText().toString())){ subCity.setError("Sub City is Required"); return false;}else subCity.setError(null);
        if(TextUtils.isEmpty(woreda.getEditText().getText().toString())){ woreda.setError("Woreda is Required");return false;}else woreda.setError(null);
        try {
            Integer d = Integer.parseInt(woreda.getEditText().getText().toString());
            woreda.setError(null);
        } catch (NumberFormatException nfe) {
            phone.setError("Enter a valid woreda");
            return false;
        }
        if(TextUtils.isEmpty(phone.getEditText().getText().toString())){ phone.setError("phone is Required");return false;}else phone.setError(null);
        if(TextUtils.isEmpty(price.getEditText().getText().toString())){ price.setError("price is Required"); return false;}else price.setError(null);
        try {
            double d = Double.parseDouble(price.getEditText().getText().toString());
            price.setError(null);
        } catch (NumberFormatException nfe) {
            phone.setError("Enter a valid price");
            return false;
        }
        if(TextUtils.isEmpty(rooms.getEditText().getText().toString())){ rooms.setError("rooms is Required"); return false;}else rooms.setError(null);
        try {
            Integer d = Integer.parseInt(rooms.getEditText().getText().toString());
            rooms.setError(null);
        } catch (NumberFormatException nfe) {
            phone.setError("Enter a valid No. of Rooms");
            return false;
        }
        if(TextUtils.isEmpty(description.getEditText().getText().toString())){ description.setError("description is Required"); return false;}else description.setError(null);
        fillClass();

     return true;
    }

    void fillClass(){
        newHome.setDescription(description.getEditText().getText().toString());
        newHome.setPhone(phone.getEditText().getText().toString());
        newHome.setPrice(Double.parseDouble( price.getEditText().getText().toString()));
        newHome.setRooms(Integer.parseInt(price.getEditText().getText().toString()));
        newHome.setSubCity(subCity.getEditText().getText().toString());
        newHome.setTitle(pstTitle.getEditText().getText().toString());
        newHome.setWoreda(Integer.parseInt(woreda.getEditText().getText().toString()));
        newHome.setUserId(uid);

        newHome.setId(UUID.randomUUID().toString());


    }

    private void uploadImage(){
        upldBtn = findViewById(R.id.upldBtn);
        homePhoto = findViewById(R.id.homePhoto);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        upldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SelectImage();
            }
        });
    }

    private void uploadImageToFirebase()
    {

        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Image...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    url= ref.toString();
                                    addToDatabase();

                                    progressDialog.dismiss();


                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            setFalse();
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(PostAHome.this,
                                            "Image Uplaod Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }
    private  void setFalse(){
        imgUpload=false;
    }

    private void addToDatabase(){
        newHome.setPhoto(url);
        mDatabase.child("Rooms").child(uid).child(newHome.getId()).setValue(newHome);

    }


    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }




    private void navigation(){


        Toolbar toolbar2;
        toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        sidenav = (NavigationView) findViewById(R.id.sidenavmenu);
        SNpropic = (CircleImageView) sidenav.getHeaderView(0).findViewById(R.id.profile_pic_SN);
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
                        Intent intent = new Intent(PostAHome.this, Profile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        break;
                    case R.id.mypostsSN:
                        //Toast.makeText(getApplicationContext(), "Myposts will Open", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent1 = new Intent(PostAHome.this, MyPosts.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);

                        break;
                    case R.id.notificationSN:
                        //Toast.makeText(getApplicationContext(), "Notifications will Open", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent2 = new Intent(PostAHome.this, Notifications.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);

                        break;
                    case R.id.settingsSN:
                        //Toast.makeText(getApplicationContext(), "Settings will Open", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent3 = new Intent(PostAHome.this, Settings.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent3);

                        break;
                    case R.id.exitSN:
                        Toast.makeText(getApplicationContext(), "Exit", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        FirebaseAuth.getInstance().signOut();

                        Intent intent7 = new Intent(PostAHome.this, MainActivity.class);
                        startActivity(intent7);
                        break;
                    case R.id.logoutSN:
                        Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        FirebaseAuth.getInstance().signOut();

                        Intent intent5 = new Intent(PostAHome.this, Login.class);
                        startActivity(intent5);
                        break;
                    case R.id.aboutusSN:
                        //  Toast.makeText(getApplicationContext(), "About Us will Open", Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent4 = new Intent(PostAHome.this, AboutUs.class);
                        intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent4);

                        break;
                }
                return true;
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.postAHome);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.homePage:
                        startActivity(new Intent(getApplicationContext(), HomePage.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), search.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.postAHome:
                        return true;

                }
                return false;
            }

        });

    }
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                homePhoto.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}


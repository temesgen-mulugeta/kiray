package com.example.kiray;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;


import java.util.ArrayList;

/*
 * 1. RECEIVE DB REFERENCE
 * 2. SAVE
 * 3. RETRIEVE
 * 4. RETURN ARRAYLIST
 */
public class FirebaseHelper {
    DatabaseReference db;
    Boolean saved=null;
    ArrayList<Home> spacecrafts=new ArrayList<>();

    /*
     PASS DATABASE REFRENCE
     */
    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
    }

    //WRITE IF NOT NULL
    public Boolean save(Home spacecraft)
    {
        if(spacecraft==null)
        {
            saved=false;

        }else
        {
            try
            {
                db.child("Spacecraft").push().setValue(spacecraft);
                saved=true;

            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }
        }

        return saved;
    }

    //IMPLEMENT FETCH DATA AND FILL ARRAYLIST
    private void fetchData(DataSnapshot dataSnapshot)
    {
        spacecrafts.clear();


            Home spacecraft=dataSnapshot.getValue(Home.class);
            spacecrafts.add(spacecraft);

    }

    //READ BY HOOKING ONTO DATABASE OPERATION CALLBACKS
    public ArrayList<Home> retrieve()
    {
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return spacecrafts;
    }

}

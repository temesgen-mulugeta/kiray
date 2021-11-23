package com.example.kiray.Adapter;


import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.kiray.Home;
import com.example.kiray.HomeDetails;
import com.example.kiray.HomePage;
import com.example.kiray.MyViewHolder;
import com.example.kiray.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HomeAdapter extends FirebaseRecyclerAdapter<
        Home, HomeAdapter.HomeViewHolder> {

    public HomeAdapter(@NonNull @NotNull FirebaseRecyclerOptions<Home> options, Context cntxt) {
        super(options);
        this.cntxt = cntxt;
    }

     Context cntxt;

    public HomeAdapter(
            @NonNull FirebaseRecyclerOptions<Home> options)
    {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull @NotNull HomeAdapter.HomeViewHolder holder, int position, @NonNull @NotNull Home model) {
            String phone = "P: " + model.getPhone();
        holder.phone.setText(phone);

                String price=Double.toString(model.getPrice());
                holder.price.setText(price+"ETB");

                String location =  model.getSubCity() + "/" + model.getWoreda();
                holder.location.setText(location);

                String rooms= Integer.toString( model.getRooms());
                holder.rooms.setText(rooms);
                holder.title.setText(model.getTitle());
       // Task<Uri> ref =  FirebaseStorage.getInstance().getReferenceFromUrl(model.getPhoto()).getDownloadUrl();
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(model.getPhoto());


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
                        .into(holder.photo);
                //do something with downloadurl
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String homeId= getRef(position).getKey();
                Intent detailInent = new Intent(cntxt,HomeDetails.class);
                detailInent.putExtra("homeId",homeId);
                cntxt.startActivity(detailInent);

            }
        });



    }

    @NonNull
    @NotNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homes_in_feed_design, parent, false);
        return new HomeAdapter.HomeViewHolder(view);
    }

    class HomeViewHolder extends RecyclerView.ViewHolder  {

        public TextView title,location,rooms,price,phone;
        public ImageView photo;
        public MaterialCardView homeCard;


        public HomeViewHolder(View itemView) {
            super(itemView);
            photo=(ImageView)itemView.findViewById(R.id.HIFhomePic);
            title=(TextView) itemView.findViewById(R.id.HIFapartmentName);
            location= (TextView) itemView.findViewById(R.id.HIFlocation);
            rooms=(TextView) itemView.findViewById(R.id.HIFrooms);
            price=(TextView) itemView.findViewById(R.id.HIFprice);
            phone=(TextView) itemView.findViewById(R.id.HIFphone);

           homeCard=(MaterialCardView) itemView.findViewById(R.id.homeCard);
//            homeCard.setOnClickListener(
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(HomePage.this, DetailActivity.class);
//                            //intent.putExtra("model", model);
//                            intent.putExtra("latitude", model.getLit());
//                            intent.putExtra("longitude", model.getLon());
//
//                            startActivity(intent);
//                        }
//                    }
//            );
        }
    }


}


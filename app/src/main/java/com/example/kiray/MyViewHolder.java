package com.example.kiray;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder  {

    public TextView title,location,rooms,price,phone;


    public MyViewHolder(View itemView) {
        super(itemView);

        title=(TextView) itemView.findViewById(R.id.HIFapartmentName);
        location= (TextView) itemView.findViewById(R.id.HIFlocation);
        rooms=(TextView) itemView.findViewById(R.id.HIFrooms);
        price=(TextView) itemView.findViewById(R.id.HIFprice);
        phone=(TextView) itemView.findViewById(R.id.HIFphone);
    }
}
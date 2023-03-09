package com.example.adminapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder{
    TextView nameView,emailView,scoreView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        scoreView = itemView.findViewById(R.id.scoreView);
        nameView = itemView.findViewById(R.id.name);
        emailView = itemView.findViewById(R.id.email);
    }
}

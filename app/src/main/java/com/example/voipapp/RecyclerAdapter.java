package com.example.voipapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{
    private String[] name;
    private String[] update;

    RecyclerAdapter(String[] name, String[] update) { this.name = name;this.update = update; }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.custom_user_layout, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapter.ViewHolder holder, final int position)
    {
        holder.usertag.setText(name[position]);
        holder.userup.setText(update[position]);
        holder.chatlL.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "click on item: " + name[position], Toast.LENGTH_LONG).show();
            }
        });

        holder.call_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Call Placed", Toast.LENGTH_SHORT).show();
                Intent it= new Intent(v.getContext(),CallScreen.class);
                v.getContext().startActivity(it);
            }
        });

        holder.video_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Video Call Placed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView userimg;
        androidx.appcompat.widget.AppCompatImageButton call_img,video_img;
        TextView usertag, userup;
        LinearLayout chatlL;

        ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.chatlL = itemView.findViewById(R.id.chatlL);
            this.userimg = itemView.findViewById(R.id.userimg);
            this.usertag = itemView.findViewById(R.id.usertag);
            this.userup = itemView.findViewById(R.id.userup);
            this.call_img = itemView.findViewById(R.id.call_img);
            this.video_img = itemView.findViewById(R.id.video_img);
        }
    }
}

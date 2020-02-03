package com.example.voipapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CallFragment extends Fragment
{
    private String[] name = {"Sam Richards", "Shirley Wood", "Dustin Guzman", "Beth Barnes", "Claude Vargas", "Lauren Scott", "Donald Oliver", "Donald Oliver", "Harold Thompson", "Jimmy Butler"};
    private String[] update = {"Sam Richards", "Shirley Wood", "Dustin Guzman", "Beth Barnes", "Claude Vargas", "Lauren Scott", "Donald Oliver", "Donald Oliver", "Harold Thompson", "Jimmy Butler"};
    private Context context;

    CallFragment(Context context) { this.context = context; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_call, container, false);
        RecyclerView review = v.findViewById(R.id.review);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        review.setLayoutManager(layoutManager);
        RecyclerView.Adapter madpter = new RecyclerAdapter(name, update);
        review.setAdapter(madpter);
        return v;
    }

}

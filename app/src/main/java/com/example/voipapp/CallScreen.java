package com.example.voipapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import  androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.app.AppCompatActivity;


public class CallScreen extends AppCompatActivity
{
   AppCompatImageButton record,hold,addcall,videoswitch,mute,speaker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_screen);
        record=findViewById(R.id.record);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CallScreen.this, "Recording", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void hold(View view) {
        Toast.makeText(this, "hold", Toast.LENGTH_SHORT).show();
    }

    public void addcall(View view) {
        Toast.makeText(this, "hold", Toast.LENGTH_SHORT).show();
    }

    public void videoswitch(View view) {
        Toast.makeText(this, "hold", Toast.LENGTH_SHORT).show();
    }

    public void mute(View view) {
        Toast.makeText(this, "hold", Toast.LENGTH_SHORT).show();
    }

    public void speaker(View view) {
        Toast.makeText(this, "hold", Toast.LENGTH_SHORT).show();
    }
}

package com.example.voipapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    ImageView wel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wel = findViewById(R.id.wel);

        wel.animate().scaleX(1.2f).scaleY(1.2f).alpha(1).setDuration(2000);
        Handler h = new Handler();
        h.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent it = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(it);
                finish();
            }
        }, 4000);
    }
}

package com.example.voipapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class FrgPssActivity extends AppCompatActivity
{
    com.google.android.material.textfield.TextInputEditText sec_ans, n_pass;
    com.google.android.material.textfield.TextInputLayout tpass,text1;
    String ques, ans;
    SQLiteDatabase sq;
    TextView quer,sec_que;
    DbManager db;
    String id;
    Button Confirm,Submit;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frg_pss);

        sec_ans = findViewById(R.id.sec_ans);
        sec_que = findViewById(R.id.sec_que);
        n_pass = findViewById(R.id.n_pass);
        quer = findViewById(R.id.quer);
        tpass = findViewById(R.id.tpass);
        text1 = findViewById(R.id.text1);
        Submit = findViewById(R.id.Submit);
        Confirm = findViewById(R.id.Confirm);

        Intent it = getIntent();
        id = it.getStringExtra("id");
        db = new DbManager(this);
        sq = db.getWritableDatabase();

        String query = "select secq from reg where email='" + id + "'";
        Cursor c = sq.rawQuery(query, null);

        if (c.moveToNext())
        {
            ques = c.getString(0);
            sec_que.setText(ques+"?");
        }
        c.close();
    }

    @SuppressLint("SetTextI18n")
    public void confirm(View view)
    {
        ans = Objects.requireNonNull(sec_ans.getText()).toString().trim();
        if (ans.isEmpty())
        {
            sec_ans.setError("Empty");
            sec_ans.requestFocus();
        }
        else
        {
            db = new DbManager(this);
            sq = db.getWritableDatabase();
            String query1 = "select seca from reg where secq='" + ques + "'";
            Cursor c1 = sq.rawQuery(query1, null);

            if (c1.moveToNext())
            {
                String d = c1.getString(0);

                if (ans.equals(d))
                {

                    sec_ans.animate().alpha(0f).setDuration(1000);
                    sec_que.animate().alpha(0f).setDuration(1000);
                    sec_ans.setEnabled(false);
                    text1.animate().alpha(0f).setDuration(1000);
                    tpass.animate().alpha(1f).translationY(-200).setDuration(1000);
                    n_pass.setEnabled(true);
                    Submit.animate().alpha(1f).setDuration(1000);
                    Submit.setEnabled(true);
                    Confirm.animate().alpha(0f).setDuration(1000);
                    Confirm.setEnabled(false);
                    Toast.makeText(this, "Enter New Password", Toast.LENGTH_SHORT).show();

                } else {
                    n_pass.setText("");
                    quer.setText("Wrong Answer!");
                }
            }
            c1.close();
        }
    }

    public void submit(View v)
    {
        String nps= Objects.requireNonNull(n_pass.getText()).toString().trim();

        String update="update reg set password='"+nps+"' where email='"+id+"'";
        sq.execSQL(update);
        Toast.makeText(this, "Password Reset Successfully", Toast.LENGTH_SHORT).show();

        Intent it= new Intent(this,LoginActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(this, LoginActivity.class);
        startActivity(it);
        finish();
        super.onBackPressed();
    }
}
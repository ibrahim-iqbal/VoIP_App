package com.example.voipapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText emailid, password;
    TextView frgpass;
    String id, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailid = findViewById(R.id.emailid);
        password = findViewById(R.id.password);
        frgpass = findViewById(R.id.frgpass);

        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        int is = sp.getInt("id", 0);
        String email = sp.getString("email", null);
        if (is == 1) {
            Intent it = new Intent(LoginActivity.this, LandingPage.class);
            it.putExtra("id", email);
            startActivity(it);
            finish();
        }
    }


    //VALIDATE THE USER LOGIN
    public void login(View v) {
        id = emailid.getText().toString().trim();
        pass = password.getText().toString().trim();

        if (id.isEmpty()) {
            emailid.setError("Empty");
            emailid.requestFocus();
        } else {
            if (pass.isEmpty()) {
                password.setError("Empty");
                password.requestFocus();
            } else {

                DbManager db = new DbManager(this);
                SQLiteDatabase con = db.getWritableDatabase();
                try {
                    String query = "select password from reg where email='" + id + "'";
                    Cursor c = con.rawQuery(query, null);
                    if (c.moveToNext()) {
                        String dbpass = c.getString(0);
                        if (pass.equals(dbpass)) {
                            Intent it = new Intent(LoginActivity.this, LandingPage.class);
                            it.putExtra("id", id);
                            startActivity(it);
                            finish();
                            SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor ed = sp.edit().putInt("id", 1).putString("email", id);
                            ed.apply();
                        } else {
                            Toast.makeText(this, "Wrong Details!", Toast.LENGTH_SHORT).show();
                            emailid.setText("");
                            password.setText("");

                            frgpass.animate().alpha(1f).setDuration(500);
                            frgpass.setEnabled(true);

                            frgpass.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent it = new Intent(LoginActivity.this, FrgPssActivity.class);
                                    it.putExtra("id", id);
                                    startActivity(it);
                                    finish();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(this, "User Not Found", Toast.LENGTH_LONG).show();
                    }
                    c.close();
                } catch (Exception e) {
                    Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void register(View view) {
        Intent it = new Intent(this, RegistrationActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(it);
        finish();
    }
}
package com.example.voipapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChgPssActivity extends AppCompatActivity {
    com.google.android.material.textfield.TextInputEditText new_pass, con_pass, old_pass;
    String opass, npass, cpass, email;
    TextView query;
    DbManager db;
    Cursor c;
    SQLiteDatabase sq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chg_pss);

        old_pass = findViewById(R.id.old_pass);
        new_pass = findViewById(R.id.new_pass);
        con_pass = findViewById(R.id.con_pass);

        query = findViewById(R.id.query);

        db = new DbManager(this);
        sq = db.getWritableDatabase();
    }

    public void confirm(View view) {
        opass = old_pass.getText().toString().trim();
        npass = new_pass.getText().toString().trim();
        cpass = con_pass.getText().toString().trim();

        if (opass.isEmpty()) {
            old_pass.setError("Empty");
            old_pass.requestFocus();
        } else if (npass.isEmpty()) {
            new_pass.setError("Empty");
            new_pass.requestFocus();
        } else if (cpass.isEmpty()) {
            con_pass.setError("Empty");
            con_pass.requestFocus();
        } else
            {

            Intent it = getIntent();
            email = it.getStringExtra("id");
            String query1 = "select password from reg where email='" +email+ "'";
            c = sq.rawQuery(query1, null);

            if (c.moveToNext())
            {
                String ps = c.getString(0);
                if (opass.equals(ps))
                {
                    if (cpass.equals(npass))
                    {
                        query.setText("");
                        String update="update reg set password='"+cpass+"' where email='"+email+"'";
                        sq.execSQL(update);

                        Intent i= new Intent(ChgPssActivity.this,LoginActivity.class);
                        startActivity(i);
                        finish();
                        Toast.makeText(this, "Password Updated", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        con_pass.setError("Passwords Do not Match");
                    }
                }
                else
                {
                    query.setText("Old Password Does Not Match");
                }
            }
        }
    }
}

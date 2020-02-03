package com.example.voipapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatatypeMismatchException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {
    int[] ids = {R.id.name, R.id.email, R.id.pass};
    EditText[] ets = new EditText[3];
    String[] values = new String[3];
    ArrayAdapter<String> adapter;
    String[] data = {
            "Select Your Security Question",
            "What is your father's  name",
            "What is your mother's  name",
            "What Is your favorite book",
            "What is your favorite food",
            "What city were you born in"};

    LinearLayout gsign;
    GoogleSignInClient mGoogleSignInClient;

    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        for (i = 0; i < ets.length; i++) {
            ets[i] = findViewById(ids[i]);
        }
        gsign = findViewById(R.id.gsign);

        gsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(RegistrationActivity.this);
                if (acct == null) {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, 1);
                } else {
                    final ProgressDialog pd = new ProgressDialog(RegistrationActivity.this);
                    pd.setMessage("Please Wait");
                    pd.show();
                    mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(RegistrationActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    });
                }
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

        try
        {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String gmail = acct.getEmail();
                String gid = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();


                Toast.makeText(this, personName, Toast.LENGTH_SHORT).show();
            }
        } catch(ApiException e){
            e.printStackTrace();
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Intent it = new Intent(RegistrationActivity.this, MainActivity.class);
            startActivity(it);

            if (Objects.requireNonNull(e.getMessage()).equals("7:")) {
                Toast.makeText(this, "Please Connect to network", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //REGISTER A NEW USER
    public void reg(View v) {
        for (i = 0; i < ets.length; i++) {
            if (ets[i].getText().toString().trim().isEmpty()) {
                ets[i].setError("Empty");
                ets[i].requestFocus();
                break;
            } else {
                values[i] = ets[i].getText().toString().trim();
            }
        }
        if (i == ets.length) {
            DbManager db1 = new DbManager(this);
            SQLiteDatabase con1 = db1.getWritableDatabase();

            String query1 = "SELECT  email FROM reg  WHERE  email='" + ets[1].getText().toString().trim() + "'";
            Cursor c = con1.rawQuery(query1, null);

            if (c.moveToNext()) {
                Toast.makeText(this, "Email Already Exists", Toast.LENGTH_SHORT).show();
                ets[1].setError("Email Already Exists");
                ets[1].requestFocus();
            } else {
                DbManager db = new DbManager(this);
                SQLiteDatabase con = db.getWritableDatabase();

                String query = "insert into reg values(NULL,'" + values[0] + "',null,'" + values[1] + "',null,null,null,null,'" + values[2] + "' )";
                try {
                    con.execSQL(query);
                } catch (SQLiteDatatypeMismatchException e) {
                    Toast.makeText(this, "error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    ets[0].setText(e.getMessage());
                }
                Toast.makeText(this, "register" + values[1] + values[2], Toast.LENGTH_LONG).show();
                Intent it = new Intent(this, LoginActivity.class);
                startActivity(it);
                finish();
            }
            c.close();
        }
    }

    //TO CLEAR OUT THE FIELDS
    public void cancel(View view) {
        for (i = 0; i < ets.length; i++) {
            ets[i].setText("");
        }
        ets[0].requestFocus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(it);
        finish();
    }
}

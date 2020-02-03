package com.example.voipapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;

import com.tooltip.Tooltip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

public class DashActivity extends AppCompatActivity {
    int[] ids = {R.id.etuser, R.id.etuserid, R.id.etemail, R.id.etmob, R.id.etdate};
    de.hdodenhof.circleimageview.CircleImageView profileimg;
    EditText[] ets = new EditText[5];
    String[] values = new String[5];
    private int mYear, mMonth, mDay;
    ImageView camera, gallery;
    AlertDialog alertDialog;
    Button save, update;
    String encodedImage;
    Bitmap bitmap;
    String email;
    int i;
    TextView cgpass;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        save = findViewById(R.id.save);
        update = findViewById(R.id.update);
        cgpass = findViewById(R.id.cgpass);
        profileimg = findViewById(R.id.profileimg);

        for (i = 0; i < ets.length; i++) {
            ets[i] = findViewById(ids[i]);
            ets[i].setEnabled(false);
        }
        Intent it = getIntent();
        email = it.getStringExtra("id");

        getdata();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            profileimg.setImageBitmap(bitmap);
            Toast.makeText(this, "" + bitmap, Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        } else if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            profileimg.setImageBitmap(bitmap);
            alertDialog.dismiss();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //UPLOADING THE UPDATED DATA
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void update(View view) {
        for (i = 0; i < ets.length; i++) {
            ets[i].setEnabled(true);
        }
        ets[2].setEnabled(false);
        save.animate().alpha(1).setDuration(1000);
        cgpass.animate().alpha(1).setDuration(1000);
        update.animate().alpha(0).setDuration(500);
        update.setEnabled(false);
        save.setEnabled(true);
        cgpass.setEnabled(true);
        profileimg.setEnabled(true);

        profileimg.setTooltipText("Click to Update Photo");

        cgpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it= new Intent(DashActivity.this, ChgPssActivity.class);
                it.putExtra("id",email);
                startActivity(it);
            }
        });

        ets[4].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(DashActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        ets[4].setText(String.format("%d-%d-%d", dayOfMonth, monthOfYear + 1, year));
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        profileimg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(DashActivity.this, "Image", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder ld = new AlertDialog.Builder(DashActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                @SuppressLint("InflateParams")
                View dialogView = inflater.inflate(R.layout.custom_img, null);
                ld.setView(dialogView);
                alertDialog = ld.create();
                alertDialog.show();
                camera = alertDialog.findViewById(R.id.camera);
                gallery = alertDialog.findViewById(R.id.gallery);

                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(DashActivity.this, "Camera", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(it, 0);
                    }
                });

                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(DashActivity.this, "gallery", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(it, 1);
                    }
                });

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(DashActivity.this, "Save Reached", Toast.LENGTH_SHORT).show();
                for (i = 0; i < ids.length; i++) {
                    values[i] = ets[i].getText().toString().trim();
                }

                String query = "update reg set name='" + values[0] + "',userid='" + values[1] + "',mobno='" + values[3] + "',dob='" + values[4] + "' where email='" + email + "'";

                DbManager db = new DbManager(DashActivity.this);
                SQLiteDatabase con = db.getWritableDatabase();
                con.execSQL(query);
                if (bitmap != null) {
                    upload();
                } else {
                    Toast.makeText(DashActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                }

                getdata();
                for (i = 0; i < ets.length; i++) {
                    ets[i].setEnabled(false);
                }
                save.animate().alpha(0).setDuration(500);
                cgpass.animate().alpha(0).setDuration(500);
                update.animate().alpha(1).setDuration(500);
                save.setEnabled(false);
                update.setEnabled(true);
                profileimg.setEnabled(false);
                cgpass.setEnabled(false);
                Toast.makeText(DashActivity.this, "Profile Updated", Toast.LENGTH_LONG).show();
            }
        });
    }
    //UPLOADING THE UPDATED IMAGE
    public void upload() {
        ImgManager db = new ImgManager(this);
        SQLiteDatabase con = db.getWritableDatabase();
        String queryImg = "select img from img where email='" + email + "'";

        Cursor c1 = con.rawQuery(queryImg, null);
        if (c1.moveToNext()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] b = baos.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            String img = "update img set img='" + encodedImage + "' where email='" + email + "'";
            con.execSQL(img);
        } else {
            if (bitmap != null) {
                ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos1);
                byte[] b1 = baos1.toByteArray();
                encodedImage = Base64.encodeToString(b1, Base64.DEFAULT);

                String img1 = "insert into img values(null,'" + email + "','" + encodedImage + "')";
                Toast.makeText(this, "" + img1, Toast.LENGTH_LONG).show();
                con.execSQL(img1);
                profileimg.setImageBitmap(bitmap);
            } else {
                Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
            }
        }
        c1.close();
    }
    //TO DISPLAY THE USER DATA
    public void getdata() {
        //for setting the image
        ImgManager im = new ImgManager(this);
        SQLiteDatabase sq = im.getWritableDatabase();
        String queryImg = "select img from img where email='" + email + "'";
        Cursor c1 = sq.rawQuery(queryImg, null);
        if (c1.moveToNext()) {
            byte[] imageBytes = Base64.decode(c1.getString(0), Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            profileimg.setImageBitmap(decodedImage);
        }
        c1.close();

        //for setting the text
        DbManager db = new DbManager(this);
        SQLiteDatabase con = db.getWritableDatabase();
        String query = "select *from reg  where email='" + email + "'";
        Cursor c = con.rawQuery(query, null);
        if (c.moveToNext()) {
            for (i = 0; i < ets.length; i++) {
                ets[i].setText(c.getString(i + 1));
            }
        }
        c.close();
    }

    public void logout(View view) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("You Want to Exit?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent it = new Intent(DashActivity.this, MainActivity.class);
                        startActivity(it);
                        finish();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

}

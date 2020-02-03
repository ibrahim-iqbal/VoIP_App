package com.example.voipapp;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    int[] ids = {R.id.etuser, R.id.etuserid, R.id.etemail, R.id.etmob, R.id.etdate};
    de.hdodenhof.circleimageview.CircleImageView profileimg;
    EditText[] ets = new EditText[5];
    String[] values = new String[5];
    ImageView camera, gallery, logout;
    AlertDialog alertDialog;
    Button save, update;
    String encodedImage;
    Bitmap bitmap;
    String email;
    int i;
    TextView cgpass;
    Context context;
    private int mYear, mMonth, mDay;

    ProfileFragment(Context context, String id) {
        this.context = context;
        email = id;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        update = v.findViewById(R.id.update);
        save = v.findViewById(R.id.save);
        logout = v.findViewById(R.id.logout);

        cgpass = v.findViewById(R.id.cgpass);
        profileimg = v.findViewById(R.id.profileimg);

        SharedPreferences sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        email = sp.getString("email", null);

        for (i = 0; i < ets.length; i++) {
            ets[i] = v.findViewById(ids[i]);
            ets[i].setEnabled(false);
        }

        getdata();
        update.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                update();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            profileimg.setImageBitmap(bitmap);
            Toast.makeText(getContext(), "" + bitmap, Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        } else if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(),uri);
            } catch (IOException e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            profileimg.setImageBitmap(bitmap);
            alertDialog.dismiss();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void update() {
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
                Intent it = new Intent(getContext(), ChgPssActivity.class);
                it.putExtra("id", email);
                startActivity(it);
            }
        });

        ets[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        ets[4].setText(String.format("%d-%d-%d", dayOfMonth, monthOfYear + 1, year));
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Image", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder ld = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
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
                        Toast.makeText(getContext(), "Camera", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(it, 0);
                    }
                });

                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "gallery", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(it, 1);
                    }
                });

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Save Reached", Toast.LENGTH_SHORT).show();
                for (i = 0; i < ids.length; i++) {
                    values[i] = ets[i].getText().toString().trim();
                }

                String query = "update reg set name='" + values[0] + "',userid='" + values[1] + "',mobno='" + values[3] + "',dob='" + values[4] + "' where email='" + email + "'";

                DbManager db = new DbManager(getContext());
                SQLiteDatabase con = db.getWritableDatabase();
                con.execSQL(query);
                if (bitmap != null) {
                    upload();
                } else {
                    Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_LONG).show();
            }
        });
    }

    //UPLOADING THE UPDATED IMAGE
    private void upload() {
        ImgManager db = new ImgManager(getContext());
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
                Toast.makeText(getContext(), "" + img1, Toast.LENGTH_LONG).show();
                con.execSQL(img1);
                profileimg.setImageBitmap(bitmap);
            } else {
                Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
            }
        }
        c1.close();
    }

    //TO DISPLAY THE USER DATA
    private void getdata() {
        //for setting the image
        ImgManager im = new ImgManager(getContext());
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
        DbManager db = new DbManager(getContext());
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

    private void logout() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage("You Want to Exit?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        SharedPreferences sa = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                        sa.edit().remove("id").apply();

                        Intent it = new Intent(getContext(), MainActivity.class);
                        startActivity(it);
                    }
                });

        builder1.setNegativeButton
                ("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
}

package com.example.eps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProfileActivity extends AppCompatActivity {
    TextView txtBack,txtFullname,txtContact,txtAddress,txtUsername,txtPassword,headfullname,userType,txt_update;

    DatabaseHelper dh;
    SQLiteDatabase db;

    private int GALLERY = 1, CAMERA = 2;
    Bitmap myBitmap;
    ImageView img;
    String imgString ="";
    String _uid = "";
    LinearLayout.LayoutParams param,param1,layout_parameters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        dh = new DatabaseHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        layout_parameters = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);

        txtFullname = findViewById(R.id.txtFullname);
        txtContact = findViewById(R.id.txtContact);
        txtAddress = findViewById(R.id.txtAddress);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        headfullname = findViewById(R.id.headfullname);
        userType = findViewById(R.id.userType);
        txtBack = findViewById(R.id.txtBack);
        txt_update = findViewById(R.id.txt_update);
        txt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
        img = findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnableRuntimePermissionToAccessCamera();
                showPictureDialog();
            }
        });
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
            }
        });
        check9();
    }
    public void check9(){
        try {
            db = dh.getWritableDatabase();

            Cursor result = db.rawQuery("SELECT username,password,fullname,contact,address,user_type,filename, us_id FROM "+ dh.USERS +" WHERE status = 1", null);

            if (result.getCount() <= 0) {

            } else {
                if(result.moveToFirst()){
                    txtFullname.setText(result.getString(2));
                    txtContact.setText(result.getString(3));
                    txtAddress.setText(result.getString(4));
                    txtUsername.setText(result.getString(0));
                    txtPassword.setText(result.getString(1));

                    _uid = result.getString(7);

                    headfullname.setText(result.getString(2));

                    if (result.getString(5).equals("1")){
                        userType.setText("Administrator");
                    }else{
                        userType.setText("User");
                    }
                    if (result.getString(6).isEmpty())
                    {
                        img.setClipToOutline(true);
                        img.setImageResource(R.drawable.no_image);

                    }else {
                        img.setClipToOutline(true);
                        Picasso.get()
                                .load(result.getString(6))
                                .into(img, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }
                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });
                    }

//
                }

            }
        }catch (SQLException e){
            message("Error",e.toString());
        } catch(Exception e){
            message("Error",e.toString());
        }

    }
    public void dataIntent(){
        try{
            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                txtFullname.setText(extras.getString("fullname"));
                txtContact.setText(extras.getString("contact"));
                txtAddress.setText(extras.getString("address"));
                txtUsername.setText(extras.getString("username"));
                txtPassword.setText(extras.getString("password"));

                headfullname.setText(extras.getString("fullname"));

                if (extras.getString("user_type").equals("1")){
                    userType.setText("Administrator");
                }else{
                    userType.setText("User");
                }




            }
        }catch (Exception e){
            message("Error",e.toString());
        }

    }
    public void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {"Select photo from gallery", "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), GALLERY);


                                break;
                            case 1:

                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA);

                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    img.setImageBitmap(myBitmap);
                    confirm();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {

            myBitmap = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(myBitmap);
        }
    }

    public void EnableRuntimePermissionToAccessCamera(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA))
        {
            Toast.makeText(this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, CAMERA);
        }
    }

    public void addProfile(){
        try {

            String username = txtUsername.getText().toString();


            if(myBitmap == null){
                imgString="null";
            }else {
                Bitmap newBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), myBitmap.getConfig());
                Canvas canvas = new Canvas(newBitmap);
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(myBitmap, 0, 0, null);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 15, outputStream);
                byte[] byteFormat = outputStream.toByteArray();
                imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
            }
            AndroidNetworking
                    .post("http://localhost/mvc/Login/profile")
                    .addBodyParameter("username",""+ _uid)
                    .addBodyParameter("img",""+ imgString)
                    .setPriority(Priority.HIGH)
                    .setTag("Add Data")
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONObject jObj1 = response.getJSONObject(0);
                                String res = jObj1.getString("result");
                                if (res.equals("error")){
                                    message("Warning","Username is already Exist!");
                                }else if (res.equals("ok")) {
                                    String fn = jObj1.getString("filename");
                                    try {
                                        db = dh.getWritableDatabase();
                                        db.execSQL(" UPDATE " + dh.USERS + " SET filename = ? WHERE username = ?",new String[]{fn, username});

                                    }catch (SQLException e){
                                        message("Error",e.toString());
                                    }catch (Exception e){
                                        message("Error",e.toString());
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                message("Error",e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            message("Error",anError.toString());
                        }
                    });


        }catch (SQLException ex){
            message("Error",ex.toString());
        }catch (Exception ex){
            message("Error",ex.toString());
        }
    }
    public void updateProfile(){
        try {
            layout_parameters.setMargins(0,0,0,6);
            final LinearLayout customLayout = new LinearLayout (this);
            customLayout.setLayoutParams(layout_parameters);
            customLayout.setOrientation(LinearLayout.VERTICAL);

            final EditText t_fullname = new EditText(this);
            final EditText t_contact = new EditText(this);
            final EditText t_address = new EditText(this);
            final EditText t_username = new EditText(this);
            final EditText t_password = new EditText(this);
            final EditText t_con_password = new EditText(this);

            t_fullname.setHint("Enter New Fullname");
            t_fullname.setLayoutParams(layout_parameters);
            t_contact.setHint("Enter New Contact");
            t_contact.setHorizontallyScrolling(true);
            t_contact.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            t_contact.setLayoutParams(layout_parameters);
            t_address.setHint("Enter New Address");
            t_address.setLayoutParams(layout_parameters);
            t_username.setHint("Enter New Username");
            t_username.setLayoutParams(layout_parameters);
            t_password.setHint("Enter New Password");
            t_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            t_password.setLayoutParams(layout_parameters);
            t_con_password.setHint("Enter Conform Pass");
            t_con_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            t_con_password.setLayoutParams(layout_parameters);

            db = dh.getWritableDatabase();
            String _sid = "";
            Cursor result = db.rawQuery("SELECT us_id,fullname,contact,address,username,password FROM "+ dh.USERS +" WHERE status = 1", null);

            if (result.getCount() <= 0) {

            } else {
                if(result.moveToFirst()){
                    _sid = result.getString(0);
                    t_fullname.setText(result.getString(1));
                    t_contact.setText(result.getString(2));
                    t_address.setText(result.getString(3));
                    t_username.setText(result.getString(4));
                    t_password.setText(result.getString(5));
                    t_con_password.setText(result.getString(5));
                }

            }

            customLayout.addView(t_fullname);
            customLayout.addView(t_contact);
            customLayout.addView(t_address);
            customLayout.addView(t_username);
            customLayout.addView(t_password);
            customLayout.addView(t_con_password);
            final SweetAlertDialog sw = new SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE);
            sw.setTitleText("Update Profile");
            sw.setCustomView(customLayout);
            sw.setConfirmButton("Submit", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    if (t_fullname.getText().toString().isEmpty()){
                        message("Warning","Please provide fullname.");
                    }else if (t_contact.getText().toString().isEmpty()){
                        message("Warning","Please provide contact.");
                    }else if (t_address.getText().toString().isEmpty()){
                        message("Warning","Please provide address.");
                    }else if (t_username.getText().toString().isEmpty()){
                        message("Warning","Please provide username.");
                    }else if (t_password.getText().toString().isEmpty()){
                        message("Warning","Please provide password.");
                    }else if (t_con_password.getText().toString().isEmpty()){
                        message("Warning","Please provide confirm password.");
                    }else{
                        if (t_con_password.getText().toString().equals(t_password.getText().toString())){
                            AndroidNetworking
                                    .post("http://localhost/mvc/Login/updateInfo")
                                    .addBodyParameter("fullname",""+ t_fullname.getText().toString())
                                    .addBodyParameter("contact",""+t_contact.getText().toString())
                                    .addBodyParameter("address",""+t_address.getText().toString())
                                    .addBodyParameter("username",""+t_username.getText().toString())
                                    .addBodyParameter("password",""+t_password.getText().toString())
                                    .addBodyParameter("id",""+_uid)
                                    .setPriority(Priority.MEDIUM)
                                    .setTag("Update Data")
                                    .build()
                                    .getAsJSONArray(new JSONArrayRequestListener() {
                                        @Override
                                        public void onResponse(JSONArray jArr1) {

                                            try {
                                                JSONObject jObj1 = jArr1.getJSONObject(0);
                                                String res = jObj1.getString("result");
                                                if (res.equals("error")){
                                                    message("Warning","Data is not updated!\nPlease please try again.");
                                                }
                                                else if (res.equals("ok")) {
                                                    try {
                                                        db = dh.getWritableDatabase();
                                                        db.execSQL(" UPDATE " + dh.USERS + " SET fullname = ?,contact=?,address=?,username=?,password=? WHERE status = 1 ",
                                                                new String[]{t_fullname.getText().toString(), t_contact.getText().toString(),t_address.getText().toString(),
                                                                        t_username.getText().toString(),t_password.getText().toString()});

                                                    }catch (SQLException e){
                                                        message("Error",e.toString());
                                                    }catch (Exception e){
                                                        message("Error",e.toString());
                                                    }
                                                    sw.dismiss();
                                                    confirmUpdate();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onError(ANError anError) {

                                        }
                                    });
                        }else{
                            message("Warning","Password not match.");
                        }
                    }
                }
            });
            sw.setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sw.dismissWithAnimation();
                }
            });
            sw.setCancelable(false);
            sw.show();
        }catch (Exception e){
            message("Error",e.toString());
        }

    }

    public void message(String title, String message) {
        SweetAlertDialog sw = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        sw.setTitleText(title);
        sw.setContentText(message);
        sw.show();
    }
    public void confirm() {
        SweetAlertDialog sw = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        sw.setContentText("Success Upload Profile");

        sw.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                addProfile();
                sw.dismissWithAnimation();
            }
        });
        sw.setCancelable(false);
        sw.show();
    }
    public void confirmUpdate() {
        SweetAlertDialog sw = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        sw.setContentText("Success Update infomation");
        sw.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sw.dismissWithAnimation();
                restartActivity();
            }
        });
        sw.setCancelable(false);
        sw.show();
    }

    public void restartActivity(){
        Intent mIntent = getIntent();
        finish();
        startActivity(mIntent);
    }
}
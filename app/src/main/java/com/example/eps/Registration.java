package com.example.eps;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Registration extends AppCompatActivity {
    TextView btnBack;
    EditText txtFullname,txtContact,txtAddress,txtUsername,txtPassword,txtConfirm;
    DatabaseHelper dh;
    SQLiteDatabase db;
    int lengmax;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        try{

            dh = new DatabaseHelper(this);
            txtFullname = findViewById(R.id.txtFullname);
            txtContact = findViewById(R.id.txtContact);
            txtAddress = findViewById(R.id.txtAddress);
            txtUsername =findViewById(R.id.txtUsername);
            txtPassword = findViewById(R.id.txtPassword);
            txtConfirm = findViewById(R.id.txtConfirm);

            btnBack = findViewById(R.id.btnBack);
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }
            });
        }catch (Exception e){
            message("Error",e.toString());
        }

    }
    @Override
    public void onBackPressed() {
        finishAffinity();
        startActivity(new Intent(getApplicationContext(), Login.class));

    }
    public void registry(View view){
        try {
            String fullname = txtFullname.getText().toString();
            String contact = txtContact.getText().toString();
            String address = txtAddress.getText().toString();
            String confirmpass = txtConfirm.getText().toString();
            String username = txtUsername.getText().toString();
            String password = txtPassword.getText().toString();
            String status = "0";

            if (fullname.isEmpty()){
                message("Empty","Please provide fullname");
            }else if (contact.isEmpty()){
                message("Empty","Please provide contact");
            }else if (address.isEmpty()){
                message("Empty","Please provide address");
            }else if (username.isEmpty()){
                message("Empty","Please provide username");
            }else if (password.isEmpty()){
                message("Empty","Please provide password");
            }else if (confirmpass.isEmpty()){
                message("Empty","Please provide confirm password");
            }else {
                if(password.equals(confirmpass)){
                    AndroidNetworking
                            .post("http://localhost/mvc/Login/Registry")
                            .addBodyParameter("fullname",""+ fullname)
                            .addBodyParameter("contact",""+ contact)
                            .addBodyParameter("address",""+ address)
                            .addBodyParameter("username",""+ username)
                            .addBodyParameter("password",""+ password)
                            .addBodyParameter("user_type",""+ "2")
                            .setPriority(Priority.HIGH)
                            .setTag("Add Data")
                            .build()
                            .getAsJSONArray(new JSONArrayRequestListener() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        JSONObject jObj1 = response.getJSONObject(0);
                                        String res = jObj1.getString("result");
                                        if (res.equals("use")){
                                            message("Warning","Username is already Exist!");
                                        }else if (res.equals("ok")) {
                                            try {
                                                String _sid = jObj1.getString("id");
                                                db = dh.getWritableDatabase();

                                                db.execSQL("INSERT INTO " + dh.USERS + " (us_id, fullname , contact ,address,username,password,filename,user_type,status) VALUES(?,?,?,?,?,?,?,?,?)",
                                                        new String[]{_sid, fullname, contact, address,username,password,"","2","1"});
                                                Intent i = new Intent(Registration.this, MainActivity.class);
                                                i.putExtra("username",username);
                                                i.putExtra("password",password);
                                                i.putExtra("fullname",fullname);
                                                i.putExtra("contact",contact);
                                                i.putExtra("address",address);
                                                i.putExtra("user_type","2");
                                                i.putExtra("filename","");
                                                startActivity(i);

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

                                }
                            });

                }else {
                    message("Warning", "Password not match!");
                }


            }

        }catch (SQLException ex){
            message("Error",ex.toString());
        }catch (Exception ex){
            message("Error",ex.toString());
        }
    }
    public void message(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}
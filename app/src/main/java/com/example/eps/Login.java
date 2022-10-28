package com.example.eps;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.eps.adapter.RecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    TextView btnSignUp,btnSignin;
    EditText txtUsername,txtPassword;
    DatabaseHelper dh;
    SQLiteDatabase db;
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    AlarmManager alarmManager;
    PendingIntent notifyPendingIntent;
    Model mod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dh = new DatabaseHelper(this);
        mod = new Model();
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
         alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        //Set up the Notification Broadcast Intent
        Intent notifyIntent = new Intent(this, AlarmReceiver.class);

        //Check if the Alarm is already set, and check the toggle accordingly
        boolean alarmUp = (PendingIntent.getBroadcast(this, 0, notifyIntent,
                PendingIntent.FLAG_NO_CREATE) != null);



        //Set up the PendingIntent for the AlarmManager
         notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);



        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignin = findViewById(R.id.btnSignin);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Registration.class));
            }
        });
        check();
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();

    }

    public void login(){
        try {
            String username = txtUsername.getText().toString();
            String password = txtPassword.getText().toString();
            String status = "1";

            if (username.isEmpty()){
                message("Empty","Please provide username");
            }else if (password.isEmpty()){
                message("Empty","Please provide password");
            }else {

                AndroidNetworking
                        .post("http://localhost/mvc/Login/login")
                        .addBodyParameter("username",""+ username)
                        .addBodyParameter("password",""+ password)
                        .setPriority(Priority.HIGH)
                        .setTag("Add Data")
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray jArr1) {
                                try {
                                    JSONObject jObj1 = jArr1.getJSONObject(0);
                                    String res = jObj1.getString("result");
                                    if(res.equals("pass")){
                                        message("Warning"," Wrong Password!\nPlease check your password.");
                                    }else if (res.equals("use")){
                                        message("Warning","Username is Not found!\nPlease check the credentials.");
                                    }else if (res.equals("ok")) {
                                        String D = jObj1.getString("D");

                                        String fullname = jObj1.getString("fullname");
                                        String contact = jObj1.getString("contact");
                                        String address = jObj1.getString("address");
                                        String filename = jObj1.getString("filename");
                                        String user_type = jObj1.getString("user_type");
                                        try {
                                            db = dh.getWritableDatabase();

                                            Cursor result = db.rawQuery("SELECT * FROM "+ dh.USERS +" WHERE username = ?", new String[]{username});

                                            if (result.getCount() <= 0) {

                                                db.execSQL("INSERT INTO " + dh.USERS + " (us_id, fullname , contact ,address,username,password,filename,user_type,status) VALUES(?,?,?,?,?,?,?,?,?)",
                                                        new String[]{D,fullname, contact, address,username,password,filename,user_type,"1"});
                                                Intent i = new Intent(Login.this, MainActivity.class);
                                                i.putExtra("username",username);
                                                i.putExtra("password",password);
                                                i.putExtra("fullname",fullname);
                                                i.putExtra("contact",contact);
                                                i.putExtra("address",address);
                                                i.putExtra("user_type",user_type);
                                                i.putExtra("filename",filename);
                                                SharedPreferences sp = getSharedPreferences("key", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sp.edit();
                                                editor.putString("fullname",fullname);
                                                editor.apply();
                                                startActivity(i);
                                            } else {

                                                db.execSQL(" UPDATE " + dh.USERS + " SET status = ? WHERE username = ?",
                                                        new String[]{status, username});

                                                Intent i = new Intent(Login.this, MainActivity.class);
                                                i.putExtra("username",username);
                                                i.putExtra("password",password);
                                                i.putExtra("fullname",fullname);
                                                i.putExtra("contact",contact);
                                                i.putExtra("address",address);
                                                i.putExtra("user_type",user_type);
                                                i.putExtra("filename",filename);
                                                startActivity(i);
                                            }
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
                                message("o",anError.toString());
                            }
                        });

            }

        }catch (SQLException ex){
            message("Error",ex.toString());
        }catch (Exception ex){
            message("Error",ex.toString());
        }
    }

    public void check(){
        try {
            long triggerTime = SystemClock.elapsedRealtime() + 1000;
            long repeatInterval = 1000;
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    triggerTime, repeatInterval, notifyPendingIntent);

            db = dh.getWritableDatabase();
            Cursor result = db.rawQuery("SELECT username,password,fullname,contact,address,user_type,filename FROM "+ dh.USERS +" WHERE status = 1", null);

            if (result.getCount() <= 0) {

            } else {
                if(result.moveToFirst()){
                    Intent i = new Intent(Login.this, MainActivity.class);
                    i.putExtra("username",result.getString(0));
                    i.putExtra("password",result.getString(1));
                    i.putExtra("fullname",result.getString(2));
                    i.putExtra("contact",result.getString(3));
                    i.putExtra("address",result.getString(4));
                    i.putExtra("user_type",result.getString(5));
                    i.putExtra("filename",result.getString(6));
                    startActivity(i);
                }

            }

            Cursor c =  db.rawQuery("SELECT * FROM "+ dh.SCH +" ", null);
            if (c.getCount() <= 0) {
                AndroidNetworking
                        .post("http://localhost/mvc/Task/getAllTask")
                        .setTag("Get Data")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {

                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject jObj = response.getJSONObject(i);
                                        String id =jObj.getString("id");
                                        String name = jObj.getString("name");
                                        String date_sched =jObj.getString("date_sched");

                                        db.execSQL("INSERT INTO " + dh.SCH + " (sc_id, name , date ) VALUES(?,?,?)",new String[]{id, name, date_sched});

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onError(ANError anError) {

                            }
                        });

            }

        }catch (SQLException e){
            message("Error",e.toString());
        } catch(Exception e){
            message("Error",e.toString());
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
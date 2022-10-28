package com.example.eps;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.eps.adapter.RecycleViewEquip;
import com.example.eps.adapter.RecycleViewNewEq;
import com.example.eps.adapter.RecycleViewOld;
import com.example.eps.adapter.RecyclerViewAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{


    private AppBarConfiguration mAppBarConfiguration;

    RecyclerView recyclerView,recyclerview2;
    ArrayList<String> building_name;
    ArrayList<String> status;
    ArrayList<String> room_name;
    ArrayList<String> b_name;
    ArrayList<String> b_id;
    ArrayList<String> imageV;
    SwipeRefreshLayout srl_main;
    View ChildView ;
    TextView txtNew,nav_header_name;
    ImageView img,imageView;
    CardView c_id,c_pro;
    int RecyclerViewItemPosition ;
    String username = "";
    String _password = "";
    String _fullname="";
    String _contact = "";
    String _address = "";
    String _usertype="";
    DatabaseHelper dh;
    SQLiteDatabase db;
    LinearLayout nav_foot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dh = new DatabaseHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerview1);

        recyclerview2 = findViewById(R.id.recyclerview2);
        c_pro = findViewById(R.id.c_pro);
        txtNew = findViewById(R.id.txtNew);

        c_id = findViewById(R.id.c_id);
        img = findViewById(R.id.im_but);



        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle tongle = new ActionBarDrawerToggle(
                this, drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(tongle);
        tongle.syncState();
        View hView = navigationView.getHeaderView(0);
        nav_header_name = (TextView)hView.findViewById(R.id.nav_header_name);
        imageView = hView.findViewById(R.id.imageView);

        nav_foot = findViewById(R.id.footer_text);
        nav_foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = dh.getWritableDatabase();
                db.execSQL(" UPDATE " + dh.USERS + " SET status = ? WHERE status = ?",new String[]{"0", "1"});
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
        dataIntent();

        srl_main = findViewById(R.id.srl_main);
        srl_main.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srl_main.setRefreshing(false);
                        displayName();
                    }
                },1000);
            }
        });
        displayName();
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }
            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {
                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {
                    RecyclerViewItemPosition = Recyclerview.getChildAdapterPosition(ChildView);
                    String id = b_id.get(RecyclerViewItemPosition);
                    String bname = building_name.get(RecyclerViewItemPosition);


                    try {

                        Intent modify_intent = new Intent(getApplicationContext(), Rooms.class);
                        modify_intent.putExtra("row_id", id);
                        modify_intent.putExtra("row_name", bname);
                        modify_intent.putExtra("row_status", "1");
                        modify_intent.putExtra("row_page", "1");
                        startActivity(modify_intent);
                    }catch (Exception e){
                        message("Error",e.toString());
                    }
                }
                return false;
            }
            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {
            }
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
        check();


    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();

    }



    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy H:mm:ss");

    //get current date time with Date()
    Date date = new Date();

    // Now format the date
    String date1= dateFormat.format(date);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
////            case R.id.notify:
////                dialogAdd();
//                return true;
//
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if( id == R.id.nav_buiding)
        {
            startActivity(new Intent(this,Building.class));
        }
        else if( id == R.id.nav_category)
        {
            startActivity(new Intent(this,Category.class));
        }
        else if( id == R.id.nav_equipment)
        {
            try {
                startActivity(new Intent(this,Equipment.class));
            }catch (Exception e){
                message("error",e.toString());
            }

        }
        else if (id == R.id.nav_profile){

            Intent i = new Intent(MainActivity.this, ProfileActivity.class);

            startActivity(i);
        }
        else if( id == R.id.nav_sched)
        {
            startActivity(new Intent(this,Schedule.class));
        }

        DrawerLayout  drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
    private  void displayName(){

        AndroidNetworking
                .post("http://localhost/mvc/Equipment/getAllEquip")
                .setTag("Get Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        c_pro.setVisibility(View.GONE);
                        img.setVisibility(View.VISIBLE);
                        txtNew.setVisibility(View.VISIBLE);
                        c_id.setVisibility(View.VISIBLE);
                        try {
                            b_name = new ArrayList<String>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jObj = response.getJSONObject(i);
                                b_name.add(jObj.getString("cate_name"));
                            }

                            room_name = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jObj = response.getJSONObject(i);
                                room_name.add(jObj.getString("name"));

                            }
                            imageV = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jObj = response.getJSONObject(i);
                                imageV.add(jObj.getString("ImageUrl"));

                            }

                            RecycleViewNewEq rvn = new RecycleViewNewEq(room_name,b_name,imageV);
                            recyclerview2.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                            recyclerview2.setAdapter(rvn);
                            rvn.setContext(getApplicationContext());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {

                    }
                });
        AndroidNetworking
                .post("http://localhost/mvc/Room/displayBname")
                .setTag("Get Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            building_name = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jObj = response.getJSONObject(i);
                                building_name.add(jObj.getString("building_name"));
                            }
                            b_id = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jObj = response.getJSONObject(i);
                                b_id.add(jObj.getString("R"));
                            }


                            RecyclerViewAdapter rva = new RecyclerViewAdapter(building_name,b_id);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                            recyclerView.setAdapter(rva);
                            rva.setContext(getApplicationContext());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public void message(String title, String message) {
        SweetAlertDialog sw = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        sw.setTitleText(title);
        sw.setContentText(message);
        sw.show();
    }

    public void dataIntent(){
        try{
            Bundle extras = getIntent().getExtras();

            if (extras != null) {
               _fullname = extras.getString("fullname");
                username = extras.getString("username");
                _password = extras.getString("password");
                _contact = extras.getString("contact");
                _address = extras.getString("address");
                _usertype = extras.getString("user_type");


            }
        }catch (Exception e){
            message("Error",e.toString());
        }

    }
    public void check(){
        try {
            db = dh.getWritableDatabase();

            Cursor result = db.rawQuery("SELECT username,password,fullname,contact,address,user_type,filename FROM "+ dh.USERS +" WHERE status = 1", null);

            if (result.getCount() <= 0) {

            } else {
                if(result.moveToFirst()){
                    nav_header_name.setText(result.getString(2));

                    if (result.getString(6).isEmpty())
                    {
                        imageView.setClipToOutline(true);
                        imageView.setImageResource(R.drawable.no_image);

                    }else {
                        imageView.setClipToOutline(true);
                        Picasso.get()
                                .load(result.getString(6))
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }
                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });
                    }

                }

            }
        }catch (SQLException e){
            message("Error",e.toString());
        } catch(Exception e){
            message("Error",e.toString());
        }

    }
    public void st(){
        startActivity(new Intent(getApplicationContext(), Equipment.class));
    }

}
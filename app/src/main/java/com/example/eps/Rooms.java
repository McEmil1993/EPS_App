package com.example.eps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.eps.adapter.RecycleViewBuilding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Rooms extends AppCompatActivity {


    RecyclerView recyclerView_building;
    RecycleViewBuilding adapterR;
    ArrayList<String> name,_id,descript,st;
    ArrayList<String> count;
    String get_id="";
    String bname= "";
    String status= "";
    String page ="";
    String result1 = null;
    CardView c_pro;
    SweetAlertDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAdd();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_backspace_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
        recieveData();
        c_pro = findViewById(R.id.c_pro);

        recyclerView_building = findViewById(R.id.recyclerview_building);
        if (status.equals("0"))
        {
            final SweetAlertDialog sw = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
            sw.setTitleText("No Room!");
            sw.setContentText("Please add room in "+bname);
            sw.setConfirmButton("Next", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sw.dismissWithAnimation();
                    dialogAdd();
                }
            });
            sw.show();
        }
        displayName();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.room_menu, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.rname);
        item.setTitle(bname);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(page.equals("1")){
                    Intent modify_intent = new Intent(Rooms.this, MainActivity.class);
                    startActivity(modify_intent);
                    this.finish();
                }else {
                    Intent modify_intent = new Intent(Rooms.this, Building.class);
                    startActivity(modify_intent);
                    this.finish();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(page.equals("1")){
            Intent modify_intent = new Intent(Rooms.this, MainActivity.class);
            startActivity(modify_intent);
            this.finish();
        }else {
            Intent modify_intent = new Intent(Rooms.this, Building.class);
            startActivity(modify_intent);
            this.finish();
        }
    }
    public void displayName(){
        AndroidNetworking
                .post("http://localhost/mvc/Room/display")
                .addBodyParameter("building_id",""+get_id)
                .setTag("Get Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray jArr) {
                        c_pro.setVisibility(View.GONE);
                        try {
                            name = new ArrayList<>();
                            for (int i = 0; i < jArr.length(); i++) {
                                JSONObject jObj = jArr.getJSONObject(i);
                                name.add(jObj.getString("room_name"));
                            }

                            count = new ArrayList<>();
                            for (int i = 0; i < jArr.length(); i++) {
                                JSONObject jObj = jArr.getJSONObject(i);
                                count.add(jObj.getString("description"));
                            }

                            _id = new ArrayList<>();
                            for (int i = 0; i < jArr.length(); i++) {
                                JSONObject jObj = jArr.getJSONObject(i);
                                _id.add(jObj.getString("id"));
                            }
                            adapterR = new RecycleViewBuilding(name,count,"",_id,descript,Rooms.this,get_id);
                            recyclerView_building.setLayoutManager(new LinearLayoutManager(Rooms.this));
                            recyclerView_building.setAdapter(adapterR);

                            adapterR.setSt(status);
                            adapterR.setBname(bname);
                            adapterR.setgId(get_id);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void recieveData() {
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        try {
            if (b != null) {
                String row1 = b.getString("row_id");
                String row2 = b.getString("row_name");
                String row3 = b.getString("row_status");
                String res = b.getString("result1");
                page = b.getString("row_page");
                result1 = res;

                get_id = row1;
                bname = row2;
                status = row3;

            }
        }catch (Exception ex) {
            message("Error", ex.toString());
        }
    }

    public void dialogAdd(){
        LinearLayout.LayoutParams layout_parameters = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        final LinearLayout customLayout = new LinearLayout (this);
        customLayout.setLayoutParams(layout_parameters);
        customLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText name = new EditText(this);
        name.setHint("Enter Room name");
        name.setLayoutParams(layout_parameters);
        final EditText des = new EditText(this);
        des.setLayoutParams(layout_parameters);
        des.setHint("Enter Room description");
        customLayout.addView(name);
        customLayout.addView(des);

        final SweetAlertDialog sw = new SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE);
        sw.setTitleText("New Room in "+bname);
        sw.setCustomView(customLayout);
        sw.setConfirmButton("Submit", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if (name.getText().toString().equals("")) {
                    message("Warning!","Please enter room name!");

                } else if (des.getText().toString().equals("")) {
                    message("Warning!","Please enter room description!");
                } else {
//                    swet();

                    AndroidNetworking
                            .post("http://localhost/mvc/Room/save")
                            .addBodyParameter("room_name", "" + name.getText().toString())
                            .addBodyParameter("description", "" + des.getText().toString())
                            .addBodyParameter("building_id", "" + get_id)
                            .setPriority(Priority.HIGH)
                            .setTag("Add Data")
                            .build()
                            .getAsJSONArray(new JSONArrayRequestListener() {
                                @Override
                                public void onResponse(JSONArray jArr1) {
                                    try {
                                        JSONObject jObj1 = jArr1.getJSONObject(0);
                                        String res = jObj1.getString("result");

                                        if (res.equals("error")){
                                            message("Warning","Data is not saved!\nPlease check the credentials.");
                                        }else {
                                            sw.dismissWithAnimation();
                                            displayName();
                                            Su(res);
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
    }
    public void swet(){
        pd.dismissWithAnimation();

        pd = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pd.setTitleText("Loading ...");
        pd.setCancelable(false);
        pd.show();
    }
//    public void swet(final String mess){
//        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading ...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                pDialog.dismissWithAnimation();
//                displayName();
//                Su(mess);
//            }
//        },1200);
//    }
    public void message(String title, String message) {
        SweetAlertDialog sw = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        sw.setTitleText(title);
        sw.setContentText(message);
        sw.show();
    }
    public void Su(String res){
        final SweetAlertDialog sw = new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE);
        sw.setTitleText("Success!");
        sw.setContentText(res);
        sw.showCancelButton(false);
        sw.show();
    }
}
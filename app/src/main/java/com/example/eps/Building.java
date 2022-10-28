package com.example.eps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.eps.adapter.RecycleViewBuilding;
import com.example.eps.adapter.RecycleViewNewEq;
import com.example.eps.adapter.RecycleViewOld;
import com.example.eps.adapter.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Building extends AppCompatActivity {


    private int progressStatus = 0;
    private Handler handler = new Handler();
    RecyclerView recyclerView_building;
    ArrayList<String> name,_id;
    ArrayList<String> descript;
    ArrayList<String> status;
    FrameLayout rootlayout;
    ProgressDialog progressDialog;
    FloatingActionButton fab;
    int _xDelta;
    int _yDelta;
    String result="";
    Models m;
    CardView card_pro;
    public SweetAlertDialog pd;
    DatabaseHelper dh;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        dh = new DatabaseHelper(this);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            String res = b.getString("result");
            result = res;
        }
        if(result != ""){
            Su(result);
        }
        final Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_backspace_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
        card_pro = (CardView) findViewById(R.id.c_pro);

        try {
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogAdd("0","","");
                }
            });
        }catch (Exception e){
            message("ERror",e.toString());
        }
        check();
        displayName();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void displayName(){
        AndroidNetworking
                .post("http://localhost/mvc/Building/getAllBuilding")
                .setTag("Get Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray jArr) {
                          card_pro.setVisibility(View.GONE);
                        try {
                            name = new ArrayList<>();
                            for (int i = 0; i < jArr.length(); i++) {
                                JSONObject jObj = jArr.getJSONObject(i);
                                name.add(jObj.getString("building_name"));
                            }
                            descript = new ArrayList<>();
                            for (int i = 0; i < jArr.length(); i++) {
                                JSONObject jObj = jArr.getJSONObject(i);
                                descript.add(jObj.getString("description"));
                            }

                            _id = new ArrayList<>();
                            for (int i = 0; i < jArr.length(); i++) {
                                JSONObject jObj = jArr.getJSONObject(i);
                                _id.add(jObj.getString("id"));
                            }

                            status = new ArrayList<>();
                            for (int i = 0; i < jArr.length(); i++) {
                                JSONObject jObj = jArr.getJSONObject(i);
                                status.add(jObj.getString("count_room"));
                            }
                            RecycleViewBuilding r = new RecycleViewBuilding(name,status,"Building",_id,descript,Building.this,"");
                            recyclerView_building= findViewById(R.id.recyclerview_building);
                            recyclerView_building.setLayoutManager(new LinearLayoutManager(Building.this));
                            recyclerView_building.setAdapter(r);
                            r.setContext(Building.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public void dialogAdd(final String bid, String bname, String descrp){
        LinearLayout.LayoutParams layout_parameters = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        final LinearLayout customLayout = new LinearLayout (this);
        customLayout.setLayoutParams(layout_parameters);
        customLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText name = new EditText(this);
        name.setHint("Enter Building name");
        name.setLayoutParams(layout_parameters);
        final EditText des = new EditText(this);
        des.setLayoutParams(layout_parameters);
        des.setHint("Enter Description");
        customLayout.addView(name);
        customLayout.addView(des);
        if (bid != "0"){
            name.setText(bname);
            des.setText(descrp);
        }
        final SweetAlertDialog sw = new SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE);
        sw.setTitleText("New Building");
        sw.setCustomView(customLayout);
        sw.setConfirmButton("Submit", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                if (name.getText().toString().equals("")) {
                    message("Warning!","Please enter building name!");
                } else if (des.getText().toString().equals("")) {
                    message("Warning!","Please enter building description!");
                } else {
                    if (bid == "0"){
                        AndroidNetworking
                                .post("http://localhost/mvc/Building/saveBuilding")
                                .addBodyParameter("building_name",""+ name.getText().toString())
                                .addBodyParameter("description",""+des.getText().toString())
                                .setPriority(Priority.HIGH)
                                .setTag("Add Data")
                                .build()
                                .getAsJSONArray(new JSONArrayRequestListener() {
                                    @Override
                                    public void onResponse(JSONArray jArr1) {
                                        try {
                                            JSONObject jObj1 = jArr1.getJSONObject(0);
                                            String res = jObj1.getString("result");
                                            if(res.equals("1")){
                                                message("Warning",name.getText().toString()+" is already exist!");
                                            }else if (res.equals("error")){
                                                message("Warning","Data is not saved!\nPlease check the credentials.");
                                            }else {
                                                sw.dismiss();
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
                    }else {
                        AndroidNetworking
                                .post("http://localhost/mvc/Building/updateBuilding")
                                .addBodyParameter("building_name",""+ name.getText().toString())
                                .addBodyParameter("description",""+des.getText().toString())
                                .addBodyParameter("id",""+bid)
                                .setPriority(Priority.HIGH)
                                .setTag("Update Data")
                                .build()
                                .getAsJSONArray(new JSONArrayRequestListener() {
                                    @Override
                                    public void onResponse(JSONArray jArr1) {
                                        try {
                                            JSONObject jObj1 = jArr1.getJSONObject(0);
                                            String res = jObj1.getString("result");

                                            if (res.equals("error")){
                                                message("Warning","Data is not updated!\nPlease check the credentials.");
                                            }else {
                                                sw.dismiss();
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

    public void message(String title, String message) {
        SweetAlertDialog sw = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        sw.setTitleText(title);
        sw.setContentText(message);
        sw.show();
    }
    public void War(String title, String message) {
        SweetAlertDialog sw = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sw.setTitleText(title);
        sw.setContentText(message);
        sw.setCancelable(false);
        sw.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                startActivity(new Intent(Building.this,Login.class));
            }
        });
        sw.show();
    }
    public void Su(String res){
        displayName();
        final SweetAlertDialog sw = new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE);
        sw.setTitleText("Success!");
        sw.setContentText(res);
        sw.showCancelButton(false);
        sw.show();
    }
    public void check(){
        try {
            db = dh.getWritableDatabase();

            Cursor result = db.rawQuery("SELECT username,password,fullname,contact,address,user_type,filename FROM "+ dh.USERS +" WHERE status = 1", null);

            if (result.getCount() <= 0) {
                War("Warning","Please Login before check task.");
            }

        }catch (SQLException e){
            message("Error",e.toString());
        } catch(Exception e){
            message("Error",e.toString());
        }

    }
}
package com.example.eps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.eps.adapter.RecycleViewBuilding;
import com.example.eps.adapter.RecycleViewCategory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Category extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<String> listCategory,listTask,listId;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    CardView c_pro;
    String result = "";
    public SweetAlertDialog pd;
    DatabaseHelper dh;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        dh = new DatabaseHelper(this);
        recieveData();
        if(result != ""){
            Su(result);
        }
        c_pro = findViewById(R.id.c_pro);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_backspace_24);
        actionBar.setDisplayHomeAsUpEnabled(true);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAdd();
            }
        });
        displayName();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

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
                .post("http://localhost/mvc/Category/display")
                .setTag("Get Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray jArr) {
                        c_pro.setVisibility(View.GONE);
                        try {
                            listCategory = new ArrayList<>();
                            for (int i = 0; i < jArr.length(); i++) {
                                JSONObject jObj = jArr.getJSONObject(i);
                                listCategory.add(jObj.getString("cate_name"));
                            }
                            listTask = new ArrayList<>();
                            for (int i = 0; i < jArr.length(); i++) {
                                JSONObject jObj = jArr.getJSONObject(i);
                                listTask.add(jObj.getString("Task"));
                            }

                            listId = new ArrayList<>();
                            for (int i = 0; i < jArr.length(); i++) {
                                JSONObject jObj = jArr.getJSONObject(i);
                                listId.add(jObj.getString("id"));
                            }
                            recyclerView = findViewById(R.id.recyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(Category.this));
                            recyclerView.setAdapter(new RecycleViewCategory(listId,listCategory,listTask,Category.this));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
    public void dialogAdd(){
        LinearLayout.LayoutParams layout_parameters = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        final LinearLayout customLayout = new LinearLayout (this);
        customLayout.setLayoutParams(layout_parameters);
        customLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText name = new EditText(this);
        name.setHint("Enter Category name");
        name.setLayoutParams(layout_parameters);
        final Spinner des = new Spinner(this);
        des.setLayoutParams(layout_parameters);
        customLayout.addView(name);
        customLayout.addView(des);

        List<String> list = new ArrayList<String>();
        list.add("Select Task");
        list.add("Monthly Task");
        list.add("Quarterly Task");
        list.add("Annual Task");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        des.setAdapter(dataAdapter);

        final SweetAlertDialog sw = new SweetAlertDialog(Category.this,SweetAlertDialog.NORMAL_TYPE);
        sw.setTitleText("New Category");
        sw.setCustomView(customLayout);
        sw.setConfirmButton("Submit", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                swet();
                if (name.getText().toString().equals("")) {
                    message("Warning!","Please enter Category name!");

                } else if (des.getSelectedItem().toString().equals("Select Task")) {
                    message("Warning!","Please Select Task!");

                } else {
                    String tsk = "";
                    if (des.getSelectedItem().toString().equals("Monthly Task")){
                        tsk = "1";
                    }else  if (des.getSelectedItem().toString().equals("Quarterly Task")){
                        tsk = "2";
                    }else  if (des.getSelectedItem().toString().equals("Annual Task")){
                        tsk = "3";
                    }
                    AndroidNetworking
                            .post("http://localhost/mvc/Category/save")
                            .addBodyParameter("cate_name",""+name.getText().toString())
                            .addBodyParameter("Task",""+tsk)
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
                                        }
                                        else if (res.equals("ok")){
                                            sw.dismiss();
                                            displayName();
                                            Su("Insert category successfully!");

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
//    public void swet(){
//        pd = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pd.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pd.setTitleText("Loading ...");
//        pd.setCancelable(false);
//        pd.show();
//    }
    private void recieveData() {
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        try {
            if (b != null) {
                String res = b.getString("result");
                 result = res;
            }
        }catch (Exception ex) {
            message("Error", ex.toString());
        }
    }
    public void Su(String res){
        final SweetAlertDialog sw = new SweetAlertDialog(Category.this,SweetAlertDialog.SUCCESS_TYPE);
        sw.setTitleText("Success!");
        sw.setContentText(res);
        sw.showCancelButton(false);
        sw.show();
    }
    public void message(String title, String message) {
        SweetAlertDialog sw = new SweetAlertDialog(Category.this, SweetAlertDialog.ERROR_TYPE);
        sw.setTitleText(title);
        sw.setContentText(message);
        sw.show();
    }
}
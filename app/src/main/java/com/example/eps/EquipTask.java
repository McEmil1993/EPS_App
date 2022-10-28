package com.example.eps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.eps.adapter.RecycleViewCategory;
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

public class EquipTask extends AppCompatActivity {
    LinearLayout parentLayout;
    CardView tvNoRecordsFound;
    SweetAlertDialog pd;
    String get_id="";
    String bname= "";
    String rname = "";
    String tname ="";
    String status ="";
    String b_id = "";
    String date_t = "";
    final static int RQS_1 = 1;
    FloatingActionButton fab;
    TextView fname;
    DatabaseHelper dh;
    SQLiteDatabase db;
    String value="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equip_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dh = new DatabaseHelper(this);
        SharedPreferences sharedPreferences = getSharedPreferences("key", MODE_PRIVATE);
        value = sharedPreferences.getString("fullname",null);

        try{
            recieveData();
//        message("Data",get_id+"-"+rname+"-"+bname+"-"+tname);
            parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
            fname =  findViewById(R.id.fname);
            fname.setText(tname);
            tvNoRecordsFound = (CardView) findViewById(R.id.tvNoRecordsFound);
            tvNoRecordsFound.setVisibility(View.GONE);
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogAdd();
                }
            });

            displayAllRecords();
        }catch (Exception e){
            message("Error",e.toString());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.room_menu, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.rname);
        item.setTitle("");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void displayAllRecords() {
        try {
            androidx.appcompat.widget.LinearLayoutCompat inflateParentView;
            parentLayout.removeAllViews();
            AndroidNetworking
                    .post("http://localhost/mvc/Task/display")
                    .addBodyParameter("building_name", "" + bname)
                    .addBodyParameter("room_name", "" + rname)
                    .addBodyParameter("Task", "" + get_id)
                    .setPriority(Priority.HIGH)
                    .setTag("Add Data")
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray jArr) {
                            try {
                                for (int i = 0; i < jArr.length(); i++) {

                                    JSONObject jObj = jArr.getJSONObject(i);
                                    String _id = jObj.getString("id");
                                    String c = jObj.getString("cate_name");
                                    String t = jObj.getString("name");
                                    String m = jObj.getString("IM");
                                    String a = jObj.getString("name_f");
                                    String dt = jObj.getString("date_time");
                                    String st = jObj.getString("status");




                                    final Holder holder = new Holder();
//
                                    final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.row_etask, null);

                                    holder.t_title = (TextView) view.findViewById(R.id.t_title);
                                    holder.btn_remove =  view.findViewById(R.id.btn_remove);
                                    holder.chk = (TextView) view.findViewById(R.id.chk);
                                    holder.t_btn_down = (TextView) view.findViewById(R.id.t_btn_down);
                                    holder.t_btn_up = (TextView) view.findViewById(R.id.t_btn_up);
                                    holder.t_category = (TextView) view.findViewById(R.id.t_category);
                                    holder.t_date_inspect = (TextView) view.findViewById(R.id.t_date_inspect);
                                    holder.t_inspect_name = (TextView) view.findViewById(R.id.t_inspect_name);
                                    holder.r_details =  view.findViewById(R.id.r_details);
                                    holder.ch_good =  view.findViewById(R.id.good_ck);
                                    holder.ch_not =  view.findViewById(R.id.notgood_ck);
                                    holder.ch_miss =  view.findViewById(R.id.missing_ck);
                                    holder.ch_other =  view.findViewById(R.id.others_ck);
                                    holder.eqpt_image =  view.findViewById(R.id.eqpt_image);
                                    holder.progressbar =  view.findViewById(R.id.progressbar);

                                    Picasso.get()
                                            .load(m)
                                            .into(holder.eqpt_image, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                    holder.progressbar.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onError(Exception e) {

                                                    message("Error",e.getMessage());
                                                }
                                            });
                                    holder.t_category.setText(c);
                                    holder.t_title.setText(t);

                                    holder.t_inspect_name.setText(a);
                                    holder.t_date_inspect.setText(dt);

                                    if (st.equals("0") && dt.equals("0000-00-00")){
                                        holder.chk.setText("");
                                    }else {
                                        holder.chk.setText("Done");
                                    }

                                    if (st.equals("1")){
                                        holder.ch_good.setChecked(true);
                                    }else if (st.equals("2")){
                                        holder.ch_not.setChecked(true);
                                    }else if (st.equals("3")){
                                        holder.ch_miss.setChecked(true);
                                    }else if (st.equals("4")){
                                        holder.ch_other.setChecked(true);
                                    }
                                    holder.ch_good.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            holder.ch_not.setChecked(false);
                                            holder.ch_miss.setChecked(false);
                                            holder.ch_other.setChecked(false);
                                            String st = "";
                                            if(holder.ch_good.isChecked())
                                            {
                                                st = "1";
                                            }
                                            else
                                            {
                                                st = "0";
                                            }
                                            String finalSt = st;
                                            holder.checkDis(_id,finalSt,value);
                                        }
                                    });
                                    holder.ch_not.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            holder.ch_good.setChecked(false);
                                            holder.ch_miss.setChecked(false);
                                            holder.ch_other.setChecked(false);
                                            String st = "";
                                            if(holder.ch_not.isChecked())
                                            {
                                                st = "2";
                                            }
                                            else
                                            {
                                                st = "0";
                                            }
                                            String finalSt = st;
                                            holder.checkDis(_id,finalSt,value);

                                        }
                                    });
                                    holder.ch_miss.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            holder.ch_good.setChecked(false);
                                            holder.ch_not.setChecked(false);
                                            holder.ch_other.setChecked(false);
                                            String st = "";
                                            if(holder.ch_miss.isChecked())
                                            {
                                                st = "3";
                                            }
                                            else
                                            {
                                                st = "0";
                                            }
                                            String finalSt = st;
                                            holder.checkDis(_id,finalSt,value);
                                        }
                                    });
                                    holder.ch_other.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            holder.ch_good.setChecked(false);
                                            holder.ch_not.setChecked(false);
                                            holder.ch_miss.setChecked(false);
                                            String st = "";
                                            if(holder.ch_other.isChecked())
                                            {
                                                st = "4";
                                            }
                                            else
                                            {
                                                st = "0";
                                            }
                                            String finalSt = st;
                                            holder.checkDis(_id,finalSt,value);
                                        }
                                    });
                                    holder.t_btn_down.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            holder.t_btn_up.setVisibility(View.VISIBLE);
                                            holder.t_btn_down.setVisibility(View.GONE);
                                            holder.r_details.setVisibility(View.VISIBLE);
                                        }
                                    });
                                    holder.t_btn_up.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            holder.t_btn_up.setVisibility(View.GONE);
                                            holder.t_btn_down.setVisibility(View.VISIBLE);
                                            holder.r_details.setVisibility(View.GONE);
                                        }
                                    });
                                    holder.btn_remove.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            AndroidNetworking
                                                    .post("http://localhost/mvc/Task/delete")
                                                    .addBodyParameter("id", "" + _id)
                                                    .setTag("Get Data")
                                                    .setPriority(Priority.MEDIUM)
                                                    .build()
                                                    .getAsJSONArray(new JSONArrayRequestListener() {
                                                        @Override
                                                        public void onResponse(JSONArray jArr1) {
                                                            try {
                                                                JSONObject jObj1 = jArr1.getJSONObject(0);
                                                                String res = jObj1.getString("result");

                                                                if(res.equals("error")){
                                                                    message("Warning","Data is not saved!\nPlease check the credentials.");
                                                                }else {
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
                                    });
                                    parentLayout.addView(view);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        } catch(Exception e){
            message("Error",e.toString());
        }

    }
    public void dialogAdd(){
        List<String> list = new ArrayList<String>();
        list.add("Select Equipment");
        AndroidNetworking
                .post("http://localhost/mvc/Task/getAllEquip")
                .addBodyParameter("Task", "" + get_id)
                .setTag("Get Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray jArr) {
                        try {

                            for (int i = 0; i < jArr.length(); i++) {
                                JSONObject jObj = jArr.getJSONObject(i);
                                if (jObj.getString("quantity").equals("0")){

                                }else{
                                    list.add(jObj.getString("name"));
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {

                    }
                });
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        LinearLayout.LayoutParams layout_parameters = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        param.setMargins(0,22,0,22);
        final LinearLayout customLayout = new LinearLayout (this);

        customLayout.setLayoutParams(layout_parameters);
        customLayout.setOrientation(LinearLayout.VERTICAL);

        final Spinner ename = new Spinner(this);
        final EditText quan = new EditText(this);
        ename.setLayoutParams(param);



        customLayout.addView(ename);



        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ename.setAdapter(dataAdapter);

        final SweetAlertDialog sw = new SweetAlertDialog(EquipTask.this,SweetAlertDialog.NORMAL_TYPE);
        sw.setTitleText("Task Form");
        sw.setCustomView(customLayout);
        sw.setConfirmButton("Submit", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if (ename.getSelectedItem().equals("Select Equipment")){
                    message("Warning","Please Select Equipment.!");
                }else {
//                    message("",bname+"--"+rname+"-"+ename.getSelectedItem().toString()+"-"+get_id);
                    AndroidNetworking
                            .post("http://localhost/mvc/Task/save")
                            .addBodyParameter("b_id", "" + bname)
                            .addBodyParameter("r_id", "" + rname)
                            .addBodyParameter("ename",""+ename.getSelectedItem().toString())
                            .addBodyParameter("Task",""+get_id)
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
                                            message("Warning",ename.getSelectedItem().toString()+" is not already inspect!\nPlease inspect "+ename.getSelectedItem().toString()+" before add new.");

                                        }else if (res.equals("error")){
                                            message("Warning","Data is not saved!\nPlease check the credentials.");
                                        }
                                        else {
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

    private class Holder {
        TextView  t_title,t_btn_down,t_btn_up,t_category,t_date_inspect,t_inspect_name,chk,t_inspect_;
        RelativeLayout r_details;
        CheckBox ch_good,ch_not,ch_miss,ch_other;
        ImageView eqpt_image;
        ProgressBar progressbar;
        Button btn_remove;

        public void checkDis(String _id,String finalSt,String i_name){

            AndroidNetworking
                    .post("http://localhost/mvc/Task/checkStatus")
                    .addBodyParameter("id", "" + _id)
                    .addBodyParameter("status", "" + finalSt)
                    .addBodyParameter("i_name", "" + i_name)
                    .setTag("Get Data")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray jArr1) {
                            try {
                                JSONObject jObj1 = jArr1.getJSONObject(0);
                                String res = jObj1.getString("result");

                                if(res.equals("error")){
                                    message("Warning","Data is not saved!\nPlease check the credentials.");
                                }else {
                                    t_date_inspect.setText(res);
                                    t_inspect_name.setText(value);
                                    chk.setText("Done");
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
    public void Su(String res){
        final SweetAlertDialog sw = new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE);
        sw.setTitleText("Success!");
        sw.setContentText(res);
        sw.setConfirmButton("Submit", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                restartActivity();
            }
        });
        sw.show();
    }

    private void recieveData() {
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        try {
            if (b != null) {
                String row1 = b.getString("row_id");
                String row2 = b.getString("row_name");
                String row3 = b.getString("row_bname");
                String row4 = b.getString("row_tname");
                String row5 = b.getString("row_b_id");
                String row6= b.getString("row_status");

                get_id = row1;
                rname = row2;
                bname = row3;
                tname = row4;
                b_id = row5;
                status = row6;

            }
        }catch (Exception ex) {
            message("Error", ex.toString());
        }
    }
    public void restartActivity(){
        Intent mIntent = getIntent();
        finish();
        startActivity(mIntent);
    }
    public void message(String title, String message) {
        SweetAlertDialog sw = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        sw.setTitleText(title);
        sw.setContentText(message);
        sw.show();
    }
}
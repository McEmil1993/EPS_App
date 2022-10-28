package com.example.eps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Schedule extends AppCompatActivity {
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView setMonth,setQuar,setAnn,txtM,txtQ,txtA;
    private int year, month, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        setMonth = (TextView) findViewById(R.id.setMonth);
        setQuar = (TextView) findViewById(R.id.setQuar);
        setAnn = (TextView) findViewById(R.id.setAnnual);
        txtM = (TextView) findViewById(R.id.txtM);
        txtQ = (TextView) findViewById(R.id.txtQ);
        txtA = (TextView) findViewById(R.id.txtA);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_backspace_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        setMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(999);
            }
        });
        setQuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(998);
            }
        });
        setAnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(997);
            }
        });
        displaySched();
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

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }else if (id == 998) {
            return new DatePickerDialog(this,
                    quarter, year, month, day);
        }else if (id == 997) {
            return new DatePickerDialog(this,
                    annual, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3,1);
                }
            };

    private DatePickerDialog.OnDateSetListener quarter = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3,2);
                }
            };

    private DatePickerDialog.OnDateSetListener annual = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3,3);
                }
            };

    private void showDate(int year, int month, int day,int task) {
        if (task==1){
            txtM.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
            setSched(task,txtM.getText().toString());
        }else if (task == 2){
            txtQ.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
            setSched(task,txtQ.getText().toString());
        }else {
            txtA.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
            setSched(task,txtA.getText().toString());
        }

    }
    public void setSched(int task,String _date){
        AndroidNetworking
                .post("http://localhost/mvc/Task/qur")
                .addBodyParameter("task", "" + task)
                .addBodyParameter("date_sched", "" + _date)
                .setPriority(Priority.HIGH)
                .setTag("Add Data")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray jArr1) {
                        try {
                            JSONObject jObj1 = jArr1.getJSONObject(0);
                            String res = jObj1.getString("result");
                            if(res.equals("ok")){
                                Su("Set schedule success.!");
                            }else if (res.equals("error")){
                                message("Warning","Data is not saved!\nPlease check the credentials.");
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

    public void displaySched(){
        AndroidNetworking
                .post("http://localhost/mvc/Task/displaySched")
                .setPriority(Priority.HIGH)
                .setTag("GEt Data")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {


                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jObj = response.getJSONObject(i);
                                String tsk = jObj.getString("id");

                                if (tsk.equals("1")) {
                                    txtM.setText(jObj.getString("date_sched"));
                                }
                                if (tsk.equals("2")) {
                                    txtQ.setText(jObj.getString("date_sched"));
                                }
                                if (tsk.equals("3")) {
                                    txtA.setText(jObj.getString("date_sched"));
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
    }
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
        sw.setConfirmButton("Submit", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sw.dismissWithAnimation();
            }
        });
        sw.show();
    }

}
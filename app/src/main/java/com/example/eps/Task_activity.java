package com.example.eps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Task_activity extends AppCompatActivity {
    TextView data1,textview1;
    TableLayout tbl1;
    TableRow tr1;
    Button btnAction;
    String get_id="";
    String bname= "";
    String rname = "";
    String tname ="";

    AlertDialog.Builder alertdialogbuilder;
    String[] AlertDialogItems = new String[]{
            "January",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "July",
            "Aug",
            "Sept",
            "Oct",
            "Nov",
            "Dec"
    };
    List<String> ItemsIntoList;
    boolean[] Selectedtruefalse = new boolean[]{
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_activity);
        recieveData();
        textview1 = (TextView) findViewById(R.id.textview1);

        tbl1 = (TableLayout) findViewById(R.id.tbl1);


//        generate_column_headers1();


        textview1.setText(bname+"/"+rname+"/"+tname);
    }

    private void generate_column_headers(){
        TableRow.LayoutParams layout_parameters = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        this.tr1 = new TableRow(this);
        this.tr1.setLayoutParams(layout_parameters);


        this.data1 = new TextView(this);
        this.data1.setLayoutParams(layout_parameters);
        this.data1.setText("Category");
        this.data1.setTextSize(12);
        this.data1.setBackgroundColor(Color.parseColor("#8e9599"));
        this.data1.setTextColor(Color.parseColor("#ffffff"));
        this.data1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.data1.setPadding(5,5,5,5);
        this.tr1.addView(this.data1);

        this.data1 = new TextView(this);
        this.data1.setLayoutParams(layout_parameters);
        this.data1.setText("Eqp. Name");
        this.data1.setTextSize(12);
        this.data1.setBackgroundColor(Color.parseColor("#8e9599"));
        this.data1.setTextColor(Color.parseColor("#ffffff"));
        this.data1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.data1.setPadding(5,5,5,5);
        this.tr1.addView(this.data1);

        this.data1 = new TextView(this);
        this.data1.setLayoutParams(layout_parameters);
        this.data1.setText("Description");
        this.data1.setTextSize(12);
        this.data1.setBackgroundColor(Color.parseColor("#8e9599"));
        this.data1.setTextColor(Color.parseColor("#ffffff"));
        this.data1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.data1.setPadding(5,5,5,5);
        this.tr1.addView(this.data1);

        this.data1 = new TextView(this);
        this.data1.setLayoutParams(layout_parameters);
        this.data1.setText("Tool");
        this.data1.setTextSize(12);
        this.data1.setBackgroundColor(Color.parseColor("#8e9599"));
        this.data1.setTextColor(Color.parseColor("#ffffff"));
        this.data1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.data1.setPadding(5,5,5,5);
        this.tr1.addView(this.data1);

        this.tbl1.addView(this.tr1);
    }

//    private void generate_column_headers1(){
//        generate_column_headers();
//        try {
//            String url[] = {"http://192.168.8.113:8081/phpfile/getTask.php"};
//            String[] params = {"building_name", "room_name", "Task"};
//            String[] values = {bname, rname, get_id};
//            String result = new Connection().execute(url, params, values).get();
//            JSONArray jArr = new JSONArray(result);
//            for (int i = 0; i < jArr.length(); i++) {
//
//                JSONObject jObj = jArr.getJSONObject(i);
//                String status = "", btn_text = "";
//
//                TableRow.LayoutParams layout_parameters = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
//                this.tr1 = new TableRow(this);
//                this.tr1.setLayoutParams(layout_parameters);
//
//
//                this.data1 = new TextView(this);
//                this.data1.setLayoutParams(layout_parameters);
//                this.data1.setText(jObj.getString("cate_name"));
//                this.data1.setTextSize(12);
//                this.data1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                this.data1.setPadding(5,5,5,5);
//                this.tr1.addView(this.data1);
//
//                this.data1 = new TextView(this);
//                this.data1.setLayoutParams(layout_parameters);
//                this.data1.setText(jObj.getString("name"));
//                this.data1.setTextSize(12);
//                this.data1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                this.data1.setPadding(5,5,5,5);
//                this.tr1.addView(this.data1);
//
//                this.data1 = new TextView(this);
//                this.data1.setLayoutParams(layout_parameters);
//                this.data1.setText(jObj.getString("ED"));
//                this.data1.setTextSize(12);
//                this.data1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                this.data1.setPadding(5,5,5,5);
//                this.tr1.addView(this.data1);
//
//                this.data1 = new TextView(this);
//                this.data1.setLayoutParams(layout_parameters);
//                this.data1.setText("Action");
//                this.data1.setTextSize(12);
//                this.data1.setTextColor(Color.parseColor("#3F51B5"));
//                this.data1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                this.data1.setPadding(5,5,5,5);
//                this.data1.setOnClickListener(btn);
//                this.tr1.addView(this.data1);
//
//                this.tbl1.addView(this.tr1);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            message("Error", e.toString());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//    }
    private View.OnClickListener btn = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            textview1.setText("");
            alertdialogbuilder = new AlertDialog.Builder(Task_activity.this);
            ItemsIntoList = Arrays.asList(AlertDialogItems);
            
            alertdialogbuilder.setMultiChoiceItems(AlertDialogItems, Selectedtruefalse, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                }
            });
            alertdialogbuilder.setCancelable(false);
            alertdialogbuilder.setTitle("Select Subjects Here");
            alertdialogbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int a = 0;
                    while(a < Selectedtruefalse.length)
                    {
                        boolean value = Selectedtruefalse[a];
                        if(value){
                            textview1.setText(textview1.getText() + ItemsIntoList.get(a) + "\n");
                        }
                        a++;
                    }

                }
            });
            alertdialogbuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog dialog = alertdialogbuilder.create();
            dialog.show();
        }
    };

    private void recieveData() {
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        try {
            if (b != null) {
                String row1 = b.getString("row_id");
                String row2 = b.getString("row_name");
                String row3 = b.getString("row_bname");
                String row4 = b.getString("row_tname");

                get_id = row1;
                rname = row2;
                bname = row3;
                tname = row4;
            }
        }catch (Exception ex) {
            message("Error", ex.toString());
        }
    }
    private void message(String title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(true);
        dialog.show();
    }}
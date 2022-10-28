package com.example.eps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ListTask extends AppCompatActivity {
    TextView noData,txtname,searh,btnSubmit;
    Button brn;
    SearchView txtsearch;
    LinearLayout parentLayout;
    LinearLayout layoutDisplayPeople;
    CardView card_view2;
    String get_id="";
    String bname= "";
    String rname = "";
    String tname ="";
    String vis ="";
    String status ="";
    String b_id = "";
    private Animation animShow, animHide;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        recieveData();
        message("Data",get_id+"-"+rname+"-"+bname+"-"+tname);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_backspace_24);
        actionBar.setDisplayHomeAsUpEnabled(true);

        txtsearch = findViewById(R.id.txtsearch);

        searh = findViewById(R.id.searh);

        animShow = AnimationUtils.loadAnimation( this, R.anim.down);
        animHide = AnimationUtils.loadAnimation( this, R.anim.up);

        noData = findViewById(R.id.tvNoRecordsFound);
        card_view2 = findViewById(R.id.card_view2);
        card_view2.setVisibility(View.GONE);
        txtname = findViewById(R.id.txtname);
        parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
        layoutDisplayPeople = (LinearLayout) findViewById(R.id.layoutDisplayPeople);
        displayAllRecords();
        txtname.setText(bname+"/"+rname+"/"+tname);
        searh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vis.equals("")){
                    card_view2.setVisibility(View.VISIBLE);
                    txtsearch.setVisibility(View.VISIBLE);
                    vis = "1";
                }else if (vis.equals("1")){
                    card_view2.setVisibility(View.GONE);
                    txtsearch.setVisibility(View.GONE);
                    txtsearch.setQuery("",true);
                    txtsearch.setQueryHint("Search Here");
                    vis = "";
                }
            }
        });
        txtsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                androidx.appcompat.widget.LinearLayoutCompat inflateParentView;
                parentLayout.removeAllViews();

//                try {
//                    String url[] = {"http://localhost:8081/phpfile/getSearch.php"};
//                    String[] params = {"building_name", "room_name", "Task","name"};
//                    String[] values = {bname, rname, get_id,query};
//                    String result = new Connection().execute(url, params, values).get();
//                    JSONArray jArr = new JSONArray(result);
//                    for (int i = 0; i < jArr.length(); i++) {
//
//                        JSONObject jObj = jArr.getJSONObject(i);
//                        String status = "", btn_text = "";
//
//                        final Holder holder = new Holder();
//                        noData.setVisibility(View.GONE);
//
//                        final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_view, null);
//                        inflateParentView = (androidx.appcompat.widget.LinearLayoutCompat) view.findViewById(R.id.inflateParentView);
//                        String c = jObj.getString("cate_name");
//                        String t = jObj.getString("name");
//                        String a = jObj.getString("ED");
//                        String r = "Action";
//
//                        holder.simpleTextView1 = (TextView) view.findViewById(R.id.simpleTextView1);
//                        holder.simpleTextView2 = (TextView) view.findViewById(R.id.simpleTextView2);
//                        holder.simpleTextView3 = (TextView) view.findViewById(R.id.simpleTextView3);
//                        holder.simpleTextView4 = (TextView) view.findViewById(R.id.simpleTextView4);
//
//                        String personName = c+"\b"+t+"\b"+a+"\b"+"\b"+r;
//                        holder.simpleTextView1.setText(c);
//                        holder.simpleTextView2.setText(t);
//                        holder.simpleTextView3.setText(a);
//                        holder.simpleTextView4.setText(r);
//                        holder.simpleTextView4.setTextColor(Color.parseColor("#3F51B5"));
//                        parentLayout.addView(view);
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    message("Error", e.toString());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                return false;
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent modify_intent = new Intent(this, Rooms.class);
                modify_intent.putExtra("row_id", b_id);
                modify_intent.putExtra("row_name", bname);
                modify_intent.putExtra("row_status", status);
                startActivity(modify_intent);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayAllRecords() {
        androidx.appcompat.widget.LinearLayoutCompat inflateParentView;
        parentLayout.removeAllViews();
//        AndroidNetworking
//                .post("https://projectsample1933.000webhostapp.com/mvc/Room/update")
//                .addBodyParameter("room_name", "" + name.getText().toString())
//                .addBodyParameter("description", "" + des.getText().toString())
//                .addBodyParameter("building_id", "" + id)
//                .setPriority(Priority.HIGH)
//                .setTag("Add Data")
//                .build()
//                .getAsJSONArray(new JSONArrayRequestListener() {
//                    @Override
//                    public void onResponse(JSONArray jArr1) {
//                        try {
//                            JSONObject jObj1 = jArr1.getJSONObject(0);
//                            String res = jObj1.getString("result");
//
//                            if (res.equals("error")){
//                                message("Warning","Data is not updated!\nPlease check the credentials.");
//                            }else {
//                                sw.dismissWithAnimation();
//                                swet(res);
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//
//                    }
//                });
//        try {
//
//            String url[] = {"https://projectsample1933.000webhostapp.com/mvc/Task/display"};
//            String[] params = {"building_name", "room_name", "Task"};
//            String[] values = {bname, rname, get_id};
//            String result = new Connection().execute(url, params, values).get();
//            JSONArray jArr = new JSONArray(result);
//            for (int i = 0; i < jArr.length(); i++) {
//
//                JSONObject jObj = jArr.getJSONObject(i);
//                String status = "", btn_text = "";
//
//                final Holder holder = new Holder();
//                noData.setVisibility(View.GONE);
//
//                    final View view = LayoutInflater.from(this).inflate(R.layout.list_view, null);
//                    inflateParentView = (androidx.appcompat.widget.LinearLayoutCompat) view.findViewById(R.id.inflateParentView);
//                    String c = jObj.getString("cate_name");
//                    String t = jObj.getString("name");
//                    String a = jObj.getString("ED");
//                    String r = "Action";
//
//                    holder.simpleTextView1 = (TextView) view.findViewById(R.id.simpleTextView1);
//                    holder.simpleTextView2 = (TextView) view.findViewById(R.id.simpleTextView2);
//                    holder.simpleTextView3 = (TextView) view.findViewById(R.id.simpleTextView3);
//                    holder.simpleTextView4 = (TextView) view.findViewById(R.id.simpleTextView4);
//
//
////                    String p = result.getString(4);
////                    String s = result.getString(5);
//
//
//                    String personName = c+"\b"+t+"\b"+a+"\b"+"\b"+r;
//                    holder.simpleTextView1.setText(c);
//                    holder.simpleTextView2.setText(t);
//                    holder.simpleTextView3.setText(a);
//                    holder.simpleTextView4.setText(r);
//                    holder.simpleTextView4.setTextColor(Color.parseColor("#3F51B5"));
//
//                    parentLayout.addView(view);
//
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            message("Error", e.toString());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
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

    private void message(String title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(true);
        dialog.show();
    }

    private class Holder {
        public TextView simpleTextView1;
        TextView  simpleTextView2,simpleTextView3,simpleTextView4;

    }

}
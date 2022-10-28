package com.example.eps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.eps.adapter.RecycleViewBuilding;

import java.util.ArrayList;

public class Task extends AppCompatActivity {

    RecyclerView recyclerView_building;
    ArrayList<String> name,_id,descript;
    ArrayList<String> count;
    View ChildView ;
    int RecyclerViewItemPosition ;

    String get_id="";
    String r_name="";
    String b_name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_backspace_24);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView_building = findViewById(R.id.recyclerview_building);
        recyclerView_building.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_building.setAdapter(new RecycleViewBuilding(name,count,"Task",_id,descript,Task.this,""));

//        recyclerView_building.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            GestureDetector gestureDetector = new GestureDetector(Task.this, new GestureDetector.SimpleOnGestureListener() {
//                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {
//                    return true;
//                }
//            });
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {
//                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
//                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {
//                    RecyclerViewItemPosition = Recyclerview.getChildAdapterPosition(ChildView);
//
//                    String id = _id.get(RecyclerViewItemPosition);
//                    String tname = name.get(RecyclerViewItemPosition);
//
//                    try {
//
//                        Intent modify_intent = new Intent(getApplicationContext(), ListTask.class);
//                        modify_intent.putExtra("row_id", id);
//                        modify_intent.putExtra("row_name", r_name);
//                        modify_intent.putExtra("row_tname",tname);
//                        modify_intent.putExtra("row_bname", b_name);
//
//                        startActivity(modify_intent);
//                    }catch (Exception e){
//                        message("Error",e.toString());
//                    }
//
//                }
//                return false;
//            }
//            @Override
//            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {
//            }
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//            }
//        });

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
}

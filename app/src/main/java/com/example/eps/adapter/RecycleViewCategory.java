package com.example.eps.adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.eps.Category;
import com.example.eps.R;
import com.example.eps.Rooms;

import cn.pedant.SweetAlert.SweetAlertDialog;
import android.graphics.Color;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecycleViewCategory extends RecyclerView.Adapter<RecycleViewCategory.MyView> {

    private int progressStatus = 0;
    private Handler handler = new Handler();
    private List<String> id;
    private List<String> category;
    private List<String> Task;
    private Context context;
    String taskNum;

    public class MyView extends RecyclerView.ViewHolder {

        public TextView txtCategory,txtTask,txtUpdate,txtDelete;

        public MyView(View view) {
            super(view);
            txtCategory = (TextView) view.findViewById(R.id.txtCategory);
            txtTask = (TextView) view.findViewById(R.id.txtTask);
            txtUpdate = (TextView) view.findViewById(R.id.txtUpdate);
            txtDelete = (TextView) view.findViewById(R.id.txtDelete);

        }
    }

    public RecycleViewCategory(List<String> id,List<String> category,List<String> Task,Context context) {
        this.context = context;
        this.category = category;
        this.Task = Task;
        this.id = id;
    }
    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);
        return new MyView(itemView);
    }
    @Override
    public void onBindViewHolder(final MyView holder, @SuppressLint("RecyclerView") final int position) {

        if (Task.get(position).equals("1")){
            setTaskNum("Monthly Task");
        }else if (Task.get(position).equals("2")){
            setTaskNum("Quarterly Task");
        }else if (Task.get(position).equals("3")){
            setTaskNum("Annual Task");
        }
        holder.txtCategory.setText(category.get(position));
        holder.txtTask.setText(getTaskNum());
        final String t = holder.txtTask.getText().toString().trim();
        holder.txtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    dialogUpdate(id.get(position),category.get(position),t);
                } catch (Exception e) {
                    message("Error", e.toString());
                }

            }
        });
        holder.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogDelete(id.get(position),category.get(position));
            }
        });

    }
    public void dialogUpdate(final String id, String cname, String taskn){
        LinearLayout.LayoutParams layout_parameters = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        final LinearLayout customLayout = new LinearLayout (context);
        customLayout.setLayoutParams(layout_parameters);
        customLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText name = new EditText(context);
        name.setHint("Enter Category name");
        name.setLayoutParams(layout_parameters);
        final Spinner des = new Spinner(context);
        des.setLayoutParams(layout_parameters);
        customLayout.addView(name);
        customLayout.addView(des);

        List<String> list = new ArrayList<String>();
        list.add("Select Task");
        list.add("Monthly Task");
        list.add("Quarterly Task");
        list.add("Annual Task");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        des.setAdapter(dataAdapter);


        name.setText(cname);
        des.setSelection(list.indexOf(taskn));
        final Category e = (Category) context;
        final SweetAlertDialog sw = new SweetAlertDialog(context,SweetAlertDialog.NORMAL_TYPE);
        sw.setTitleText("Update Category");
        sw.setCustomView(customLayout);
        sw.setConfirmButton("Submit", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                e.swet();
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
                                .post("http://localhost/mvc/Category/update")
                                .addBodyParameter("cate_name",""+name.getText().toString())
                                .addBodyParameter("Task",""+tsk)
                                .addBodyParameter("id",""+id)
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
                                            }
                                            else if (res.equals("ok")) {
                                                sw.dismiss();
                                                e.displayName();
                                                e.Su("Update category successfully!");

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


    public void message(String title, String message) {
        SweetAlertDialog sw = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        sw.setTitleText(title);
        sw.setContentText(message);
        sw.show();
    }
    public void showAlertDialogDelete(final String id,String name) {
        final Category e = (Category) context;
        final SweetAlertDialog sw = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sw.setContentText("You won't to delete? \n" +name);
        sw.setConfirmButton("Yes", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                e.swet();
                AndroidNetworking
                        .post("http://localhost/mvc/Category/delete")
                        .addBodyParameter("id",""+id)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray jArr1) {
                                try {
                                    JSONObject jObj1 = jArr1.getJSONObject(0);
                                    String res = jObj1.getString("result");

                                    if (res.equals("error")){
                                        message("Warning","Data is not deleted!\nPlease check the query.");
                                    }
                                    else if (res.equals("ok")){
                                        sw.dismiss();
                                        e.displayName();
                                        e.Su("Delete category successfully!");
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
        sw.setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sw.dismissWithAnimation();
            }
        });
        sw.setCancelable(false);
        sw.show();

    }

    @Override
    public int getItemCount() {
        return category.size();
    }
    public String getTaskNum(){ return taskNum; }

    public void setTaskNum(String taskNum){ this.taskNum = taskNum; }
}


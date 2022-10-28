package com.example.eps.adapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.eps.Building;
import com.example.eps.EquipTask;
import com.example.eps.R;
import com.example.eps.Rooms;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class RecycleViewBuilding extends RecyclerView.Adapter<RecycleViewBuilding.MyView> {

    private List<String> list;
    private String get_id = "";
    private List<String> status;
    private List<String> descript;
    String Aname = "";
    private List<String> row_id;
    Context context;
    String st;
    String bname;
    String gId;
    SweetAlertDialog pd;

    private int progressStatus = 0;
    private Handler handler = new Handler();

    public class MyView extends RecyclerView.ViewHolder {

        public TextView textView,txtcount,txtnamecount,txtUpdate,txtDelete,txtRoom,txtTask,txtsample;
        public ImageView task_image;
        public CardView card_view;
        public LinearLayout linear;

        public MyView(View view) {
            super(view);
            task_image = (ImageView) view.findViewById(R.id.task_image);
            txtnamecount = (TextView) view.findViewById(R.id.textview);
            textView = (TextView) view.findViewById(R.id.textview1);
            txtcount = (TextView) view.findViewById(R.id.textview2);
            txtTask = (TextView) view.findViewById(R.id.txtTask);
            txtUpdate = (TextView) view.findViewById(R.id.txtUpdate);
            txtDelete = (TextView) view.findViewById(R.id.txtDelete);
            txtRoom = (TextView) view.findViewById(R.id.txtRoom);
            card_view = (CardView) view.findViewById(R.id.card_view);
            txtsample =new TextView(context);
            linear = (LinearLayout) view.findViewById(R.id.linear);
//            Animation anim = AnimationUtils.loadAnimation(context, R.anim.up);
//            linear.startAnimation(anim);
        }

    }
    public RecycleViewBuilding(List<String> horizontalList,List<String> status, String name,List<String> row_id,List<String> descrt,Context context,String get_id) {
        this.list = horizontalList;
        this.status = status;
        this.Aname = name;
        this.row_id = row_id;
        this.descript = descrt;
        this.context = context;
        this.get_id = get_id;
    }
    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_building, parent, false);

        return new MyView(itemView);
    }


    @Override
    public void onBindViewHolder(final MyView holder, @SuppressLint("RecyclerView") final int position) {

        if(Aname.equals("Building")){

            holder.textView.setText(list.get(position));
            holder.txtcount.setVisibility(View.GONE);
            holder.txtnamecount.setText("Room Count: "+status.get(position));
            holder.txtUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Building b = (Building) getContext();
                    b.dialogAdd(row_id.get(position),list.get(position),descript.get(position));

                }
            });
            holder.txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showAlertDialogDelete(row_id.get(position),list.get(position));
                }
            });
            holder.txtRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Intent modify_intent = new Intent(context, Rooms.class);
                        modify_intent.putExtra("row_id", row_id.get(position));
                        modify_intent.putExtra("row_name", list.get(position));
                        modify_intent.putExtra("row_status", status.get(position));
                        modify_intent.putExtra("row_page", "0");
                        context.startActivity(modify_intent);

                    }catch (Exception e){
                        message("Error",e.toString());
                    }
                }
            });
        }else {
            holder.textView.setText(list.get(position));
            holder.txtcount.setText(status.get(position));

            holder.txtnamecount.setText("Description");

            holder.txtTask.setVisibility(View.VISIBLE);
            holder.txtRoom.setVisibility(View.GONE);
            holder.txtsample.setText("Annual Task");
            final String tc = holder.txtsample.getText().toString();
            holder.txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AndroidNetworking
                            .post("http://localhost/mvc/Room/delete")
                            .addBodyParameter("id",""+row_id.get(position))
                            .addBodyParameter("b_id",""+get_id)
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
                                            message("Warning","Data is not deleted!\nPlease check the query.");
                                        }else {

                                            Rooms e = (Rooms) getContext();
                                            e.displayName();
                                            e.Su(res);
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

            holder.txtTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder= new AlertDialog.Builder(context);
                    final View customLayout = LayoutInflater.from(context).inflate(R.layout.task_dialog, null);
                    builder.setView(customLayout);

                    final AlertDialog dialog= builder.create();
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    dialog.show();
                    final Button btnMon = (Button) customLayout.findViewById(R.id.btnMon);
                    final Button btnQuar = (Button) customLayout.findViewById(R.id.btnQuar);
                    final Button btnAnnual = (Button) customLayout.findViewById(R.id.btnAnnual);

                    btnMon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AndroidNetworking
                                    .post("http://localhost/mvc/Task/clickTask")
                                    .setTag("Get Data")
                                    .setPriority(Priority.MEDIUM)
                                    .build()
                                    .getAsJSONArray(new JSONArrayRequestListener() {
                                        @Override
                                        public void onResponse(JSONArray jArr1) {
                                            try {
                                                JSONObject jObj1 = jArr1.getJSONObject(0);



                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        @Override
                                        public void onError(ANError anError) {

                                        }
                                    });
                            try{

                                Intent modify_intent = new Intent(context, EquipTask.class);
                                modify_intent.putExtra("row_id", "1");
                                modify_intent.putExtra("row_name", list.get(position));
                                modify_intent.putExtra("row_tname","Monthly Task");
                                modify_intent.putExtra("row_bname", getBname());

                                modify_intent.putExtra("row_b_id", getgId());
                                modify_intent.putExtra("row_status", getSt());


                                context.startActivity(modify_intent);
                                dialog.dismiss();
                            }catch (Exception e){
                                message("Error",e.toString());
                            }

                        }
                    });
                    btnQuar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AndroidNetworking
                                    .post("http://localhost/mvc/Task/clickTask")
                                    .setTag("Get Data")
                                    .setPriority(Priority.MEDIUM)
                                    .build()
                                    .getAsJSONArray(new JSONArrayRequestListener() {
                                        @Override
                                        public void onResponse(JSONArray jArr1) {
                                            try {
                                                JSONObject jObj1 = jArr1.getJSONObject(0);



                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        @Override
                                        public void onError(ANError anError) {

                                        }
                                    });
                            try{
                                Intent modify_intent = new Intent(context, EquipTask.class);
                                modify_intent.putExtra("row_id", "2");
                                modify_intent.putExtra("row_name", list.get(position));
                                modify_intent.putExtra("row_tname","Quarterly Task");
                                modify_intent.putExtra("row_bname", getBname());

                                modify_intent.putExtra("row_b_id", getgId());
                                modify_intent.putExtra("row_status", getSt());


                                context.startActivity(modify_intent);
                                dialog.dismiss();
                            }catch (Exception e){
                                message("Error",e.toString());
                            }
                        }
                    });
                    btnAnnual.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AndroidNetworking
                                    .post("http://localhost/mvc/Task/clickTask")
                                    .setTag("Get Data")
                                    .setPriority(Priority.MEDIUM)
                                    .build()
                                    .getAsJSONArray(new JSONArrayRequestListener() {
                                        @Override
                                        public void onResponse(JSONArray jArr1) {
                                            try {
                                                JSONObject jObj1 = jArr1.getJSONObject(0);



                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        @Override
                                        public void onError(ANError anError) {

                                        }
                                    });
                            try{
                                Intent modify_intent = new Intent(context, EquipTask.class);
                                modify_intent.putExtra("row_id", "3");
                                modify_intent.putExtra("row_name", list.get(position));
                                modify_intent.putExtra("row_tname","Annual Task");
                                modify_intent.putExtra("row_bname", getBname());

                                modify_intent.putExtra("row_b_id", getgId());
                                modify_intent.putExtra("row_status", getSt());


                                context.startActivity(modify_intent);
                                dialog.dismiss();
                            }catch (Exception e){
                                message("Error",e.toString());
                            }
                        }
                    });
                }
            });
            holder.txtUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAdd(row_id.get(position),list.get(position),status.get(position),"Update Room");
                }
            });
        }
    }
    public void showAlertDialogDelete(final String id,String tname) {
        final Building e = (Building) getContext();
        final SweetAlertDialog sw = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sw.setContentText("You won't to delete? \n" +tname);
        sw.setConfirmButton("Yes", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {


                        AndroidNetworking
                                .post("http://localhost/mvc/Building/deleteBuilding")
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
                                                message("Warning","Data is not deleted!\nPlease check the query.");
                                            }else {
                                                sw.dismiss();
                                                e.displayName();
                                                e.Su(res);
                                            }

                                        } catch (JSONException e) {
                                            message("",e.toString());
                                        }catch (Exception e){
                                            message("",e.toString());
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

    public void dialogAdd(final String id,String uname,String description,String dialog_title){
        LinearLayout.LayoutParams layout_parameters = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        final LinearLayout customLayout = new LinearLayout (context);
        customLayout.setLayoutParams(layout_parameters);
        customLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText name = new EditText(context);
        name.setHint("Enter Building name");
        name.setLayoutParams(layout_parameters);
        final EditText des = new EditText(context);
        des.setLayoutParams(layout_parameters);
        des.setHint("Enter Description");
        customLayout.addView(name);
        customLayout.addView(des);

        des.setText(description);
        name.setText(uname);
        final SweetAlertDialog sw = new SweetAlertDialog(context,SweetAlertDialog.NORMAL_TYPE);
        sw.setTitleText(dialog_title);
        sw.setCustomView(customLayout);
        sw.setConfirmButton("Submit", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                    if (name.getText().toString().equals("")) {
                        Toast.makeText(context, "Please enter room name!", Toast.LENGTH_SHORT).show();
                    } else if (des.getText().toString().equals("")) {
                        Toast.makeText(context, "Please enter room description!", Toast.LENGTH_SHORT).show();
                    } else {
                        AndroidNetworking
                                .post("http://localhost/mvc/Room/update")
                                .addBodyParameter("room_name", "" + name.getText().toString())
                                .addBodyParameter("description", "" + des.getText().toString())
                                .addBodyParameter("id", "" + id)
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
                                                sw.dismissWithAnimation();
                                                Rooms e = (Rooms) getContext();
                                                e.displayName();
                                                e.Su(res);
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
public void setSt(String status){
       this.st = status;
}
public String getSt(){
        return st;
}
public void setBname(String bname){
        this.bname = bname;
}
public String getBname(){
        return bname;
}
public void setgId(String id){
        this.gId = id;
}
public String getgId(){
        return gId;
}

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}

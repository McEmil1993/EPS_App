package com.example.eps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.model.Progress;
import com.example.eps.adapter.RecycleViewCategory;
import com.example.eps.adapter.RecycleViewEquip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Equipment extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<String> eqp_name,descrip,eqp_id,image_url,cate_name,status,quantity;
    List<String> list1,bname,rname;
    private int GALLERY = 1, CAMERA = 2;
    Bitmap myBitmap;
    ImageView img;
    String ch = "";
    LinearLayout.LayoutParams param,param1,layout_parameters;
    RecycleViewEquip eqpr;
    String ca = "";
    SwipeRefreshLayout srl_main;
    CardView c_pro;
    SweetAlertDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);
        srl_main    = findViewById(R.id.srl_main);
        c_pro    = findViewById(R.id.c_pro);
        param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        param1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        layout_parameters = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_backspace_24);
        actionBar.setDisplayHomeAsUpEnabled(true);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAdd(null,null,null,null,null,null);
            }
        });

        srl_main.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srl_main.setRefreshing(false);
                        displayName();
                    }
                },1200);
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
    private  void displayName(){

        list1 = new ArrayList<String>();
        list1.add("Select Category");
        AndroidNetworking
                .post("http://localhost/mvc/Category/display")
                .setTag("Get Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jObj = response.getJSONObject(i);
                                list1.add(jObj.getString("cate_name"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {

                    }
                });
        AndroidNetworking
                .post("http://localhost/mvc/Equipment/getAllEquip")
                .setTag("Get Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        c_pro.setVisibility(View.GONE);
                        try {
                            eqp_name = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jObj = response.getJSONObject(i);
                                eqp_name.add(jObj.getString("name"));
                            }
                            eqp_id = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jObj = response.getJSONObject(i);
                                eqp_id.add(jObj.getString("id"));
                            }

                            descrip = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jObj = response.getJSONObject(i);
                                descrip.add(jObj.getString("description"));
                            }
                            image_url = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jObj = response.getJSONObject(i);
                                image_url.add(jObj.getString("ImageUrl"));
                            }

                            cate_name = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jObj = response.getJSONObject(i);
                                cate_name.add(jObj.getString("cate_name"));
                            }

                            status = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jObj = response.getJSONObject(i);
                                status.add(jObj.getString("status"));
                            }
                            quantity = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jObj = response.getJSONObject(i);
                                quantity.add(jObj.getString("quantity"));
                            }
                            eqpr = new RecycleViewEquip(eqp_name,descrip,eqp_id,image_url,cate_name,quantity);
                            recyclerView = findViewById(R.id.recyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(Equipment.this));
                            recyclerView.setAdapter(eqpr);
                            eqpr.setContext(Equipment.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {

                    }
                });


    }
    public void dialogAdd(final String e_id,String cname, String eqname,String desript,String imUrl,String quant){

        layout_parameters.setMargins(0,0,0,5);
        param.setMargins(0,22,0,22);
        param1.setMargins(0,0,0,30);
        final LinearLayout customLayout = new LinearLayout (this);
        customLayout.setLayoutParams(layout_parameters);
        customLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText ename = new EditText(this);
        final EditText descrip = new EditText(this);
        final EditText quan = new EditText(this);
        final Spinner cate = new Spinner(this);
        img = new ImageView(this);
        final TextView chosse = new TextView(this);

        ename.setHint("Enter Equipment name");
        ename.setLayoutParams(layout_parameters);
        descrip.setHint("Enter desription");
        descrip.setLayoutParams(layout_parameters);
        quan.setHint("Enter quantity");
        quan.setHorizontallyScrolling(true);
        quan.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        quan.setLayoutParams(layout_parameters);
        chosse.setText("Click to choose image/take Picture");
        chosse.setLayoutParams(param1);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(250, 250);
        img.setLayoutParams(layoutParams);
        cate.setLayoutParams(param);
        img.setImageResource(R.drawable.ic_baseline_image_24);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list1);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cate.setAdapter(dataAdapter);

        chosse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableRuntimePermissionToAccessCamera();
                showPictureDialog();
            }
        });


        if (e_id != null) {
            ename.setText(eqname);
            descrip.setText(desript);
            quan.setText(quant);
            cate.setSelection(list1.indexOf(cname));
            if (imUrl.isEmpty()) {
                img.setImageResource(R.drawable.ic_baseline_image_24);
            } else {
                Picasso.get()
                        .load(imUrl)
                        .into(img, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
            }
        }

        customLayout.addView(ename);
        customLayout.addView(descrip);
        customLayout.addView(quan);
        customLayout.addView(cate);
        customLayout.addView(img);
        customLayout.addView(chosse);
        final SweetAlertDialog sw = new SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE);
        sw.setTitleText("Equipment");
        sw.setCustomView(customLayout);
        sw.setConfirmButton("Submit", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                c_pro.setVisibility(View.VISIBLE);
                if (ename.getText().toString().equals("")) {
                    message("Warning!","Please enter equiment name!");

                }else  if (descrip.getText().toString().equals("")) {
                    message("Warning!","Please enter equipment description!");

                }
                else  if (quan.getText().toString().equals("")) {
                    message("Warning!","Please enter equipment quantity!");

                }
                else if (cate.getSelectedItem().toString().equals("Select Category")) {
                    message("Warning!","Please Select Category!");

                }
//                else if (ch.equals("")) {
//                    chosse.setTextColor(Color.parseColor("#D61F1F"));
//                    message("Warning!","Please Select image or take a picture!");
//
//                }
                else {
                    swet();
                    try {
                        String imgString ="";

                        if(myBitmap == null){
                            imgString="null";

                        }else {
                            Bitmap newBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), myBitmap.getConfig());
                            Canvas canvas = new Canvas(newBitmap);
                            canvas.drawColor(Color.WHITE);
                            canvas.drawBitmap(myBitmap, 0, 0, null);
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            newBitmap.compress(Bitmap.CompressFormat.JPEG, 15, outputStream);
                            byte[] byteFormat = outputStream.toByteArray();
                            imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

                        }

                        if (e_id == null){

                            AndroidNetworking
                                    .post("http://localhost/mvc/Equipment/saveEquip")
                                    .addBodyParameter("cate_name",""+ cate.getSelectedItem().toString())
                                    .addBodyParameter("name",""+ename.getText().toString())
                                    .addBodyParameter("description",""+descrip.getText().toString())
                                    .addBodyParameter("ImageUrl",""+imgString)
                                    .addBodyParameter("quantity",""+quan.getText().toString())
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
                                                    message("Warning","Data is not saved!\nPlease please try again.");
                                                }
                                                else {
                                                    pd.dismissWithAnimation();
                                                    sw.dismiss();
                                                    Su(res);
                                                    displayName();
                                                    myBitmap=null;
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
                                    .post("http://localhost/mvc/Equipment/updateEquip")
                                    .addBodyParameter("cate_name",""+ cate.getSelectedItem().toString())
                                    .addBodyParameter("name",""+ename.getText().toString())
                                    .addBodyParameter("description",""+descrip.getText().toString())
                                    .addBodyParameter("ImageUrl",""+imgString)
                                    .addBodyParameter("quantity",""+quan.getText().toString())
                                    .addBodyParameter("id",""+e_id)
                                    .setPriority(Priority.MEDIUM)
                                    .setTag("Update Data")
                                    .build()
                                    .getAsJSONArray(new JSONArrayRequestListener() {
                                        @Override
                                        public void onResponse(JSONArray jArr1) {

                                            try {
                                                JSONObject jObj1 = jArr1.getJSONObject(0);
                                                String res = jObj1.getString("result");
                                                if (res.equals("error")){
                                                    message("Warning","Data is not updated!\nPlease please try again.");
                                                }
                                                else {
                                                    pd.dismissWithAnimation();
                                                    sw.dismiss();
                                                    Su(res);
                                                    displayName();
                                                    myBitmap=null;
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


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        sw.setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sw.dismissWithAnimation();
                myBitmap=null;
            }
        });
        sw.setCancelable(false);
        sw.show();
    }

    public void swet(){
        pd = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pd.setTitleText("Loading ...");
        pd.setCancelable(false);
        pd.show();

    }
    public void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {"Select photo from gallery", "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), GALLERY);
                                ch ="1";

                                break;
                            case 1:

                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA);
                                ch ="1";
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    img.setImageBitmap(myBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {

            myBitmap = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(myBitmap);
        }
    }

    public void EnableRuntimePermissionToAccessCamera(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA))
        {
            Toast.makeText(this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, CAMERA);
        }
    }
    public void showAlertDialogDelete(final String id,String name) {
        final SweetAlertDialog sw = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sw.setContentText("You won't to delete " +name+"?");
        sw.setConfirmButton("Yes", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                swet();
                try {
                    AndroidNetworking
                            .post("http://localhost/mvc/Equipment/delete")
                            .addBodyParameter("id",""+id)
                            .setTag("Delete Data")
                            .build()
                            .getAsJSONArray(new JSONArrayRequestListener() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        JSONObject jObj1 = response.getJSONObject(0);
                                        String res = jObj1.getString("result");
                                        sw.dismiss();
                                        pd.dismissWithAnimation();
                                        Su(res);
                                        displayName();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                @Override
                                public void onError(ANError anError) {

                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
    public void Su(String res){
        final SweetAlertDialog sw = new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE);
        sw.setTitleText("Success!");
        sw.setContentText(res);
        sw.showCancelButton(false);
        sw.show();
    }
    public void message(String title, String message) {
        SweetAlertDialog sw = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        sw.setTitleText(title);
        sw.setContentText(message);
        sw.show();
    }


}
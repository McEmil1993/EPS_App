package com.example.eps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Loading extends AppCompatActivity {
    private int progressStatus = 0;
    private Handler handler = new Handler();
    private static NetworkInfo networkInfo;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        image = findViewById(R.id.image);

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(false);
        pDialog.show();
        progressStatus = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressStatus < 100){
                    progressStatus +=1;
                    try{
                        Thread.sleep(20);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            if(progressStatus == 100){
                                pDialog.dismiss();
                                checkInternetConenction();
                            }
                        }
                    });
                }
            }
        }).start();
    }
    private boolean checkInternetConenction() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() ==
                NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ) {
            image.setImageResource(R.drawable.ok_kayo);
            startActivity(new Intent(Loading.this,MainActivity.class));
            finish();
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() ==
                        NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                NetworkInfo.State.DISCONNECTED  ) {

            message1("Wala kay Internet!","Please turn-on your wifi or mobile data.");

            return false;
        }
        return false;
    }
    public void message1(String title, String message) {
        final SweetAlertDialog sw = new SweetAlertDialog(Loading.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        sw.setTitleText(title);
        sw.setContentText(message);
        sw.setCustomImage(R.drawable.laug1);
        sw.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sw.dismissWithAnimation();
                finishAffinity();
            }
        });
        sw.setCancelable(false);
        sw.show();
    }

}
/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.eps;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AlarmReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 0;
    DatabaseHelper dh;
    SQLiteDatabase db;
    Model mod;
    public AlarmReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        AndroidNetworking
                .post("http://localhost/mvc/Task/cSched")
                .setPriority(Priority.HIGH)
                .setTag("GEt Data")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            JSONObject jObj = response.getJSONObject(0);
                            String m = jObj.getString("1");
                            String q = jObj.getString("2");
                            String a = jObj.getString("3");

                            if (m.equals("1")) {
                                NotificationManager notificationManager = (NotificationManager)
                                        context.getSystemService(Context.NOTIFICATION_SERVICE);

                                //Create the content intent for the notification, which launches this activity
                                Intent contentIntent = new Intent(context, Building.class);
                                PendingIntent contentPendingIntent = PendingIntent.getActivity
                                        (context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                //Build the notification
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                                        .setSmallIcon(R.drawable.ic_baseline_check_circle_outline_24)
                                        .setContentTitle("Monthly")
                                        .setContentText("Please Check Task")
                                        .setContentIntent(contentPendingIntent)
                                        .setAutoCancel(false)
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setDefaults(NotificationCompat.DEFAULT_ALL);

                                //Deliver the notification
                                notificationManager.notify(NOTIFICATION_ID, builder.build());
                            }
                            if (q.equals("1")) {
                                NotificationManager notificationManager = (NotificationManager)
                                        context.getSystemService(Context.NOTIFICATION_SERVICE);

                                //Create the content intent for the notification, which launches this activity
                                Intent contentIntent = new Intent(context, Building.class);
                                PendingIntent contentPendingIntent = PendingIntent.getActivity
                                        (context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                //Build the notification
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                                        .setSmallIcon(R.drawable.ic_baseline_check_circle_outline_24)
                                        .setContentTitle("Quarterly")
                                        .setContentText("Please Check Task")
                                        .setContentIntent(contentPendingIntent)
                                        .setAutoCancel(false)
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setDefaults(NotificationCompat.DEFAULT_ALL);

                                //Deliver the notification
                                notificationManager.notify(NOTIFICATION_ID, builder.build());
                            }
                            if (a.equals("1")) {
                                NotificationManager notificationManager = (NotificationManager)
                                        context.getSystemService(Context.NOTIFICATION_SERVICE);

                                //Create the content intent for the notification, which launches this activity
                                Intent contentIntent = new Intent(context, Building.class);
                                PendingIntent contentPendingIntent = PendingIntent.getActivity
                                        (context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                //Build the notification
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                                        .setSmallIcon(R.drawable.ic_baseline_check_circle_outline_24)
                                        .setContentTitle("Annual")
                                        .setContentText("Please Check Task")
                                        .setContentIntent(contentPendingIntent)
                                        .setAutoCancel(false)
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setDefaults(NotificationCompat.DEFAULT_ALL);

                                //Deliver the notification
                                notificationManager.notify(NOTIFICATION_ID, builder.build());
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

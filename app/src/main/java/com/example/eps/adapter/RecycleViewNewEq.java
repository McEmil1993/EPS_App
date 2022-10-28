package com.example.eps.adapter;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eps.Equipment;
import com.example.eps.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;



public class RecycleViewNewEq extends RecyclerView.Adapter<RecycleViewNewEq.MyView> {
    ProgressDialog progressDialog;
    private List<String> list;
    private List<String> list1,imageV;
    Context context;
    public class MyView extends RecyclerView.ViewHolder {

        public TextView textView,product_attribute;
        public ImageView eq_image;
        public ProgressBar progressBar;
        CardView card_view;

        public MyView(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.textview1);
            product_attribute = (TextView) view.findViewById(R.id.product_attribute);
            eq_image = (ImageView) view.findViewById(R.id.eq_image);
            progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
            card_view =  view.findViewById(R.id.card_view);

        }
    }


    public RecycleViewNewEq(List<String> horizontalList,List<String> horizontalList1,List<String> imageV) {
        this.list = horizontalList;
        this.list1 = horizontalList1;
        this.imageV = imageV;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_new_home, parent, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {

        holder.textView.setText(list.get(position));
        holder.product_attribute.setText(list1.get(position));
        String url= imageV.get(position);
        Picasso.get()
                .load(url)
                .into(holder.eq_image, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                        message("Error",e.getMessage());
                    }
                });
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(), Equipment.class));
            }

        });
    }

    public void message(String title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(true);
        AlertDialog dialog1 = dialog.create();
        dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog1.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    public Context getContext(){

        return context;
    }
    public void setContext(Context context){
        this.context=context;
    }

}

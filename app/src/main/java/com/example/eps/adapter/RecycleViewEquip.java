package com.example.eps.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eps.Equipment;
import com.example.eps.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class RecycleViewEquip extends RecyclerView.Adapter<RecycleViewEquip.MyView> {


    private List<String> eq_title,eq_descrip,eq_id,e_image,cate_name,quantity;
    Context context;

    String etitle,edesrip,eimage,ecname;

    public class MyView extends RecyclerView.ViewHolder {
        public TextView eqpt_title,eqt_description,eqt_quan,eqt_cate_name,btnUpdate,btnDelete;
        public ImageView eqpt_image;
        public ProgressBar progressbar;
        public MyView(View view) {
            super(view);
            eqpt_title = (TextView) view.findViewById(R.id.eqpt_title);
            eqt_description = (TextView) view.findViewById(R.id.eqt_description);
            eqt_quan = (TextView) view.findViewById(R.id.eqt_quan);
            eqt_cate_name = (TextView) view.findViewById(R.id.eqt_cate_name);
            btnUpdate = (TextView) view.findViewById(R.id.btnUpdate);
            btnDelete = (TextView) view.findViewById(R.id.btndelete);
            eqpt_image = view.findViewById(R.id.eqpt_image);
            progressbar = view.findViewById(R.id.progressbar);

        }
    }



    public RecycleViewEquip(List<String> eq_title,List<String> eq_descrip,List<String> eq_id,List<String> eqpt_image,List<String> cate_name,List<String> quantity) {
        this.eq_title = eq_title;
        this.eq_descrip = eq_descrip;
        this.eq_id = eq_id;
        this.e_image = eqpt_image;
        this.cate_name = cate_name;
        this.quantity = quantity;

    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_equipment, parent, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, @SuppressLint("RecyclerView") final int position) {

        holder.eqpt_title.setText(eq_title.get(position));
        holder.eqt_description.setText("Desc : "+eq_descrip.get(position));
        holder.eqt_quan.setText("Quantity: "+quantity.get(position));
        holder.eqt_cate_name.setText("Category: "+cate_name.get(position));

        if (e_image.get(position).isEmpty())
        {
            holder.progressbar.setVisibility(View.GONE);
            holder.eqpt_image.setClipToOutline(true);
            holder.eqpt_image.setImageResource(R.drawable.no_image);

        }else {
            holder.eqpt_image.setClipToOutline(true);
            Picasso.get()
                    .load(e_image.get(position))
                    .into(holder.eqpt_image, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressbar.setVisibility(View.GONE);
                        }
                        @Override
                        public void onError(Exception e) {

                        }
                    });
        }
        holder.eqpt_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder= new AlertDialog.Builder(context);
                final View customLayout = LayoutInflater.from(context).inflate(R.layout.sw_dialog, null);
                builder.setView(customLayout);

                final AlertDialog dialog= builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.setCancelable(false);
                dialog.show();
                final  ImageView view_image = customLayout.findViewById(R.id.view_image);
                final  ProgressBar pr = customLayout.findViewById(R.id.progressbar);
                final TextView txtClose = customLayout.findViewById(R.id.txtClose);
                txtClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                view_image.setClipToOutline(true);
                Picasso.get()
                        .load(e_image.get(position))
                        .into(view_image, new Callback() {
                            @Override
                            public void onSuccess() {
                                pr.setVisibility(View.GONE);
                            }
                            @Override
                            public void onError(Exception e) {

                            }
                        });

            }
        });
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Equipment e = (Equipment) getContext();
                e.dialogAdd(eq_id.get(position),cate_name.get(position), eq_title.get(position),eq_descrip.get(position),e_image.get(position),quantity.get(position));
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Equipment e = (Equipment) getContext();
                e.showAlertDialogDelete(eq_id.get(position),eq_title.get(position));
            }
        });
    }

    public void message(String title, String message) {
        SweetAlertDialog sw = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        sw.setTitleText(title);
        sw.setContentText(message);
        sw.show();
    }
    @Override
    public int getItemCount() {
        return eq_id.size();
    }
    public Context getContext(){
        return context;
    }
    public void setContext(Context context){
        this.context=context;
    }

}


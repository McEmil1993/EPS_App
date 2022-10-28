package com.example.eps.adapter;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.example.eps.R;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyView> {

    private List<String> list,b_id;
    Context context;
    public class MyView extends RecyclerView.ViewHolder {

        public TextView textView;
        String _b_id;
        public MyView(View view) {
            super(view);

            textView = (TextView) view.findViewById(R.id.textview1);
            _b_id = "";

        }
    }


    public RecyclerViewAdapter(List<String> horizontalList,List<String> b_id) {
        this.list = horizontalList;
        this.b_id = b_id;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_category, parent, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {

        holder.textView.setText(list.get(position));
        holder._b_id = b_id.get(position);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public Context getContext(){
        return context;
    }
    public void setContext(Context context){
        this.context=context;
    }

}

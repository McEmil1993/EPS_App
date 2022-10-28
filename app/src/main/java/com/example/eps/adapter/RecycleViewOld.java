package com.example.eps.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eps.R;
import java.util.List;



public class RecycleViewOld extends RecyclerView.Adapter<RecycleViewOld.MyView> {

    private List<String> list;
    private List<String> list1;

    public class MyView extends RecyclerView.ViewHolder {

        public TextView textView,product_attribute;

        public MyView(View view) {
            super(view);

            textView = (TextView) view.findViewById(R.id.textview1);
            product_attribute = (TextView) view.findViewById(R.id.product_attribute);

        }
    }


    public RecycleViewOld(List<String> horizontalList,List<String> horizontalList1) {
        this.list = horizontalList;
        this.list1 = horizontalList1;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_old_home, parent, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {

        holder.textView.setText(list.get(position));
        holder.product_attribute.setText(list1.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

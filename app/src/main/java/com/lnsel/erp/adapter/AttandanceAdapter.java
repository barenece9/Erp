package com.lnsel.erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.lnsel.erp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AttandanceAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    public AttandanceAdapter(Context context,
                             ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }

    @Override
    public int getCount() {
       // return data.size();
        return 24;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View itemView = convertView;
        final MyViewHolder holder;
        if (itemView == null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.attandance_list_item, null, false);
            holder = new MyViewHolder();
           // holder.event_date=(TextView) itemView.findViewById(R.id.price);
            itemView.setTag(holder);
        } else {
            holder = (MyViewHolder) itemView.getTag();
        }
        //resultp = data.get(position);

        //holder.text_exam_name.setText(resultp.get("id"));

        return itemView;
    }
    private static class MyViewHolder {
        public TextView event_date;
    }


}
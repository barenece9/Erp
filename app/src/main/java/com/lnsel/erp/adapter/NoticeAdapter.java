package com.lnsel.erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lnsel.erp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by db on 2/23/2017.
 */
public class NoticeAdapter extends BaseAdapter {
    Context context;
    private static LayoutInflater inflater=null;
    ArrayList<HashMap<String,String>> notice_list;


    public NoticeAdapter(Context context, ArrayList<HashMap<String,String>> notice_list) {
        this.context = context;
        this.notice_list = notice_list;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public NoticeAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this.context=context;
        /*inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
    }

    @Override
    public int getCount() {
        return notice_list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView tv_heading,tv_body;
        ImageView img_notice;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.notice_list_item, null);
        holder.tv_heading = (TextView) rowView.findViewById(R.id.tv_heading);
        holder.tv_body=(TextView) rowView.findViewById(R.id.tv_body);

        holder.img_notice=(ImageView)rowView.findViewById(R.id.img_notice);


        holder.tv_heading.setText(notice_list.get(position).get("notice_heading"));
        holder.tv_body.setText(notice_list.get(position).get("notice_body"));


        return rowView;
    }


}
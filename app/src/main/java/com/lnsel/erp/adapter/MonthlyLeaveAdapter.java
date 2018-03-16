package com.lnsel.erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.lnsel.erp.R;
import com.lnsel.erp.settergetter.Holiday;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by db on 2/23/2017.
 */
public class MonthlyLeaveAdapter extends BaseAdapter {
    Context context;
    private static LayoutInflater inflater=null;
    ArrayList<HashMap<String,String>> opening_list;


    public MonthlyLeaveAdapter(Context context, ArrayList<HashMap<String,String>> opening_list) {
        this.context = context;
        this.opening_list = opening_list;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public MonthlyLeaveAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this.context=context;
        /*inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
    }

    @Override
    public int getCount() {
        return opening_list.size();
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
        TextView tv_type,tv_day,tv_date;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.opening_list_item, null);
        holder.tv_day = (TextView) rowView.findViewById(R.id.tv_day);
        holder.tv_type=(TextView) rowView.findViewById(R.id.tv_type);
        holder.tv_date=(TextView) rowView.findViewById(R.id.tv_date);
        holder.tv_date.setVisibility(View.VISIBLE);



        holder.tv_day.setText(opening_list.get(position).get("no_of_leave"));
        holder.tv_type.setText(opening_list.get(position).get("leave_type_name"));
        String month_name=getMonthForInt(Integer.valueOf(opening_list.get(position).get("sal_m")));
        holder.tv_date.setText(month_name+","+opening_list.get(position).get("sal_y"));


        return rowView;
    }

    String getMonthForInt(int m) {
        m=m-1;
        String month = "invalid";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (m >= 0 && m <= 11 ) {
            month = months[m];
        }
        return month;
    }


}
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

/**
 * Created by db on 2/23/2017.
 */
public class ClosingLeaveAdapter extends BaseAdapter {
    Context context;
    private static LayoutInflater inflater=null;
    ArrayList<HashMap<String,String>> opening_list;
    ArrayList<HashMap<String,String>> monthly_list;


    public ClosingLeaveAdapter(Context context, ArrayList<HashMap<String,String>> opening_list, ArrayList<HashMap<String,String>> monthly_list) {
        this.context = context;
        this.opening_list = opening_list;
        this.monthly_list = monthly_list;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ClosingLeaveAdapter(Context context) {
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
        TextView tv_type,tv_day;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.opening_list_item, null);
        holder.tv_day = (TextView) rowView.findViewById(R.id.tv_day);
        holder.tv_type=(TextView) rowView.findViewById(R.id.tv_type);

        //Float count=0.f;
        Float count=Float.valueOf(opening_list.get(position).get("no_of_leave"));

        System.out.println(opening_list.size()+"  ====Length====  "+monthly_list.size());

        for(int i=0;i<monthly_list.size();i++){
            if(monthly_list.get(i).get("mstr_leave_type_id").equalsIgnoreCase(opening_list.get(position).get("mstr_leave_type_id"))){
               // count=Float.valueOf(opening_list.get(position).get("no_of_leave"))- Float.valueOf(monthly_list.get(i).get("no_of_leave"));
                count=count- Float.valueOf(monthly_list.get(i).get("no_of_leave"));
            }
        }


        if(count>0){
            holder.tv_day.setText(String.valueOf(count));
        }else {
            holder.tv_day.setText("0");
        }
        holder.tv_type.setText(opening_list.get(position).get("leave_type_name"));


        return rowView;
    }


}
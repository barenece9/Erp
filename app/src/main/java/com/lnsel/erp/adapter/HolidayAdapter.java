package com.lnsel.erp.adapter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import com.lnsel.erp.R;
import com.lnsel.erp.settergetter.Holiday;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by db on 2/23/2017.
 */
public class HolidayAdapter extends BaseAdapter {
    Context context;
    private static LayoutInflater inflater=null;
    private List<Holiday> worldpopulationlist = null;
    private ArrayList<Holiday> arraylist;


    public HolidayAdapter(Context context,List<Holiday> worldpopulationlist) {
        this.context = context;
        this.worldpopulationlist = worldpopulationlist;
        this.arraylist = new ArrayList<Holiday>();
        this.arraylist.addAll(worldpopulationlist);

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public HolidayAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this.context=context;
        /*inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
    }

    @Override
    public int getCount() {
        return worldpopulationlist.size();
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
        TextView tv_day,tv_month,tv_holiday_type_name,tv_year;
        Button btn_call;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.holiday_list_item, null);
        holder.tv_day = (TextView) rowView.findViewById(R.id.tv_day);
        holder.tv_month=(TextView) rowView.findViewById(R.id.tv_month);
        holder.tv_year=(TextView) rowView.findViewById(R.id.tv_year);
        holder.tv_holiday_type_name=(TextView) rowView.findViewById(R.id.tv_holiday_type_name);


        holder.tv_day.setText(worldpopulationlist.get(position).getDay());
        holder.tv_month.setText(worldpopulationlist.get(position).getMonth());
        holder.tv_year.setText(worldpopulationlist.get(position).getYear());
        holder.tv_holiday_type_name.setText(worldpopulationlist.get(position).getHoliday_type_name());

        return rowView;
    }


    // Filter Class
    public void filter(String charText,View btn_clear) {
        charText = charText.toLowerCase(Locale.getDefault());
        worldpopulationlist.clear();
        if (charText.length() == 0||charText.equalsIgnoreCase("")) {
            worldpopulationlist.addAll(arraylist);
            btn_clear.setVisibility(View.GONE);
        }
        else
        {
            for (Holiday wp : arraylist)
            {
                if (wp.getDay().toLowerCase(Locale.getDefault()).contains(charText)||
                        wp.getMonth().toLowerCase(Locale.getDefault()).contains(charText)||
                        wp.getYear().toLowerCase(Locale.getDefault()).contains(charText)||
                        wp.getDay().toLowerCase(Locale.getDefault()).contains(charText)||
                        wp.getHoliday_type_name().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    worldpopulationlist.add(wp);
                }
            }

            btn_clear.setVisibility(View.VISIBLE);
        }
        notifyDataSetChanged();
    }


}
package com.lnsel.erp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lnsel.erp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class LeaveAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    public LeaveAdapter(Context context,
                        ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }

    @Override
    public int getCount() {
        return data.size();
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
            itemView = inflater.inflate(R.layout.leave_list_item_new, null, false);
            holder = new MyViewHolder();
            holder.rep_emp_name=(TextView) itemView.findViewById(R.id.rep_emp_name);
            holder.leave_frm_dt=(TextView) itemView.findViewById(R.id.leave_frm_dt);
            holder.leave_to_dt=(TextView) itemView.findViewById(R.id.leave_to_dt);
            holder.leave_frm_dt_st=(TextView) itemView.findViewById(R.id.leave_frm_dt_st);
            holder.leave_to_dt_st=(TextView) itemView.findViewById(R.id.leave_to_dt_st);
            holder.number_of_days=(TextView) itemView.findViewById(R.id.number_of_days);
            holder.leave_type_name=(TextView) itemView.findViewById(R.id.leave_type_name);
            holder.dt_created=(TextView) itemView.findViewById(R.id.dt_created);
            holder.approval_status_name=(TextView) itemView.findViewById(R.id.approval_status_name);

            holder.btn_open=(Button)itemView.findViewById(R.id.btn_open);
            holder.btn_close=(Button)itemView.findViewById(R.id.btn_close);
            holder.img_more=(ImageView)itemView.findViewById(R.id.img_more);

            itemView.setTag(holder);
        } else {
            holder = (MyViewHolder) itemView.getTag();
        }
        resultp = data.get(position);

        //holder.text_exam_name.setText(resultp.get("id"));
        holder.rep_emp_name.setText("REPORTING PERSON : "+resultp.get("rep_emp_name"));
        holder.leave_frm_dt.setText("LEAVE APPLICATION FROM DATE : "+resultp.get("leave_frm_dt"));
        holder.leave_to_dt.setText("LEAVE APPLICATION TO DATE : "+resultp.get("leave_to_dt"));
        holder.leave_frm_dt_st.setText(""+resultp.get("leave_frm_dt_st"));
        holder.leave_to_dt_st.setText(""+resultp.get("leave_to_dt_st"));
        holder.number_of_days.setText("TOTAL NO OF DAYS : "+resultp.get("number_of_days"));
        holder.leave_type_name.setText("LEAVE TYPE : "+resultp.get("leave_type_name"));
        holder.dt_created.setText("APPLIED ON : "+resultp.get("dt_created"));
        holder.approval_status_name.setText(""+resultp.get("approval_status_name"));

        holder.img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLeaveDialog(context,position,data);
            }
        });

        holder.btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.rep_emp_name.setVisibility(View.VISIBLE);
                holder.leave_frm_dt.setVisibility(View.VISIBLE);
                holder.leave_to_dt.setVisibility(View.VISIBLE);
                holder.leave_frm_dt_st.setVisibility(View.VISIBLE);
                holder.leave_to_dt_st.setVisibility(View.VISIBLE);
                holder.btn_open.setVisibility(View.GONE);
                holder.btn_close.setVisibility(View.VISIBLE);
            }
        });

        holder.btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.rep_emp_name.setVisibility(View.GONE);
                holder.leave_frm_dt.setVisibility(View.GONE);
                holder.leave_to_dt.setVisibility(View.GONE);
                holder.leave_frm_dt_st.setVisibility(View.GONE);
                holder.leave_to_dt_st.setVisibility(View.GONE);
                holder.btn_close.setVisibility(View.GONE);
                holder.btn_open.setVisibility(View.VISIBLE);
            }
        });

        return itemView;
    }
    private static class MyViewHolder {
       public Button btn_open,btn_close;
        public ImageView img_more;
        public TextView rep_emp_name,leave_frm_dt,leave_to_dt,leave_frm_dt_st,leave_to_dt_st,number_of_days,leave_type_name,dt_created,approval_status_name;
    }


    public static void showLeaveDialog(Context context,int position, ArrayList<HashMap<String, String>> data ){

        HashMap<String, String> resultp_dialog = new HashMap<String, String>();
        resultp_dialog = data.get(position);

        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_leave);

        TextView rep_emp_name,leave_frm_dt,leave_to_dt,leave_frm_dt_st,leave_to_dt_st,number_of_days,leave_type_name,dt_created,approval_status_name;

        rep_emp_name=(TextView) dialog.findViewById(R.id.rep_emp_name);
        leave_frm_dt=(TextView) dialog.findViewById(R.id.leave_frm_dt);
        leave_to_dt=(TextView) dialog.findViewById(R.id.leave_to_dt);
        leave_frm_dt_st=(TextView) dialog.findViewById(R.id.leave_frm_dt_st);
        leave_to_dt_st=(TextView) dialog.findViewById(R.id.leave_to_dt_st);
        number_of_days=(TextView) dialog.findViewById(R.id.number_of_days);
        leave_type_name=(TextView) dialog.findViewById(R.id.leave_type_name);
        dt_created=(TextView) dialog.findViewById(R.id.dt_created);
        approval_status_name=(TextView) dialog.findViewById(R.id.approval_status_name);



        rep_emp_name.setText("REPORTING PERSON : "+resultp_dialog.get("rep_emp_name"));
        leave_frm_dt.setText("LEAVE APPLICATION FROM DATE : "+resultp_dialog.get("leave_frm_dt"));
        leave_to_dt.setText("LEAVE APPLICATION TO DATE : "+resultp_dialog.get("leave_to_dt"));
        leave_frm_dt_st.setText("APPROVED LEAVE FROM DATE : "+resultp_dialog.get("leave_frm_dt_st"));
        leave_to_dt_st.setText("APPROVED LEAVE TO DATE : "+resultp_dialog.get("leave_to_dt_st"));
        number_of_days.setText("TOTAL NO OF DAYS : "+resultp_dialog.get("number_of_days"));
        leave_type_name.setText("LEAVE TYPE : "+resultp_dialog.get("leave_type_name"));
        dt_created.setText("APPLIED ON : "+resultp_dialog.get("dt_created"));
        approval_status_name.setText("APPROVAL STATUS : "+resultp_dialog.get("approval_status_name"));

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


}
package com.lnsel.erp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lnsel.erp.MainActivity;
import com.lnsel.erp.R;
import com.lnsel.erp.activity.ReviewLeaveActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ReviewLeaveAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    public ReviewLeaveAdapter(Context context,
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
            itemView = inflater.inflate(R.layout.review_leave_list_item_new, null, false);
            holder = new MyViewHolder();
            holder.emp_name=(TextView) itemView.findViewById(R.id.emp_name);
            holder.rep_emp_name=(TextView) itemView.findViewById(R.id.rep_emp_name);
            holder.leave_frm_dt=(TextView) itemView.findViewById(R.id.leave_frm_dt);
            holder.leave_to_dt=(TextView) itemView.findViewById(R.id.leave_to_dt);
           // holder.leave_frm_dt_st=(TextView) itemView.findViewById(R.id.leave_frm_dt_st);
          //  holder.leave_to_dt_st=(TextView) itemView.findViewById(R.id.leave_to_dt_st);
            holder.number_of_days=(TextView) itemView.findViewById(R.id.number_of_days);
            holder.leave_type_name=(TextView) itemView.findViewById(R.id.leave_type_name);
            holder.dt_created=(TextView) itemView.findViewById(R.id.dt_created);
            holder.approval_status_name=(TextView) itemView.findViewById(R.id.approval_status_name);

            holder.btn_action=(Button)itemView.findViewById(R.id.btn_action);
            holder.img_more=(ImageView)itemView.findViewById(R.id.img_more);



            itemView.setTag(holder);
        } else {
            holder = (MyViewHolder) itemView.getTag();
        }
        resultp = data.get(position);

        //holder.text_exam_name.setText(resultp.get("id"));
        holder.emp_name.setText(""+resultp.get("employee_name"));
        holder.rep_emp_name.setText("REPORTING PERSON : "+resultp.get("rep_emp_name"));
        holder.leave_frm_dt.setText("LEAVE APPLICATION FROM DATE : "+resultp.get("leave_frm_dt"));
        holder.leave_to_dt.setText("LEAVE APPLICATION TO DATE : "+resultp.get("leave_to_dt"));
      //  holder.leave_frm_dt_st.setText(""+resultp.get("leave_frm_dt_st"));
       // holder.leave_to_dt_st.setText(""+resultp.get("leave_to_dt_st"));
        holder.number_of_days.setText(""+resultp.get("number_of_days"));
        holder.leave_type_name.setText("LEAVE TYPE : "+resultp.get("leave_type_name"));
        holder.dt_created.setText(""+resultp.get("dt_created"));
        holder.approval_status_name.setText(""+resultp.get("approval_status_name"));

        holder.btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("Position =======================  "+position);
                Intent intent=new Intent(context, ReviewLeaveActivity.class);
                intent.putExtra("leave_application_tbl_id",data.get(position).get("leave_application_tbl_id"));
                System.out.println("@@@@@@@@@@@@@@ leave_application_tbl_id : "+data.get(position).get("leave_application_tbl_id"));
                context.startActivity(intent);
                // context.startActivity(new Intent(context, ReviewLeaveActivity.class));
            }
        });

        holder.img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Position =======================  "+position);
                Intent intent=new Intent(context, ReviewLeaveActivity.class);
                intent.putExtra("leave_application_tbl_id",data.get(position).get("leave_application_tbl_id"));
                System.out.println("@@@@@@@@@@@@@@ leave_application_tbl_id : "+data.get(position).get("leave_application_tbl_id"));
                context.startActivity(intent);
            }
        });

        return itemView;
    }
    private static class MyViewHolder {
        public TextView emp_name,rep_emp_name,leave_frm_dt,leave_to_dt,leave_frm_dt_st,leave_to_dt_st,number_of_days,leave_type_name,dt_created,approval_status_name;
        public Button btn_action;
        public ImageView img_more;
    }


}
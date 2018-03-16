package com.lnsel.erp.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lnsel.erp.R;
import com.lnsel.erp.appconroller.AppController;
import com.lnsel.erp.constant.Sharepreferences;
import com.lnsel.erp.constant.Webservice;
import com.lnsel.erp.other.CreateDialog;
import com.lnsel.erp.settergetter.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ApplyLeaveActivity extends Activity {


    Button btn_frm_date,btn_to_date,btn_back,btn_submit;
    TextView tv_frm_date,tv_to_date;
    EditText etn_reason_of_leave;
    Spinner spinner_leave_type;
    String frm_date="",to_date="",reason_of_leave="",leave_type="",leave_id="",no_of_days="";
    ArrayAdapter<String> leave_typeAdapter;
    ArrayList<String> leave_typeList = new ArrayList<String>();
    ArrayList<String> leave_typeId = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave);
        btn_frm_date=(Button)findViewById(R.id.btn_frm_date);
        btn_to_date=(Button)findViewById(R.id.btn_to_date);
        btn_back=(Button)findViewById(R.id.btn_back);
        btn_submit=(Button)findViewById(R.id.btn_submit);

        tv_frm_date=(TextView)findViewById(R.id.tv_frm_date);
        tv_to_date=(TextView)findViewById(R.id.tv_to_date);
        etn_reason_of_leave=(EditText)findViewById(R.id.etn_reason_of_leave);
        spinner_leave_type=(Spinner)findViewById(R.id.spinner_leave_type);

        spinner_leave_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                leave_id = leave_typeId.get(position);
                leave_type=leave_typeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        /*leave_typeList.clear();
        leave_typeList.add("-SELECT-");
        leave_typeList.add("CASUAL LEAVE");
        leave_typeList.add("SPECIAL LEAVE");*/
        leave_typeAdapter = new ArrayAdapter<String>(ApplyLeaveActivity.this,R.layout.spinner_rows, leave_typeList);
        leave_typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_leave_type.setAdapter(leave_typeAdapter);

        btn_frm_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrmDateDialog();
            }
        });

        btn_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(frm_date.equalsIgnoreCase("")){
                    CreateDialog.showDialog(ApplyLeaveActivity.this,"first select leave from date");
                }else {
                    ToDateDialog();
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button header_btn_back=(Button)findViewById(R.id.header_btn_back);
        header_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView header_text_title=(TextView)findViewById(R.id.header_text_title);
        header_text_title.setText("Apply Leave");

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reason_of_leave=etn_reason_of_leave.getText().toString();
                if(frm_date.equalsIgnoreCase("")){
                    CreateDialog.showDialog(ApplyLeaveActivity.this,"select leave from date");
                }else if(to_date.equalsIgnoreCase("")){
                    CreateDialog.showDialog(ApplyLeaveActivity.this,"select leave to date");
                }else if(convertdate(frm_date).after(convertdate(to_date))){
                    CreateDialog.showDialog(ApplyLeaveActivity.this,"leave-to date can not be before leave-from date");
                }else if(leave_type.equalsIgnoreCase("")||leave_type.equalsIgnoreCase("-Select-")){
                    CreateDialog.showDialog(ApplyLeaveActivity.this,"select leave type");
                }else if(reason_of_leave.equalsIgnoreCase("")){
                    CreateDialog.showDialog(ApplyLeaveActivity.this,"reason of leave can not be blank");
                }else {
                    /*if (leave_type.equalsIgnoreCase("CASUAL LEAVE")){
                        leave_id="2";
                        ApplyLeave();
                    }else {
                        leave_id="5";
                        ApplyLeave();
                    }*/
                    ApplyLeave();
                }
            }
        });

        doLeaveType();
    }

    public void FrmDateDialog(){

        /*SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        long max_date=0;
        try {
            Date date1 = myFormat.parse(to_date);
            max_date = date1.getTime();
            // System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(ApplyLeaveActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                DecimalFormat mFormat= new DecimalFormat("00");
                mFormat.setRoundingMode(RoundingMode.DOWN);
                selectedmonth = selectedmonth + 1;
                String select_date =  selectedyear + "-" +  mFormat.format(Double.valueOf(selectedmonth)) + "-" +  mFormat.format(Double.valueOf(selectedday));
                tv_frm_date.setText(select_date);
                frm_date=select_date;
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select Date");
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
      //  mDatePicker.getDatePicker().setMaxDate(max_date);
        mDatePicker.show();
    }

    public void ToDateDialog(){

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        long mim_date=0;
        try {
            Date date1 = myFormat.parse(frm_date);
            mim_date = date1.getTime();
            // System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(ApplyLeaveActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                DecimalFormat mFormat= new DecimalFormat("00");
                mFormat.setRoundingMode(RoundingMode.DOWN);
                selectedmonth = selectedmonth + 1;
                String select_date =  selectedyear + "-" +  mFormat.format(Double.valueOf(selectedmonth)) + "-" +  mFormat.format(Double.valueOf(selectedday));
                tv_to_date.setText(select_date);
                to_date=select_date;
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select Date");
       // mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        mDatePicker.getDatePicker().setMinDate(mim_date);
        mDatePicker.show();
    }

    public void ApplyLeave(){

        final ProgressDialog progressDialog=new ProgressDialog(ApplyLeaveActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = Webservice.APPLY_LEAVE;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        JSONresponse(response);
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        System.out.println("Error=========="+error);
                        Toast.makeText(ApplyLeaveActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                NoOfDay();
                String create_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String update_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());   //("yyyy/MM/dd HH:mm:ss")

                UserInfo rememberData= Sharepreferences.getUserinfo(ApplyLeaveActivity.this);
                Map<String, String>  params = new HashMap<String, String>();
                params.put("employee_id",rememberData.getUserId());
                params.put("leave_frm_dt",frm_date);
                params.put("leave_to_dt",to_date);
                params.put("leave_frm_dt_st",frm_date);
                params.put("leave_to_dt_st",to_date);
                params.put("number_of_days",no_of_days);
                params.put("mstr_leave_type_id",leave_id);
                params.put("reason_of_leave",reason_of_leave);
                params.put("lk_approval_status_id","1");
                params.put("reporting_emp_id",rememberData.getReporting_emp_id());
                params.put("dt_created",create_date);
                params.put("dt_updated",update_date);
                params.put("update_by_emp_id",rememberData.getUserId());
                params.put("is_deleted","0");


                Log.d("employee_id",rememberData.getUserId());
                Log.d("leave_frm_dt",frm_date);
                Log.d("leave_to_dt",to_date);
                Log.d("leave_frm_dt_st",frm_date);
                Log.d("leave_to_dt_st",to_date);
                Log.d("number_of_days",no_of_days);
                Log.d("mstr_leave_type_id",leave_id);
                Log.d("reason_of_leave",reason_of_leave);
                Log.d("lk_approval_status_id","1");
                Log.d("reporting_emp_id",rememberData.getReporting_emp_id());
                Log.d("dt_created",create_date);
                Log.d("dt_updated",update_date);
                Log.d("update_by_emp_id",rememberData.getUserId());
                Log.d("is_deleted","0");

                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
    }

    public void JSONresponse(String responsedata){

        try {
            JSONObject parentObj = new JSONObject(responsedata);
            String status=parentObj.optString("status");
            if(status.equalsIgnoreCase("true")){
                String response=parentObj.getString("response");
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                UserInfo rememberData= Sharepreferences.getUserinfo(ApplyLeaveActivity.this);
                if(rememberData.getReporting_person().equalsIgnoreCase("1")) {
                    startActivity(new Intent(ApplyLeaveActivity.this, HomeScreen.class));
                    finish();
                }else {
                    startActivity(new Intent(ApplyLeaveActivity.this, HomeScreen_2.class));
                    finish();
                }

            }else {
                String error_msg=parentObj.getString("response");
                CreateDialog.showDialog(ApplyLeaveActivity.this,error_msg);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void NoOfDay(){
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = myFormat.parse(frm_date);
            Date date2 = myFormat.parse(to_date);
            long diff = date2.getTime() - date1.getTime();
            no_of_days=String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            int days=Integer.valueOf(no_of_days)+1;
            no_of_days=String.valueOf(days);
            System.out.println("no_of_days $$$$$$$$$$ : "+no_of_days);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    void doLeaveType()
    {
        final ProgressDialog progressDialog4=new ProgressDialog(ApplyLeaveActivity.this);
        progressDialog4.setMessage("loading...");
        progressDialog4.show();
        progressDialog4.setCancelable(false);
        progressDialog4.setCanceledOnTouchOutside(false);
        String url;

        url =Webservice.LEAVE_TYPE;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        parseLeaveType(response);
                        if(progressDialog4!=null)
                            progressDialog4.dismiss();
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(progressDialog4!=null)
                            progressDialog4.dismiss();
                        System.out.println("Error=========="+error);
                        //doStateList(countryid);
                        Toast.makeText(ApplyLeaveActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void parseLeaveType(String response)
    {
        try {

            JSONObject parentObj = new JSONObject(response);

            JSONArray jArray = parentObj.getJSONArray("result");

            if(leave_typeList.size()>0){

                leave_typeList.clear();
                leave_typeId.clear();
            }

            leave_typeList.add("-Select-");
            leave_typeId.add("-1");

            for(int i=0;i<jArray.length();i++){

                JSONObject records = jArray.optJSONObject(i);
                //JSONObject records = innerObj.optJSONObject("records");

                leave_typeList.add(records.optString("leave_type_name"));
                leave_typeId.add(records.optString("mstr_leave_type_id"));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        leave_typeAdapter.notifyDataSetChanged();
        //int position = leave_typeId.indexOf(mstr_leave_type_id);
       // spinner_leave_type.setSelection(position);
    }


    public Date convertdate(String date_str){
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date=null;
        try {
            date = myFormat.parse(date_str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}

package com.lnsel.erp.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lnsel.erp.R;
import com.lnsel.erp.adapter.ClosingLeaveAdapter;
import com.lnsel.erp.adapter.OpeningLeaveAdapter;
import com.lnsel.erp.adapter.ReviewLeaveAdapter;
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

public class ReviewLeaveActivity extends Activity {


    Button btn_frm_date,btn_to_date,btn_back,btn_submit;
    TextView tv_frm_date,tv_to_date,tv_emp_name,tv_frm_date_app,tv_to_date_app;
    EditText etn_reason_of_leave;
    Spinner spinner_leave_type,spinner_leave_approved_status;
    String frm_date="",to_date="",reason_of_leave="",leave_type="",leave_id="",no_of_days="",approval_type="",approval_id="";

    ArrayAdapter<String> leave_typeAdapter;
    ArrayList<String> leave_typeList = new ArrayList<String>();
    ArrayList<String> leave_typeId = new ArrayList<String>();

    ArrayAdapter<String> approval_typeAdapter;
    ArrayList<String> approval_typeList = new ArrayList<String>();
    ArrayList<String> approval_typeId = new ArrayList<String>();

    String mstr_leave_type_id="";
    String lk_approval_status_id="";
    String leave_application_tbl_id="";



    String employee_id="";
    String fin_year_identifier="16041703";
    ListView list_opening_balance,list_closing_balance;
    ArrayList<HashMap<String,String>> opening_list=new ArrayList<>();
    ArrayList<HashMap<String,String>> closing_list=new ArrayList<>();
    ArrayList<HashMap<String,String>> monthly_list=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_leave);
        Bundle bundle = getIntent().getExtras();
        leave_application_tbl_id = bundle.getString("leave_application_tbl_id");
        btn_frm_date=(Button)findViewById(R.id.btn_frm_date);
        btn_to_date=(Button)findViewById(R.id.btn_to_date);
        btn_back=(Button)findViewById(R.id.btn_back);
        btn_submit=(Button)findViewById(R.id.btn_submit);

        tv_frm_date=(TextView)findViewById(R.id.tv_frm_date);
        tv_to_date=(TextView)findViewById(R.id.tv_to_date);
        ///////////////////////
        tv_emp_name=(TextView)findViewById(R.id.tv_emp_name);
        tv_frm_date_app=(TextView)findViewById(R.id.tv_frm_date_app);
        tv_to_date_app=(TextView)findViewById(R.id.tv_to_date_app);
        spinner_leave_approved_status=(Spinner)findViewById(R.id.spinner_leave_approved_status);

        list_closing_balance=(ListView)findViewById(R.id.list_closing_balance);

        spinner_leave_approved_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                approval_id = approval_typeId.get(position);
                approval_type=approval_typeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });


        approval_typeAdapter = new ArrayAdapter<String>(ReviewLeaveActivity.this,R.layout.spinner_rows, approval_typeList);
        approval_typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_leave_approved_status.setAdapter(approval_typeAdapter);


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

        leave_typeAdapter = new ArrayAdapter<String>(ReviewLeaveActivity.this,R.layout.spinner_rows, leave_typeList);
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
                    CreateDialog.showDialog(ReviewLeaveActivity.this,"first select leave from date");
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
        header_text_title.setText("Review Leave Application");

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reason_of_leave=etn_reason_of_leave.getText().toString();
                if(frm_date.equalsIgnoreCase("")){
                    CreateDialog.showDialog(ReviewLeaveActivity.this,"select approve leave from date");
                }else if(to_date.equalsIgnoreCase("")){
                    CreateDialog.showDialog(ReviewLeaveActivity.this,"select approve leave to date");
                }else if(convertdate(frm_date).after(convertdate(to_date))){
                    CreateDialog.showDialog(ReviewLeaveActivity.this,"leave-to date can not be before leave-from date");
                }else if(leave_type.equalsIgnoreCase("")||leave_type.equalsIgnoreCase("-SELECT-")){
                    CreateDialog.showDialog(ReviewLeaveActivity.this,"select leave type");
                }else if(reason_of_leave.equalsIgnoreCase("")){
                    CreateDialog.showDialog(ReviewLeaveActivity.this,"reason of leave can not be blank");
                }else if(approval_type.equalsIgnoreCase("")||approval_type.equalsIgnoreCase("-SELECT-")){
                    CreateDialog.showDialog(ReviewLeaveActivity.this,"select approval status");
                }else {
                    ReviewLeave();
                   // NoOfDay();
                }
            }
        });
        doReviewLeave();
    }

    public void FrmDateDialog(){
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(ReviewLeaveActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        mDatePicker = new DatePickerDialog(ReviewLeaveActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    public void ReviewLeave(){

        final ProgressDialog progressDialog=new ProgressDialog(ReviewLeaveActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = Webservice.UPDATE_REVIEW_LEAVE;

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
                        Toast.makeText(ReviewLeaveActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                NoOfDay();
                String create_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String update_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());   //("yyyy/MM/dd HH:mm:ss")

                UserInfo rememberData= Sharepreferences.getUserinfo(ReviewLeaveActivity.this);
                Map<String, String>  params = new HashMap<String, String>();

                params.put("leave_application_tbl_id",leave_application_tbl_id);
                params.put("leave_frm_dt_st",frm_date);
                params.put("leave_to_dt_st",to_date);
                params.put("number_of_days",no_of_days);
                params.put("mstr_leave_type_id",leave_id);
                params.put("reason_of_leave",reason_of_leave);
                params.put("lk_approval_status_id",approval_id);
                params.put("dt_updated",update_date);
                params.put("update_by_emp_id",rememberData.getUserId());
                params.put("is_deleted","0");


                Log.d("leave_application_id",leave_application_tbl_id);
                Log.d("leave_frm_dt_st",frm_date);
                Log.d("leave_to_dt_st",to_date);
                Log.d("number_of_days",no_of_days);
                Log.d("mstr_leave_type_id",leave_id);
                Log.d("reason_of_leave",reason_of_leave);
                Log.d("lk_approval_status_id",approval_id);
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
                //finish();
                startActivity(new Intent(ReviewLeaveActivity.this,HomeScreen.class));
                finish();

            }else {
                String error_msg=parentObj.getString("response");
                CreateDialog.showDialog(ReviewLeaveActivity.this,error_msg);
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



    public void doReviewLeave(){
        final ProgressDialog progressDialog=new ProgressDialog(ReviewLeaveActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = Webservice.REVIEW_LEAVE_DETAILS;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        JSONLeaveDetails(response);
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
                        Toast.makeText(ReviewLeaveActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("leave_application_tbl_id",leave_application_tbl_id);
                System.out.println("############### leave_application_tbl_id : "+leave_application_tbl_id);
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
    }

    public void JSONLeaveDetails(String responsedata){

        try {
            JSONObject parentObj = new JSONObject(responsedata);
            String status=parentObj.optString("status");
            if(status.equalsIgnoreCase("true")){
                JSONArray result=parentObj.getJSONArray("result");
                for(int i=0;i<result.length();i++){
                    JSONObject obj=result.getJSONObject(i);
                    HashMap<String,String> item=new HashMap<>();
                    item.put("leave_application_tbl_id",obj.getString("leave_application_tbl_id"));
                    item.put("leave_frm_dt",obj.getString("leave_frm_dt"));

                    tv_frm_date.setText(obj.getString("leave_frm_dt_st"));
                    tv_to_date.setText(obj.getString("leave_to_dt_st"));

                    to_date=obj.getString("leave_to_dt_st");
                    frm_date=obj.getString("leave_frm_dt_st");

                    tv_frm_date_app.setText(obj.getString("leave_frm_dt"));
                    tv_to_date_app.setText(obj.getString("leave_to_dt"));


                    etn_reason_of_leave.setText(obj.getString("reason_of_leave"));
                    tv_emp_name.setText(obj.getString("employee_name"));
                    employee_id=obj.getString("employee_id");

                    mstr_leave_type_id=obj.getString("mstr_leave_type_id");
                    lk_approval_status_id=obj.getString("lk_approval_status_id");

                    item.put("leave_to_dt",obj.getString("leave_to_dt"));
                    item.put("leave_frm_dt_st",obj.getString("leave_frm_dt_st"));
                    item.put("leave_to_dt_st",obj.getString("leave_to_dt_st"));
                    item.put("number_of_days",obj.getString("number_of_days"));
                    item.put("reason_of_leave",obj.getString("reason_of_leave"));
                    item.put("dt_created",obj.getString("dt_created"));
                    item.put("leave_type_name",obj.getString("leave_type_name"));
                    item.put("rep_emp_name",obj.getString("rep_emp_name"));
                    item.put("approval_status_name",obj.getString("approval_status_name"));
                    item.put("mstr_leave_type_id",obj.getString("mstr_leave_type_id"));
                    item.put("employee_name",obj.getString("employee_name"));


                   // list.add(item);
                }

               // doLeaveapprovalType();
                doLeaveDetails(employee_id);

            }else {
                String error_msg=parentObj.getString("response");
                CreateDialog.showDialog(ReviewLeaveActivity.this,error_msg);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    //==================================== Get Country List ========================
    void doLeaveapprovalType()
    {
        final ProgressDialog progressDialog2=new ProgressDialog(ReviewLeaveActivity.this);
        progressDialog2.setMessage("loading...");
        progressDialog2.show();
        progressDialog2.setCancelable(false);
        progressDialog2.setCanceledOnTouchOutside(false);
        String url;

        url =Webservice.LEAVE_APPROVAL_STATUS;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        parse_LEAVE_APPROVAL_STATUS(response);
                        if(progressDialog2!=null)
                            progressDialog2.dismiss();
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(progressDialog2!=null)
                            progressDialog2.dismiss();
                        System.out.println("Error=========="+error);
                        Toast.makeText(ReviewLeaveActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                //params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void parse_LEAVE_APPROVAL_STATUS(String response)
    {
        try {

            if(approval_typeList.size()>0){

                approval_typeList.clear();
                approval_typeId.clear();
            }

            JSONObject parentObj = new JSONObject(response);

            JSONArray jArray = parentObj.getJSONArray("result");

            approval_typeList.add("-Select-");
            approval_typeId.add("-1");

            for(int i=0;i<jArray.length();i++){

                JSONObject records = jArray.optJSONObject(i);
                //JSONObject records = innerObj.optJSONObject("records");

                approval_typeList.add(records.optString("approval_status_name"));
                approval_typeId.add(records.optString("lk_approval_status_id"));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        approval_typeAdapter.notifyDataSetChanged();
        int position = approval_typeId.indexOf(lk_approval_status_id);
        spinner_leave_approved_status.setSelection(position);
        doLeaveType();
    }

    //================================== Get state data ====================================

    void doLeaveType()
    {
        final ProgressDialog progressDialog4=new ProgressDialog(ReviewLeaveActivity.this);
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
                        Toast.makeText(ReviewLeaveActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
        int position = leave_typeId.indexOf(mstr_leave_type_id);
        spinner_leave_type.setSelection(position);
    }



    public void doLeaveDetails(final String employee_id){
        final ProgressDialog progressDialog=new ProgressDialog(ReviewLeaveActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = Webservice.LEAVE_CALCULATION;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        JSONresponseLeave(response);
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
                        Toast.makeText(ReviewLeaveActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                UserInfo rememberData= Sharepreferences.getUserinfo(ReviewLeaveActivity.this);
                Map<String, String>  params = new HashMap<String, String>();
                params.put("employee_id",employee_id);
                params.put("fin_year_identifier",rememberData.getFin_year_identifier());
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
    }

    public void JSONresponseLeave(String responsedata){

        opening_list.clear();
        closing_list.clear();
        monthly_list.clear();
        try {
            JSONObject parentObj = new JSONObject(responsedata);
            String status=parentObj.optString("status");
            if(status.equalsIgnoreCase("true")){
                JSONArray earnings=parentObj.getJSONArray("earnings");
                for(int i=0;i<earnings.length();i++){
                    JSONObject obj_earnings=earnings.getJSONObject(i);
                    HashMap<String,String> list =new HashMap<>();
                    list.put("mstr_leave_type_id",obj_earnings.getString("mstr_leave_type_id"));
                    list.put("leave_type_name",obj_earnings.getString("leave_type_name"));
                    list.put("no_of_leave",obj_earnings.getString("no_of_leave"));
                    opening_list.add(list);
                }

                JSONArray expenditure=parentObj.getJSONArray("expenditure");
                for(int i=0;i<expenditure.length();i++){
                    JSONObject obj_expenditure=expenditure.getJSONObject(i);
                    HashMap<String,String> list =new HashMap<>();
                    list.put("mstr_leave_type_id",obj_expenditure.getString("mstr_leave_type_id"));
                    list.put("leave_type_name",obj_expenditure.getString("leave_type_name"));
                    list.put("no_of_leave",obj_expenditure.getString("no_of_leave"));
                    list.put("sal_m",obj_expenditure.getString("sal_m"));
                    list.put("sal_y",obj_expenditure.getString("sal_y"));
                    monthly_list.add(list);
                }

            }else {
                String error_msg=parentObj.getString("response");
                CreateDialog.showDialog(ReviewLeaveActivity.this,error_msg);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

       // OpeningLeaveAdapter openingLeaveAdapter = new OpeningLeaveAdapter(ReviewLeaveActivity.this, opening_list);
       // list_opening_balance.setAdapter(openingLeaveAdapter);

        //  MonthlyLeaveAdapter monthlyLeaveAdapter = new MonthlyLeaveAdapter(getActivity(), monthly_list);
        // list_monthly_balance.setAdapter(monthlyLeaveAdapter);

        ClosingLeaveAdapter closingLeaveAdapter = new ClosingLeaveAdapter(ReviewLeaveActivity.this, opening_list,monthly_list);
        list_closing_balance.setAdapter(closingLeaveAdapter);

        doLeaveapprovalType();
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

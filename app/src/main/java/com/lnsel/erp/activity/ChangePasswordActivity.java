package com.lnsel.erp.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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

public class ChangePasswordActivity extends Activity {


    Button btn_back,btn_submit;
    EditText etn_old_password,etn_new_password,etn_confirm_password;

    String old_password="",new_password="",confirm_password="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        btn_back=(Button)findViewById(R.id.btn_back);
        btn_submit=(Button)findViewById(R.id.btn_submit);


        etn_old_password=(EditText)findViewById(R.id.etn_old_password);
        etn_new_password=(EditText)findViewById(R.id.etn_new_password);
        etn_confirm_password=(EditText)findViewById(R.id.etn_confirm_password);


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
        header_text_title.setText("Change Password");

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                old_password=etn_old_password.getText().toString();
                new_password=etn_new_password.getText().toString();
                confirm_password=etn_confirm_password.getText().toString();

                if(old_password.equalsIgnoreCase("")){
                    CreateDialog.showDialog(ChangePasswordActivity.this,"enter old password");
                }else if(new_password.equalsIgnoreCase("")){
                    CreateDialog.showDialog(ChangePasswordActivity.this,"enter new password");
                }else if(confirm_password.equalsIgnoreCase("")){
                    CreateDialog.showDialog(ChangePasswordActivity.this,"enter confirm password");
                }else if(!new_password.equalsIgnoreCase(confirm_password)){
                    CreateDialog.showDialog(ChangePasswordActivity.this,"confirm password not match");
                }else {
                    ChangePassword();
                }
            }
        });

    }


    public void ChangePassword(){

        final ProgressDialog progressDialog=new ProgressDialog(ChangePasswordActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = Webservice.CHANGE_PASSWORD;

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
                        Toast.makeText(ChangePasswordActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                UserInfo rememberData= Sharepreferences.getUserinfo(ChangePasswordActivity.this);
                Map<String, String>  params = new HashMap<String, String>();
                params.put("employee_id",rememberData.getUserId());
                params.put("old_password",old_password);
                params.put("new_password",new_password);
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

                UserInfo rememberData= Sharepreferences.getUserinfo(ChangePasswordActivity.this);
                if(rememberData.getReporting_person().equalsIgnoreCase("1")) {
                    startActivity(new Intent(ChangePasswordActivity.this, HomeScreen.class));
                    finish();
                }else {
                    startActivity(new Intent(ChangePasswordActivity.this, HomeScreen_2.class));
                    finish();
                }

            }else {
                String error_msg=parentObj.getString("response");
                CreateDialog.showDialog(ChangePasswordActivity.this,error_msg);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

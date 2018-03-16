package com.lnsel.erp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends Activity {

    Button btn_login;
    EditText etn_user,etn_password;
    String user="",password="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_screen);
        etn_user=(EditText)findViewById(R.id.etn_user);
        etn_password=(EditText)findViewById(R.id.etn_password);
        btn_login=(Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user=etn_user.getText().toString();
                password=etn_password.getText().toString();
                if(user.equalsIgnoreCase("")){
                    CreateDialog.showDialog(LoginScreen.this,"User Id can not be blank");
                }else if(password.equalsIgnoreCase("")){
                    CreateDialog.showDialog(LoginScreen.this,"Password can not be blank");
                }else {
                    doLogin();
                }
            }
        });
    }
    public void doLogin(){

        final ProgressDialog progressDialog=new ProgressDialog(LoginScreen.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = Webservice.EMP_LOGIN;

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
                        Toast.makeText(LoginScreen.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username",user);
                params.put("password",password);
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
                JSONObject response=parentObj.getJSONObject("response");
                String name=response.getString("name");
                String user_id=response.getString("id");
                String user_type=response.getString("user_type");
                String reporting_emp_id=response.getString("reporting_emp_id");
                String reporting_person=response.getString("reporting_person");
                String fin_year_identifier=response.getString("fin_year_identifier");
                String payroll_mstr_holiday_category_id=response.getString("payroll_mstr_holiday_category_id");
                Boolean login=true;
                Sharepreferences.setUserinfo(LoginScreen.this,login,user_id,name,user_type,payroll_mstr_holiday_category_id,reporting_emp_id,fin_year_identifier,reporting_person);

                if (reporting_person.equalsIgnoreCase("1")) {
                    startActivity(new Intent(LoginScreen.this, HomeScreen.class));
                    finish();
                }else {
                    startActivity(new Intent(LoginScreen.this, HomeScreen_2.class));
                    finish();
                }
            }else {
                String error_msg=parentObj.getString("response");
                CreateDialog.showDialog(LoginScreen.this,error_msg);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

package com.lnsel.erp.constant;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.lnsel.erp.settergetter.UserInfo;


public class Sharepreferences {


	private static String preferanceRemember = "RememberMe";
	private static String preferanceSaveUserInfo = "SaveUserInfo";


	/*public static void setRememberMe(Context context,String userName , String password , int remember)
	{
		SharedPreferences pref = context.getSharedPreferences(preferanceRemember, context.MODE_PRIVATE); 
		Editor editor = pref.edit(); 

		editor.putString("userName", userName); 
		editor.putString("password",password); 
		editor.putInt("remember", remember);   

		editor.commit();
	}

	public static RememberData getRememberMe(Context context)
	{
		RememberData rememberData = new RememberData();

		SharedPreferences pref = context.getSharedPreferences(preferanceRemember, context.MODE_PRIVATE);

		rememberData.setUserName(pref.getString("userName", ""));
		rememberData.setPassword(pref.getString("password", ""));
		rememberData.setRemember(pref.getInt("remember", 0)); 

		return rememberData; 
	}*/


	public static void setUserinfo(Context context,Boolean login,String userid,String username,String usertype,String payroll_mstr_holiday_category_id,String reporting_emp_id,String fin_year_identifier,String reporting_person )
	{
		SharedPreferences pref = context.getSharedPreferences(preferanceSaveUserInfo, context.MODE_PRIVATE);
		Editor editor = pref.edit();

		editor.putBoolean("login",login);
		editor.putString("userid",userid);
		editor.putString("username",username);
		editor.putString("usertype",usertype);
		editor.putString("payroll_mstr_holiday_category_id",payroll_mstr_holiday_category_id);
		editor.putString("reporting_emp_id",reporting_emp_id);
		editor.putString("fin_year_identifier",fin_year_identifier);
		editor.putString("reporting_person",reporting_person);

		editor.commit();
	}

	public static UserInfo getUserinfo(Context context)
	{
		UserInfo userInfo = new UserInfo();
		SharedPreferences pref = context.getSharedPreferences(preferanceSaveUserInfo, context.MODE_PRIVATE);

		userInfo.setLogin(pref.getBoolean("login",false));
		userInfo.setUserId(pref.getString("userid", ""));
		userInfo.setUserName(pref.getString("username", ""));
		userInfo.setUserType(pref.getString("usertype", ""));
		userInfo.setPayroll_mstr_holiday_category_id(pref.getString("payroll_mstr_holiday_category_id",""));
		userInfo.setReporting_emp_id(pref.getString("reporting_emp_id", ""));
		userInfo.setFin_year_identifier(pref.getString("fin_year_identifier", ""));
		userInfo.setReporting_person(pref.getString("reporting_person", ""));

		return userInfo;
	}

}

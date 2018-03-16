package com.lnsel.erp.other;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.lnsel.erp.activity.LoginScreen;
import com.lnsel.erp.constant.Sharepreferences;

/**
 * Created by db on 5/27/2017.
 */
public class Logout {
    public static void logout(Context context){
        Sharepreferences.setUserinfo(context,false,"","","","","","","");
    }
}

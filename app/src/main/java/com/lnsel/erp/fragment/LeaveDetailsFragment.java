package com.lnsel.erp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lnsel.erp.R;
import com.lnsel.erp.activity.HomeScreen;
import com.lnsel.erp.activity.HomeScreen_2;
import com.lnsel.erp.adapter.AttandanceAdapter;
import com.lnsel.erp.adapter.ClosingLeaveAdapter;
import com.lnsel.erp.adapter.HolidayAdapter;
import com.lnsel.erp.adapter.MonthlyLeaveAdapter;
import com.lnsel.erp.adapter.OpeningLeaveAdapter;
import com.lnsel.erp.appconroller.AppController;
import com.lnsel.erp.constant.Sharepreferences;
import com.lnsel.erp.constant.Webservice;
import com.lnsel.erp.other.CreateDialog;
import com.lnsel.erp.settergetter.Holiday;
import com.lnsel.erp.settergetter.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LeaveDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaveDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static View rootView;
    private OnFragmentInteractionListener mListener;

    String fin_year_identifier="";
    Spinner spinner_year;
    ListView list_opening_balance,list_closing_balance,list_monthly_balance;
    ArrayList<HashMap<String,String>> opening_list=new ArrayList<>();
    ArrayList<HashMap<String,String>> closing_list=new ArrayList<>();
    ArrayList<HashMap<String,String>> monthly_list=new ArrayList<>();

    ArrayAdapter<String> yearAdapter;
    ArrayList<String> year_List=new ArrayList<String>();
    ArrayList<String> year_Identifier=new ArrayList<String>();

    public LeaveDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeaveDetailsFragment newInstance(String param1, String param2) {
        LeaveDetailsFragment fragment = new LeaveDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_leave_details, container, false);
        } catch (InflateException e) {

        }

        UserInfo rememberData= Sharepreferences.getUserinfo(getActivity());
        if ((rememberData.getReporting_person().equalsIgnoreCase("1"))) {
            ((HomeScreen) getActivity())
                    .setToolbarTitle();
        }else {
            ((HomeScreen_2) getActivity())
                    .setToolbarTitle();
        }

        list_opening_balance=(ListView)rootView.findViewById(R.id.list_opening_balance);
        list_closing_balance=(ListView)rootView.findViewById(R.id.list_closing_balance);
        list_monthly_balance=(ListView)rootView.findViewById(R.id.list_monthly_balance);

        spinner_year=(Spinner)rootView.findViewById(R.id.spinner_year);
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if(year_Identifier.size()>0) {
                    fin_year_identifier = year_Identifier.get(position);
                    doLeaveDetails();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        year_List.clear();
        yearAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_rows, year_List);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(yearAdapter);

        doFinancialYearListing();
       // doLeaveDetails();
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void doLeaveDetails(){
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
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
                        Toast.makeText(getActivity(), "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                UserInfo rememberData= Sharepreferences.getUserinfo(getActivity());
                Map<String, String>  params = new HashMap<String, String>();
                params.put("employee_id",rememberData.getUserId());
                params.put("fin_year_identifier",fin_year_identifier);
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
    }

    public void JSONresponse(String responsedata){

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
                CreateDialog.showDialog(getActivity(),error_msg);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        OpeningLeaveAdapter openingLeaveAdapter = new OpeningLeaveAdapter(getActivity(), opening_list);
        list_opening_balance.setAdapter(openingLeaveAdapter);

        MonthlyLeaveAdapter monthlyLeaveAdapter = new MonthlyLeaveAdapter(getActivity(), monthly_list);
        list_monthly_balance.setAdapter(monthlyLeaveAdapter);

        ClosingLeaveAdapter closingLeaveAdapter = new ClosingLeaveAdapter(getActivity(), opening_list,monthly_list);
        list_closing_balance.setAdapter(closingLeaveAdapter);
    }


    // financial year listing ===================================================

    public void doFinancialYearListing(){
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = Webservice.FINANCIAL_YEAR_LIST;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        JSONresponseFinancialYear(response);
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
                        Toast.makeText(getActivity(), "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                UserInfo rememberData= Sharepreferences.getUserinfo(getActivity());
                Map<String, String>  params = new HashMap<String, String>();
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
    }

    public void JSONresponseFinancialYear(String responsedata){

        year_Identifier.clear();
        year_List.clear();
        try {
           // year_List.add("-Select-");
           // year_Identifier.add("");
            JSONObject parentObj = new JSONObject(responsedata);
            String status=parentObj.optString("status");
            if(status.equalsIgnoreCase("true")){
                JSONArray result=parentObj.getJSONArray("result");
                for(int i=result.length()-1;i>=0;i--){
                    JSONObject obj_year=result.getJSONObject(i);
                    String strt_dt=obj_year.getString("strt_dt");
                    String end_dt=obj_year.getString("end_dt");

                    String strt[] = strt_dt.split("-");
                    String strt_yer = strt[0];


                    String end[] = end_dt.split("-");
                    String end_yer = end[0];

                    String fn_yer=strt_yer+"-"+end_yer;
                    year_List.add(fn_yer);
                    year_Identifier.add(obj_year.getString("fin_year_identifier"));
                }
            }else {
                String error_msg=parentObj.getString("response");
                CreateDialog.showDialog(getActivity(),error_msg);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        yearAdapter.notifyDataSetChanged();

        if(year_Identifier.size()>0) {
            fin_year_identifier = year_Identifier.get(0);
            doLeaveDetails();
        }
    }
}

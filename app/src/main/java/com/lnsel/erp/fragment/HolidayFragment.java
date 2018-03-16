package com.lnsel.erp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lnsel.erp.R;
import com.lnsel.erp.activity.HomeScreen;
import com.lnsel.erp.adapter.AttandanceAdapter;
import com.lnsel.erp.adapter.HolidayAdapter;
import com.lnsel.erp.appconroller.AppController;
import com.lnsel.erp.constant.Sharepreferences;
import com.lnsel.erp.constant.Webservice;
import com.lnsel.erp.other.CreateDialog;
import com.lnsel.erp.settergetter.Holiday;
import com.lnsel.erp.settergetter.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HolidayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HolidayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static View rootView;

    Spinner spinner_year;
    Button btn_clear;
    ListView list_view;
    EditText edit_search;
    ArrayList<Holiday> list=new ArrayList<>();
    HolidayAdapter adapter;

    ArrayAdapter<String> yearAdapter;
    ArrayList<String> year_List=new ArrayList<String>();
    String current_fin_year="";

    private OnFragmentInteractionListener mListener;

    public HolidayFragment() {
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
    public static HolidayFragment newInstance(String param1, String param2) {
        HolidayFragment fragment = new HolidayFragment();
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
            rootView = inflater.inflate(R.layout.fragment_holiday, container, false);
        } catch (InflateException e) {

        }
        list_view=(ListView)rootView.findViewById(R.id.list_attandance);

        btn_clear=(Button)rootView.findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_search.setText("");
            }
        });

        edit_search=(EditText)rootView.findViewById(R.id.edit_search);
        edit_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // When user changed the Text
                String text = edit_search.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text,btn_clear);

            }
        });

        // spinner for year....................
        spinner_year=(Spinner)rootView.findViewById(R.id.spinner_year);
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if(year_List.size()>0) {
                    current_fin_year = year_List.get(position);
                    doHolidayList(current_fin_year);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        year_List.clear();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        year_List.add(String.valueOf(year));
        year_List.add(String.valueOf(year-1));
        year_List.add(String.valueOf(year-2));
        yearAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_rows, year_List);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(yearAdapter);

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


    public void doHolidayList(final String current_fin_year){
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = Webservice.HOLIDAY_LIST;

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
                params.put("payroll_mstr_holiday_category_id",rememberData.getPayroll_mstr_holiday_category_id());
                params.put("year",current_fin_year);
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
    }

    public void JSONresponse(String responsedata){

        list.clear();
        try {
            JSONObject parentObj = new JSONObject(responsedata);
            String status=parentObj.optString("status");
            if(status.equalsIgnoreCase("true")){
                JSONArray result=parentObj.getJSONArray("result");
                for(int i=0;i<result.length();i++){
                    JSONObject obj=result.getJSONObject(i);
                    Holiday holiday=new Holiday();
                    holiday.setDay(obj.getString("day"));
                   // holiday.setMonth(obj.getString("month"));
                    holiday.setMonth(getMonthForInt(obj.getInt("month")));
                    holiday.setYear(obj.getString("year"));
                    holiday.setDate(obj.getString("date"));
                    holiday.setHoliday_type_name(obj.getString("holiday_type_name"));
                    if(!obj.getString("mstr_holiday_type_id").equalsIgnoreCase("1")) {
                        list.add(holiday);
                    }
                }

            }else {
                String error_msg=parentObj.getString("response");
                CreateDialog.showDialog(getActivity(),error_msg);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        adapter = new HolidayAdapter(getActivity(), list);
        list_view.setAdapter(adapter);
    }


    String getMonthForInt(int m) {
        m=m-1;
        String month = "invalid";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (m >= 0 && m <= 11 ) {
            month = months[m];
        }
        return month;
    }
}

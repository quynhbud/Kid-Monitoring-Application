package com.example.kidmonitoring.controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Base64;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kidmonitoring.model.Application;
import com.example.kidmonitoring.view.FormChildrenActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AppController {
    public static void ChonNgay(Activity context, EditText edtDoB)
    {
        Calendar calendar = Calendar.getInstance();
        int ngay = 1;
        int thang = 12;
        int nam = 1999;
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                edtDoB.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },nam,thang,ngay);
        datePickerDialog.show();
    }
    public static void Insert(String url, Application application, String Email,Context context)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")) {
                    //Toast.makeText(MainActivity.this, "Thêm thông tin thành công!!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Lỗi",Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Xảy ra lỗi!"+error.toString(),Toast.LENGTH_SHORT).show();
                        //Log.d("AAA","Lỗi\n"+error.toString());
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                String imageBase64 = Base64.encodeToString(application.getmIcon(), 0);
                params.put("Email",Email);
                params.put("TenUngDung",application.getmName());
                params.put("MoTa",application.getmPackage());
                params.put("Icon",imageBase64);
                params.put("isBlocked","0");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public static void Delete(String url, String Email, Context context)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")) {
                    //Toast.makeText(FormChildrenActivity.this, "Xóa thành công!!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Lỗi del",Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Xảy ra lỗi!"+error.toString(),Toast.LENGTH_SHORT).show();
                        //Log.d("AAA","Lỗi\n"+error.toString());
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Email",Email);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}

package com.example.kidmonitoring.controller;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kidmonitoring.model.GPS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GPSController {
    public static void GetData(String url, ArrayList<GPS>gps, Context context)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i<response.length(); i++)
                    try {
                        JSONObject object = response.getJSONObject(i);
                        gps.add(new GPS(
                                object.getString("Email"),
                                object.getString("Address"),
                                object.getDouble("Latitude"),
                                object.getDouble("Longitude")
                        ));
                        // Toast.makeText(mContext, String.valueOf(lstGPS.get(0).getLatitude()), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    public static GPS findUser(String username, ArrayList<GPS> gps){
        for (GPS g : gps){
            if (g.getEmail().equals(username)){
                return g;
            }
        }
        return null;
    }
    // GPS
    public static void InsertGPS(String url,GPS gps,Context context)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")) {
                    //Toast.makeText(context, "Thêm thông tin gps thành công!!!", Toast.LENGTH_SHORT).show();
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
                params.put("Email",gps.getEmail());
                params.put("Latitude",String.valueOf(gps.getLatitude()).trim());
                params.put("Longitude",String.valueOf(gps.getLongitude()).trim());
                params.put("Address",gps.getAddress().trim());
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
                    //Toast.makeText(context, "Xóa thành công!!!", Toast.LENGTH_SHORT).show();
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

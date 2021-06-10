package com.example.kidmonitoring.controller;

import android.app.Activity;
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
import com.example.kidmonitoring.model.Information.Information;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InformationController {
    public static void GetData(String url, ArrayList<Information> information, Context context)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i<response.length(); i++)
                    try {
                        JSONObject object = response.getJSONObject(i);
                        information.add(new Information(
                                object.getString("Email"),
                                object.getString("HoTen"),
                                object.getString("NgaySinh"),
                                object.getString("GioiTinh")
                        ));
                        //Toast.makeText(context, information.get(i).getEmail(),Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                //Toast.makeText(context, information.toString(),Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    public static void Update(String url, Information information, Activity context)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")) {
                    Toast.makeText(context, "Sửa thông tin tài khoản thành công!!!", Toast.LENGTH_SHORT).show();
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
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Email",information.getEmail());
                params.put("HoTen",information.getHoTen());
                params.put("NgaySinh",information.getNgaySinh());
                params.put("GioiTinh",information.getGioiTinh());
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }
    public static boolean checkChange(Information user,Information information)
    {
        if( !user.getHoTen().trim().equals(information.getHoTen()) || !user.getNgaySinh().trim().equals(information.getNgaySinh())
                || !user.getGioiTinh().trim().equals(information.getGioiTinh()))
        {
            return true;
        }
        else return false;
    }
    public static Information findUser(String username, ArrayList<Information> informationArrayList){
        for (Information i : informationArrayList){
            if (i.getEmail().equals(username)){
                return i;
            }
        }
        return null;
    }
}

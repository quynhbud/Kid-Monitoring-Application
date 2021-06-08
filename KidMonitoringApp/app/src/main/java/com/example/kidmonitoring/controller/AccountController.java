package com.example.kidmonitoring.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kidmonitoring.model.Information;
import com.example.kidmonitoring.view.MainActivity;
import com.example.kidmonitoring.model.Accounts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountController {
    public static void GetData(String url, ArrayList<Accounts> accounts, Context context)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i<response.length(); i++)
                    try {
                        JSONObject object = response.getJSONObject(i);
                        accounts.add(new Accounts(
                                object.getString("Username"),
                                object.getString("Password")
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                //Toast.makeText(MainActivity.this, response.toString(),Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public static void updatePassword(String url, Accounts accounts, Activity context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    //Toast.makeText(context, "Sửa thông tin tài khoản thành công!!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Xảy ra lỗi!"+error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Username",accounts.getUsername());
                params.put("Password",accounts.getPassword());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public static int checkChange(Accounts acc, Accounts accounts,String currentPassword, String confirmNewPassword){
        if(accounts.getPassword().isEmpty() || currentPassword.isEmpty() || confirmNewPassword.isEmpty()  ){
            return -2;
        }
        if(!currentPassword.equals(acc.getPassword())){
            return 0;
        }
        if(accounts.getPassword().equals(acc.getPassword())){
            return 1;
        }
        if(!accounts.getPassword().equals(confirmNewPassword)){
            return -1;
        }
        return 2;
    }
    public static int checkExist(String username, String password, ArrayList<Accounts> accountsArrayList)
    {
        for(int i=0;i<accountsArrayList.size();i++)
        {
            if(accountsArrayList.get(i).getUsername().equals(username))
            {
                if(accountsArrayList.get(i).getPassword().equals(password))
                    return 2;
                else
                    return 1;
            }

        }
        return 0;
    }

    public static void Register(String url, Information information,String password, Activity context, Class<MainActivity> sub)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")) {
                    Toast.makeText(context, "Đăng ký tài khoản thành công!!!", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context,sub));
                    context.finish();
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
                params.put("Password", password);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }
    public static int checkEmpty(Information information, String password, String confirm, Context context){

        if(password != confirm)
        {
            Toast.makeText(context, "Vui lòng xác nhận lại mật khẩu!!!",Toast.LENGTH_SHORT).show();
            return 1;
        }
        if(information.getHoTen().isEmpty() || information.getNgaySinh().isEmpty() || information.getEmail().isEmpty()
                || information.getGioiTinh().isEmpty() || password == "")
        {
            Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin!!!", Toast.LENGTH_SHORT).show();
            return 1;
        }
        return 0;
    }
    public static Accounts findUser(String username, ArrayList<Accounts> accountArrayList){
        for (Accounts i : accountArrayList){
            if (i.getUsername().equals(username)){
                return i;
            }
        }
        return null;
    }

}

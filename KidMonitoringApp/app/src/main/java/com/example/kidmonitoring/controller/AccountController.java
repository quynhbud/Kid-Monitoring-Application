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

    public static void Register(String url, ArrayList<String> lst, Activity context, Class<MainActivity> sub)
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
                params.put("Email",lst.get(0));
                params.put("HoTen",lst.get(1));
                params.put("NgaySinh",lst.get(2));
                params.put("GioiTinh",lst.get(3));
                params.put("Password", lst.get(4));
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }
    public static int checkEmpty(ArrayList<String> lst, Context context){

        if(!lst.get(4).equals(lst.get(5)))
        {
            Toast.makeText(context, "Vui lòng xác nhận lại mật khẩu!!!",Toast.LENGTH_SHORT).show();
            return 1;
        }
        if(lst.get(0).isEmpty() || lst.get(1).isEmpty() || lst.get(2).isEmpty() || lst.get(4).isEmpty() || lst.get(5).isEmpty())
        {
            Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin!!!", Toast.LENGTH_SHORT).show();
            return 1;
        }
        return 0;
    }

}

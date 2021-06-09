package com.example.kidmonitoring.controller;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kidmonitoring.model.Accounts;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordDecorator extends AccountControllerDecorator {
    public ChangePasswordDecorator(IAccountController iaccountController) {
        super(iaccountController);
    }
    public static void updatePassword(String url, Accounts account, Activity context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    Toast.makeText(context, "Sửa mk thành công!!!", Toast.LENGTH_SHORT).show();
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
                params.put("Username",account.getUsername());
                params.put("Password",account.getPassword());
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
}

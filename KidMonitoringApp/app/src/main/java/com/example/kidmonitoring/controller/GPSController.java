package com.example.kidmonitoring.controller;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.kidmonitoring.model.GPS;
import com.example.kidmonitoring.model.Information;
import com.example.kidmonitoring.view.FormMainActivity;
import com.example.kidmonitoring.view.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
}

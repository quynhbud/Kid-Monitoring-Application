package com.example.kidmonitoring.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kidmonitoring.R;
import com.example.kidmonitoring.controller.CurrentRunningApp;
import com.example.kidmonitoring.controller.GPSController;
import com.example.kidmonitoring.controller.SessionManager;
import com.example.kidmonitoring.model.Application;
import com.example.kidmonitoring.model.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LockScreenPattern extends AppCompatActivity implements PatternLockViewListener {

    String pass="",confirm="",us,patternpass;
    PatternLockView patternLockView;
    TextView tv_notification, tv_next,tv_back;
    public static boolean isSetPassword = true;
    SessionManager sessionManager;
    String url = "https://kid-monitoring.000webhostapp.com/insertDataPattern.php";
    String urlGetdata = "https://kid-monitoring.000webhostapp.com/getdataPattern.php";
    String urlDeldata = "https://kid-monitoring.000webhostapp.com/deletePattern.php";
    ArrayList<Pattern> patterns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen_pattern);
        patternLockView=(PatternLockView)findViewById(R.id.pattern);
        patternLockView.addPatternLockListener(this);
        sessionManager = SessionManager.getInstance(this);
        sessionManager.checkLogin();

        patterns=new ArrayList<>();


        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        us = user.get(SessionManager.KEY_USERNAME);
        //Toast.makeText(this, us, Toast.LENGTH_SHORT).show();
        AnhXa();
        GetData(urlGetdata);
        GetPattern();
        //tv_notification.setText("Please draw your pattern to set password");
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patternLockView.setEnabled(true);
                tv_back.setVisibility(View.INVISIBLE);
                tv_next.setVisibility(View.INVISIBLE);
                tv_notification.setText("Please draw your pattern to set password");
                pass="";
            }
        });
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePattern(urlDeldata);
                startActivity(new Intent(LockScreenPattern.this, FormMainActivity.class));

                SetPattern(url,pass);
                Toast.makeText(LockScreenPattern.this, "Set password successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    @Override
    public void onStarted() {

    }
    public void onBackPressed() {
        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
    }
    @Override
    public void onProgress(List<PatternLockView.Dot> progressPattern) {

    }

    @Override
    public void onComplete(List<PatternLockView.Dot> pattern) {
        GetPattern();
        //Toast.makeText(this, patternpass, Toast.LENGTH_SHORT).show();
        if(isSetPassword==true) {
            if (pass == "") {

                patternLockView.setEnabled(true);
                tv_back.setVisibility(View.INVISIBLE);
                tv_next.setVisibility(View.INVISIBLE);

                pass = PatternLockUtils.patternToString(patternLockView, pattern);
                patternLockView.clearPattern();
            }
            if (pass != "" && confirm == "") {
                patternLockView.setEnabled(true);
                tv_notification.setText("Please draw your pattern again to confirm password");
                tv_back.setVisibility(View.VISIBLE);
                tv_next.setVisibility(View.INVISIBLE);
                confirm = PatternLockUtils.patternToString(patternLockView, pattern);
                patternLockView.clearPattern();
            }
            if (pass.equals(confirm) && pass != "" && confirm != "") {
                patternLockView.setEnabled(false);
                tv_notification.setText("Click Finish to set this pattern password");
                tv_next.setVisibility(View.VISIBLE);
            } else if (!pass.equals(confirm) && pass != "" && confirm != ""){
                tv_next.setVisibility(View.INVISIBLE);
                tv_notification.setText("Please draw again your confirm password");
                confirm="";
            }
        }
        else{
            String tmp=CurrentRunningApp.runningPackName;
            //Toast.makeText(this, tmp+"/"+FormChildrenActivity.packages.size(), Toast.LENGTH_SHORT).show();
            tv_notification.setText("Please draw your pattern");
            if (PatternLockUtils.patternToString(patternLockView, pattern).equalsIgnoreCase(patternpass)) {
                patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
                for(int i=0;i<FormChildrenActivity.packages.size();i++)
                {
                    if(FormChildrenActivity.packages.get(i).trim().equals(tmp))
                        FormChildrenActivity.packages.remove(i);
                }
                finish();
            } else {

                patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                patternLockView.clearPattern();
                Toast.makeText(this, "Not Correct", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SetPattern(String url, String pass)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")) {
                }
                else {
                    Toast.makeText(LockScreenPattern.this, "Lỗi",Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LockScreenPattern.this,"Xảy ra lỗi!"+error.toString(),Toast.LENGTH_SHORT).show();
                        //Log.d("AAA","Lỗi\n"+error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Username",us);
                params.put("PasswordPattern",pass);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }
    private void GetPattern(){
        for (int i=0;i<patterns.size();i++)
        {
            if(patterns.get(i).getUsername().trim().equals(us))
                patternpass = patterns.get(i).getPassword().trim();
        }
    }
    private void GetData(String url)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i<response.length(); i++)
                    try {
                        JSONObject object = response.getJSONObject(i);
                        patterns.add(new Pattern(
                                object.getString("Username"),
                                object.getString("PasswordPattern")
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),error.toString(),Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jsonArrayRequest);

    }
    private void deletePattern(String url)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")) {
                    //Toast.makeText(FormChildrenActivity.this, "Xóa thành công!!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LockScreenPattern.this, "Lỗi del",Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LockScreenPattern.this,"Xảy ra lỗi!"+error.toString(),Toast.LENGTH_SHORT).show();
                        //Log.d("AAA","Lỗi\n"+error.toString());
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Username",us);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    @Override
    public void onCleared() {

    }
    private void AnhXa()
    {
        tv_notification=(TextView)findViewById(R.id.tv_notification);
        tv_next=(TextView)findViewById(R.id.btn_next);
        tv_back=(TextView)findViewById(R.id.tv_back);
    }
}
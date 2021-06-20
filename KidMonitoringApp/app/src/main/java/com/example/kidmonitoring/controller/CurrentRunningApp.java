package com.example.kidmonitoring.controller;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.kidmonitoring.model.Application;
import com.example.kidmonitoring.view.FormChildrenActivity;
import com.example.kidmonitoring.view.LockScreenPattern;
import com.example.kidmonitoring.view.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class CurrentRunningApp extends AccessibilityService {

    SessionManager sessionManager;
    String us;
    String urlGetData="https://kid-monitoring.000webhostapp.com/getdataApps.php";
    public static String runningPackName;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        sessionManager = SessionManager.getInstance(this);
        sessionManager.checkLogin();
        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        us = user.get(SessionManager.KEY_USERNAME);
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String runningPackageName = event.getPackageName().toString();
//        if (runningPackageName.contains("me.mycake")) {
//            blockApp();
//        }
        for(String pack : FormChildrenActivity.packages)
        {
            if (runningPackageName.contains(pack)) {
                runningPackName=pack;
                blockApp();
            }
        }
    }
    private void blockApp() {
        LockScreenPattern.isSetPassword=false;
        startActivity(new Intent(getApplicationContext(), LockScreenPattern.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    @Override
    public void onInterrupt() {

    }
}

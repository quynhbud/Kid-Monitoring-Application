package com.example.kidmonitoring.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.kidmonitoring.view.MainActivity;

import java.util.HashMap;


public class SessionManager {
    private String TAG = SessionManager.class.getName();
    SharedPreferences preferences;
    Context context;
    SharedPreferences.Editor editor;

    private int PRE_MODE = 0;
    private static final String NAME = "DATA";
    public static final String KEY_USERNAME = "usn";
    public static final String KEY_PASSWORD = "psw";
    private static final String KEY_LOGIN = "IsLoggedIn";
    public static final String KEY_ROLE = "IsParent";

    @SuppressLint("WrongConstant")
    public SessionManager (Context context){
        this.context = context;
        preferences = context.getSharedPreferences(NAME,PRE_MODE);
        editor = preferences.edit();
    }
    public void createLoginSession(String username, String password){
        // Storing login value as TRUE
        editor.putBoolean(KEY_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_USERNAME, username);

        // Storing email in pref
        editor.putString(KEY_PASSWORD, password);

        // commit changes
        editor.commit();
    }
    public void createRoleSession(String Role)
    {
        editor.putString(KEY_ROLE,Role);
        editor.commit();
    }
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            Toast.makeText(context,"Chưa đăng nhập!",Toast.LENGTH_LONG).show();
        }
    }
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_USERNAME, preferences.getString(KEY_USERNAME, null));

        // user email id
        user.put(KEY_PASSWORD, preferences.getString(KEY_PASSWORD, null));

        // return user
        return user;
    }
    public HashMap<String, String> getRoles(){
        HashMap<String, String> role = new HashMap<String, String>();
        // role
        role.put(KEY_ROLE, preferences.getString(KEY_ROLE, null));

        // return role
        return role;
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }
    public boolean isLoggedIn(){
        return preferences.getBoolean(KEY_LOGIN, false);
    }
}

package com.example.kidmonitoring.controller;

import android.app.Activity;
import android.content.Context;

import com.example.kidmonitoring.model.Accounts;
import com.example.kidmonitoring.model.Information.Information;
import com.example.kidmonitoring.view.MainActivity;

import java.util.ArrayList;

public interface IAccountController {
    static void GetData(String url, ArrayList<Accounts> accounts, Context context) {

    }

    static void Register(String url, Information information, String password, Activity context, Class<MainActivity> sub) {

    }

    static void updatePassword(String url, Accounts accounts, Activity context) {

    }
}

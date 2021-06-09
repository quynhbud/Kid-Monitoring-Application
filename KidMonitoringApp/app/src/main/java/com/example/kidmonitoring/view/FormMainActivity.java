package com.example.kidmonitoring.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.kidmonitoring.R;
import com.example.kidmonitoring.controller.AccountController;
import com.example.kidmonitoring.controller.GPSController;
import com.example.kidmonitoring.controller.InformationController;
import com.example.kidmonitoring.controller.SessionManager;
import com.example.kidmonitoring.model.Accounts;
import com.example.kidmonitoring.model.GPS;
import com.example.kidmonitoring.model.Information;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FormMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
    TextView tvUsername;
    ArrayList<Information> information;
    public static Information user;
    public static Accounts acc;
    public static GPS gps;
    SessionManager sessionManager;
    String urlGetData = "https://kid-monitoring.000webhostapp.com/getdataInfor.php";
    String urlGetDataAccount = "https://kid-monitoring.000webhostapp.com/getdata.php";
    ArrayList<GPS> lstGPS= new ArrayList<>();
    String us;

    String urlGetDataGPS="https://kid-monitoring.000webhostapp.com/getdataGPS.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();

        information=new ArrayList<>();
        InformationController.GetData(urlGetData,information,this);


        AccountController accountController = AccountController.getInstance();
        AccountController.GetData(urlGetDataAccount,this);

        sessionManager = SessionManager.getInstance(this);
        sessionManager.checkLogin();

        GPSController.GetData(urlGetDataGPS,lstGPS,this);
        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        us = user.get(SessionManager.KEY_USERNAME);
        ActionToolBar();


//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
//                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_profile:
                //Intent intent = new Intent(FormMainActivity.this,ProfileActivity.class);
                //startActivity(intent);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                user=InformationController.findUser(us.trim(),information);
                break;

            case R.id.nav_gps:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GPSFragment()).commit();
                gps=GPSController.findUser(us.trim(),lstGPS);
                user=InformationController.findUser(us.trim(),information);

                break;
            case R.id.Logout:
                sessionManager.logoutUser();
                startActivity(new Intent(FormMainActivity.this,MainActivity.class));
                finish();
                break;

            case R.id.nav_appsManager:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AppsManagerFragment()).commit();
                user=InformationController.findUser(us.trim(),information);
                break;

            case R.id.nav_changePasswword:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChangePasswordFragment()).commit();
                acc=AccountController.findUser(us.trim());
                break;

            case R.id.nav_pattern:
                Intent intent = new Intent(this, LockScreenPattern.class);
                this.startActivity(intent);
                LockScreenPattern.isSetPassword=true;
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
                tvUsername.setText(us.trim());
            }
        });
    }
    private void AnhXa()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        tvUsername = (TextView) headerView.findViewById(R.id.textViewUsername);
    }
}
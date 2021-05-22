package com.example.kidmonitoring.view;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telephony.CarrierConfigManager;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kidmonitoring.R;
import com.example.kidmonitoring.controller.AppController;
import com.example.kidmonitoring.controller.GPSController;
import com.example.kidmonitoring.controller.GPSLocator;
import com.example.kidmonitoring.controller.GPSService;
import com.example.kidmonitoring.controller.SessionManager;
import com.example.kidmonitoring.model.Application;
import com.example.kidmonitoring.model.GPS;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FormChildrenActivity extends AppCompatActivity{

    String urlInsertData="https://kid-monitoring.000webhostapp.com/insertDataApps.php";
    String urlDeleteData="https://kid-monitoring.000webhostapp.com/deleteDataApps.php";

    ArrayList<Application> applications;
    CardView cvLogout;
    SessionManager sessionManager;
    double latitude,longitude;
    String myAddress = "";
    List<Address> addresses;
    String us;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.children_activity_main);
        AnhXa();
        sessionManager = new SessionManager(this);
        applications = new ArrayList<>();
        applications = getInstalledAppList();
        sessionManager.checkLogin();
        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        us = user.get(SessionManager.KEY_USERNAME);
        //AppController.Delete(urlDeleteData,us,this);

//        for(int i=0; i<applications.size();i++)
//        {
//            AppController.Insert(urlInsertData,applications.get(i),us,this);
//            if(i==applications.size()-1)
//                Toast.makeText(FormChildrenActivity.this, "Done", Toast.LENGTH_SHORT).show();
//        }

        GPSLocator gpsLocator = new GPSLocator(getApplicationContext());
        Location location = gpsLocator.GetLocation();
        if(location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        Geocoder geoCoder = new Geocoder(
                getBaseContext(), Locale.getDefault());

        try {
            addresses = geoCoder.getFromLocation(latitude,longitude, 1);
            //String add = "";
            if (addresses.size() > 0)
            {
                myAddress =addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //GPSController.Delete(urlDeleteDataGPS,us,this);
        //GPSController.InsertGPS(urlInsertDataGPS,new GPS(us,myAddress,latitude,longitude),this);
        Intent myIntent = new Intent(this, GPSService.class);
        this.startService(myIntent);
        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();
                startActivity(new Intent(FormChildrenActivity.this,MainActivity.class));
                FormChildrenActivity.this.stopService(myIntent);
                finish();
            }
        });
    }

    private void AnhXa() {
        cvLogout = (CardView)findViewById(R.id.cardViewLogout);
    }

    private ArrayList<Application> getInstalledAppList() {
        ArrayList<Application> listApps = new ArrayList<Application>();
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List pkgAppsList = getPackageManager().queryIntentActivities(mainIntent, 0);
        // Them vao list
        for (Object object : pkgAppsList) {
            ResolveInfo info = (ResolveInfo) object;

            Drawable icon = getBaseContext().getPackageManager().getApplicationIcon(info.activityInfo.applicationInfo);
            byte[] bicon = drawable2Bytes(icon);

            String strPackageName = info.activityInfo.applicationInfo.packageName.toString();
            final String title = (String) ((info != null) ? getBaseContext().getPackageManager().getApplicationLabel(info.activityInfo.applicationInfo) : "???");
            Application app = new Application();
            app.setmIcon(bicon);
            app.setmName(title);
            app.setmPackage(strPackageName);
            app.setmEmail(us);
            listApps.add(app);
        }
        return listApps;
    }
    public static byte[] drawable2Bytes(Drawable d) {
        Bitmap bitmap = drawable2Bitmap(d);
        return bitmap2Bytes(bitmap);
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }




}

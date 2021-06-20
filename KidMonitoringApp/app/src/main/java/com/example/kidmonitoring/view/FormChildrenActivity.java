package com.example.kidmonitoring.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.text.Editable;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.example.kidmonitoring.controller.CurrentRunningApp;
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
    String urlGetData="https://kid-monitoring.000webhostapp.com/getdataApps.php";
    String urlInsertData="https://kid-monitoring.000webhostapp.com/insertDataApps.php";
    String urlDeleteData="https://kid-monitoring.000webhostapp.com/deleteDataApps.php";
    public static ArrayList<String> packages = new ArrayList<>();
    ArrayList<Application> applications,apps;
    CardView cvLogout,cvAccess,cvGetApp;
    SessionManager sessionManager;
    double latitude,longitude;
    String myAddress = "";
    List<Address> addresses;
    String us,ps;
    boolean isLogout;
    Intent myIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.children_activity_main);
        AnhXa();
        isLogout =false;
        sessionManager = SessionManager.getInstance(this);
        applications = new ArrayList<>();
        apps=new ArrayList<>();
        applications = getInstalledAppList();
        sessionManager.checkLogin();
        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        us = user.get(SessionManager.KEY_USERNAME);
        ps = user.get(SessionManager.KEY_PASSWORD);

        GetDataAppOfUser();

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
        myIntent = new Intent(this, GPSService.class);
        this.startService(myIntent);
        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_ConfirmPass();
                }
        });
        cvAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packages.clear();
                for(int i=0;i<apps.size();i++) {
                    if(apps.get(i).isChecked()==true)
                    {
                        packages.add(apps.get(i).getmPackage());
                    }
                }

                alertDi("Yêu cầu bật trợ năng", "Yêu cầu quyền quản lý cho ứng dụng", Settings.ACTION_ACCESSIBILITY_SETTINGS);

            }
        });
        cvGetApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.Delete(urlDeleteData,us,FormChildrenActivity.this);

                for(int i=0; i<applications.size();i++)
                {
                    AppController.Insert(urlInsertData,applications.get(i),us,FormChildrenActivity.this);
                    if(i==applications.size()-1)
                        Toast.makeText(FormChildrenActivity.this, "Done", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void GetDataAppOfUser()
    {
        GetData(urlGetData);
        for(int i=0;i<apps.size();i++)
        {
            if(apps.get(i).getmEmail().trim().equals(us))
            {
                continue;
            }
            else {
                apps.remove(i);
                i--;
            }
        }
    }
    private void AnhXa() {
        cvLogout = (CardView)findViewById(R.id.cardViewLogout);
        cvAccess = (CardView)findViewById(R.id.cardViewAccessService);
        cvGetApp = (CardView)findViewById(R.id.cardViewGetApp);
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

                        //Blob blob = (Blob) object.get("Icon");
                        byte[] bytes=null;
                        bytes= Base64.decode(object.getString("Icon"),0);

                        boolean check;
                        if(object.getString("isBlocked").equals("1"))
                            check=true;
                        else
                            check=false;
                        //Toast.makeText(mContext, object.getString("TenUngDung"), Toast.LENGTH_SHORT).show();
                        apps.add(new Application(
                                object.getString("Email"),
                                object.getString("TenUngDung"),
                                object.getString("MoTa"),
                                bytes,
                                check
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                Collections.sort(apps, new Comparator<Application>() {
                    @Override
                    public int compare(Application o1, Application o2) {
                        return o1.getmName().compareTo(o2.getmName());
                    }
                });
                //Toast.makeText(mContext, apps.get(apps.size()-1).getmName(),Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),error.toString(),Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jsonArrayRequest);

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
            app.setChecked(false);
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


    public void alertDi(String Title, String msg, final String action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle(Title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(action);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void Dialog_ConfirmPass()
    {
        Dialog dialog =new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirmpassword);
        dialog.setCanceledOnTouchOutside(false);
        EditText edtEmail=(EditText)dialog.findViewById(R.id.edtEmail);
        EditText edtConfirm=(EditText)dialog.findViewById(R.id.edtConfirmPass);
        CardView cvConfirm=(CardView) dialog.findViewById(R.id.cardViewConfirmPass);
        edtEmail.setText(us);
        cvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = edtConfirm.getText().toString().trim();
                if(ps.equals(pass))
                {
                    sessionManager.logoutUser();
                    startActivity(new Intent(FormChildrenActivity.this, MainActivity.class));
                    FormChildrenActivity.this.stopService(myIntent);
                    finish();
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(FormChildrenActivity.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
}

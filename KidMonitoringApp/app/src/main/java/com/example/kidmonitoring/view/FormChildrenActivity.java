package com.example.kidmonitoring.view;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kidmonitoring.R;
import com.example.kidmonitoring.controller.SessionManager;
import com.example.kidmonitoring.model.Application;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormChildrenActivity extends AppCompatActivity {

    String urlInsertData="https://kid-monitoring.000webhostapp.com/insertDataApps.php";
    String urlDeleteData="https://kid-monitoring.000webhostapp.com/deleteDataApps.php";
    ArrayList<Application> applications;
    CardView cvLogout;
    SessionManager sessionManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.children_activity_main);
        AnhXa();
        sessionManager = new SessionManager(this);
        applications = new ArrayList<>();
        applications = getInstalledAppList();
        Delete(urlDeleteData);
        for(int i=0; i<applications.size();i++)
        {
            Insert(urlInsertData,applications.get(i));
            if(i==applications.size()-1)
                Toast.makeText(FormChildrenActivity.this, "Done", Toast.LENGTH_SHORT).show();
        }
        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();
                startActivity(new Intent(FormChildrenActivity.this,MainActivity.class));
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
            app.setmEmail(MainActivity.Email);
            listApps.add(app);
        }
        return listApps;
    }
    private void Insert(String url,Application application)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")) {
                    //Toast.makeText(MainActivity.this, "Thêm thông tin thành công!!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(FormChildrenActivity.this, "Lỗi",Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FormChildrenActivity.this,"Xảy ra lỗi!"+error.toString(),Toast.LENGTH_SHORT).show();
                        //Log.d("AAA","Lỗi\n"+error.toString());
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                String imageBase64 = Base64.encodeToString(application.getmIcon(), 0);
                params.put("Email",MainActivity.Email);
                params.put("TenUngDung",application.getmName());
                params.put("MoTa",application.getmPackage());
                params.put("Icon",imageBase64);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void Delete(String url)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")) {
                    //Toast.makeText(MainActivity.this, "Thêm thông tin thành công!!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(FormChildrenActivity.this, "Lỗi del",Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FormChildrenActivity.this,"Xảy ra lỗi!"+error.toString(),Toast.LENGTH_SHORT).show();
                        //Log.d("AAA","Lỗi\n"+error.toString());
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Email",MainActivity.Email);
                return params;
            }
        };
        requestQueue.add(stringRequest);
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

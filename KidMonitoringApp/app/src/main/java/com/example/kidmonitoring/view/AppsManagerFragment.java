package com.example.kidmonitoring.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kidmonitoring.R;
import com.example.kidmonitoring.adapter.AppAdapter;
import com.example.kidmonitoring.controller.AppController;
import com.example.kidmonitoring.controller.InformationController;
import com.example.kidmonitoring.model.Application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppsManagerFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;

    public AppsManagerFragment() {
        // Required empty public constructor
    }
    ProgressDialog mDialog;
    View dashboardInflatedView;
    ListView lvapp;
    ArrayList<Application> applications,apps;
    AppAdapter adapter;
    String urlGetData="https://kid-monitoring.000webhostapp.com/getdataApps.php";
    String urlInsertData="https://kid-monitoring.000webhostapp.com/insertDataApps.php";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dashboardInflatedView = inflater.inflate(R.layout.fragment_appsmanager, container, false);
        apps = new ArrayList<Application>();
        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();
        GetDataAppOfUser();

        lvapp = (ListView)dashboardInflatedView.findViewById(R.id.listviewapp);
        adapter = new AppAdapter(mContext,R.layout.dong_app,apps);
        lvapp.setAdapter(adapter);

        return dashboardInflatedView;
    }
    private void GetDataAppOfUser()
    {
        GetData(urlGetData);
        for(int i=0;i<apps.size();i++)
        {
            if(apps.get(i).getmEmail().trim().equals(MainActivity.Email))
            {
                continue;
            }
            else {
                apps.remove(i);
                i--;
            }
        }
    }
    private void GetData(String url)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i<response.length(); i++)
                    try {
                        JSONObject object = response.getJSONObject(i);

                        //Blob blob = (Blob) object.get("Icon");
                        byte[] bytes=null;
                        bytes=Base64.decode(object.getString("Icon"),0);
                        //Toast.makeText(MainActivity.this,object.getString("MoTa").toString(),Toast.LENGTH_SHORT).show();

                        apps.add(new Application(
                                object.getString("Email"),
                                object.getString("TenUngDung"),
                                object.getString("MoTa"),
                                bytes
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                adapter.notifyDataSetChanged();
                    mDialog.dismiss();
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
                Toast.makeText(mContext,error.toString(),Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jsonArrayRequest);

    }
}

package com.example.kidmonitoring.view;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.kidmonitoring.R;
import com.example.kidmonitoring.adapter.AppAdapter;
import com.example.kidmonitoring.controller.GPSLocator;
import com.example.kidmonitoring.model.Application;
import com.example.kidmonitoring.model.GPS;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class GPSFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;

    public GPSFragment() {
        // Required empty public constructor
    }

    private GoogleMap gm;
    double latitude,longitude;
    String myAddress = "";
    List<Address> addresses;
    View dashboardInflatedView;

    double latitudeC,longitudeC;
    String ChildAddress = "";
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }
    // TODO: Rename and change types and number of parameters
    public static GPSFragment newInstance(String param1, String param2) {
        GPSFragment fragment = new GPSFragment();
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
        dashboardInflatedView = inflater.inflate(R.layout.fragment_gps, container, false);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        createMap();
        longitudeC=FormMainActivity.gps.getLongitude();
        latitudeC=FormMainActivity.gps.getLatitude();
        ChildAddress=FormMainActivity.gps.getAddress();

        return dashboardInflatedView;
    }

    @Override
    public void onResume() {
        super.onResume();
        createMap();
    }

    private void createMap() {
        SupportMapFragment smf = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        smf.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GPSLocator gpsLocator = new GPSLocator(mContext.getApplicationContext());
        Location location = gpsLocator.GetLocation();
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        Geocoder geoCoder = new Geocoder(
                mContext, Locale.getDefault());

        try {
            addresses = geoCoder.getFromLocation(latitude, longitude, 1);
            //String add = "";
            if (addresses.size() > 0) {
                myAddress = addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Parent
        LatLng Parent = new LatLng(latitude, longitude);
        gm = googleMap;
        MarkerOptions option1 = new MarkerOptions();
        option1.position(Parent);
        option1.title("Your Location").snippet(myAddress);
        option1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        option1.alpha(0.8f);
        option1.rotation(0);
        Marker maker = gm.addMarker(option1);
        maker.showInfoWindow();
        CameraPosition cp1 = new CameraPosition.Builder().target(Parent).zoom(18).build();
        gm.animateCamera(CameraUpdateFactory.newCameraPosition(cp1));




        //Children
        LatLng Children = new LatLng(latitudeC, longitudeC);
        gm = googleMap;
        MarkerOptions option2 = new MarkerOptions();
        option2.position(Children);
        option2.title("Your Children Location").snippet(ChildAddress);
        option2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        option2.alpha(0.8f);
        option2.rotation(0);
        Marker maker2 = gm.addMarker(option2);
        maker2.showInfoWindow();
        CameraPosition cp2 = new CameraPosition.Builder().target(Children).zoom(18).build();
        gm.animateCamera(CameraUpdateFactory.newCameraPosition(cp2));


    }


}

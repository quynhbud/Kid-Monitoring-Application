package com.example.kidmonitoring.controller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.kidmonitoring.R;
import com.example.kidmonitoring.model.GPS;
import com.example.kidmonitoring.view.FormChildrenActivity;
import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class GPSService extends Service {
    public GPSService() {
    }
    double latitude,longitude;
    String myAddress = "";
    List<Address> addresses;
    CountDownTimer countDownTimer;

    String urlInsertDataGPS="https://kid-monitoring.000webhostapp.com/insertDataGPS.php";
    String urlDeleteDataGPS="https://kid-monitoring.000webhostapp.com/deleteGPS.php";
    String us;
    SessionManager sessionManager;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

        sessionManager = SessionManager.getInstance(this);
        sessionManager.checkLogin();
        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        us = user.get(SessionManager.KEY_USERNAME);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    private void addGPS()
    {
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
        GPSController.Delete(urlDeleteDataGPS,us,this);
        GPSController.InsertGPS(urlInsertDataGPS,new GPS(us,myAddress,latitude,longitude),this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        countDownTimer = new CountDownTimer(9000000,300000) {
            @Override
            public void onTick(long millisUntilFinished) {
                addGPS();
            }

            @Override
            public void onFinish() {
                countDownTimer.start();
            }
        }.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
        stopForeground(true);
    }
}
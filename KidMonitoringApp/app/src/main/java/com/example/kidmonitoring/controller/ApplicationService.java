package com.example.kidmonitoring.controller;

import android.app.Service;
import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * REFERENCES:
 * https://www.youtube.com/watch?v=FbpD5RZtbCc&list=PLrnPJCHvNZuBhmqlWEQfvxbNtY6B_XJ3n&index=2
 */
public class ApplicationService extends Service {

    public static class MyAdmin extends DeviceAdminReceiver {

        private static final String TAG = "MyAdmin";


        @Override
        public void onEnabled(@NonNull Context context, @NonNull Intent intent) {
            super.onEnabled(context, intent);
            Toast.makeText(context, "Device Admin: Enabled", Toast.LENGTH_SHORT).show();
        }


        @Override
        public void onDisabled(@NonNull Context context, @NonNull Intent intent) {
            Toast.makeText(context, "Device Admin: Disabled", Toast.LENGTH_SHORT).show();
        }



        @Override
        public void onReceive(@NonNull Context context, @NonNull Intent intent) {
            super.onReceive(context, intent);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //only required with bound services.
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();


    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}

package com.quocnam.myapplication.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.quocnam.myapplication.interfaces.IApiAccessResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncTaskNetwork extends AsyncTask<String, Void, String> {
    ProgressDialog progressDialog;
    Context context;
    TextView textView;
    public IApiAccessResponse delegate=null;


    public AsyncTaskNetwork(Context context, TextView textView) {
        this.context = context;
        this.textView = textView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Hiển thị Dialog khi bắt đầu xử lý.
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("HowKteam");
        progressDialog.setMessage("Dang xu ly...");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            StringBuilder sb;
            // Website này sẽ cho biết địa chỉ IP của bạn.
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                Log.e("Dia chi IP ", sb.toString());
                br.close();
                return sb.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(String aString) {
        super.onPostExecute(aString);
        // Hủy dialog đi.
        progressDialog.dismiss();
        // Hiển thị IP lên TextView.
        textView.setText(aString);
        delegate.postResult(aString);
    }
}



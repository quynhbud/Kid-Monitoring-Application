package com.quocnam.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mysql.jdbc.JDBC4Connection;
import com.quocnam.myapplication.interfaces.IApiAccessResponse;
import com.quocnam.myapplication.model.dao.UserDAO;
import com.quocnam.myapplication.model.dto.UserDTO;
import com.quocnam.myapplication.utils.AsyncTaskNetwork;
import com.quocnam.myapplication.utils.DatabaseConnection;
import com.quocnam.myapplication.utils.Query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}


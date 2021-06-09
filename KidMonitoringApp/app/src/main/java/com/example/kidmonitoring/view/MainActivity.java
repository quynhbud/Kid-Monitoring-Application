package com.example.kidmonitoring.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.kidmonitoring.R;
import com.example.kidmonitoring.controller.AccountController;
import com.example.kidmonitoring.controller.SessionManager;
import com.example.kidmonitoring.model.Accounts;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity  {

    public static String Email="";
    ArrayList<Accounts> accounts;
    EditText usn,psw;
    CardView cvLogin;
    TextView tvRegister;
    String urlGetData="https://kid-monitoring.000webhostapp.com/getdata.php";
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        AccountController accountController = AccountController.getInstance();
        accountController.GetData(urlGetData,this);
        AnhXa();
        //sessionManager = new SessionManager(this);
        sessionManager = SessionManager.getInstance(this);

        //Đăng nhập
        cvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usn.getText().toString().trim();
                String password = psw.getText().toString().trim();
                if(accountController.checkExist(username,password)==1)
                {
                    Toast.makeText(MainActivity.this, "Nhập sai mật khẩu!", Toast.LENGTH_SHORT).show();
                }
                else if(accountController.checkExist(username,password)==2)
                {
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    Email = username;
                    sessionManager.createLoginSession(username, password);
                    startActivity(new Intent(MainActivity.this,RolesActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(MainActivity.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        //Mở form đăng ký
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                finish();
            }
        });
        sessionManager.checkLogin();
        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        String us = user.get(SessionManager.KEY_USERNAME);

        // email
        String ps = user.get(SessionManager.KEY_PASSWORD);

        usn.setText(us);
        psw.setText(ps);

        HashMap<String, String> role = sessionManager.getRoles();
        String ROLE = role.get(SessionManager.KEY_ROLE);
       // Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
        if(ROLE != null && ROLE.trim().equals("Parent"))
        {
            startActivity(new Intent(MainActivity.this,FormMainActivity.class));
            finish();
        }
        else if(ROLE != null && ROLE.trim().equals("Children"))
        {
            startActivity(new Intent(MainActivity.this, FormChildrenActivity.class));
            finish();
        }
    }
    private void AnhXa()
    {
        usn = (EditText)findViewById(R.id.editTextUsernameLogin);
        psw = (EditText)findViewById(R.id.editTextPasswordLogin);
        cvLogin = (CardView)findViewById(R.id.cardViewLogin);
        tvRegister = (TextView)findViewById(R.id.textViewRegister);
    }
}
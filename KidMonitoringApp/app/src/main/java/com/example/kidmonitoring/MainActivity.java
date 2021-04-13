package com.example.kidmonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Accounts> accounts;
    EditText usn,psw;
    CardView cvLogin;
    TextView tvRegister;
    String urlGetData="https://kid-monitoring.000webhostapp.com/getdata.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        accounts=new ArrayList<>();
        GetData(urlGetData);
        usn=(EditText)findViewById(R.id.editTextUsernameLogin);
        psw=(EditText)findViewById(R.id.editTextPasswordLogin);
        cvLogin=(CardView)findViewById(R.id.cardViewLogin);
        tvRegister=(TextView)findViewById(R.id.textViewRegister);

        //Đăng nhập
        cvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usn.getText().toString().trim();
                String password = psw.getText().toString().trim();
                if(checkExist(username,password,accounts)==1)
                {
                    Toast.makeText(MainActivity.this, "Nhập sai mật khẩu!", Toast.LENGTH_SHORT).show();
                }
                else if(checkExist(username,password,accounts)==2)
                {
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,FormMainActivity.class));
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


    }
    private void GetData(String url)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i<response.length(); i++)
                try {
                    JSONObject object = response.getJSONObject(i);
                    accounts.add(new Accounts(
                            object.getString("Username"),
                            object.getString("Password")
                    ));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(MainActivity.this, response.toString(),Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private int checkExist(String username, String password, ArrayList<Accounts> accountsArrayList)
    {
        for(int i=0;i<accountsArrayList.size();i++)
        {
            if(accountsArrayList.get(i).getUsername().equals(username))
            {
                if(accountsArrayList.get(i).getPassword().equals(password))
                    return 2;
                else
                    return 1;
            }

        }
        return 0;
    }

}
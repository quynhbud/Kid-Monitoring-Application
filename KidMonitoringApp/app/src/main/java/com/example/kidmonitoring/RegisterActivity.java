package com.example.kidmonitoring;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText edtFullName, edtDoB, edtEmail, edtPassword, edtConfirm;
    RadioGroup rgGender;
    RadioButton rdMale, rdFemale;
    String gender = "Nam";
    CardView cvRegister;
    String urlInsert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        urlInsert = "https://kid-monitoring.000webhostapp.com/insert.php";
        AnhXa();
        edtDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChonNgay();
            }
        });
        rdMale.setOnCheckedChangeListener(listenerRadio);
        rdFemale.setOnCheckedChangeListener(listenerRadio);

        cvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEmpty()==0)
                {
                    Register(urlInsert);
                }
            }
        });
    }
    private void Register(String url)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản thành công!!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Lỗi",Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this,"Xảy ra lỗi!"+error.toString(),Toast.LENGTH_SHORT).show();
                //Log.d("AAA","Lỗi\n"+error.toString());
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Email",edtEmail.getText().toString().trim());
                params.put("HoTen",edtFullName.getText().toString().trim());
                params.put("NgaySinh",edtDoB.getText().toString().trim());
                params.put("GioiTinh",gender);
                params.put("Password", edtPassword.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    CompoundButton.OnCheckedChangeListener listenerRadio
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                gender =  compoundButton.getText().toString();
            }
        }
    };
    //Ánh xạ
    private void AnhXa(){
        edtFullName = (EditText) findViewById(R.id.editTextFullname);
        edtDoB = (EditText) findViewById(R.id.editTextDateOfBirth);
        edtEmail = (EditText) findViewById(R.id.editTextEmail);
        edtPassword = (EditText) findViewById(R.id.editTextPassword);
        edtConfirm = (EditText) findViewById(R.id.editTextPasswordAgain);
        rgGender = (RadioGroup)findViewById(R.id.groupGender);
        cvRegister = (CardView)findViewById(R.id.cardViewRegister);
        rdFemale = (RadioButton)findViewById(R.id.radioFemale);
        rdMale = (RadioButton)findViewById(R.id.radioMale);
    }
    private void ChonNgay()
    {
        Calendar calendar = Calendar.getInstance();
        int ngay = 1;
        int thang = 12;//calendar.get(Calendar.MONTH);
        int nam = 1999;//calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                edtDoB.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },nam,thang,ngay);
        datePickerDialog.show();
    }
    private int checkEmpty(){
        String email = edtEmail.getText().toString().trim();
        String fullname = edtFullName.getText().toString().trim();
        String DoB = edtDoB.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();
        String confirm = edtConfirm.getText().toString().trim();

        if(!pass.equals(confirm))
        {
            Toast.makeText(RegisterActivity.this, "Vui lòng xác nhận lại mật khẩu!!!",Toast.LENGTH_SHORT).show();
            return 1;
        }
        if(email.isEmpty() || fullname.isEmpty() || DoB.isEmpty() || pass.isEmpty() || confirm.isEmpty())
        {
            Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin!!!", Toast.LENGTH_SHORT).show();
            return 1;
        }
        return 0;
    }
}
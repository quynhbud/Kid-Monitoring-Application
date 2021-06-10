package com.example.kidmonitoring.view;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.kidmonitoring.R;
import com.example.kidmonitoring.controller.AccountController;
import com.example.kidmonitoring.controller.AppController;
import com.example.kidmonitoring.model.Information.Information;
import com.example.kidmonitoring.model.Information.InformationConcreteBuilder;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    EditText edtFullName, edtDoB, edtEmail, edtPassword, edtConfirm;
    RadioGroup rgGender;
    RadioButton rdMale, rdFemale;
    String gender = "Nam";
    CardView cvRegister;
    String urlInsert;
    Information information;
    ArrayList<String> infoRegister = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        urlInsert = "https://kid-monitoring.000webhostapp.com/insert.php";
        AnhXa();
        edtDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.ChonNgay(RegisterActivity.this,edtDoB);
            }
        });
        rdMale.setOnCheckedChangeListener(listenerRadio);
        rdFemale.setOnCheckedChangeListener(listenerRadio);

        cvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String hoTen = edtFullName.getText().toString().trim();
                String ngaySinh = edtDoB.getText().toString().trim();
                String Gender = gender;
                String password = edtPassword.getText().toString().trim();
                String confirm = edtConfirm.getText().toString().trim();
                information =  new InformationConcreteBuilder().Email(email).HoTen(hoTen)
                        .NgaySinh(ngaySinh).GioiTinh(Gender).build();
                if(AccountController.checkEmpty(information,password,confirm,RegisterActivity.this)==0)
                {
                    AccountController.Register(urlInsert,information,password,RegisterActivity.this,MainActivity.class);
                }
            }
        });


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
        edtPassword = (EditText) findViewById(R.id.editTextPasswordRegister);
        edtConfirm = (EditText) findViewById(R.id.editTextPasswordAgain);
        rgGender = (RadioGroup)findViewById(R.id.groupGender);
        cvRegister = (CardView)findViewById(R.id.cardViewRegister);
        rdFemale = (RadioButton)findViewById(R.id.radioFemale);
        rdMale = (RadioButton)findViewById(R.id.radioMale);
    }

}
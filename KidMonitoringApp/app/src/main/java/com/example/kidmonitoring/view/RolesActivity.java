package com.example.kidmonitoring.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kidmonitoring.R;
import com.example.kidmonitoring.controller.SessionManager;

public class RolesActivity extends AppCompatActivity {

    CardView cvParent, cvKid;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roles);
        sessionManager = SessionManager.getInstance(this);
        AnhXa();

        cvParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.createRoleSession("Parent");
                startActivity(new Intent(RolesActivity.this, FormMainActivity.class));
                finish();
            }
        });
        cvKid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.createRoleSession("Children");
                startActivity(new Intent(RolesActivity.this, FormChildrenActivity.class));
                finish();
            }
        });
    }
    private void AnhXa()
    {
        cvParent = (CardView)findViewById(R.id.cardViewParent);
        cvKid = (CardView) findViewById(R.id.cardViewKid);
    }
}
package com.example.kidmonitoring.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kidmonitoring.R;

public class RolesActivity extends AppCompatActivity {

    CardView cvParent, cvKid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roles);

        AnhXa();

        cvParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RolesActivity.this, FormMainActivity.class));
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
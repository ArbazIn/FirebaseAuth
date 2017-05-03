package com.tech.firebasetestapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tech.firebasetestapp.R;

public class SelectionActivity extends AppCompatActivity implements View.OnClickListener {

    private android.widget.Button btnEmailPass;
    private android.widget.Button btnGoogle;
    Toolbar toolbar;
    TextView tbTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);


        //Custom Toolbar
        toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        tbTitle = (TextView) toolbar.findViewById(R.id.tbTitle);
        tbTitle.setVisibility(View.VISIBLE);
        tbTitle.setText(getResources().getString(R.string.selectLogin));
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        this.btnGoogle = (Button) findViewById(R.id.btnGoogle);
        this.btnEmailPass = (Button) findViewById(R.id.btnEmailPass);

        btnEmailPass.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEmailPass:
                startActivity(new Intent(SelectionActivity.this, EmailPasswordActivity.class));
                break;
            case R.id.btnGoogle:
                startActivity(new Intent(SelectionActivity.this,GoogleActivity.class ));
                break;
            default:
                break;
        }

    }
}

package com.jua.corpcolle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ReachUsActivity extends AppCompatActivity {
private TextView reachus, mail, phone, address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reach_us);


        reachus = (TextView) findViewById(R.id.reach_us);
        mail = (TextView) findViewById(R.id.mail);
        phone = (TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.address);
    }
}

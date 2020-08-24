package com.jua.corpcolle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class BusinessProductDetails extends AppCompatActivity {
    TextView mbusinessname, mproductname, mdescription, mprice;
    ImageView mProductImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_product_details);
        mprice= findViewById(R.id.priceofproduct);
        mbusinessname = (TextView) findViewById(R.id.dtbusinessname);
        mproductname = (TextView) findViewById(R.id.forproductname);
        mdescription = findViewById(R.id.fordesc);
        mdescription.setMovementMethod(new ScrollingMovementMethod());
        mProductImage = (ImageView) findViewById(R.id.dtproductimage);
        String pprice = getIntent().getStringExtra("productprice");
        String pname = getIntent().getStringExtra("productname");
        String desc = getIntent().getStringExtra("productdescription");
        String announceimg = getIntent().getStringExtra("productimage");
        String name = getIntent().getStringExtra("businessname");
        mprice.setText(pprice);
        mbusinessname.setText(name);
        mdescription.setText(desc);
        mproductname.setText(pname);
        Glide.with(getApplicationContext()).load(announceimg).into(mProductImage);
    }
}

package com.jua.corpcolle;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import de.hdodenhof.circleimageview.CircleImageView;

public class BusinessMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference profileBusinessRef;

    private String currentBusinessId;
    private FirebaseAuth mAuth;
    private DrawerLayout drawer;
    private TextView namebusiness, businessemail;
    private CircleImageView businessimage;
    private long countRequests =0;
    private DatabaseReference ReqCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_main);

        mAuth = FirebaseAuth.getInstance();
        currentBusinessId = mAuth.getCurrentUser().getUid();
        profileBusinessRef = FirebaseDatabase.getInstance().getReference().child("Businesses").child(currentBusinessId);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("CorpColle");

        drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Query query = FirebaseDatabase.getInstance().getReference().child("Requests").orderByChild("businessid")
                .equalTo(currentBusinessId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        countRequests = dataSnapshot.getChildrenCount();

                    } else {
                        countRequests = 0;
                    }
                Menu menu =navigationView.getMenu();
                MenuItem tools = menu.findItem(R.id.nav_myrequests);
                tools.setTitle("My Requests"+"("+countRequests+")");
                navigationView.setNavigationItemSelectedListener(BusinessMainActivity.this);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


        profileBusinessRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){


                    businessimage = (CircleImageView) findViewById(R.id.nav_businessimage);
                    namebusiness =  (TextView)findViewById(R.id.nav_businessname);
                    businessemail =  (TextView)findViewById(R.id.nav_business_email);
                    String myBusinessImage = dataSnapshot.child("businessimage").getValue().toString();
                    String myBusinessname = dataSnapshot.child("businessname").getValue().toString();
                    String myBusinessEmail= dataSnapshot.child("businessemail").getValue().toString();
                    Glide.with(getApplicationContext()).load(myBusinessImage).into(businessimage);
                    namebusiness.setText(myBusinessname);
                    businessemail.setText(myBusinessEmail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MyProductFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_myproducts);}
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_myproducts:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyProductFragment()).commit();
                break;
            case R.id.nav_mysales:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MySalesFragment()).commit();
                break;
            case R.id.nav_myrequests:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyBusinessRequests()).commit();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.logmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        super.onBackPressed();

    }
}

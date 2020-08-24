package com.jua.corpcolle;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
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

public class ClientMainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference profileUserRef;

    private String currentUserId;
    private FirebaseAuth mAuth;
    private DrawerLayout drawer;
    private TextView nameuser, userphone;
    private CircleImageView userimage;
    private long countOrders =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("CorpColle");

        drawer = findViewById(R.id.drawer_layout2);
        final NavigationView navigationView = findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        Query query = FirebaseDatabase.getInstance().getReference().child("Requests").orderByChild("clientid")
                .equalTo(currentUserId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    countOrders = dataSnapshot.getChildrenCount();

                } else {
                    countOrders = 0;
                }
                Menu menu =navigationView.getMenu();
                MenuItem tools = menu.findItem(R.id.nav_myorders);
                tools.setTitle("My Orders"+"("+countOrders+")");
                navigationView.setNavigationItemSelectedListener(ClientMainActivity.this);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){


                    userimage = (CircleImageView) findViewById(R.id.nav_userimage);
                    nameuser =  (TextView)findViewById(R.id.nav_username);
                    userphone =  (TextView)findViewById(R.id.nav_userphone);
                    String myUserImage = dataSnapshot.child("userimage").getValue().toString();
                    String myUsername = dataSnapshot.child("useridentity").getValue().toString();
                    String myPhone= dataSnapshot.child("userPhone").getValue().toString();
                    Glide.with(getApplicationContext()).load(myUserImage).into(userimage);
                    nameuser.setText(myUsername);
                    userphone.setText(myPhone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new BusinessesFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_allbusinesses);}
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_allbusinesses:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BusinessesFragment()).commit();
                break;
            case R.id.nav_mypurchases:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyPurchasesFragment()).commit();
                break;
            case R.id.nav_myorders:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyOrdersFragment()).commit();
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

package com.jua.corpcolle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import android.view.View.OnClickListener;
import android.view.View;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ClientProductDetailsActivity extends AppCompatActivity implements OnClickListener {
    TextView mbusinessname, mproductname, mdescription, mprice, mbusinessid;
    private TextView usAddress;
    ImageView mProductImage;
    private TextView timeof;
    FirebaseAuth mAuth;
    private long countPosts =0;
    private DatabaseReference CountsRef;
    private DatabaseReference profileUserRef;
    private String pname;
    private String pprice;
    private String desc;
    private String productimg;
    private String name;
    private String busid;
    private String busimage;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser fuser;
    private DatabaseReference mButtoncontrol;
    Button giveorder_button;
    Button cancelorder_button;
    private TextView clientname,userP;
    private ImageView clientimage;
    private String myPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_product_details);

        mprice= findViewById(R.id.priceofproduct);
        mbusinessname = (TextView) findViewById(R.id.dtbusinessname);
        mproductname = (TextView) findViewById(R.id.forproductname);
        mbusinessid = (TextView) findViewById(R.id.businessid);
        mdescription = findViewById(R.id.fordesc);
        mdescription.setMovementMethod(new ScrollingMovementMethod());
        mProductImage = (ImageView) findViewById(R.id.dtproductimage);
        pprice = getIntent().getStringExtra("productprice");
        pname = getIntent().getStringExtra("productname");
        desc = getIntent().getStringExtra("productdescription");
        productimg = getIntent().getStringExtra("productimage");
        name = getIntent().getStringExtra("businessname");
        busid = getIntent().getStringExtra("businessid");
        busimage = getIntent().getStringExtra("businessimage");
        mbusinessid.setText(busid);
        mprice.setText(pprice);
        mbusinessname.setText(name);
        mdescription.setText(desc);
        mproductname.setText(pname);
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        timeof = findViewById(R.id.timeof);
        timeof.setText(currentDate);
        Glide.with(getApplicationContext()).load(productimg).into(mProductImage);
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid());
        CountsRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        CountsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    countPosts = dataSnapshot.getChildrenCount();

                } else {
                    countPosts = 0;
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    usAddress = (TextView) findViewById(R.id.usAddress);
                    userP = (TextView) findViewById(R.id.userPh);
                    clientname= (TextView)findViewById(R.id.clientname);
                    clientimage = (ImageView) findViewById(R.id.clientimage);
                    myPhoto = dataSnapshot.child("userimage").getValue().toString();
                    String myUsername = dataSnapshot.child("useridentity").getValue().toString();
                    String myPhone= dataSnapshot.child("userPhone").getValue().toString();
                    String myAdd = dataSnapshot.child("userAddress").getValue().toString();
                    Glide.with(getApplicationContext()).load(myPhoto).into(clientimage);
                    clientname.setText(myUsername);
                    userP.setText(myPhone);
                    usAddress.setText(myAdd);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    final String userId = user.getUid();
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(userId)){

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {

                }
            }

        };

        mAuth.addAuthStateListener(mAuthListener);

        giveorder_button = (Button)findViewById(R.id.giveorder_button);
        cancelorder_button = (Button)findViewById(R.id.cancelorder_button);
        cancelorder_button.setOnClickListener(this);
        giveorder_button.setOnClickListener(this);
        mButtoncontrol = FirebaseDatabase.getInstance().getReference().child("Requests").child(fuser.getUid()+pname);

        mButtoncontrol.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                   giveorder_button.setVisibility(View.GONE);
                   cancelorder_button.setVisibility(View.VISIBLE);

                }else{
                   cancelorder_button.setVisibility(View.GONE);
                   giveorder_button.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.giveorder_button:
                CountsRef = FirebaseDatabase.getInstance().getReference().child("Requests");
                CountsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            countPosts = dataSnapshot.getChildrenCount();

                        } else {
                            countPosts = 0;
                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
                final DatabaseReference giveorderdf;
                giveorderdf = FirebaseDatabase.getInstance().getReference();
                        Query query = FirebaseDatabase.getInstance().getReference().child("Requests")
                                .child(fuser.getUid()+pname);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() > 0) {
                                    Toast.makeText(ClientProductDetailsActivity.this, "You already ordered the product", Toast.LENGTH_SHORT).show();
                                }else {
                                    CountsRef = FirebaseDatabase.getInstance().getReference().child("Requests");
                                    CountsRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                countPosts = dataSnapshot.getChildrenCount();

                                            } else {
                                                countPosts = 0;
                                            }

                                        }


                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }

                                    });
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("requestid", fuser.getUid() + pname);
                                    hashMap.put("clientid", fuser.getUid());
                                    hashMap.put("businessid", busid);
                                    hashMap.put("businessimage", busimage);
                                    hashMap.put("businessname", name);
                                    hashMap.put("situation", "waiting");
                                    hashMap.put("productname", pname);
                                    hashMap.put("productprice", pprice);
                                    hashMap.put("productdescription", desc);
                                    hashMap.put("productimage", productimg);
                                    hashMap.put("useridentity", String.valueOf(clientname.getText()) );
                                    hashMap.put("userPhone", String.valueOf(userP.getText()));
                                    hashMap.put("userimage",myPhoto);
                                    hashMap.put("counter",countPosts);
                                    hashMap.put("userAddress", String.valueOf(usAddress.getText()));
                                    hashMap.put("date", timeof.getText().toString());
                                    giveorderdf.child("Requests").child(fuser.getUid() + pname).setValue(hashMap);
                                    Toast.makeText(ClientProductDetailsActivity.this, "The order added", Toast.LENGTH_SHORT).show();
                                }}
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                break;
            case R.id.cancelorder_button:
                final DatabaseReference cancelord = FirebaseDatabase.getInstance().getReference("Requests/"+(fuser.getUid()+pname));
                cancelord.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                Toast.makeText(ClientProductDetailsActivity.this, "The order cancelled", Toast.LENGTH_SHORT).show();
                                giveorder_button.setClickable(true);
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;

        }
    }
}

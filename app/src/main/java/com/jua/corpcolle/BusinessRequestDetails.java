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

public class BusinessRequestDetails extends AppCompatActivity {
    private TextView clientphone, clientname, clientAddress, date, productname, productdesc, productprice;
    private ImageView productimage;
    private CircleImageView clientimage;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser fuser;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_request_details);

        productimage = (ImageView) findViewById(R.id.productimage);
        clientimage = (CircleImageView) findViewById(R.id.clientimage);
        clientphone = (TextView)findViewById(R.id.clientphone);
        clientname = (TextView)findViewById(R.id.clientname);
        clientAddress = (TextView)findViewById(R.id.clientAddress);
        clientAddress.setMovementMethod(new ScrollingMovementMethod());
        date = (TextView)findViewById(R.id.date);
        productname = (TextView)findViewById(R.id.productname);
        productdesc = (TextView)findViewById(R.id.productdesc);
        productdesc.setMovementMethod(new ScrollingMovementMethod());
        productprice = (TextView)findViewById(R.id.productprice);

        String phone = getIntent().getStringExtra("userPhone");
        String clname = getIntent().getStringExtra("useridentity");
        String address= getIntent().getStringExtra("userAddress");
        String pname=getIntent().getStringExtra("productname");
        String pdesc = getIntent().getStringExtra("productdescription");
        String price =getIntent().getStringExtra("productprice");
        String datee = getIntent().getStringExtra("date");
        String clientimg = getIntent().getStringExtra("userimage");
        String pimage = getIntent().getStringExtra("productimage");
        clientphone.setText(phone);
        clientname.setText(clname);
        clientAddress.setText(address);
        productname.setText(pname);
        productdesc.setText(pdesc);
        productprice.setText(price);
        date.setText(datee);
        Glide.with(getApplicationContext()).load(pimage).into(productimage);
        Glide.with(getApplicationContext()).load(clientimg).into(clientimage);
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    final String userId = user.getUid();
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Businesses");
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

    }
}

package com.jua.corpcolle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
Button businesslogin, clientlogin;
    private DatabaseReference userDR;
    private DatabaseReference userDF;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        businesslogin = (Button) findViewById(R.id.businesslogin);
        clientlogin = (Button) findViewById(R.id.clientlogin);
        auth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //check users
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if( user!=null)
                {
                    final String userId = user.getUid();
                    userDR = FirebaseDatabase.getInstance().getReference().child("Businesses").child(userId);
                    userDR.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Intent moveToHome = new Intent(MainActivity.this, BusinessMainActivity.class);
                                moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);;
                                startActivity(moveToHome);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    userDF = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                    userDF.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Intent moveToHome = new Intent(MainActivity.this, ClientMainActivity.class);
                                moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(moveToHome);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }

            }
        };
        auth.addAuthStateListener(mAuthListener);



        businesslogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToBusiness = new Intent(MainActivity.this, BusinessLoginActivity.class);
                moveToBusiness.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(moveToBusiness);
                finish();
            }
        });

        clientlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToClient = new Intent(MainActivity.this, ClientLoginActivity.class);
                moveToClient.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(moveToClient);
                finish();
            }
        });




    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(mAuthListener);
    }
}

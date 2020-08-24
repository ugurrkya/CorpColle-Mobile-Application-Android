package com.jua.corpcolle;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

public class BusinessLoginActivity extends AppCompatActivity {
    EditText businessemail,businesspassword;
    Button business_login;
    FirebaseAuth auth;
    private String currentBusinessId;
    FirebaseAuth.AuthStateListener mAuthListener;
    private Toolbar toolbar;
    ProgressDialog mProgressDialog;
    private DatabaseReference userDR;
    TextView reach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_login);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CorpColle");
        auth = FirebaseAuth.getInstance();
        businessemail = (EditText)findViewById(R.id.businessemail);
        businesspassword = (EditText)findViewById(R.id.businesspassword);
        businesspassword.setTransformationMethod(new PasswordTransformationMethod());
        business_login =(Button) findViewById(R.id.business_login);
        mProgressDialog = new ProgressDialog(this);
        reach = (TextView) findViewById(R.id.reach);
        //firebase
        reach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BusinessLoginActivity.this, ReachUsActivity.class));
            }
        });


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
                            Intent moveToHome = new Intent(BusinessLoginActivity.this, BusinessMainActivity.class);
                            moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);;
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

        business_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog.setTitle("Logging the business");
                mProgressDialog.setMessage("Please wait...");
                mProgressDialog.show();
                loginUser();

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

    private void loginUser() {


        String businessEmail, businessPassword;

        businessEmail = businessemail.getText().toString().trim();
        businessPassword = businesspassword.getText().toString().trim();


        if (!TextUtils.isEmpty(businessEmail) && !TextUtils.isEmpty(businessPassword))
        {

            auth.signInWithEmailAndPassword(businessEmail,businessPassword).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful())
                    {
                        mProgressDialog.dismiss();
                        FirebaseUser user = auth.getCurrentUser();
                        Intent moveToHome = new Intent(BusinessLoginActivity.this, BusinessMainActivity.class);
                        moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(moveToHome);
                    }else
                    {
                        Toast.makeText(BusinessLoginActivity.this, "Unable to logging business", Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();
                    }
                }
            });
        }else
        {
            Toast.makeText(BusinessLoginActivity.this, "Please enter business email and password", Toast.LENGTH_LONG).show();
            mProgressDialog.dismiss();
        }

    }
}

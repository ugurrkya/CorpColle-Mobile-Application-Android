package com.jua.corpcolle;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class UserProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final int ImageBack = 1;
    private Uri ImageData =null;
    private StorageReference Folder;
    private ProgressDialog progressDialog;
    //private static final int SELECT_FILE = 2;
    //  private static final int REQUEST_CAMERA= 3;
    private EditText userNameEditText;
    private CircleImageView userImageProfileView;
    LinearLayout saveProfileBtn;
    //private Uri resultUri=null;
    private EditText userIdentity;
    private EditText userAddress;
    private EditText userPhone;
    //firebase auth

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    //private String downloadUrl="20";
    //database fields
    private String currentUserID;
    //StorageReference UserProfileImagesRef;
    //StorageReference mStorageRef;
    private DatabaseReference RootRef;

    ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userPhone = (EditText) findViewById(R.id.userPhone);
        userAddress = (EditText) findViewById(R.id.userAddress);
        userAddress.setMovementMethod(new ScrollingMovementMethod());
        userIdentity = (EditText) findViewById(R.id.userIdentity);
        userImageProfileView = (CircleImageView) findViewById(R.id.userProfileImageView);
        userNameEditText = (EditText) findViewById(R.id.userProfileName);

        saveProfileBtn = (LinearLayout)findViewById(R.id.saveProfile);

        //assign instances

        mAuth = FirebaseAuth.getInstance();
        //progress dialog
        currentUserID = mAuth.getCurrentUser().getUid();
        mProgress = new ProgressDialog(this);

        //firebase database instance
        RootRef = FirebaseDatabase.getInstance().getReference();
        //  mStorageRef = FirebaseStorage.getInstance().getReference();
        // onclick save profile
        progressDialog = new ProgressDialog(this);
        saveProfileBtn.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")

            @Override
            public void onClick(View v) {

                //logic for saving user profile
                Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username")
                        .equalTo(String.valueOf(userNameEditText.getText()));
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            Toast.makeText(UserProfileActivity.this, "Choose a different user name", Toast.LENGTH_SHORT).show();
                        } else if (ImageData == null) {
                            Toast.makeText(UserProfileActivity.this, "You didn't choose an image", Toast.LENGTH_SHORT).show();
                        } else {
                            if (String.valueOf(userNameEditText.getText()).equals("")) {
                                Toast.makeText(UserProfileActivity.this, "Please type a username", Toast.LENGTH_SHORT).show();
                            } else if (String.valueOf(userAddress.getText()).equals("")) {
                                Toast.makeText(UserProfileActivity.this, "Please type your address", Toast.LENGTH_SHORT).show();
                            } else if (String.valueOf(userPhone.getText()).equals("")) {
                                Toast.makeText(UserProfileActivity.this, "Please type your phone number", Toast.LENGTH_SHORT).show();
                            } else if (String.valueOf(userIdentity.getText()).equals("")) {
                                Toast.makeText(UserProfileActivity.this, "Please type your name and surname", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                progressDialog.setTitle("Saving Profile..");
                                progressDialog.setMessage("Please Wait..");
                                progressDialog.show();


                                StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("FileFolder");
                                Uri IndividualFile = ImageData;


                                final StorageReference ImageName = ImageFolder.child("Image" + IndividualFile.getLastPathSegment());
                                ImageName.putFile(IndividualFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                    @Override

                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {


                                            @Override

                                            public void onSuccess(Uri uri) {




                                                String url = String.valueOf(uri);

                                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                                HashMap hashMp = new HashMap();

                                                hashMp.put("username", String.valueOf(userNameEditText.getText()));
                                                hashMp.put("userimage", url);
                                                hashMp.put("useridentity", String.valueOf(userIdentity.getText()));
                                                hashMp.put("userAddress", userAddress.getText().toString());
                                                hashMp.put("userPhone", String.valueOf(userPhone.getText()));
                                                hashMp.put("userid",currentUserID);

                                                databaseReference.child("Users").child(currentUserID).setValue(hashMp);
                                                SendtoMainActivity();

                                            }

                                        });

                                        Toast.makeText(UserProfileActivity.this, "The account created successfully", Toast.LENGTH_SHORT).show();
                                        saveProfileBtn.setVisibility(View.GONE);
                                        progressDialog.dismiss();
                                    }

                                });


                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        //user imageview onclick listener

        userImageProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mProgress.setTitle("");
                //logic for picking image
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("*/*");
                startActivityForResult(intent,ImageBack);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ImageBack){
            if(resultCode == RESULT_OK){
                ImageData = data.getData();
                userImageProfileView.setImageURI(ImageData);
            }
        }


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




    private void SendtoMainActivity(){
        Intent mainIntent = new Intent(UserProfileActivity.this, ClientMainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}

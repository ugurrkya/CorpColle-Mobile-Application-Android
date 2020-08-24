package com.jua.corpcolle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
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
import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final int ImageBack = 1;
    private Uri ImageData =null;
    Button addproduct_button,choose;
    EditText productname,productdescription,productprice;
    private CircleImageView businessimage;
    private TextView nameofbusiness,emailofbusiness,businessabout;
    DatabaseReference reference;
    private FirebaseUser fuser;
    private String myPhoto;
    private DatabaseReference ProductsRef;
    private ProgressDialog progressDialog;
    private long countProducts =0;
    private ImageView productimage;

    DatabaseReference databaseReference;
    private StorageReference Folder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        productimage = findViewById(R.id.productimage);
        productname  = findViewById(R.id.productname);
        productdescription = findViewById(R.id.productdescription);
        productdescription.setMovementMethod(new ScrollingMovementMethod());
        productprice = findViewById(R.id.productprice);

        choose = findViewById(R.id.chooser);
        addproduct_button = findViewById(R.id.addproduct_button);
        businessimage = findViewById(R.id.businessimage);
        nameofbusiness = findViewById(R.id.nameofbusiness);
        emailofbusiness = findViewById(R.id.emailofbusiness);
        businessabout = findViewById(R.id.businessabout);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Businesses").child(fuser.getUid());
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(fuser.getUid());
        ProductsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    countProducts = dataSnapshot.getChildrenCount();

                } else {
                    countProducts = 0;
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    BusinessData businessData = dataSnapshot.getValue(BusinessData.class);
                    myPhoto = dataSnapshot.child("businessimage").getValue().toString();
                    Glide.with(getApplicationContext()).load(businessData.getBusinessimage()).into(businessimage);
                    String myEmail = dataSnapshot.child("businessemail").getValue().toString();
                    String myBusinessname = dataSnapshot.child("businessname").getValue().toString();
                    String aboutbusiness = dataSnapshot.child("businessabout").getValue().toString();
                    emailofbusiness.setText(myEmail);
                    nameofbusiness.setText(myBusinessname);
                    businessabout.setText(aboutbusiness);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        progressDialog = new ProgressDialog(this);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("*/*");
                startActivityForResult(intent,ImageBack);
            }
        });

        addproduct_button.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")

            @Override

            public void onClick(View view) {
                ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(fuser.getUid());
                ProductsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            countProducts = dataSnapshot.getChildrenCount();

                        } else {
                            countProducts = 0;
                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
                Query query = FirebaseDatabase.getInstance().getReference().child("Products").child(fuser.getUid()).orderByChild("productname")
                        .equalTo(String.valueOf(productname.getText()));
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            Toast.makeText(AddProductActivity.this, "Choose a different product name", Toast.LENGTH_SHORT).show();
                        } else if (ImageData == null) {
                            Toast.makeText(AddProductActivity.this, "You didn't choose an image", Toast.LENGTH_SHORT).show();
                        } else {
                            if (String.valueOf(productname.getText()).equals("")) {
                                Toast.makeText(AddProductActivity.this, "Please type a product name", Toast.LENGTH_SHORT).show();
                            } else if (String.valueOf(productname.getText()).contains(".")) {
                                Toast.makeText(AddProductActivity.this, "You should use *,* instead of *.*", Toast.LENGTH_SHORT).show();
                            } else if (String.valueOf(productdescription.getText()).equals("")) {
                                Toast.makeText(AddProductActivity.this, "You should fill product description area", Toast.LENGTH_SHORT).show();
                            } else if (String.valueOf(productprice.getText()).equals("")) {
                                Toast.makeText(AddProductActivity.this, "You should fill product price area", Toast.LENGTH_SHORT).show();
                            } else if (String.valueOf(productname.getText()).contains("#")) {
                                Toast.makeText(AddProductActivity.this, "You can't use special characters in the product", Toast.LENGTH_SHORT).show();
                            } else if (String.valueOf(productname.getText()).contains("$")) {
                                Toast.makeText(AddProductActivity.this, "You can't use special characters in the product", Toast.LENGTH_SHORT).show();
                            } else if (String.valueOf(productname.getText()).contains("[")) {
                                Toast.makeText(AddProductActivity.this, "You can't use special characters in the product", Toast.LENGTH_SHORT).show();
                            } else if (String.valueOf(productname.getText()).contains("]")) {
                                Toast.makeText(AddProductActivity.this, "You can't use special characters in the product", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.setMessage("Product is Adding, Please Wait...........");
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
                                                ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(fuser.getUid());
                                                ProductsRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            countProducts = dataSnapshot.getChildrenCount();

                                                        } else {
                                                            countProducts = 0;
                                                        }

                                                    }


                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }

                                                });

                                                String url = String.valueOf(uri);
                                                String point= "TL";
                                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                                HashMap hashMp = new HashMap();

                                                hashMp.put("businessname", String.valueOf(nameofbusiness.getText()));
                                                hashMp.put("businessemail", String.valueOf(emailofbusiness.getText()));
                                                hashMp.put("businessimage", myPhoto);
                                                hashMp.put("businessid",fuser.getUid());
                                                hashMp.put("businessabout", String.valueOf(businessabout.getText()));
                                                hashMp.put("productname", String.valueOf(productname.getText()));
                                                hashMp.put("counter", countProducts);
                                                hashMp.put("productdescription", productdescription.getText().toString());
                                                hashMp.put("productprice", productprice.getText().toString()+" "+point);
                                                hashMp.put("productimage", url);

                                                databaseReference.child("Products").child(fuser.getUid()).child(String.valueOf(countProducts)).setValue(hashMp);


                                            }

                                        });

                                        Toast.makeText(AddProductActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        addproduct_button.setVisibility(View.GONE);
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
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ImageBack){
            if(resultCode == RESULT_OK){
                ImageData = data.getData();
                productimage.setImageURI(ImageData);
            }
        }


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}

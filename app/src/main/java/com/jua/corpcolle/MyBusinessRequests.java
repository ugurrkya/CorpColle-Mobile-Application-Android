package com.jua.corpcolle;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class MyBusinessRequests extends Fragment {
    RecyclerView mRecyclerView;
    private FirebaseUser fuser;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mRef;
    private long countProducts =0;
    private DatabaseReference ConfirmRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_business_requests, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        mRecyclerView = getView().findViewById(R.id.recyclerViewbusreq);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);




        mfirebaseDatabase = FirebaseDatabase.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        ConfirmRef = FirebaseDatabase.getInstance().getReference().child("Confirmed");
        ConfirmRef.addValueEventListener(new ValueEventListener() {
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

        mRef = mfirebaseDatabase.getReference("Requests");

        Query Sorts = mRef.orderByChild("businessid").equalTo(fuser.getUid());

        FirebaseRecyclerAdapter<WaitingBusData, ViewHolderbusreq> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<WaitingBusData, ViewHolderbusreq>(
                        WaitingBusData.class,
                        R.layout.rowbusreq,
                        ViewHolderbusreq.class,
                        Sorts
                ){
                    @Override
                    protected void populateViewHolder(ViewHolderbusreq viewHolder, WaitingBusData waitingRequestData, int position) {

                        viewHolder.setDetails(getActivity().getApplicationContext(),waitingRequestData.getRequestid(),waitingRequestData.getClientid(),
                                waitingRequestData.getBusinessid(),waitingRequestData.getBusinessimage(),waitingRequestData.getBusinessname(),
                                waitingRequestData.getSituation(),waitingRequestData.getProductname(),waitingRequestData.getProductprice(),
                                waitingRequestData.getProductdescription(), waitingRequestData.getProductimage(),waitingRequestData.getUseridentity(),
                                waitingRequestData.getUserPhone(),waitingRequestData.getUserimage(),waitingRequestData.getDate(),
                                waitingRequestData.getUserAddress(), waitingRequestData.getCounter());

                    }

                    @Override
                    public ViewHolderbusreq onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolderbusreq viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolderbusreq.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                String mRequestid = getItem(position).getRequestid();
                                String mClientid = getItem(position).getClientid();
                                String mBusinessID= getItem(position).getBusinessid();
                                String mBusinessImage =getItem(position).getBusinessimage();
                                String mBusinessName =getItem(position).getBusinessname();
                                String mSituation = getItem(position).getSituation();
                                String mProductName = getItem(position).getProductname();
                                String mProductPrice =getItem(position).getProductprice();
                                String mProductDescription =getItem(position).getProductdescription();
                                String mProductImage =getItem(position).getProductimage();
                                String mUserIdentity = getItem(position).getUseridentity();
                                String mUserPhone = getItem(position).getUserPhone();
                                String mUserImage = getItem(position).getUserimage();
                                String mDate = getItem(position).getDate();
                                String mAddress= getItem(position).getUserAddress();

                                //pass this data to new activity
                                Intent intent = new Intent(getContext(),BusinessRequestDetails.class);
                                intent.putExtra("businessname", mBusinessName);
                                intent.putExtra("productname",mProductName);
                                intent.putExtra("productimage",mProductImage);
                                intent.putExtra("businessimage",mBusinessImage);
                                intent.putExtra("productprice",mProductPrice);
                                intent.putExtra("productdescription",mProductDescription);
                                intent.putExtra("businessid", mBusinessID);
                                intent.putExtra("requestid", mRequestid);
                                intent.putExtra("clientid", mClientid);
                                intent.putExtra("situation", mSituation);
                                intent.putExtra("useridentity", mUserIdentity);
                                intent.putExtra("userPhone", mUserPhone);
                                intent.putExtra("userimage", mUserImage);
                                intent.putExtra("date", mDate);
                                intent.putExtra("userAddress", mAddress);
                                startActivity(intent);
                            }
                            @Override
                            public void onItemLongClick(View view, int position) {
                                 String reqid = getItem(position).getRequestid();
                                 String idofbusiness = getItem(position).getBusinessid();
                                 String idofclient = getItem(position).getClientid();
                                 String nameofbus = getItem(position).getBusinessname();
                                 String nameofclient = getItem(position).getUseridentity();
                                 String imageofclient = getItem(position).getUserimage();
                                 String descofproduct = getItem(position).getProductdescription();
                                 String prodImage = getItem(position).getProductimage();
                                 String address = getItem(position).getUserAddress();
                                 String phone = getItem(position).getUserPhone();
                                 String price = getItem(position).getProductprice();
                                 String pname = getItem(position).getProductname();
                                showConfirmDataDialog(reqid,idofbusiness,idofclient,nameofbus,nameofclient,
                                        imageofclient,descofproduct,prodImage,address,phone,price,pname);
                            }
                        });

                        return viewHolder;
                    }
                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    private void showConfirmDataDialog(final String reqid, final String idofbusiness,
                                       final String idofclient,final String nameofbus,final String nameofclient,
                                       final String imageofclient, final String descofproduct,
                                       final String prodImage, final String address, final String phone,
                                       final String price, final String pname) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Product");
        builder.setMessage("Are you sure to confirm the product order?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Query mQuery = mRef.orderByChild("requestid").equalTo(reqid);
                mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(getContext(), "The order rejected.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                ConfirmRef = FirebaseDatabase.getInstance().getReference().child("Confirmed");
                ConfirmRef.addValueEventListener(new ValueEventListener() {
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

                Calendar calendar = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                HashMap hashMp = new HashMap();
                hashMp.put("businessid", idofbusiness);
                hashMp.put("clientid", idofclient);
                hashMp.put("businessname", nameofbus);
                hashMp.put("useridentity", nameofclient);
                hashMp.put("userimage", imageofclient);
                hashMp.put("productdescription", descofproduct);
                hashMp.put("productimage", prodImage);
                hashMp.put("userAddress", address);
                hashMp.put("userPhone", phone);
                hashMp.put("productprice", price);
                hashMp.put("date", currentDate);
                hashMp.put("productname",pname);

                ConfirmRef.child(String.valueOf(countProducts)).setValue(hashMp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        Query mQuery = mRef.orderByChild("requestid").equalTo(reqid);
                        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds: dataSnapshot.getChildren()){
                                    ds.getRef().removeValue();
                                }
                                Toast.makeText(getContext(), "The order confirmed.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });



            }
        });
        builder.create().show();
    }




    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

}

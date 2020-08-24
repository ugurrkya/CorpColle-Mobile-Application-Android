package com.jua.corpcolle;


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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class MyOrdersFragment extends Fragment {
    RecyclerView mRecyclerView;
    private FirebaseUser fuser;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_orders, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        mRecyclerView = getView().findViewById(R.id.recyclerViewclientreq);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);




        mfirebaseDatabase = FirebaseDatabase.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();




        mAuth = FirebaseAuth.getInstance();
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

        mRef = mfirebaseDatabase.getReference("Requests");

        Query Sorts = mRef.orderByChild("clientid").equalTo(fuser.getUid());

        FirebaseRecyclerAdapter<WaitingRequestData, ViewHolderclientreq> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<WaitingRequestData, ViewHolderclientreq>(
                        WaitingRequestData.class,
                        R.layout.rowclientrequests,
                        ViewHolderclientreq.class,
                        Sorts
                ){
                    @Override
                    protected void populateViewHolder(ViewHolderclientreq viewHolder, WaitingRequestData waitingRequestData, int position) {

                        viewHolder.setDetails(getActivity().getApplicationContext(),waitingRequestData.getRequestid(),waitingRequestData.getClientid(),
                                waitingRequestData.getBusinessid(),waitingRequestData.getBusinessimage(),waitingRequestData.getBusinessname(),
                                waitingRequestData.getSituation(),waitingRequestData.getProductname(),waitingRequestData.getProductprice(),
                                waitingRequestData.getProductdescription(), waitingRequestData.getProductimage(),waitingRequestData.getUseridentity(),
                                waitingRequestData.getUserPhone(),waitingRequestData.getUserimage(),waitingRequestData.getDate(),
                                waitingRequestData.getUserAddress());

                    }

                    @Override
                    public ViewHolderclientreq onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolderclientreq viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolderclientreq.ClickListener() {
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
                                Intent intent = new Intent(getContext(),ClientProductDetailsActivity.class);
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
                                //   String currentProduct = getItem(position).getProductname();
                                // String currentImage = getItem(position).getProductimage();
                            }
                        });

                        return viewHolder;
                    }
                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
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

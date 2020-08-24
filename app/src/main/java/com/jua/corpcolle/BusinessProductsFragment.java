package com.jua.corpcolle;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import static com.google.firebase.storage.FirebaseStorage.getInstance;



public class BusinessProductsFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_business_products, container, false);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String idofbus = getArguments().getString("businessid");


        mRecyclerView = getView().findViewById(R.id.recyclerViewbusp);
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

        mRef = mfirebaseDatabase.getReference("Products").child(idofbus);

        Query Sorts = mRef.orderByChild("counter");

        FirebaseRecyclerAdapter<BusinessProductData, ViewHolderPro> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<BusinessProductData, ViewHolderPro>(
                        BusinessProductData.class,
                        R.layout.businessproductrow2,
                        ViewHolderPro.class,
                        Sorts
                ){
                    @Override
                    protected void populateViewHolder(ViewHolderPro viewHolder, BusinessProductData productmodel, int position) {

                        viewHolder.setDetails(getActivity().getApplicationContext(),productmodel.getBusinessemail(),productmodel.getBusinessabout(),productmodel.getBusinessid(),
                                productmodel.getBusinessimage(),productmodel.getBusinessname(),productmodel.getProductimage(),productmodel.getProductprice(),
                                productmodel.getProductdescription(),productmodel.getProductname());

                    }

                    @Override
                    public ViewHolderPro onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolderPro viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolderPro.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {


                                String mProductName = getItem(position).getProductname();
                                String mBusinessName =getItem(position).getBusinessname();
                                String mProductImage =getItem(position).getProductimage();
                                String mBusinessImage =getItem(position).getBusinessimage();
                                String mProductPrice =getItem(position).getProductprice();
                                String mProductDescription =getItem(position).getProductdescription();
                                String mBusinessID= getItem(position).getBusinessid();


                                //pass this data to new activity
                                Intent intent = new Intent(getContext(),ClientProductDetailsActivity.class);
                                intent.putExtra("businessname", mBusinessName);
                                intent.putExtra("productname",mProductName);
                                intent.putExtra("productimage",mProductImage);
                                intent.putExtra("businessimage",mBusinessImage);
                                intent.putExtra("productprice",mProductPrice);
                                intent.putExtra("productdescription",mProductDescription);
                                intent.putExtra("businessid", mBusinessID);

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

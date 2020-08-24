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

/**
 */
public class MyProductFragment extends Fragment {
    RecyclerView mRecyclerView;
    Button addproduct_button;
    private FirebaseUser fuser;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mRef;
    String busname;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_product, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addproduct_button = (Button) getView().findViewById(R.id.addproduct_button);

        addproduct_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddProductActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        mRecyclerView = getView().findViewById(R.id.recyclerView);
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
        mRef = mfirebaseDatabase.getReference("Products").child(fuser.getUid());

        Query Sorts = mRef.orderByChild("counter");

        FirebaseRecyclerAdapter<BusinessProductData, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<BusinessProductData, ViewHolder>(
                        BusinessProductData.class,
                        R.layout.businessproductrow,
                        ViewHolder.class,
                        Sorts
                ){
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, BusinessProductData productmodel, int position) {

                        viewHolder.setDetails(getActivity().getApplicationContext(),productmodel.getBusinessname(),productmodel.getBusinessabout(),productmodel.getBusinessid(),
                                productmodel.getBusinessimage(),productmodel.getBusinessemail(),productmodel.getProductimage(),productmodel.getProductprice(),
                                productmodel.getProductdescription(),productmodel.getProductname());

                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {


                                String mProductName = getItem(position).getProductname();
                                String mBusinessName =getItem(position).getBusinessname();
                                String mProductImage =getItem(position).getProductimage();
                                String mBusinessImage =getItem(position).getBusinessimage();
                                String mProductPrice =getItem(position).getProductprice();
                                String mProductDescription =getItem(position).getProductdescription();


                                //pass this data to new activity
                                Intent intent = new Intent(getContext(),BusinessProductDetails.class);
                                intent.putExtra("businessname", mBusinessName);
                                intent.putExtra("productname",mProductName);
                                intent.putExtra("productimage",mProductImage);
                                intent.putExtra("businessimage",mBusinessImage);
                                intent.putExtra("productprice",mProductPrice);
                                intent.putExtra("productdescription",mProductDescription);


                                startActivity(intent);
                            }
                            @Override
                            public void onItemLongClick(View view, int position) {
                                String currentProduct = getItem(position).getProductname();
                                String currentImage = getItem(position).getProductimage();

                                showDeleteDataDialog(currentProduct,currentImage);
                            }
                        });

                        return viewHolder;
                    }
                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void showDeleteDataDialog(final String currentProduct, final String currentImage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Product");
        builder.setMessage("Are you sure to delete the product?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Query mQuery = mRef.orderByChild("productname").equalTo(currentProduct);
                mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(getContext(), "Deleted successfully..", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                StorageReference mPicRef = getInstance().getReferenceFromUrl(currentImage);
                mPicRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Product image deleted successfully..", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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

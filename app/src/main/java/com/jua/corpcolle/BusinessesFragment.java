package com.jua.corpcolle;



import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class BusinessesFragment extends Fragment {
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
        return inflater.inflate(R.layout.fragment_businesses, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        setHasOptionsMenu(true);
        Toolbar toolbar = getView().findViewById(R.id.toolbar);
        mRecyclerView = getView().findViewById(R.id.recyclerViewbus);
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
        mRef = mfirebaseDatabase.getReference().child("Businesses");

        FirebaseRecyclerAdapter<BusinessData, ViewHolderbus> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<BusinessData, ViewHolderbus>(
                        BusinessData.class,
                        R.layout.rowbusinesslist,
                        ViewHolderbus.class,
                        mRef
                ){
                    @Override
                    protected void populateViewHolder(ViewHolderbus viewHolder, BusinessData businessData, int position) {

                        viewHolder.setDetails(getActivity().getApplicationContext(),businessData.getBusinessemail(),businessData.getBusinessabout(),businessData.getBusinessid(),
                               businessData.getBusinessimage(),businessData.getBusinessname(),businessData.getBusinesspass());

                    }

                    @Override
                    public ViewHolderbus onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolderbus viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolderbus.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                String mBusinessID =getItem(position).getBusinessid();
                                Fragment fragment = new BusinessProductsFragment();
                                Bundle i= new Bundle();
                                i.putString("businessid", mBusinessID);
                                fragment.setArguments(i);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null)
                                        .commit();

                            }
                            @Override
                            public void onItemLongClick(View view, int position) {
                                //String currentProduct = getItem(position).getProductname();
                                //String currentImage = getItem(position).getProductimage();

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
        FirebaseRecyclerAdapter<BusinessData, ViewHolderbus> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<BusinessData, ViewHolderbus>(
                        BusinessData.class,
                        R.layout.rowbusinesslist,
                        ViewHolderbus.class,
                        mRef
                ){
                    @Override
                    protected void populateViewHolder(ViewHolderbus viewHolder, BusinessData businessData, int position) {

                        viewHolder.setDetails(getActivity().getApplicationContext(),businessData.getBusinessemail(),businessData.getBusinessabout(),businessData.getBusinessid(),
                                businessData.getBusinessimage(),businessData.getBusinessname(),businessData.getBusinesspass());

                    }

                    @Override
                    public ViewHolderbus onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolderbus viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolderbus.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                String mBusinessID =getItem(position).getBusinessid();
                                Fragment fragment = new BusinessProductsFragment();
                                Bundle i= new Bundle();
                                i.putString("businessid", mBusinessID);
                                fragment.setArguments(i);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null)
                                        .commit();

                            }
                            @Override
                            public void onItemLongClick(View view, int position) {
                                //String currentProduct = getItem(position).getProductname();
                                //String currentImage = getItem(position).getProductimage();

                            }
                        });

                        return viewHolder;
                    }
                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    //search data
    private void firebaseSearch(String searchText){

        Query firebaseSearchQuery = mRef.orderByChild("search").startAt(searchText).endAt(searchText+"\uf8ff");


        FirebaseRecyclerAdapter<BusinessData, ViewHolderbus> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<BusinessData, ViewHolderbus>(
                        BusinessData.class,
                        R.layout.rowbusinesslist,
                        ViewHolderbus.class,
                        firebaseSearchQuery
                ){
                    @Override
                    protected void populateViewHolder(ViewHolderbus viewHolder, BusinessData businessData, int position) {

                        viewHolder.setDetails(getActivity().getApplicationContext(),businessData.getBusinessemail(),businessData.getBusinessabout(),businessData.getBusinessid(),
                                businessData.getBusinessimage(),businessData.getBusinessname(),businessData.getBusinesspass());

                    }

                    @Override
                    public ViewHolderbus onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolderbus viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolderbus.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                String mBusinessID =getItem(position).getBusinessid();
                                Fragment fragment = new BusinessProductsFragment();
                                Bundle i= new Bundle();
                                i.putString("businessid", mBusinessID);
                                fragment.setArguments(i);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null)
                                        .commit();

                            }
                            @Override
                            public void onItemLongClick(View view, int position) {
                                //String currentProduct = getItem(position).getProductname();
                                //String currentImage = getItem(position).getProductimage();

                            }
                        });

                        return viewHolder;
                    }
                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //infilate the menu this add items to the action bar if it present
        getActivity().getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Filter as you type
                firebaseSearch(newText);
                return false;
            }
        });

    }

}

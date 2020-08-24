package com.jua.corpcolle;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderbusreq extends RecyclerView.ViewHolder {
    View mView;

    public ViewHolderbusreq(@NonNull View itemView) {
        super(itemView);

        mView = itemView;


        //item click

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });


        //item long click

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });

    }


    public void setDetails (Context ctx, String requestid, String clientid, String businessid, String businessimage, String businessname, String situation, String productname, String productprice, String productdescription, String productimage, String useridentity, String userPhone, String userimage, String date, String userAddress, long counter){
        ImageView mProductImage = mView.findViewById(R.id.productImage);
        TextView mProductName= mView.findViewById(R.id.productname);
        TextView mClientName= mView.findViewById(R.id.clientname);
        TextView mDate = mView.findViewById(R.id.date);
        TextView mPrice = mView.findViewById(R.id.proprice);
        Glide.with(ctx.getApplicationContext()).load(productimage)
                .apply(new RequestOptions())
                .into(mProductImage);
        mProductName.setText(productname);
        mDate.setText(date);
        mPrice.setText(productprice);
        mClientName.setText(useridentity);




    }

    private ViewHolderbusreq.ClickListener mClickListener;


    //interface to send callbacks

    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolderbusreq.ClickListener clickListener){

        mClickListener = clickListener;

    }
}




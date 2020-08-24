package com.jua.corpcolle;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderMyPurchases extends RecyclerView.ViewHolder {

    View mView;

    public ViewHolderMyPurchases(@NonNull View itemView) {
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


    public void setDetails (Context ctx, String businessid, String businessname, String clientid, String date, String productdescription, String productimage, String productprice, String userAddress, String userPhone, String useridentity, String userimage, String productname){
        ImageView mProductImage = mView.findViewById(R.id.productImage);
        TextView mProductName= mView.findViewById(R.id.productname);
        TextView mDate = mView.findViewById(R.id.date);
        TextView mPrice = mView.findViewById(R.id.proprice);
        Glide.with(ctx.getApplicationContext()).load(productimage)
                .apply(new RequestOptions())
                .into(mProductImage);
        mProductName.setText(productname);
        mDate.setText(date);
        mPrice.setText(productprice);




    }

    private ViewHolderMyPurchases.ClickListener mClickListener;


    //interface to send callbacks

    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolderMyPurchases.ClickListener clickListener){

        mClickListener = clickListener;

    }
}




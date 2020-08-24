package com.jua.corpcolle;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public ViewHolder(@NonNull View itemView) {
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


    public void setDetails (Context ctx, String businessemail, String businessabout, String businessid, String businessimage, String businessname,String productimage, String productprice, String productdescription, String productname){
        TextView mProductName= mView.findViewById(R.id.productname);
        TextView mProductPrice = mView.findViewById(R.id.priceofproduct);
        ImageView mProductImage = mView.findViewById(R.id.productimage);

        Glide.with(ctx.getApplicationContext()).load(productimage)
                .apply(new RequestOptions())
                .into(mProductImage);
        mProductName.setText(productname);
        mProductPrice.setText(productprice);




    }

    private ViewHolder.ClickListener mClickListener;


    //interface to send callbacks

    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){

        mClickListener = clickListener;

    }
}

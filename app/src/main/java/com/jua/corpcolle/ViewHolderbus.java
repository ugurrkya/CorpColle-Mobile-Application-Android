package com.jua.corpcolle;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderbus extends RecyclerView.ViewHolder {
    View mView;

    public ViewHolderbus(@NonNull View itemView) {
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


    public void setDetails (Context ctx, String businessemail, String businessabout, String businessid, String businessimage, String businessname, String businesspass){
        ImageView mBusinessImage = mView.findViewById(R.id.businessImage);
        TextView mBusinessName= mView.findViewById(R.id.businessName);

        Glide.with(ctx.getApplicationContext()).load(businessimage)
                .apply(new RequestOptions())
                .into(mBusinessImage);
        mBusinessName.setText(businessname);




    }

    private ViewHolderbus.ClickListener mClickListener;


    //interface to send callbacks

    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolderbus.ClickListener clickListener){

        mClickListener = clickListener;

    }
}


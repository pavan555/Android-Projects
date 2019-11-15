package com.example.pavan.instagramclone.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pavan.instagramclone.R;
import com.example.pavan.instagramclone.UserFeedActivity;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{

    private  int numberOfItems;

    public ImageAdapter(int numberOfItems) {

        Log.i("IPPIP","hey");
        this.numberOfItems = numberOfItems;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context=viewGroup.getContext();
        int layoutId=R.layout.item_layout;
        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(layoutId,viewGroup, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        imageViewHolder.bind(i);

    }

    @Override
    public int getItemCount() {
        return numberOfItems;
    }


    class ImageViewHolder extends RecyclerView.ViewHolder{
        TextView textDesc;
        ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textDesc=itemView.findViewById(R.id.image_desc);
            imageView=itemView.findViewById(R.id.imageView);
        }
        void bind(int listIndex){
            textDesc.setText(String.valueOf(listIndex));
            Log.i("IMAGE BITMAP", String.valueOf(UserFeedActivity.BytesList.get(listIndex-1)));
            imageView.setImageBitmap(UserFeedActivity.BytesList.get(listIndex-1));
        }

        }
    }

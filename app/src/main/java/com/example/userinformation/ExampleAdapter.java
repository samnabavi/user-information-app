package com.example.userinformation;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder>  {
    private ArrayList<ExampleItem> mExampleList;
    private OnItemClickListener mListener;
    //SharedPreferences sharedpreferences;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    //sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public static int counter = 0;
        //public SharedPreferences sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        //Random rand = new Random();
        public ExampleViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            //int pos = rand.nextInt(1000);
            String strPos = String.valueOf(counter++);
            //if(counter == 11) {counter = 0;}
            //Glide.with(itemView).load("https://robohash.org/saman/" + strPos).into(mImageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }


    public ExampleAdapter(ArrayList<ExampleItem> exampleList) {
        mExampleList = exampleList;
    }


    @NonNull
    @Override
    public ExampleAdapter.ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleAdapter.ExampleViewHolder holder, int position) {
        ImageView iv = holder.mImageView;
        ExampleItem currentItem = mExampleList.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        Glide.with(iv).load("https://robohash.org/saman/" + String.valueOf(position)).into(iv);
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}

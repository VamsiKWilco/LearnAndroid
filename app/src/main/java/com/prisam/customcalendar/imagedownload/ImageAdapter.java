package com.prisam.customcalendar.imagedownload;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.prisam.customcalendar.R;
import com.prisam.customcalendar.common.MyApplication;
import com.prisam.customcalendar.common.OnItemClickListener;

import java.util.ArrayList;

/**
 * Created by vamsi on 10/12/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ImageResponseModel> imageResponseModels;
    private OnItemClickListener mOnItemClickListener;

    ImageAdapter(Context mContext, ArrayList<ImageResponseModel> imageResponseModels, OnItemClickListener listener) {
        this.mContext = mContext;
        this.imageResponseModels = imageResponseModels;
        this.mOnItemClickListener = listener;
    }

    TranslateAnimation animation;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.image_inflater, parent, false);

        animation = new TranslateAnimation(0.0f, 400.0f,
                0.0f, 0.0f);
        animation.setDuration(5000);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setFillAfter(true);

        return new MyViewHolder(mContext, itemView, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bindDataToView(position);
    }

    @Override
    public int getItemCount() {
        return this.imageResponseModels.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        Context mContext;
        View view;
        OnItemClickListener mListener;

        TextView textView;
        CheckBox checkBox;
        View viewGap;
        ImageView imageView;

        MyViewHolder(Context context, View itemView, OnItemClickListener listener) {
            super(itemView);

            view = itemView;
            mContext = context;
            mListener = listener;

            /*textView = (TextView) view.findViewById(R.id.title);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);*/
            viewGap = (View) view.findViewById(R.id.viewGap);
            imageView = (ImageView) view.findViewById(R.id.imageView);

        }

        void bindDataToView(int position) {

//            imageView.setAnimation(animation);

             /*Recommended by google to load and cache images*/
            Glide.with(MyApplication.getInstance())
                    .load(imageResponseModels.get(position).getUrl().getLarge())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.blueprogressbar)
                    .into(imageView);

            /*textView.setText(imageResponseModels.get(position).getName());

            if (!imageResponseModels.get(position).getChecked()) {
                checkBox.setChecked(false);
                checkBox.setEnabled(true);
            } else {
                checkBox.setChecked(true);
                if (!imageResponseModels.get(position).getDownloaded())
                    checkBox.setEnabled(true);
                else
                    checkBox.setEnabled(false);
            }


            checkBox.setOnClickListener(view -> {
                if (mListener != null) {
                    mListener.onItemClick(view, position);

                    if (checkBox.isChecked())
                        imageResponseModels.get(position).setChecked(true);
                    else
                        imageResponseModels.get(position).setChecked(false);

                    notifyDataSetChanged();
                }
            });*/
        }
    }


}

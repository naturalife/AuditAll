package com.faraway.auditall;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.faraway.auditall.DataBase.AuditPhotos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fan
 * @description
 * @date 2019-08-19
 * @time 14:00
 **/
public class MyAdapterAuditFragment extends RecyclerView.Adapter<MyAdapterAuditFragment.MyViewHolder> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private List<AuditPhotos> auditPhotoList = new ArrayList<>(9);
    private static final int MAX = 6;

    public MyAdapterAuditFragment(Context mContext, List<AuditPhotos> List) {
        this.mContext = mContext;
        this.auditPhotoList = List;
        this.mLayoutInflater = LayoutInflater.from(mContext);

    }

    //重置adpter的list
    public void setAdapterList(List<AuditPhotos> photoPaths) {
        this.auditPhotoList = photoPaths;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = mLayoutInflater.inflate(R.layout.recyclerview2, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


        holder.pictureView.setBackgroundResource(R.drawable.add1);

        if (position == auditPhotoList.size()) {
            holder.pictureView.setImageDrawable(null);
            holder.pictureView.setBackgroundResource(R.drawable.add1);
            if (auditPhotoList.size() == MAX) {
                holder.pictureView.setImageDrawable(null);
                holder.pictureView.setBackgroundResource(0);
            }
        } else {
            if (auditPhotoList.size() != 0 && (position < auditPhotoList.size())) {
//                holder.pictureView.setImageURI(Uri.fromFile(new File(auditPhotoList.get(position).getPhotoPath())));

                Glide.with(mContext).load(auditPhotoList.get(position).getPhotoPath()).into(holder.pictureView);
            }
        }


        holder.selectNumber.setVisibility(View.INVISIBLE);
        if (position < auditPhotoList.size()) {
            holder.selectNumber.setVisibility(View.VISIBLE);
        }

        holder.pictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.OnImageClick(view, position);
                }
            }
        });

        holder.selectNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.OnTextClick(view, position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return auditPhotoList.size() + 1;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView pictureView;
        private ImageView selectView;
        private ImageView selectNumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pictureView = itemView.findViewById(R.id.fiv2);
            selectNumber = itemView.findViewById(R.id.iv_del2);
        }
    }

    public interface OnItemClickListener {
        void OnTextClick(View view, int position);
        void OnImageClick(View view, int position);
    }


    private OnItemClickListener onItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}

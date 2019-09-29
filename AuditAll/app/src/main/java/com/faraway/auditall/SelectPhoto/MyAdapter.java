package com.faraway.auditall.SelectPhoto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.faraway.auditall.DataBase.AuditPhotos;
import com.faraway.auditall.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fan
 * @description照片选择recyclerView的adapter
 * @date 2019-09-02
 * @time 14:04
 **/

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private int index = 0;
    private List<AuditPhotos> auditPhotoList = new ArrayList<>(9);//存储相片List

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<PictureBean> mPictureBeanList;//存储相册全部图片
    private static final int MAX =6;//最多可选择相片数量



    public MyAdapter(Context mContext, ArrayList<PictureBean> mList, List<AuditPhotos>List) {
        this.mContext = mContext;
        this.mPictureBeanList = mList;
        this.auditPhotoList = List;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.gv_filter_image, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        //相册所有照片放入pictureView

//        holder.pictureView.setImageURI(Uri.fromFile(new File(mPictureBeanList.get(position).getPhotoPath())));
        Glide.with(mContext).load(mPictureBeanList.get(position).getPhotoPath()).into(holder.pictureView);

        //照片单击事件
        holder.pictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.OnImageClick(view, position);
                }
            }
        });

        //照片选择框编号设置
        if (!mPictureBeanList.get(position).isSelectStatue()) {
            holder.selectNumber.setBackgroundResource(R.drawable.bg_unselected);
            holder.selectNumber.setTextColor(Color.parseColor("#B6B6B6"));
        } else {
            holder.selectNumber.setBackgroundResource(R.drawable.bg_select_true_easy_photos);

            //获得选择相片的编号
            index = getIndex(position, index);

            //设置选择相片的编号
            holder.selectNumber.setText("" + index);

            //设置编号数字颜色
            holder.selectNumber.setTextColor(Color.parseColor("#B6B6B6"));

        }


        //照片选择框的单击事件
        holder.selectNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.OnTextClick(view, position);
            }
        });
    }

    //获得选择照片编号
    private int getIndex(int position, int index) {
        for (int i = 0; i < auditPhotoList.size(); i++) {
            if ((mPictureBeanList.get(position).getPhotoPath()).equals(auditPhotoList.get(i).getPhotoPath())) {
                index = i + 1;
            }
        }
        return index;
    }


    @Override
    public int getItemCount() {
        return mPictureBeanList.size();
    }


    //重置adpter的list
    public void setAdapterList(ArrayList<PictureBean> mlist, List<AuditPhotos> photoPaths) {
        this.mPictureBeanList = mlist;
        this.auditPhotoList = photoPaths;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView pictureView;
        private TextView selectNumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pictureView = itemView.findViewById(R.id.fiv);
            selectNumber = itemView.findViewById(R.id.iv_del);
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


    //暂时不用
    private void showPic(int position, ImageView imageView) {
        Bitmap bitmap = BitmapFactory.decodeFile(mPictureBeanList.get(position).getPhotoPath());
        int height = bitmap.getHeight();
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
//        计算缩放比例
        float sca = layoutParams.height * 1.0f / height;
//        根据高度来确定宽度
        layoutParams.width = (int) (bitmap.getWidth() * sca);
        imageView.setLayoutParams(layoutParams);
    }


}
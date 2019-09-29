package com.faraway.auditall.SelectPhoto;

/**
 * @author Fan
 * @description 显示系统所有图片，返回选择的图片
 * @date 2019-08-19
 * @time 13:39
 **/

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faraway.auditall.AuditPagerActivity;
import com.faraway.auditall.DataBase.App;
import com.faraway.auditall.DataBase.AuditInfo;
import com.faraway.auditall.DataBase.AuditInfoDao;
import com.faraway.auditall.DataBase.AuditPhotos;
import com.faraway.auditall.DataBase.AuditPhotosDao;
import com.faraway.auditall.DataBase.DaoSession;
import com.faraway.auditall.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends AppCompatActivity {

    private int selectNumber = 0;//已选择图片数量
    private int auditId;
    private static final int MAX = 6;//最大可选择图片数量
    private static final int REQUEST_CODE_FINAL_SELECT_ACTIVITY = 1;
    private static final int SHOWBIG_PHOTO =2;

    private ImageView backImageView;//返回按钮
    private RecyclerView mRecyclerView;

    private TextView totalNumber;//完成按钮

    private MyAdapter myAdapter;//RecyclerView  Adapter

    private ArrayList<PictureBean> mPictureBeanList = new ArrayList<>();//手机中所有图片
    private ArrayList<String> photoPaths = new ArrayList<>();//存入手机中所有图片,bundle用

    private AuditPhotosDao auditPhotosDao;//auditPhoto数据库

    private List<AuditPhotos> auditPhotoList = new ArrayList<>(9);//auditPhoto数据list


    private AuditInfo auditInfo = new AuditInfo();
    private AuditPhotos auditPhotos;


    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        totalNumber = findViewById(R.id.selectStatus1);
        backImageView = findViewById(R.id.arrowBack1);
        mRecyclerView = findViewById(R.id.recyclerView01);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            auditId = bundle.getInt("auditId");
        }
//        Log.d("HHH", "SelectAcitivity" + "--->" + "auditId" + "--->" + auditId + "");

        //初始化数据库
        initialDao();

        //获得数据库audit数据，并放入auditInfoList中
//        getSelectPictureList();

        //获得数据库中已有照片，并放入auditPhotoList中
        getPhotoPathsList();


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.addItemDecoration(new MyDecoration());

        //获得系统所有照片
        getAllPhotos();


        //初始化照片选择状态，从数据库获得数据
        if (mPictureBeanList != null && auditPhotoList != null) {
            for (int i = 0; i < mPictureBeanList.size(); i++) {
                for (int j = 0; j < auditPhotoList.size(); j++) {
                    if ((mPictureBeanList.get(i).getPhotoPath()).equals(auditPhotoList.get(j).getPhotoPath())) {
                        mPictureBeanList.get(i).setSelectStatue(true);
                    }
                }
            }
        }


        //初始化完成按钮状态
        if (auditPhotoList.size() == 0) {
            totalNumber.setVisibility(View.INVISIBLE);
        } else {
            totalNumber.setVisibility(View.VISIBLE);
            totalNumber.setText("完成" + "(" + auditPhotoList.size() + "/" + MAX + ")");
        }


        myAdapter = new MyAdapter(this, mPictureBeanList, auditPhotoList);
        mRecyclerView.setAdapter(myAdapter);


        //选择框加监听,设置完成按钮状态
        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void OnTextClick(View view, int position) {

                if ((auditPhotoList.size()) < MAX) {//选择照片数量满足要求时
                    if (!mPictureBeanList.get(position).isSelectStatue()) {//择照片数量满足要求，且照片选择框状态为：未选择

                        //设置照片选择框状态为：已选择
                        mPictureBeanList.get(position).setSelectStatue(true);

                        //将选择的照片数据，插入至PhotoPathsDB数据库
                        insertPhotoPathsDB(position);

                        //获得选择相片，并保存至auditPhotoList
                        getPhotoPathsList();

                        //更新Adapter数据
                        myAdapter.setAdapterList(mPictureBeanList, auditPhotoList);
                        myAdapter.notifyDataSetChanged();

                        //设置完成按钮状态
                        totalNumber.setVisibility(View.VISIBLE);
                        totalNumber.setText("完成" + "(" + auditPhotoList.size() + "/" + MAX + ")");

                    } else {//择照片数量满足要求，且照片选择框状态为：已选择
                        if (mPictureBeanList.get(position).isSelectStatue()) {

                            //设置照片选择框状态为：未选择
                            mPictureBeanList.get(position).setSelectStatue(false);

                            //将照片从auditPhotoList删除
                            if (auditPhotoList.size() > 0) {
                                for (int i = 0; i < auditPhotoList.size(); i++) {
                                    if (auditPhotoList.get(i).getPosition() == (0l + position)) {
                                        auditPhotoList.remove(i);
                                    }
                                }
                            }

                            //将照片从数据库删除
                            deletePhotoPathsDB(position);

                            //更新Adapter数据
                            myAdapter.setAdapterList(mPictureBeanList, auditPhotoList);
                            myAdapter.notifyDataSetChanged();
                            //设置完成按钮状态
                            totalNumber.setVisibility(View.VISIBLE);
                            totalNumber.setText("完成" + "(" + auditPhotoList.size() + "/" + MAX + ")");

                        }
                    }
                } else {//择照片数量达到上限，且照片选择框状态为：已选择
                    if (mPictureBeanList.get(position).isSelectStatue()) {

                        //设置照片选择框状态为：未选择
                        mPictureBeanList.get(position).setSelectStatue(false);

                        //将照片从auditPhotoList删除
                        if (auditPhotoList.size() > 0) {
                            for (int i = 0; i < auditPhotoList.size(); i++) {
                                if (auditPhotoList.get(i).getPosition() == (0l + position)) {
                                    auditPhotoList.remove(i);
                                }
                            }
                        }

                        //将照片从数据库删除
                        deletePhotoPathsDB(position);


                        //更新Adapter数据
                        myAdapter.setAdapterList(mPictureBeanList, auditPhotoList);
                        myAdapter.notifyDataSetChanged();

                        //设置完成按钮状态
                        totalNumber.setVisibility(View.VISIBLE);
                        totalNumber.setText("完成" + "(" + auditPhotoList.size() + "/" + MAX + ")");

                    } else {//择照片数量达到上限，且照片选择框状态为：未选择
                        Toast.makeText(SelectActivity.this, "最多选" + MAX + "张哦", Toast.LENGTH_SHORT).show();
                    }

                }

            }

            //点击图片放大
            @Override
            public void OnImageClick(View view, int position) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("allphotos", photoPaths);
                bundle.putInt("whichActivity", 1000);
                bundle.putInt("number", position);
                bundle.putInt("auditId",auditId);
                intent.putExtras(bundle);

                intent.setClass(SelectActivity.this, ShowBigPictureActivity.class);
                startActivityForResult(intent,SHOWBIG_PHOTO);
//                finish();
            }
        });


        //完成按钮点击事件
        totalNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //显示载入对话框
                showDialog();

                Intent intent = new Intent(SelectActivity.this, AuditPagerActivity.class);
                setResult(Activity.RESULT_OK,intent);

//                startActivity(intent);
                finish();

            }
        });

        //返回按钮点击事件
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SelectActivity.this, "载入中，请稍候", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SelectActivity.this, AuditPagerActivity.class);
                setResult(Activity.RESULT_OK,intent);
//                startActivity(intent);
                finish();
            }
        });

    }


    //显示载入对话框
    private void showDialog() {
        dialog = new Dialog(SelectActivity.this,R.style.new_circle_progress);
        dialog.setContentView(R.layout.layout_progressbar);

        //获取屏幕宽高
        WindowManager wm = (WindowManager) SelectActivity.this
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width =display.getWidth();
        int height=display.getHeight();

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        //lp.x与lp.y表示相对于原始位置的偏移.
        //将对话框的大小按屏幕大小的百分比设置
        lp.x = (int) (width*0.45); // 新位置X坐标
        lp.y = (int) (height*0.2); // 新位置Y坐标
        // lp.width = (int) (width*1); // 宽度
         // lp.height = (int) (height*1); // 高度

        dialogWindow.setAttributes(lp);

        dialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog!=null){
            dialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!= Activity.RESULT_OK) return;
        if (requestCode ==SHOWBIG_PHOTO){
            //获得选择相片，并保存至auditPhotoList
            getPhotoPathsList();

            //更新Adapter数据
            myAdapter.setAdapterList(mPictureBeanList, auditPhotoList);
            myAdapter.notifyDataSetChanged();
        }
    }

    //添加分隔线
    class MyDecoration extends RecyclerView.ItemDecoration {
        //复写getItemOffsets方法
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int length = getResources().getDimensionPixelOffset(R.dimen.dividerHeight);
            outRect.set(length, length, length, length);
        }
    }

    /*
     * 获得系统图片
     * */
    private ArrayList<PictureBean> getAllPhotos() {

        int number = 0;

        //获取cursor
        Cursor cursor = this.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // URI,可以有多种形式
                null,
                null,
                null,
                null);
//图片路径所在列的索引
        int indexPhotoPath = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        while (cursor.moveToNext()) {
            PictureBean mPictureBean = new PictureBean();
            mPictureBean.setPhotoPath(cursor.getString(indexPhotoPath));
            mPictureBean.setPhotoNumber(number);
            number++;

            mPictureBeanList.add(mPictureBean);
            try {
                finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            photoPaths.add(cursor.getString(indexPhotoPath));
        }
        cursor.close();
        return mPictureBeanList;
    }

    //初始化数据库
    private void initialDao() {
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        daoSession.clear();
        auditPhotosDao = daoSession.getAuditPhotosDao();
    }



    //获得数据库中已有照片list,并按id排序,可以增加一个参数，按参数排序
    private void getPhotoPathsList() {
        if (auditPhotosDao != null) {
            auditPhotoList = auditPhotosDao.queryBuilder()
                    .where(AuditPhotosDao.Properties.IdAuditPhotos.eq(auditId))
                    .list();
        }
    }

    //选择的数据，插入数据库auditPhotosDao
    private void insertPhotoPathsDB(int position) {
        auditPhotos = new AuditPhotos();
        auditPhotos.setIdAuditPhotos(auditId);
        auditPhotos.setPosition(0l + position);
        auditPhotos.setPhotoPath(mPictureBeanList.get(position).getPhotoPath());
        auditPhotosDao.insert(auditPhotos);
    }

    //根据position，删除数据库auditPhotosDao
    private void deletePhotoPathsDB(int position) {
        List<AuditPhotos> auditPhotos = auditPhotosDao.queryBuilder()
                .where(AuditPhotosDao.Properties.IdAuditPhotos.eq(auditId))
                .list();
        for (int i = 0; i < auditPhotos.size(); i++) {
            if (auditPhotos!=null){
                if (auditPhotos.get(i).getPosition() == position){
                    auditPhotosDao.delete(auditPhotos.get(i));
                }
            }
        }
    }

    //更新系统图库,未使用
    public static void updateFileFromDatabase(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] paths = new String[]{Environment.getExternalStorageDirectory().toString()};
            MediaScannerConnection.scanFile(context, paths, null, null);
            MediaScannerConnection.scanFile(context, new String[]{
                            file.getAbsolutePath()},
                    null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } else {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }


}

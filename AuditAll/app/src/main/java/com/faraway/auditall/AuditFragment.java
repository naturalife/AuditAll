package com.faraway.auditall;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faraway.auditall.DataBase.App;
import com.faraway.auditall.DataBase.AuditInfo;
import com.faraway.auditall.DataBase.AuditInfoDao;
import com.faraway.auditall.DataBase.AuditItem;
import com.faraway.auditall.DataBase.AuditItemDao;
import com.faraway.auditall.DataBase.AuditPhotos;
import com.faraway.auditall.DataBase.AuditPhotosDao;
import com.faraway.auditall.DataBase.BasicInfo;
import com.faraway.auditall.DataBase.BasicInfoDao;
import com.faraway.auditall.DataBase.DaoSession;
import com.faraway.auditall.SelectPhoto.SelectActivity;
import com.faraway.auditall.SelectPhoto.ShowBigPictureActivity;
import com.faraway.auditall.Utils.ExcelUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ${Faraway}
 * @description审核主界面
 * @date ${data}
 **/
public class AuditFragment extends Fragment implements View.OnClickListener {
    public static final String EXTRA_AUDIT_ID =
            "com.bignerdranch.android.myviewpager.crime_id";


    private ImageView imageView_backArrow;
    private Button button_finish;

    private TextView textView_question;
    private RadioGroup radioGroup_judge;
    private RadioButton radioButton_yes;
    private RadioButton radioButton_no;
    private RecyclerView recyclerView_picture;
    private MyAdapterAuditFragment mAdapter;
    private EditText editText_find;
    private ImageButton imageButton_save;
    private Button button_back;
    private Button button_exit;
    private TextView textView_yesNumber;
    private TextView textView_noNumber;
    private TextView textView_proceed;

    private boolean radioGroupClick = false;

    private int auditId;
    private int maxSelectNum = 9;
    private int idAuditItem;


    private static final int SELECT_PHOTO = 1;
    private static final int SHOWBIG_PHOTO = 2;
    private static final int CAMERA_RESULT = 3;

    private long numberYes;
    private long numberNo;
    private long numberTotal;
    private long numberFinish;

    private String filePath = Environment.getExternalStorageDirectory() + "/AuditSomething";//excel和图片存放路径
    private String fileNameExcel = "/AuditAll.xls";//excel文件名字
    private String filePathExcel;//excel文件路径（filePaht+fileNameExcel），新建excel文件用
    private final static String UTF8_ENCODING = "UTF-8";
    private String stringAuditQuestion;
    private String stringAuditItem;
    private String stringAuditRes;
    private String photoPath;


    private AuditInfo auditInfo = new AuditInfo();
    private BasicInfo basicInfo = new BasicInfo();

    private AuditInfoDao auditInfoDao;
    private AuditPhotosDao auditPhotosDao;
    private AuditItemDao auditItemDao;
    private BasicInfoDao basicInfoDao;

    private List<AuditInfo> auditInfoList;
    private List<AuditItem> auditItemList;
    private List<AuditPhotos> auditPhotoList;//已选择图片存放的List
    private List<AuditPhotos> totalAuditPhotoList;//已选择图片存放的List
    private List<BasicInfo> basicInfoList;
    private ArrayList<String> mPictureList;//传到Bundle的list

    private Context mContext;
    private AlertDialog alertDialog;
    private Uri imageUri;
    private AuditPhotos auditPhotos;
    private int k;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mContext = getActivity();
        auditId = (int) getArguments().getSerializable(EXTRA_AUDIT_ID);
        initialDao();//初始化数据库
        auditInfo.setNumber(auditId);

        k = auditId;
        filePathExcel = filePath + fileNameExcel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_audit, container, false);

        initialView(v);//初始化控件

        Log.d("AuditFragment", "AUDITFRAGMENT" + "-->" + "auditId" + "-->" + auditId);
        renewAuditInfoList();//获得数据库数据至auditInfoList

        getBasicInfoList();//获得数据库数据至basicInforList(取得auditItemNum)


        //获得审核项目对应的id:idAuditItem
        if (basicInfoList.size() > 0) {
            idAuditItem = basicInfoList.get(0).getAuditItemNum();
        }

        getAuditItemList();//获得数据库数据至auditItemList

        //初始化审核问题textView_question，从auditInfoList中获取；
        if (auditItemList.size() > 0) {
            stringAuditItem = auditItemList.get(auditId).getAuditItem();
            textView_question.setText((auditId + 1) + "-" + " " + (auditItemList.get(auditId).getAuditItem()));
        }


        //初始化textView_yesNumber,textView_noNumber,textView_proceed，从basicInfoList众获取
        if (basicInfoList.size() > 0) {
            initiaTextView();
        }

        //符合性判断按钮的单击事件，并设置textView_yesNumber,textView_noNumber,textView_proceed
        radioGroup_judge.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                imageButton_save.setBackgroundResource(R.drawable.finishbefore);
                imageButton_save.setEnabled(true);

                if (i == radioButton_yes.getId()) {
                    stringAuditRes = "符合";
                    auditInfo.setAuditResult("符合");
                    auditInfo.setYesNumber(1);
                    auditInfo.setNoNumber(0);
                    radioButton_yes.setTextColor(getResources().getColorStateList(R.color.easy_photos_fg_accent));
                    radioButton_no.setTextColor(getResources().getColorStateList(R.color.black));
                    numberYes = 0;
                    numberNo = 0;


                    //更新数据库auditInfoDao与auditInfoList
                    renewAuditInfoList();


                    if (auditInfoList != null) {
                        for (int j = 0; j < auditInfoList.size(); j++) {
                            numberYes += auditInfoList.get(j).getYesNumber();
                            numberNo += auditInfoList.get(j).getNoNumber();
                        }
                    }
                    numberFinish = numberYes + numberNo;
                    String proceed = (numberFinish) + "/" + auditInfoList.size();

                    basicInfo.setId(2L);
                    basicInfo.setTotalYesNumber(numberYes);
                    basicInfo.setTotalNoNumber(numberNo);
                    basicInfo.setProceed(proceed);

                    renewBasicInfoList();//更新数据库BasicInfoDao和basicInfoList

                    textView_yesNumber.setText(String.valueOf(numberYes));
                    textView_noNumber.setText(String.valueOf(numberNo));
                    textView_proceed.setText(proceed);

                } else if (i == radioButton_no.getId()) {

                    stringAuditRes = "不符合";
                    auditInfo.setAuditResult("不符合");
                    auditInfo.setYesNumber(0);
                    auditInfo.setNoNumber(1);
                    radioButton_yes.setTextColor(getResources().getColorStateList(R.color.black));
                    radioButton_no.setTextColor(getResources().getColorStateList(R.color.easy_photos_fg_accent));
                    numberYes = 0;
                    numberNo = 0;


                    //更新数据库auditInfoDao与auditInfoList
                    renewAuditInfoList();

                    if (auditInfoList != null) {
                        for (int j = 0; j < auditInfoList.size(); j++) {
                            long numberY = auditInfoList.get(j).getYesNumber();
                            numberYes = numberY + numberYes;
                            long numberN = auditInfoList.get(j).getNoNumber();
                            numberNo = numberN + numberNo;
                        }
                    }
                    numberFinish = numberYes + numberNo;
                    String proceed = (numberFinish) + "/" + auditInfoList.size();

                    basicInfo.setId(2L);
                    basicInfo.setTotalYesNumber(numberYes);
                    basicInfo.setTotalNoNumber(numberNo);
                    basicInfo.setProceed(proceed);

                    renewBasicInfoList();//更新数据库BasicInfoDao和basicInfoList

                    textView_yesNumber.setText(String.valueOf(numberYes));
                    textView_noNumber.setText(String.valueOf(numberNo));
                    textView_proceed.setText(proceed);
                }
            }
        });

        //获得数据库图片至auditPhotoList
        getAuditPhotoList();

        //数据库auditPhotoList转入mPictureList（bundle）用
        mPictureList.clear();
        for (int i = 0; i < auditPhotoList.size(); i++) {
            mPictureList.add(auditPhotoList.get(i).getPhotoPath());
        }


        //设置recyclerView_picture显示样式
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView_picture.setLayoutManager(gridLayoutManager);

        //设置recyclerView添加线框
        recyclerView_picture.addItemDecoration(new MyDecoration());

        //将auditPhotoList中图片，adapter至recyclerView
        mAdapter = new MyAdapterAuditFragment(getContext(), auditPhotoList);
        recyclerView_picture.setAdapter(mAdapter);

        //图片与删除按钮的单击事件
        mAdapter.setOnItemClickListener(new MyAdapterAuditFragment.OnItemClickListener() {
            @Override
            public void OnTextClick(View view, int position) {
                imageButton_save.setBackgroundResource(R.drawable.finishbefore);
                imageButton_save.setEnabled(true);

                if (auditPhotoList.size() > 0) {
                    auditPhotosDao.delete(auditPhotoList.get(position));
                    getAuditPhotoList();
                    mAdapter.setAdapterList(auditPhotoList);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void OnImageClick(View view, int position) {

                if (position == auditPhotoList.size()) {
                    imageButton_save.setBackgroundResource(R.drawable.finishbefore);
                    imageButton_save.setEnabled(true);

                    if (auditPhotoList.size() != maxSelectNum) {
                        showDialog();//照片方式选择对话框
                    }
                } else {
                    Intent intent = new Intent(getActivity(), ShowBigPictureActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("allphotos", mPictureList);
                    bundle.putInt("whichActivity", 2000);
                    bundle.putInt("number", position);
                    intent.putExtras(bundle);

                    startActivityForResult(intent, SHOWBIG_PHOTO);
                }
            }
        });

//        imageButton_save.setVisibility(View.INVISIBLE);
        //完成按钮和保存按钮的背景设置（imageButton_finish，imageButton_save）
//        if (auditId == (auditItemList.size() - 1)) {
//            imageButton_save.setVisibility(View.VISIBLE);
//            imageButton_save.setBackgroundResource(R.drawable.finishbefore);
//        } else {
//            imageButton_save.setVisibility(View.INVISIBLE);
//        }


        //审核发现的输入监听，并把数据传至audiInfo对象（editText_find）
        editText_find.addTextChangedListener(new TextWatcher() {//获得审核结果
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                auditInfo.setAuditFind(s.toString());
                imageButton_save.setBackgroundResource(R.drawable.finishbefore);
                imageButton_save.setEnabled(true);
                //更新数据库auditInfoDao与auditInfoList
                renewAuditInfoList();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //保存按钮的单击监听（imageButton_finish）
        imageButton_save.setOnClickListener(this);

        //返回登录页按钮的单击监听（imageButton_finish）
        button_back.setOnClickListener(this);
        button_exit.setOnClickListener(this);

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (resultCode != Activity.RESULT_OK) {
//            return;
//        }

        switch (requestCode) {
            case SELECT_PHOTO:
                //获得数据库图片至auditPhotoList
                getAuditPhotoList();

                //数据库auditPhotoList转入mPictureList（bundle）用
                mPictureList.clear();

                for (int i = 0; i < auditPhotoList.size(); i++) {
                    mPictureList.add(auditPhotoList.get(i).getPhotoPath());
                }

                if (null != auditPhotoList) {
                    mAdapter.setAdapterList(auditPhotoList);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case SHOWBIG_PHOTO:
                //获得数据库图片至auditPhotoList
                getAuditPhotoList();

//                //数据库auditPhotoList转入mPictureList（bundle）用
//                for (int i = 0; i < auditPhotoList.size(); i++) {
//                    mPictureList.add(auditPhotoList.get(i).getPhotoPath());
//                }

                if (null != auditPhotoList) {
                    mAdapter.setAdapterList(auditPhotoList);
                    mAdapter.notifyDataSetChanged();
                }
                break;

            case CAMERA_RESULT:

                //获得拍照结果，并存入数据库
                if (auditPhotoList.size() < maxSelectNum) {
                    auditPhotos.setIdAuditPhotos(auditId);
                    auditPhotos.setId(auditPhotoList.size() + 0L);
                    auditPhotos.setPhotoPath(photoPath);
                    auditPhotosDao.insert(auditPhotos);
                }

                getAuditPhotoList();

                mPictureList.clear();
                for (int i = 0; i < auditPhotoList.size(); i++) {
                    mPictureList.add(auditPhotoList.get(i).getPhotoPath());
                }

                if (null != auditPhotoList) {

                    mAdapter.setAdapterList(auditPhotoList);
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }


    //初始化textView_yesNumber,textView_noNumber,textView_proceed，从basicInfoList众获取
    private void initiaTextView() {
        numberYes = basicInfoList.get(1).getTotalYesNumber();
        numberNo = basicInfoList.get(1).getTotalNoNumber();
        numberFinish = numberYes + numberNo;
        textView_yesNumber.setText(String.valueOf(numberYes));
        textView_noNumber.setText(String.valueOf(numberNo));
        textView_proceed.setText(basicInfoList.get(1).getProceed());
        radioButton_yes.setTextColor(getResources().getColorStateList(R.color.black));
        radioButton_no.setTextColor(getResources().getColorStateList(R.color.black));
    }


    //初始化控件
    private void initialView(View v) {
        imageView_backArrow = v.findViewById(R.id.imageView_auditfragment_arrowBack);//返回箭头
        textView_question = v.findViewById(R.id.textView_auditfragment_question);//审核项目
        radioGroup_judge = v.findViewById(R.id.radiogroup_auditfragment_judge);//符合判断按钮
        radioButton_yes = v.findViewById(R.id.radioButton_auditfragment_yes);//符合按钮
        radioButton_no = v.findViewById(R.id.radioButton_auditfragment_no);//不符合按钮
        recyclerView_picture = v.findViewById(R.id.recyclerView_auditFragment_auditPicture);//审核图片
        editText_find = v.findViewById(R.id.audit_fragment_find);//审核文字问题点
        imageButton_save = v.findViewById(R.id.imageButton_auditfragment_save);//暂存按钮，数据暂存至excel
        button_back = v.findViewById(R.id.button_auditfragment_back);//返回值登录页面
        button_exit = v.findViewById(R.id.button_auditfragment_exit);//返回值登录页面
        textView_yesNumber = v.findViewById(R.id.textview_auditfragment_yesnumber);//符合的项目数
        textView_noNumber = v.findViewById(R.id.textview_auditfragment_nonumber);//不符合的项目数
        textView_proceed = v.findViewById(R.id.textview_auditfragment_proceed);//审核进度，完成/总数

        imageButton_save.setEnabled(true);
        imageButton_save.setBackgroundResource(R.drawable.finishbefore);

        auditInfoList = new ArrayList<>();
        auditItemList = new ArrayList<>();
        auditPhotoList = new ArrayList<>();//已选择图片存放的List
        basicInfoList = new ArrayList<>();
        mPictureList = new ArrayList<>();//传到Bundle的list

        auditPhotos = new AuditPhotos();

    }


    //单击监听
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imageButton_auditfragment_save:

                imageButton_save.setBackgroundResource(R.drawable.finishafter);
                //更新auditInfoList
                renewAuditInfoList();

                getBasicInfoList();

                getTotalAuditPhotoList();

                boolean tempb = false;
                //在最后一页，将数据写入excel,保存并分享文件
                if (auditId == (auditItemList.size() - 1)) {
                    for (int j = 0; j < auditInfoList.size(); j++) {
                        tempb = (auditInfoList.get(j).getYesNumber() > 0) ||
                                ((auditInfoList.get(j).getNoNumber()) > 0);
                        if (!tempb) {//判断符合性是否已经判断
                            Toast.makeText(mContext, "P" + (j + 1) + "未判断符合性", Toast.LENGTH_SHORT).show();
                            imageButton_save.setBackgroundResource(R.drawable.finishbefore);
                            imageButton_save.setEnabled(true);
                        }
                    }
                }


                if (tempb) {
                    initialExcel(basicInfoList);//初始化excel

                    //开辟数据写入excel线程，并写入数据
                    Thread thread1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ExcelUtil.writeAllAuditToExcel(auditInfoList, auditItemList, filePathExcel);
                        }
                    });
                    thread1.start();

                    //开辟图片写入excel线程
                    Thread thread2 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < auditItemList.size(); i++) {
                                List<AuditPhotos> tempList = new ArrayList();
                                tempList = auditPhotosDao.queryBuilder().where(AuditPhotosDao.Properties.IdAuditPhotos.eq(i))
                                        .list();
                                if (tempList.size() > 0) {
                                    ExcelUtil.writePictureToExcel(tempList, filePath, i, filePathExcel, auditInfoList.size() - 1);
                                }
                            }
                        }
                    });

                    //判断数据是否写入excel
                    while (true) {
                        if (!(thread1.isAlive())) {
                            thread2.start();
                            break;
                        }
                    }

                    //判断数据是否都写入完成，写入完成后结束程序
                    while (true) {
                        if (thread2.isAlive()) {

                        } else {
                            shareFiles(mContext, new File(filePath + fileNameExcel));//分享Excel
                            break;
                        }
                    }
                }
                imageButton_save.setEnabled(false);
                break;
            case R.id.button_auditfragment_exit:
                Intent k = new Intent(getActivity(), FinishActivity.class);
                startActivity(k);
                getActivity().finish();
                break;

            case R.id.button_auditfragment_back:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }

    //初始化Excel
    private void initialExcel(List<BasicInfo> basicInfoList) {
        //初始化excel
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        filePathExcel = filePath + fileNameExcel;
        String[] title = {"序号", "审核项目", "审核结论", "审核发现"};
        String[] basicInfoItem = {"审核人", "审核时间", "符合情况", "审核对象", "审核层级"};
        String sheetName = "auditSheet";

        // 初始化excel
        ExcelUtil.initExcel(filePathExcel, sheetName, title, basicInfoItem, basicInfoList);
    }


    /**
     * 依据auditId创建新的AuditFragment实例
     *
     * @param auditId
     */
    public static AuditFragment newInstance(int auditId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_AUDIT_ID, auditId);

        AuditFragment fragment = new AuditFragment();
        fragment.setArguments(args);
        return fragment;
    }


    /*
     * 文件转字符数组
     * @param filePath
     *
     * */
    public static byte[] fileToByteArray(String filePath) {
        File src = new File(filePath);
        byte[] dest = null;

        InputStream is = null;
        ByteArrayOutputStream baos = null;

        try {
            is = new FileInputStream(src);
            baos = new ByteArrayOutputStream();

            byte[] flush = new byte[1024 * 10];
            int len = -1;
            while ((len = is.read(flush)) != -1) {
                baos.write(flush, 0, len);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
     * 字符数组转文件
     * @param src,filepath
     * */

    public static void byteArrayToFile(byte[] src, String filePath) {
        File dest = new File(filePath);

        InputStream is = null;
        OutputStream os = null;
        try {
            is = new ByteArrayInputStream(src);
            os = new FileOutputStream(dest);

            byte[] flush = new byte[5];
            int len = -1;
            while ((len = is.read(flush)) != -1) {
                os.write(flush, 0, len);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os) {
                }
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //获得数据库数据，并放入auditPhotoList中
    private void getAuditPhotoList() {
        if (auditPhotosDao != null) {
            auditPhotoList = auditPhotosDao.queryBuilder()
                    .where(AuditPhotosDao.Properties.IdAuditPhotos.eq(auditId))
                    .list();
        }
    }

    private void getTotalAuditPhotoList() {
        if (auditPhotosDao != null) {
            totalAuditPhotoList = auditPhotosDao.queryBuilder().list();
        }
    }


    //获得数据库数据至auditInfoList
    private void getAuditInfoList() {
        if (auditInfoDao != null) {
            auditInfoList = auditInfoDao.queryBuilder().list();
        }
    }


    //获得数据库数据至auditItemList
    private void getAuditItemList() {
        if (auditItemDao != null) {
            auditItemList = auditItemDao.queryBuilder()
                    .where(AuditItemDao.Properties.IdAuditItem.eq(idAuditItem))
                    .list();
        }
    }


    //获得数据库数据至basicInfoList
    private void getBasicInfoList() {
        if (basicInfoDao != null) {
            basicInfoList = basicInfoDao.queryBuilder().list();
        }
    }


    //更新auditInfoList
    private void renewBasicInfoList() {
        basicInfoList.clear();
        basicInfo.setId(2L);
        basicInfoDao.insertOrReplace(basicInfo);

        if (basicInfoDao != null) {
            basicInfoList = basicInfoDao.queryBuilder().list();
        }
    }


    //更新auditInfoList
    private void renewAuditInfoList() {
        auditInfoList.clear();
        auditInfo.setId(auditId + 0L);
        auditInfoDao.insertOrReplace(auditInfo);

        if (auditInfoDao != null) {
            auditInfoList = auditInfoDao.queryBuilder().list();
        }
    }


    //初始化数据库
    private void initialDao() {
        DaoSession daoSession = ((App) getActivity().getApplication()).getDaoSession();
        daoSession.clear();
        auditInfoDao = daoSession.getAuditInfoDao();
        auditPhotosDao = daoSession.getAuditPhotosDao();
        basicInfoDao = daoSession.getBasicInfoDao();
        auditItemDao = daoSession.getAuditItemDao();
    }


    //RecyclerView添加分隔线
    class MyDecoration extends RecyclerView.ItemDecoration {
        //复写getItemOffsets方法
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int length = getResources().getDimensionPixelOffset(R.dimen.dividerHeight);
            outRect.set(length, length, length, length);
        }
    }

    /**
     * 对话框提示
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showDialog() {

        // 构建dialog显示的view布局
        View view_dialog = getLayoutInflater().from(mContext).inflate(R.layout.view_alertdialog, null);

        Button buttonAlertDialogAlbum = view_dialog.findViewById(R.id.button_view_alertdialog_album);
        Button buttonAlertDialogTakePhoto = view_dialog.findViewById(R.id.button_view_alertdialog_takePhoto);

        buttonAlertDialogAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();//提示框消失
                Intent intent = new Intent(getActivity(), SelectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("auditId", auditId);
                intent.putExtras(bundle);
                startActivityForResult(intent, SELECT_PHOTO);

            }
        });

        buttonAlertDialogTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();//提示框消失

                File file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }

                File photoDir = new File(filePath, System.currentTimeMillis() + "audit.jpg");

                photoPath = photoDir.toString();

                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(mContext,
                            "com.faraway.auditall.fileProvider", photoDir);
                } else {
                    imageUri = Uri.fromFile(photoDir);
                }

                //启动相机拍照，存入photoPath;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, CAMERA_RESULT);

            }
        });


        if (alertDialog == null) {
            // 创建AlertDialog对象

            alertDialog = new AlertDialog.Builder(mContext)
                    .create();

            alertDialog.show();

            // 设置点击可取消
            alertDialog.setCancelable(true);

            // 获取Window对象
            Window window = alertDialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

         /*   WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();

            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            params.height = displayMetrics.widthPixels - 800;
            params.width = (int) (params.height * 1.3);
            window.setAttributes(params);*/
            // 设置显示视图内容
            window.setContentView(view_dialog);
        } else {
            alertDialog.show();
        }
    }


    public void shareFiles(Context context, File file) {
        if (file != null && file.exists()) {
            Intent shareIntent = new Intent();//创建intent
            shareIntent.setAction(Intent.ACTION_SEND);//指定intent类型
            Uri uri;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(context, "com.faraway.auditall.fileProvider", file);
            } else {
                uri = Uri.fromFile(file);
            }

            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);//放入uri
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//增加标志
            shareIntent.setType("*/*");//设置样式
            Log.d("AuditFragment", "AUDITFRAGMENT" + "-->" + "shareFiles" + "-->" + auditId);

            context.startActivity(Intent.createChooser(shareIntent, "分享到"));//启动分享
        }
    }


}






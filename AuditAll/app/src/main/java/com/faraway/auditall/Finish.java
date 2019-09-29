package com.faraway.auditall;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.faraway.auditall.Utils.ExcelUtil;

import java.io.File;

/**
 * @author ${Faraway}
 * @description审核完成界面
 * @date ${data}
 **/

public class Finish extends AppCompatActivity implements View.OnClickListener {
    private TextView mTextView;
//    private Audit mAudit;
//    private Audit mAudit1;
//    private ArrayList<Audit> mAudits;
    private Button mFinishButton;
    private String filePath = Environment.getExternalStorageDirectory() + "/AuditSomething";
    private String fileName = "/audit.xls";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

//        mAudits = AuditLab.get(this).getAudits();

        mTextView = findViewById(R.id.finish_tv1);
        mFinishButton = findViewById(R.id.btn_content_finish);
        mFinishButton.setOnClickListener(this);

//        mAudit = AuditLab.get(this).getAudit(0);
//        mAudit1 = AuditLab.get(this).getAudit(1);
//        mTextView.setText(mAudit.getFind() + mAudit1.getFind());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_content_finish:
                exportExcel(this);//导出数据至Excel
                shareFiles(this, new File(filePath + fileName));//分享Excel
                break;
            default:
                break;
        }
    }

    private void openDir() {

        File file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setDataAndType(Uri.fromFile(file), "file/");
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "没有正确打开文件管理器", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportExcel(Context context) {

        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        String[] title = {"姓名", "年龄", "男孩"};
        String sheetName = "auditSheet";

        filePath = filePath + fileName;

//        ExcelUtil.initExcel(filePath, sheetName, title);
//        ExcelUtil.writeObjListToExcel(mAudits, filePath, context);
        filePath = Environment.getExternalStorageDirectory() + "/AuditSomething";
    }


    public void shareFiles(Context context, File file) {
        if (file != null && file.exists()) {
            Intent shareIntent = new Intent();//创建intent
            shareIntent.setAction(Intent.ACTION_SEND);//指定intent类型
            Uri uri;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(context, "com.bignerdranch.android.myviewpager01.fileProvider", file);
            } else {
                uri = Uri.fromFile(file);
            }

            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);//放入uri
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//增加标志
            shareIntent.setType("*/*");//设置样式
            context.startActivity(Intent.createChooser(shareIntent, "分享到"));//启动分享
        }
    }

}

package com.faraway.auditall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.faraway.auditall.DataBase.App;
import com.faraway.auditall.DataBase.AuditInfoDao;
import com.faraway.auditall.DataBase.AuditPhotosDao;
import com.faraway.auditall.DataBase.BasicInfoDao;
import com.faraway.auditall.DataBase.DaoSession;
import com.faraway.auditall.Utils.FileUtils;

import java.util.Timer;
import java.util.TimerTask;
/**
 * @author ${Faraway}
 * @description请求系统权限许可，删除以往数据；并跳转至登录界面
 * @date ${String[] permissions:需许可权限数组}
 */

public class InitialPageActivity extends AppCompatActivity {
    private AlertDialog alertDialog;
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ,Manifest.permission.CAMERA};

    private String filePath = Environment.getExternalStorageDirectory() + "/AuditSomething";

    private int REQUEST_PERMISSION_CODE = 1000;

//    private AuditInfoDao auditInfoDao;
//    private AuditPhotosDao auditPhotosDao;
//    private BasicInfoDao basicInfoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialpage);

        Log.d("InitialPageActivity","isExit:"+000);


//        initialDao();


    }

    @Override
    protected void onStart() {
        Log.d("InitialPageActivity","onStart:"+111);
        super.onStart();
        requestPermission();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isExit = intent.getBooleanExtra("TAG_EXIT", false);
            Log.d("InitialPageActivity","isExit:"+isExit);
            if (isExit) {
                this.finish();
                System.exit(0);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                FileUtils.deleteDir(filePath);//删除excel文件夹
                final Intent it = new Intent(this, LoginActivity.class); //你要转向的Activity
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(it); //执行
                    }
                };
                timer.schedule(task,1000 * 3);
                Log.i("onPermissionsResult:", "权限" + permissions[0] + "申请成功");
            } else {
                Log.i("onPermissionsResult:", "用户拒绝了申请权限");
                AlertDialog.Builder builder = new AlertDialog.Builder(InitialPageActivity.this);
                builder.setTitle("permission")
                        .setMessage("点击允许才可以哦")
                        .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (alertDialog != null && alertDialog.isShowing()) {
                                    alertDialog.dismiss();
                                }
                                ActivityCompat.requestPermissions(InitialPageActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            }
                        });
                alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        }
    }

    private void showDialogTipUserRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }



    private void requestPermission() {
        if (Build.VERSION.SDK_INT > 23) {
            if (ContextCompat.checkSelfPermission(InitialPageActivity.this, permissions[0])
                    == PackageManager.PERMISSION_GRANTED) {
                Log.i("requestPermission:", "用户之前已经授予了权限！");
                final Intent it = new Intent(this, LoginActivity.class); //你要转向的Activity
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(it); //执行
                    }
                };
                timer.schedule(task,1000 * 3);
            } else {
                Log.i("requestPermission", "未获得权限，现在申请！");
                requestPermissions(permissions, REQUEST_PERMISSION_CODE);
            }
        }
    }
    //初始化数据库
//    private void initialDao() {
//        DaoSession daoSession = ((App) getApplication()).getDaoSession();
//        auditInfoDao = daoSession.getAuditInfoDao();
//        auditPhotosDao = daoSession.getAuditPhotosDao();
//        basicInfoDao = daoSession.getBasicInfoDao();
//    }

}

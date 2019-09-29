package com.faraway.auditall;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.faraway.auditall.DataBase.App;
import com.faraway.auditall.DataBase.AuditInfo;
import com.faraway.auditall.DataBase.AuditInfoDao;
import com.faraway.auditall.DataBase.AuditItem;
import com.faraway.auditall.DataBase.AuditItemDao;
import com.faraway.auditall.DataBase.AuditPhotosDao;
import com.faraway.auditall.DataBase.BasicInfo;
import com.faraway.auditall.DataBase.BasicInfoDao;
import com.faraway.auditall.DataBase.DaoSession;
import com.faraway.auditall.Utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ${Faraway}
 * @description审核主界面AuditFramgent的宿主
 * @date ${data}
 **/
public class AuditPagerActivity extends FragmentActivity {
    private ViewPager mViewPager;//存放审核页面AuditFragment

    private AuditInfoDao auditInfoDao;
    private AuditPhotosDao auditPhotosDao;
    private BasicInfoDao basicInfoDao;
    private AuditItemDao auditItemDao;


    private AuditInfo auditInfo;
    private String filePath = Environment.getExternalStorageDirectory() + "/AuditSomething";

    private int idAuditItem;

    private List<BasicInfo> basicInfoList = new ArrayList<>();
    private List<AuditItem> auditItemList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialDao();//初始化数据库

        getBasicInfoList();
        if (basicInfoList.size()>0){
            idAuditItem = basicInfoList.get(0).getAuditItemNum();
        }

        getAuditItemList();

        //删除以往审核数据
        FileUtils.deleteDir(filePath);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int id) {
                return AuditFragment.newInstance(id);
            }

            @Override
            public int getCount() {
                return auditItemList.size();
            }
        });
    }

    //初始化数据库
    private void initialDao() {
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        daoSession.clear();
        basicInfoDao = daoSession.getBasicInfoDao();
        auditItemDao = daoSession.getAuditItemDao();
    }

    //获得数据库数据至basicInfoList
    private void getBasicInfoList() {
        if (basicInfoDao != null) {
            basicInfoList = basicInfoDao.queryBuilder().list();
        }
    }


    //初始化AuditInfoList
    private void getAuditItemList() {
        if (auditItemDao != null) {
            auditItemList = auditItemDao.queryBuilder()
                    .where(AuditItemDao.Properties.IdAuditItem.eq(idAuditItem))
                    .list();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
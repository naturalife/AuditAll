package com.faraway.auditall.DataBase;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fan
 * @description数据库单例
 * @date 2019-09-02
 * @time 14:04
 **/
public class App extends Application {
    private DaoSession daoSession;
    private  List<String> auditItemList;
    private Map<String,ArrayList<String>> auditItemMap;

    @Override
    public void onCreate() {
        super.onCreate();
        auditItemList = new ArrayList<>();
        auditItemMap = new HashMap<>();//返回的Map

        // regular SQLite database
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db");
        Database db = helper.getWritableDb();

        // encrypted SQLCipher database
        // note: you need to add SQLCipher to your dependencies, check the build.gradle file
        // DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db-encrypted");
        // Database db = helper.getEncryptedWritableDb("encryption-key");

        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public Map<String,ArrayList<String>> getAuditItemMap(){
        return auditItemMap;
    }
}

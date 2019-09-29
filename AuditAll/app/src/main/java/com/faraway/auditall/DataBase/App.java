package com.faraway.auditall.DataBase;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fan
 * @description数据库单例
 * @date 2019-09-02
 * @time 14:04
 **/
public class App extends Application {
    private DaoSession daoSession;
    private  List<File> allPictureFilePngList;

    @Override
    public void onCreate() {
        super.onCreate();
        allPictureFilePngList = new ArrayList<>();

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

    public List<File> getAllPictureFilePngList(){
        return allPictureFilePngList;
    }
}

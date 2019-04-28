package com.clz.oxforddic.model.db;

import android.arch.persistence.room.Room;

import com.clz.oxforddic.app.BaseApplication;

/**
 * Create by stevcao on 2019/4/28
 */
public class DataBaseHelper {

    static DataBaseHelper sDataBaseHelper;
    WordDataBase mDb;

    private DataBaseHelper(){}

    public static final DataBaseHelper getInstance() {
        if (sDataBaseHelper == null) {
            sDataBaseHelper = new DataBaseHelper();
        }
        return sDataBaseHelper;
    }

    public WordDao getWordDao() {
        if (!isInit) {
            init();
        }
        return mDb.getWordDao();
    }

    boolean isInit = false;

    public void init() {
        if (!isInit) {
            mDb = Room.databaseBuilder(BaseApplication.sContext, WordDataBase.class, "word_db").build();
            isInit = true;
        }
    }

    public void destory() {
        mDb.close();
        mDb = null;
    }

}

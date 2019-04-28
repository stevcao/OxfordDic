package com.clz.oxforddic.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.clz.oxforddic.model.entity.Word;

/**
 * Create by stevcao on 2019/4/28
 */
@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class WordDataBase extends RoomDatabase{

    public abstract WordDao getWordDao();

}

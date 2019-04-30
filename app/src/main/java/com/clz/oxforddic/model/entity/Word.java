package com.clz.oxforddic.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Create by stevcao on 2019/4/28
 */
@Entity(tableName = "Word")
public class Word {

    @NonNull
    @PrimaryKey
    public String _id;

    @NonNull
    @ColumnInfo(name = "word")
    public String word;

    @ColumnInfo(name = "wordLowerCase")
    public String wordLowerCase;

    @ColumnInfo(name = "html")
    public String html;

}

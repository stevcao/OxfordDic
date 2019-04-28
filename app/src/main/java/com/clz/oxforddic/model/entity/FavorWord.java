package com.clz.oxforddic.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Create by stevcao on 2019/4/28
 */
@Entity(tableName = "FavorWord")
public class FavorWord {

    @NonNull
    @PrimaryKey
    public String _id;

    @ColumnInfo(name = "wordId")
    public String wordId;

    @ColumnInfo(name = "comment")
    public String comment;
}
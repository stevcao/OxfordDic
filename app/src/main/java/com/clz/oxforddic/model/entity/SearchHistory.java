package com.clz.oxforddic.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Create by stevcao on 2019/4/28
 */
@Entity(tableName = "SearchHistory")
public class SearchHistory {

    @NonNull
    @PrimaryKey
    String searchKey;

    @ColumnInfo(name = "wordId")
    String wordId;

    @ColumnInfo(name = "searchTime")
    String searchTime;

}

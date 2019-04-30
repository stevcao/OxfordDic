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

    public SearchHistory(@NonNull String searchKey, String wordId, long searchTime) {
        this.searchKey = searchKey;
        this.wordId = wordId;
        this.searchTime = searchTime;
    }

    @NonNull
    @PrimaryKey
    public String searchKey;

    @ColumnInfo(name = "wordId")
    public String wordId;

    @ColumnInfo(name = "searchTime")
    public long searchTime;

}

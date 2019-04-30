package com.clz.oxforddic.model.db;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.clz.oxforddic.model.entity.FavorWord;

import java.util.List;

@Dao
public interface FavorDao {

    @Query("SELECT * FROM FavorWord")
    List<FavorWord> getFavorWords();


    @Query("SELECT * FROM FavorWord WHERE wordId=:wordId")
    FavorWord getFavorWord(String wordId);

    @Query("SELECT * FROM FavorWord ORDER BY word COLLATE NOCASE ASC")
    DataSource.Factory<Integer, FavorWord> getFavorWordsDataFactory();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addFavor(FavorWord favorWord);

    @Delete
    public void removeFavor(FavorWord... favorWord);
}

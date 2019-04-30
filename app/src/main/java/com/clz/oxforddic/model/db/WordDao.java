package com.clz.oxforddic.model.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.clz.oxforddic.model.entity.Word;

import java.util.List;

/**
 * Create by stevcao on 2019/4/28
 */
@Dao
public interface WordDao {

    @Query("SELECT * FROM Word")
    public List<Word> getAll();


    @Query("SELECT _id, word FROM Word")
    public List<Word> getAllOnlyWord();

    @Query("SELECT * FROM Word where word = :word")
    public List<Word> queryWord(String word);

    @Query("SELECT * FROM Word where _id = :id")
    public Word queryWordById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Word ... words);

    @Query("DELETE FROM Word")
    public void clear();

    @Delete
    public void delete(Word ...word);

}

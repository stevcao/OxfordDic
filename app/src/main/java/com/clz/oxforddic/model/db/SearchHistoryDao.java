package com.clz.oxforddic.model.db;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.clz.oxforddic.model.entity.SearchHistory;

import java.util.List;

@Dao
public interface SearchHistoryDao {

    @Query("SELECT * FROM SearchHistory")
    public List<SearchHistory> getAllHistorySearch();

    @Query("SELECT * FROM SearchHistory ORDER BY searchTime DESC")
    public DataSource.Factory<Integer, SearchHistory> getSearchHistoryDataFactory();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertHistory(SearchHistory history);

    @Delete
    public void deleteHistory(SearchHistory... history);


    @Query("DELETE FROM SearchHistory")
    public void clearHistory();
}

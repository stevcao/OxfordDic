package com.clz.oxforddic.model.repository;

import com.clz.oxforddic.model.db.DataBaseHelper;
import com.clz.oxforddic.model.entity.SearchHistory;
import com.clz.oxforddic.model.entity.Word;

public class HistoryRepository {

    /**
     * 保存搜索历史记录
     * @param keyWord
     * @param result
     */
    public void saveHistory(String keyWord, Word result) {
        SearchHistory history = new SearchHistory(keyWord, result._id, System.currentTimeMillis());
        DataBaseHelper.getInstance().getHistoryDao().insertHistory(history);
    }

}

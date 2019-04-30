package com.clz.oxforddic.model.repository;

import com.clz.oxforddic.model.db.DataBaseHelper;
import com.clz.oxforddic.model.db.WordDao;
import com.clz.tri.TriTreeHelper;

import java.util.ArrayList;
import java.util.List;

public class WordRepository {

    private TriTreeHelper parser;

    private volatile boolean isReady = false;

    WordRepository() {
        parser = new TriTreeHelper();
        Thread loadThread = new Thread() {
            @Override
            public void run() {
                load();
                isReady = true;
            }
        };
        loadThread.start();
    }

    private void load() {
        WordDao dao = DataBaseHelper.getInstance().getWordDao();
        parser.parse(dao.getAllOnlyWord());
    }

    public List<String> getAssociatedKey(String key) {
        List<String> associatedKeys = new ArrayList<>();
        if (isReady) {
            return parser.getAssociatedKeys(key, 20);
        }
        return associatedKeys;
    }

}

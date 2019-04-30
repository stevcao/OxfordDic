package com.clz.oxforddic.model.repository;

public class RepositoryManager {

    private RepositoryManager(){
        mFavorRepository = new FavorRepository();
        mHistoryRepository = new HistoryRepository();
        mWordRepository = new WordRepository();
    }

    private static RepositoryManager sInstance;

    public static RepositoryManager getInstance() {
        if (sInstance == null) {
            sInstance = new RepositoryManager();
        }
        return sInstance;
    }

    FavorRepository mFavorRepository;
    HistoryRepository mHistoryRepository;
    WordRepository mWordRepository;

    public FavorRepository getFavorRepository() {
        return mFavorRepository;
    }

    public HistoryRepository getHistoryRepository() {
        return mHistoryRepository;
    }

    public WordRepository getWordRepository() {
        return mWordRepository;
    }
}

package com.clz.oxforddic.model.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.clz.oxforddic.model.db.DataBaseHelper;
import com.clz.oxforddic.model.entity.FavorWord;
import com.clz.oxforddic.model.entity.Word;

import java.util.List;

public class FavorRepository {

    LiveData<List<FavorWord>> mFavorWords;

    FavorRepository() {
        new Thread() {
            @Override
            public void run() {
                mFavorWords = DataBaseHelper.getInstance().getFavorDao().getFavorWords();
                mFavorWords.observeForever(new Observer<List<FavorWord>>() {//保证有一个激活的Observer
                    @Override
                    public void onChanged(@Nullable List<FavorWord> favorWords) {
//                        mFavorWords.removeObserver(this);
                        Log.d("stevcao", "favor changed!");
                    }
                });
            }
        }.start();
    }

    public boolean isFavor(Word word) {
        if (mFavorWords != null) {
            List<FavorWord> favorWords = mFavorWords.getValue();
            if (favorWords != null) {
                for (FavorWord favorWord : favorWords) {
                    if (favorWord.wordId.equals(word._id)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void addFavor(final Word word) {
        final FavorWord favorWord = new FavorWord();
        favorWord.addTime = System.currentTimeMillis();
        favorWord.word = word.word;
        favorWord.wordId = word._id;

        new Thread() {
            @Override
            public void run() {
                DataBaseHelper.getInstance().getFavorDao().addFavor(favorWord);
            }
        }.start();

    }

    public void removeFavor(Word word) {
        final FavorWord favorWord = new FavorWord();
        favorWord.addTime = System.currentTimeMillis();
        favorWord.word = word.word;
        favorWord.wordId = word._id;

        new Thread() {
            @Override
            public void run() {
                DataBaseHelper.getInstance().getFavorDao().removeFavor(favorWord);
            }
        }.start();
    }

}

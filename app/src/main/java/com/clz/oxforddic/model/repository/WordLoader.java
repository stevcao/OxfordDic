package com.clz.oxforddic.model.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.clz.oxforddic.app.BaseApplication;
import com.clz.oxforddic.model.db.DataBaseHelper;
import com.clz.oxforddic.model.entity.Word;
import com.clz.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * Create by stevcao on 2019/4/28
 */
public class WordLoader {

    public static final String TAG = "WordLoader";

    private static final float COPY_BASE = 0.25F;
    private static final float WRITE_BASE = 0.75F;
    private static final String EXCEL_NAME = "Oxford_Words";
    private static final String EXCEL_SUFFIX = ".xls";
    private static final String SD_CARD_DIR = Environment.getExternalStorageDirectory() + File.separator + "Oxford";
    private static final String SP_KEY_DATABASE_READY = "dbready";
    private static final String SP_KEY_ISCOPY = "iscopy";
    private static final String SP_NAME = "config";

    SharedPreferences sp;
    LoadCallback mCallback;

    public WordLoader(Context context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    private void copyAssertToSD() {
        File file = new File(SD_CARD_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            String[] fileNames = BaseApplication.sContext.getAssets().list("");
            if (fileNames.length > 0) {
                for (String fileName : fileNames) {
                    if (!fileName.startsWith(EXCEL_NAME) || !fileName.endsWith(EXCEL_SUFFIX)) {
                        continue;
                    }
                    FileUtils.moveAssetToSD(BaseApplication.sContext, fileName, SD_CARD_DIR + File.separator + fileName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startLoad(LoadCallback callback) {
        if (mCallback != null) {
            Log.d(TAG, "allready loading!");
            return;
        }
        this.mCallback = callback;
        Thread t = new Thread() {
            @Override
            public void run() {
                realStart();
            }
        };
        t.start();
    }

    public void realStart() {
        boolean isCopy = isCopyToSD();
        if (!isCopy) {
            copyAssertToSD();
            mCallback.onLoadProgress((int)(COPY_BASE * 100));
            sp.edit().putBoolean(SP_KEY_ISCOPY, true).commit();
        }
        if (!isRead()) {
            readExcelAndStoreIntoDb();
            sp.edit().putBoolean(SP_KEY_DATABASE_READY, true).commit();
            mCallback.onLoadProgress((int)((COPY_BASE + WRITE_BASE) * 100));
        }
        mCallback.onLoadComplete();
    }

    /**
     * 数据是否准备OK
     * @return
     */
    public boolean isRead() {
        return sp.getBoolean(SP_KEY_DATABASE_READY, false);
//        return false;
    }

    public boolean isCopyToSD() {
        return sp.getBoolean(SP_KEY_ISCOPY, false);
//        return false;
    }

    private void readExcelAndStoreIntoDb() {
        long timeStart = System.currentTimeMillis();
        File dir = new File(SD_CARD_DIR);
        if(dir.exists()) {
            String[] fileList = dir.list();
            int fileCount = fileList.length;
            int completeCount = 0;
            DataBaseHelper.getInstance().getWordDao().clear();
            for (String fileName : fileList) {
                if (fileName.endsWith(EXCEL_SUFFIX)) {
                    File excelFile = new File(dir.getAbsoluteFile() + File.separator + fileName);
                    try {
                        Sheet sheet = Workbook.getWorkbook(excelFile).getSheet(0);
                        int rowCount = sheet.getRows();
                        List<Word> words = new ArrayList<>();
                        for (int row = 0; row < rowCount; row++) {
                            Cell[] cells = sheet.getRow(row);
                            Word word = new Word();
                            for (int i = 0; i < cells.length; i++) {
                                if (i == 0) {
                                    word.word = cells[0].getContents();
                                    word.wordLowerCase = word.word.toLowerCase();
                                    if (TextUtils.isEmpty(word.word)) break;
                                } else if (i == 1) {
                                    word.html = cells[1].getContents();
                                } else {
                                    break;
                                }
                            }
                            if (!TextUtils.isEmpty(word.word) && !TextUtils.isEmpty(word.html)) {
                                word._id = word.word + System.currentTimeMillis();
                                words.add(word);
                            }
                        }
                        Word[] wordArray = new Word[words.size()];
                        for (int i = 0; i < wordArray.length; i++) {
                            wordArray[i] = words.get(i);
                        }
                        DataBaseHelper.getInstance().getWordDao().insert(wordArray);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                completeCount++;
                float percent = (float)completeCount / fileCount;
                int totalComplete = (int)((COPY_BASE + percent * WRITE_BASE) * 100);
                if (totalComplete >= 100) {
                    totalComplete = 100;
                }
                mCallback.onLoadProgress(totalComplete);
            }
        }

    }

    public interface LoadCallback {
        void onLoadComplete();
        void onLoadProgress(int progress);
    }

}

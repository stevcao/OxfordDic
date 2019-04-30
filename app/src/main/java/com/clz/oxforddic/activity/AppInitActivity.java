package com.clz.oxforddic.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.clz.oxforddic.R;
import com.clz.oxforddic.model.repository.WordLoader;
import com.clz.util.DisplayUtils;

import java.security.Permissions;

public class AppInitActivity extends FragmentActivity {

    public static final String TAG = "Init." + WordLoader.TAG;

    TextView mProgress;
    TextView mProgressBg;
    TextView mProgressText;

    int progressWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_init_load_data);
        mProgress = findViewById(R.id.progress);
        mProgressText = findViewById(R.id.progress_text);

        DisplayMetrics paramBundle = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(paramBundle);
        progressWidth = ((int)(paramBundle.widthPixels - DisplayUtils.convertDpToPixel(this, 60.0F)));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] perssions = new String[] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
            };
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(perssions, 1001);
            } else {
                startLoad();
            }
        } else {
            startLoad();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "权限未授予，无法初始化数据!",Toast.LENGTH_LONG).show();
            finish();
        }
        startLoad();
    }

    int curProgress = 0;
    private void updateProgress(int progress)
    {
        if (curProgress < progress) {
            curProgress = progress;
        } else {
            return;
        }
        float progressf = progress / 100f;
        if (progressf <= 0.0F) {
            progressf = 0.0F;
        }
        if (progressf >= 1.0F) {
            progressf = 1.0F;
        }
        ViewGroup.LayoutParams lp = mProgress.getLayoutParams();
        lp.width = ((int)(progressWidth * progressf));
        mProgress.setLayoutParams(lp);
        String progressStr = String.format("%.1f", new Object[] { Float.valueOf(100.0F * progressf) }) + "%";
        mProgressText.setText(progressStr);
    }

    private void startLoad() {
        WordLoader loader = new WordLoader(getApplicationContext());
        if (!loader.isRead()) {
            loader.startLoad(new WordLoader.LoadCallback() {
                @Override
                public void onLoadComplete() {
                    Log.d(TAG, "loadComplete");
                    Intent i = new Intent(AppInitActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }

                @Override
                public void onLoadProgress(final int progress) {
                    Log.d(TAG, "onProgress: " + progress);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateProgress(progress);
                        }
                    });
                }
            });
        }
    }
}

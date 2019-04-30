package com.clz.oxforddic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

import com.clz.oxforddic.R;
import com.clz.oxforddic.model.repository.WordLoader;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        WordLoader loader = new WordLoader(this.getApplicationContext());
        if (loader.isRead()) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(this, AppInitActivity.class);
            startActivity(i);
        }
        finish();
    }

}

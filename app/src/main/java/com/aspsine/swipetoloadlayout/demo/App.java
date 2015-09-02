package com.aspsine.swipetoloadlayout.demo;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.aspsine.swipetoloadlayout.BuildConfig;

/**
 * Created by Aspsine on 2015/9/2.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setStrictMode();
    }

    private void setStrictMode() {
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.enableDefaults();
        }
    }


}

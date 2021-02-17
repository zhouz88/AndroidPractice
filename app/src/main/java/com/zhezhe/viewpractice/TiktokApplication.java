package com.zhezhe.viewpractice;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class TiktokApplication extends Application {
    @Override
    public void onCreate() {
        Fresco.initialize(this);
        super.onCreate();
    }
}

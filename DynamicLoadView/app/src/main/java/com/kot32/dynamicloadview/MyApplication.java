package com.kot32.dynamicloadview;

import android.app.Application;

import com.kot32.dynamicloadviewlibrary.config.DynamicViewConfig;
import com.kot32.dynamicloadviewlibrary.core.DynamicViewManager;

/**
 * Created by kot32 on 16/9/20.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DynamicViewConfig config = new DynamicViewConfig.Builder()
                .context(this)
                .getUpdateInfoApi("http://vpscn.ifancc.com/php/dynamicView.php")
                .build();
        DynamicViewManager.getInstance(config).init();
    }
}

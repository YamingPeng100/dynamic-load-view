package com.kot32.dynamicloadview;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import com.kot32.dynamicloadviewlibrary.core.DynamicViewGroup;

/**
 * Created by kot32 on 16/9/14.
 */
public class MyView extends DynamicViewGroup {

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public Resources getResources() {
        return super.getResources();
    }
}

package com.kot32.dynamicloadviewlibrary.model;

/**
 * Created by kot32 on 16/9/20.
 */
public class DynamicViewInfo {

    public LayoutInfo layoutParams;

    //must be truly package name.
    public String packageName;

    //Unique identifier for dynamic update
    public String uuid;

    public static class LayoutInfo {

        public static final int MATCH_PARENT = -1;
        public static final int WRAP_CONTENT = -2;

        public int width = WRAP_CONTENT;
        public int height = WRAP_CONTENT;

    }

}

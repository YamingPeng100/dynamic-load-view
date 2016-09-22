package com.kot32.dynamicloadviewlibrary.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kot32 on 16/9/20.
 */
public class DynamicInfo {
    //the version of latest apk.
    public int version = 1;

    //download patch apk path.
    public String downLoadPath;

    //download patch apk filename.
    public String fileName;

    //the updateInfo list of views.
    public List<DynamicViewInfo> viewInfo = new ArrayList<>();

}

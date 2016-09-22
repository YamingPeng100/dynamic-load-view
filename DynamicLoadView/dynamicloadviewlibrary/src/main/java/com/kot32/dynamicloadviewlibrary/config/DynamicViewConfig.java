package com.kot32.dynamicloadviewlibrary.config;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by kot32 on 16/9/14.
 */
public class DynamicViewConfig {

    private String updateFileDir = "dynamic_view_update_dir";
    private String dexOutDir = "dynamic_view_dex_out_dir";

    private String getUpdateInfoApi;

    private Context context;

    private DynamicViewConfig(Builder builder) {
        if (!TextUtils.isEmpty(builder.updateFileDir)) {
            updateFileDir = builder.updateFileDir;
        }
        if (!TextUtils.isEmpty(builder.dexOutDir)) {
            dexOutDir = builder.dexOutDir;
        }
        getUpdateInfoApi = builder.getUpdateInfoApi;
        context = builder.context;
    }


    public String getUpdateFileDir() {
        return updateFileDir;
    }

    public String getDexOutDir() {
        return dexOutDir;
    }

    public String getGetUpdateInfoApi() {
        return getUpdateInfoApi;
    }

    public Context getContext() {
        return context;
    }

    public static final class Builder {
        private String updateFileDir;
        private String dexOutDir;
        private String getUpdateInfoApi;
        private Context context;

        public Builder() {
        }

        public Builder updateFileDir(String val) {
            updateFileDir = val;
            return this;
        }

        public Builder dexOutDir(String val) {
            dexOutDir = val;
            return this;
        }

        public Builder getUpdateInfoApi(String val) {
            getUpdateInfoApi = val;
            return this;
        }

        public Builder context(Context val) {
            context = val;
            return this;
        }

        public DynamicViewConfig build() {
            return new DynamicViewConfig(this);
        }
    }
}

package com.kot32.dynamicloadviewlibrary.core;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.kot32.dynamicloadviewlibrary.config.DynamicViewConfig;
import com.kot32.dynamicloadviewlibrary.model.DynamicInfo;
import com.kot32.dynamicloadviewlibrary.util.DownloadTask;
import com.kot32.dynamicloadviewlibrary.util.PreferenceManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kot32 on 16/9/20.
 */
public class DynamicViewManager {

    private static final String TAG = DynamicViewManager.class.getSimpleName();

    private DynamicViewConfig config;

    private static DynamicViewManager instance;

    private DynamicInfo dynamicInfo;

    private List<DynamicViewGroup> dynamicViewGroups;

    private String updateFileDirPath;
    private String dexOutDirPath;

    private String updateFileFullPath;

    OkHttpClient client = new OkHttpClient();

    private static final String KEY_VIEW_VERSION = "dynamic_view_info";
    private static final String KEY_SHOULD_UPDATE = "dynamic_should_update";

    public static DynamicViewManager getInstance(DynamicViewConfig config) {
        if (instance == null) {
            instance = new DynamicViewManager(config);
        }
        return instance;
    }

    private DynamicViewManager(DynamicViewConfig config) {
        this.config = config;
        this.dynamicViewGroups = new ArrayList<>();
    }

    public static DynamicViewManager getInstance() {
        if (instance == null) {
            Log.e(TAG, "Please set config first.");
        }
        return instance;
    }

    private boolean shouldUpdate;

    public void init() {
        PreferenceManager.init(config.getContext());
        initFiles();

        if (TextUtils.isEmpty(config.getGetUpdateInfoApi())) {
            Log.e(TAG, "Please set the apiUrl of update");
            return;
        }
        shouldUpdate = (boolean) PreferenceManager.getPreference(KEY_SHOULD_UPDATE, false);
        String oldViewInfoJson = (String) PreferenceManager.getPreference(KEY_VIEW_VERSION, "");

        if (!TextUtils.isEmpty(oldViewInfoJson)) {
            dynamicInfo = new Gson().fromJson(oldViewInfoJson, DynamicInfo.class);
        }

        if (dynamicInfo != null) {
            updateFileFullPath = updateFileDirPath + "/" + dynamicInfo.version + "_" + dynamicInfo.fileName;
            if (shouldUpdate) {
                initResource(updateFileFullPath);
            }
            File file = new File(updateFileFullPath);
            if (!file.exists()) {
                fetchUpdate();
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(config.getGetUpdateInfoApi())
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    DynamicInfo newInfo = new Gson().fromJson(response.body().string(), DynamicInfo.class);
                    if (newInfo == null) {
                        return;
                    }
                    if (dynamicInfo == null || newInfo.version > dynamicInfo.version) {
                        if (dynamicInfo != null && dynamicInfo.downLoadPath.equals(newInfo.downLoadPath)) {
                            dynamicInfo.viewInfo = newInfo.viewInfo;
                            PreferenceManager.addStringPreference(KEY_VIEW_VERSION, new Gson().toJson(dynamicInfo));
                            PreferenceManager.addBooleanPreference(KEY_SHOULD_UPDATE, true);
                        } else {
                            //start download apk
                            dynamicInfo = newInfo;
                            fetchUpdate();
                        }

                    }
                } catch (IOException e) {
                    System.out.println(e);
                    e.printStackTrace();
                }

            }
        }).start();

    }


    private void fetchUpdate() {

        final String apkPath = updateFileDirPath + "/" + dynamicInfo.version + "_" + dynamicInfo.fileName;

        File apk = new File(apkPath);
        if (apk.exists()) {
            //delete the old version but same name apk.
            apk.delete();
            return;
        }
        new DownloadTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d(TAG, "Start download hotfix patch.");
            }

            @Override
            protected void onPostExecute(byte[] data) {
                super.onPostExecute(data);
                PreferenceManager.addStringPreference(KEY_VIEW_VERSION, new Gson().toJson(dynamicInfo));
                //download complete,when the app restart it will work
                PreferenceManager.addBooleanPreference(KEY_SHOULD_UPDATE, true);
            }
        }.execute(dynamicInfo.downLoadPath, apkPath);

        PreferenceManager.addBooleanPreference(KEY_SHOULD_UPDATE, false);
        shouldUpdate = false;
    }

    /**
     * add resource path to host app.
     */
    private void initResource(String path) {

    }

    public DynamicViewConfig getConfig() {
        return config;
    }

    public DynamicInfo getDynamicInfo() {
        return dynamicInfo;
    }

    public String getUpdateFileFullPath() {
        return updateFileFullPath;
    }

    public void addViewGroup(DynamicViewGroup viewGroup) {
        if (viewGroup == null) {
            return;
        }
        if (!dynamicViewGroups.contains(viewGroup)) {
            this.dynamicViewGroups.add(viewGroup);
        }
    }

    private void initFiles() {
        updateFileDirPath = config.getContext().getFilesDir() + "/" + config.getUpdateFileDir();
        dexOutDirPath = config.getContext().getFilesDir() + "/" + config.getDexOutDir();
        File f = new File(updateFileDirPath);
        if (!f.exists()) {
            f.mkdirs();
        }
        f = new File(dexOutDirPath);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    public String getDexOutDirPath() {
        return dexOutDirPath;
    }

    public boolean getShouldUpdate() {
        return shouldUpdate;
    }


    public void clearUpdateCache() {
        File dir = new File(updateFileDirPath);
        if (dir.exists() && dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                if (!f.delete()) {
                    f.delete();
                }
            }
        }
    }

}

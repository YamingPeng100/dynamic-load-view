package com.kot32.dynamicloadviewlibrary.core;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.kot32.dynamicloadviewlibrary.R;
import com.kot32.dynamicloadviewlibrary.delegate.IDynamic;
import com.kot32.dynamicloadviewlibrary.model.DynamicViewInfo;
import com.kot32.dynamicloadviewlibrary.reflect.Reflect;
import com.kot32.dynamicloadviewlibrary.util.DisplayUtil;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by kot32 on 16/9/14.
 * <p>
 * It will replace child view with latest update class when update() calls.
 * if isAutoUpdate true, will update automatic.
 * One DynamicViewGroup only added One View best.
 */
public class DynamicViewGroup extends RelativeLayout implements IDynamic {

    // the uuid of view, when update it can matches the view has same uuid .
    private String uuid = "";

    private View dynamicView;

    public DynamicViewGroup(Context context) {
        super(context);
        init();
    }

    public DynamicViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initXmlAttrs(context, attrs);
        init();
    }

    void initXmlAttrs(Context context, AttributeSet attributeSet) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.DynamicViewGroup,
                0, 0);

        try {
            uuid = a.getString(R.styleable.DynamicViewGroup_uuid);
        } finally {
            a.recycle();
        }
    }

    private void init() {
        if (DynamicViewManager.getInstance().getShouldUpdate()) {
            initResource();
        }
        DynamicViewManager.getInstance().addViewGroup(this);
    }


    /**
     * Replace the Resource class in Host Activity
     */
    private void initResource() {
        Resources resources = getContext().getResources();
        try {
            AssetManager newManager = AssetManager.class.newInstance();
            Method addAssetPath = newManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(newManager, DynamicViewManager.getInstance().getUpdateFileFullPath());
            Resources newResources = new Resources(newManager,
                    resources.getDisplayMetrics(), resources.getConfiguration());
            Reflect.onObject(getContext()).set("mResources", newResources);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Auto update when App restart
     */
    @Override
    public void update() {

        if (TextUtils.isEmpty(DynamicViewManager.getInstance().getUpdateFileFullPath())) {
            return;
        }
        File apkFile = new File(DynamicViewManager.getInstance().getUpdateFileFullPath());

        if (apkFile.exists()) {
            DexClassLoader classLoader = new DexClassLoader(apkFile.getAbsolutePath()
                    , DynamicViewManager.getInstance().getDexOutDirPath()
                    , null
                    , getClass().getClassLoader());

            if (DynamicViewManager.getInstance().getDynamicInfo() == null) {
                return;
            }

            for (DynamicViewInfo viewInfo : DynamicViewManager.getInstance().getDynamicInfo().viewInfo) {
                if (viewInfo != null) {
                    if (viewInfo.uuid.equals(uuid)) {
                        try {
                            Class newViewClazz = classLoader.loadClass(viewInfo.packageName);
                            Constructor con = newViewClazz.getConstructor(Context.class);
                            //first use Activity's Resource lie to View
                            if (dynamicView == null) {
                                dynamicView = (View) con.newInstance(getContext());
                            }
                            //Replace the View's mResources and recovery the Activity's avoid disorder of Resources
                            Reflect.onObject(getContext()).set("mResources", null);
                            getContext().getResources();

                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayUtil.dip2px(getContext(), viewInfo.layoutParams.width),
                                    DisplayUtil.dip2px(getContext(), viewInfo.layoutParams.height));
                            removeAllViews();
                            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                            addView(dynamicView, layoutParams);

                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            postInvalidate();
        }

    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (DynamicViewManager.getInstance().getShouldUpdate()) {
            update();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DynamicViewGroup that = (DynamicViewGroup) o;

        return uuid != null ? uuid.equals(that.uuid) : that.uuid == null;

    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    public View getDynamicView() {
        if (dynamicView == null) {
            if (getChildAt(0) != null) {
                return getChildAt(0);
            }
        }
        return dynamicView;
    }

}

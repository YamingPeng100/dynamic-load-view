# Dynamic-load-view

<img src="http://7fvj70.com1.z0.glb.clouddn.com/dlv_logo.png" width = "80" height = "80"/>

## 概述

**Dynamic-load-view 能够动态加载外部apk中的View以及资源，能够热修复线上View，以及模块化更新。** 

## 屏幕截图

<img src="http://7fvj70.com1.z0.glb.clouddn.com/dynamic-load-view.gif">

[下载演示APK](https://github.com/kot32go/dynamic-load-view/blob/master/dynamic-load-view.apk)

[截图地址](http://7fvj70.com1.z0.glb.clouddn.com/dynamic-load-view.gif)

## 特点

* 插件程序完全独立于宿主。
* 以 View作为模块进行模块化开发更新。
* 你也可以把View 铺满整个Activity，相当于更新Activity。
* 副作用小，没有加载Activity 带来的生命周期等问题。
* 兼容性好。Android 4.0~6.0 都没有问题。
* 简单。核心代码不超过400行。可以自行下载源码，修改更新规则。


## 如何使用

* 下载库，并作为library 引用。
* 需要在宿主程序的Application 的onCreat 中初始化，代码如下：.


```
DynamicViewConfig config = new DynamicViewConfig.Builder()
    .context(this)
    .getUpdateInfoApi("http://vpscn.ifancc.com/php/dynamicView.php")
    .build();
DynamicViewManager.getInstance(config).init();
```

getUpdateInfoApi 这个方法需要传入一个API地址，这个API地址给客户端提供更新的信息. 在上面的地址中，服务器返回了下面这样的JSON 串：

```
{
  "version": 39,
  "downLoadPath": "http://obfgb7oet.bkt.clouddn.com/patch101.apk",
  "fileName": "patch101.apk",
  "viewInfo": [
    {
      "packageName": "com.kot32.testdynamicviewproject.MyButton",
      "uuid": "test",
      "layoutParams": {
        "width": 100,
        "height": 100
      }
    },
    {
      "packageName": "com.kot32.testdynamicviewproject.MyButton1",
      "uuid": "test_activity",
      "layoutParams": {
        "width": -1,
        "height": -1
      }
    }
  ]
}
```

上面的JSON 串定义了本次更新的版本以及更新包的地址，并且提供了对每个View 的详细更新信息。

##### packageName :插件APK 中View 的完整包名.
##### uuid : 和宿主程序中待更新 View 相同的 UUID.
##### layoutParams:布局参数.

你也可以自己修改服务器需要提供的参数，更改com.kot32.dynamicloadviewlibrary.model 包中的模型类即可。


* 待更新的View 需要xml 布局文件中如下声明.注意uuid 属性必须赋值。更新时会匹配uuid 相同的View。

```
 <com.kot32.dynamicloadviewlibrary.core.DynamicViewGroup
        android:id="@+id/dv"
        android:layout_width="200dp"
        android:layout_height="200dp"
        app:uuid="test"
        android:layout_centerInParent="true">

        <!--default view -->
        <ImageView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:src="@mipmap/ic_launcher" />

</com.kot32.dynamicloadviewlibrary.core.DynamicViewGroup>
```

* 对于插件程序，只需要定义View 就好了，之后直接打成APK 包即可。


### 更多详细信息，请直接下载示例源码查看，源码不多，也很好理解。


##缺陷

* 现在可以加载插件程序中的string和drawable 资源，但是style.xml 和 dimens.xml 的加载还存在一些问题。
* 插件程序中的资源文件的名字最好不要和主程序中重复。



# Dynamic-load-view

<img src="http://7fvj70.com1.z0.glb.clouddn.com/dlv_logo.png" width = "80" height = "80"/>

## Overview

[中文介绍](https://github.com/kot32go/dynamic-load-view/blob/master/README_zh.md)

**Dynamic-load-view can load View in external apk. you can use it to hotfix View or update module in your App.** 

## ScreenShot

<img src="http://7fvj70.com1.z0.glb.clouddn.com/dynamic-load-view.gif">

[download Demo APK](https://github.com/kot32go/dynamic-load-view/blob/master/dynamic-load-view.apk)

[PicUrl](http://7fvj70.com1.z0.glb.clouddn.com/dynamic-load-view.gif)

## Future

* The plugin is independent to host App totaly.
* You can choose a subview as your fix module instead of a whole Activity or App.
* Also you can let DynamicViewGroup be content view of your Activity.
* Safty.when hotfix failed,just rollback to your default view.
* Compatibility. all version between 4.0~6.0 device is ok.
* Simple. download the source you can custom any rules.


## How to use

* Download the library and add dependent.
* For the Host App,you need add this code in your custom Application.


```
DynamicViewConfig config = new DynamicViewConfig.Builder()
    .context(this)
    .getUpdateInfoApi("http://vpscn.ifancc.com/php/dynamicView.php")
    .build();
DynamicViewManager.getInstance(config).init();
```

getUpdateInfoApi need a url for fetch hotfix info. In this example,this PHP url can return the JSON String like this:

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

This JSON String defined version of this hotfix update and APK patch download url.It also provide each View info you want to fix.

##### packageName :the fixed Class's package name.
##### uuid : this uuid matches your old view's uuid in Host App.
##### layoutParams:you know what it means.

you can change those paramter custom.just change the class below:com.kot32.dynamicloadviewlibrary.model

* In your activity xml.Attention the uuid property.

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

* For the plugin App,just need define View .

### Please download the example source code to get more usage.

##Attention

* For now,Resouce drawble and string can be asses in plugin App, but there still have some trouble in style.xml and dimens.xml
* The Resource of plugin best don't use the same name with Host's Resource.  

# License
```
Copyright 2016 Kot32

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```




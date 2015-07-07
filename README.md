# ScaleView

###效果###
![img](http://7lrzgb.com1.z0.glb.clouddn.com/72CEC0AA-1462-444F-AA5E-E4E0AD09E656.png)

###自定义###

* 可以自定义表盘背景色
* 进度颜色
* 刻度颜色
* 文本颜色
* 刻度字体大小
* 文本字体大小
* 起始结束角度
* 起始结束值

###用法###

1. 在布局中声明

```xml
	xmlns:scale="http://schemas.android.com/apk/res-auto"
```

2. 使用view, 未定义的属性使用默认的属性

```xml
    <com.scorpioneal.scaleview.ScaleView
        android:id="@+id/scaleview"
        scale:scaleBackground="@android:color/holo_red_light"
        scale:scaleSecondaryBackground="@android:color/holo_orange_light"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

3. 在java文件中

```java
        mScaleView = (ScaleView)findViewById(R.id.scaleview);
        mScaleView.setShownValue(110f);//设置当前值
        mScaleView.setShowText("110公斤"); //设置显示文本
```

###TODO###

 * 现在只支持均分的，不均分暂时不支持
 * 异常数据还没做处理
 * 支持手势操作
 * 适配
 * 表针可替换
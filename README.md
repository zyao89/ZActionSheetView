# ZActionSheetView
kotlin编写的自定义底部弹出选择框，主要用于学习Kotlin和分享的项目

---

##Kotlin 引言
>**Google IO 2017 宣布了 Kotlin 会成为 Android 官方开发语言。**

Kotlin 是一个基于 JVM 的新的编程语言，由 [JetBrains](http://baike.baidu.com/item/JetBrains) 开发。
Kotlin可以编译成Java字节码，也可以编译成JavaScript，方便在没有JVM的设备上运行。
JetBrains，作为目前广受欢迎的Java IDE [IntelliJ](http://baike.baidu.com/item/IntelliJ) 的提供商，在 Apache 许可下已经开源其Kotlin 编程语言。
Kotlin已正式成为Android官方开发语言。

## 正文
今天我主要介绍的是使用Kotlin去写一个简单的自定义View，从而学习Kotlin语言的各种魅力。

### Gradle引入
model中
```gradle
apply plugin: 'kotlin-android'

android {
    sourceSets {
        main.java.srcDirs += 'src/main/kotlin'
    }
}

compile 'org.jetbrains.kotlin:kotlin-stdlib:1.1.2-4'
```

工程中引入
```gradle
buildscript {
    ext.kotlin_version = '1.1.2-4'
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.3.2'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
```

### Kotlin代码
常量：ActionSheetConstants.kt
```kotlin
/**
 * Created by zyao89 on 2017/5/24.
 * Contact me at 305161066@qq.com or zyao89@gmail.com
 * For more projects: https://github.com/zyao89
 * My Blog: http://zyao89.me
 */
const val DEFAULT_COLOR: Int = 0xFF_02_7B_FF.toInt()
const val DEFAULT_SHADE_COLOR: Int = 0x88_00_00_00.toInt()
const val DEFAULT_BTN_CANCEL_COLOR: Int = 0xFF_87_87_87.toInt()
const val DEFAULT_BTN_SELECT_COLOR: Int = 0xFF_3D_DD_B0.toInt()
```

代码块一： BaseActionSheetView.kt
```kotlin
/**
 * Created by zyao89 on 2017/5/24.
 * Contact me at 305161066@qq.com or zyao89@gmail.com
 * For more projects: https://github.com/zyao89
 * My Blog: http://zyao89.me
 */
abstract class BaseActionSheetView(val context: Context, val list: List<ActionSheetBtnInfo>, val rootView: ViewGroup = ((context as Activity).window.decorView as ViewGroup)) : View.OnClickListener, View.OnTouchListener {
    protected val mBtnViewList: LinkedList<TextView> = LinkedList()
    protected var mCustomContent: LinearLayout? = null
    protected var mBtnCancel: TextView? = null
    //POP总容器
    private var mFrameLayout: FrameLayout = FrameLayout(context)
            .apply {
                setBackgroundColor(DEFAULT_SHADE_COLOR)
            }
    private var mPopupWindow: PopupWindow = PopupWindow(this.initView()
            .apply {
                mCustomContent = findViewById(R.id.pop_layout) as LinearLayout
                setOnTouchListener(this@BaseActionSheetView)
            }, MATCH_PARENT, MATCH_PARENT, true)
            .apply {
                animationStyle = R.style.popwin_anim_style
                setOnDismissListener { this@BaseActionSheetView.dismiss() }
                inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
                softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
            }

    init {
        this.initBtn()
        this.initCancel()
    }

    protected abstract fun initView(): View
    protected abstract fun initBtn()
    protected abstract fun initCancel()

    override fun onClick(v: View?) {
        when (v) {
            mBtnCancel -> {

            }
            is TextView, is Button -> v.tag.takeIf { it is Int }?.let {
                val onClickListener = list[v.tag as Int].onClickListener
                onClickListener?.onClick(v)
            }
            else -> {

            }
        }
        mPopupWindow.dismiss()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val height: Int = mCustomContent?.top!!.toInt()
        val y: Int = event?.y!!.toInt()
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                y.takeIf { it < height }.let {
                    println("y: $y")
                    println("height: $height")
                    mPopupWindow.dismiss()
                }
            }
        }
        return true
    }

    private fun dismiss() {
        rootView.removeView(mFrameLayout)
    }

    fun setCancelText(text: String) {
        this.mBtnCancel?.text = text
    }

    fun setCancelTextColor(color: Int) {
        this.mBtnCancel?.setTextColor(color)
    }

    fun setCancelTextSize(size: Float) {
        this.mBtnCancel?.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    fun hideCancel() {
        this.mBtnCancel?.visibility = View.GONE
    }

    fun show() {
        rootView.addView(mFrameLayout, MATCH_PARENT, MATCH_PARENT)
        mPopupWindow.showAtLocation(mFrameLayout, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    protected fun dip2px(context: Context, dpValue: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.resources.displayMetrics).toInt()
    }
}
```

代码块二：ActionSheetBtnInfo.kt
```kotlin
/**
 * Created by zyao89 on 2017/5/24.
 * Contact me at 305161066@qq.com or zyao89@gmail.com
 * For more projects: https://github.com/zyao89
 * My Blog: http://zyao89.me
 */
data class ActionSheetBtnInfo(var text: String, var textColor: Int = DEFAULT_COLOR, var backgroundResource: Int = R.drawable.custom_view_select_pop_selector_btn) : Serializable {
    companion object {
        //这里可以填写单例
    }

    var onClickListener: View.OnClickListener? = null

    /**
     * 自定义get和set方法
     */
    var textSize: Float? = null
        get() = field
        set(value) {
            field = value
        }
}
```

代码块三：ActionSheetView.kt
```kotlin
/**
 * Created by zyao89 on 2017/5/24.
 * Contact me at 305161066@qq.com or zyao89@gmail.com
 * For more projects: https://github.com/zyao89
 * My Blog: http://zyao89.me
 */
class ActionSheetView(context: Context, list: List<ActionSheetBtnInfo>) : BaseActionSheetView(context, list) {

    override fun initView(): View {
        return View.inflate(context, R.layout.custom_view_select_pop, null)
    }

    override fun initBtn() {
        for ((index, btnInfo) in list.withIndex()) {
            TextView(context)
                    .apply {
                        tag = index
                        text = btnInfo.text
                        btnInfo.textSize?.let {
                            setTextSize(TypedValue.COMPLEX_UNIT_SP, it)
                        }
                        height = dip2px(context, 44.0f)
                        gravity = Gravity.CENTER
                        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, dip2px(context, 44.0f))
                        setTextColor(btnInfo.textColor)
                        setBackgroundResource(btnInfo.backgroundResource)
                        setOnClickListener(this@ActionSheetView)
                    }
                    .also {
                        val layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        layoutParams.topMargin = dip2px(context, 5.0f)
                        mCustomContent?.addView(it, index, layoutParams)
                    }
                    .let {
                        mBtnViewList.add(it)
                    }
        }
    }

    override fun initCancel() {
        mBtnCancel = TextView(context)
                .apply {
                    text = "Cancel"
                    height = dip2px(context, 44.0f)
                    gravity = Gravity.CENTER
                    layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, dip2px(context, 44.0f))
                    setTextColor(if (list.isEmpty()) DEFAULT_BTN_SELECT_COLOR else list[0].textColor)
                    setBackgroundResource(if (list.isEmpty()) R.drawable.custom_view_select_pop_selector_btn else list[0].backgroundResource)
                    setOnClickListener(this@ActionSheetView)
                }
                .apply {
                    val layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    layoutParams.topMargin = dip2px(context, 15.0f)
                    layoutParams.bottomMargin = dip2px(context, 20.0f)
                    mCustomContent?.let {
                        mCustomContent?.addView(this@apply, it.childCount, layoutParams)
                    }
                }
    }
}
```

代码块四：ActionSheetListView.kt
```kotlin
/**
 * Created by zyao89 on 2017/5/24.
 * Contact me at 305161066@qq.com or zyao89@gmail.com
 * For more projects: https://github.com/zyao89
 * My Blog: http://zyao89.me
 */
class ActionSheetListView(context: Context, list: List<ActionSheetBtnInfo>) : BaseActionSheetView(context, list) {

    override fun initView(): View {
        return View.inflate(context, R.layout.custom_view_select_list_pop, null)
    }

    override fun initBtn() {
        for ((index, btnInfo) in list.withIndex()) {
            TextView(context)
                    .apply {
                        tag = index
                        text = btnInfo.text
                        btnInfo.textSize?.let {
                            setTextSize(TypedValue.COMPLEX_UNIT_SP, it)
                        }
                        height = dip2px(context, 44.0f)
                        gravity = Gravity.CENTER
                        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, dip2px(context, 44.0f))
                        setTextColor(btnInfo.textColor)
                        setBackgroundResource(R.drawable.custom_view_select_list_pop_selector_btn)
                        setOnClickListener(this@ActionSheetListView)
                    }
                    .also {
                        val layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        layoutParams.topMargin = 1
                        mCustomContent?.addView(it, index, layoutParams)
                    }
                    .let {
                        mBtnViewList.add(it)
                    }
        }
    }

    override fun initCancel() {
        mBtnCancel = TextView(context)
                .apply {
                    text = "Cancel"
                    height = dip2px(context, 44.0f)
                    gravity = Gravity.CENTER
                    layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, dip2px(context, 44.0f))
                    setTextColor(DEFAULT_BTN_CANCEL_COLOR)
                    setBackgroundResource(R.drawable.custom_view_select_list_pop_selector_btn)
                    setOnClickListener(this@ActionSheetListView)
                }
                .apply {
                    val layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    layoutParams.topMargin = 1
                    layoutParams.bottomMargin = dip2px(context, 1.0f)
                    mCustomContent?.let {
                        mCustomContent?.addView(this@apply, it.childCount, layoutParams)
                    }
                }
    }
}
```

---
### 布局文件Layout
layout布局一：custom_view_select_pop.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
                android:layout_width="fill_parent"
                android:layout_height="wrap_content"
                android:gravity="center_horizontal"
                android:orientation="vertical" >

    <LinearLayout
        android:id="@+id/pop_layout"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:gravity="center_horizontal"
        android:layout_marginLeft="20dp"
        android:layout_marginRight="20dp"
        android:orientation="vertical" >
    </LinearLayout>

</RelativeLayout>

```


layout布局二：custom_view_select_list_pop.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
                android:layout_width="fill_parent"
                android:layout_height="wrap_content"
                android:gravity="center_horizontal"
                android:orientation="vertical" >

    <LinearLayout
        android:id="@+id/pop_layout"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:gravity="center_horizontal"
        android:orientation="vertical" >
    </LinearLayout>

</RelativeLayout>

```

anim文件
ppwindow_show_anim.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="300"
        android:fromXDelta="0"
        android:toXDelta="0"
        android:fromYDelta="200"
        android:toYDelta="0"
        />
    <alpha
        android:duration="300"
        android:fromAlpha="0"
        android:toAlpha="1"
        />
</set>
```

ppwindow_hide_anim.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="200"
        android:fromXDelta="0"
        android:toXDelta="0"
        android:fromYDelta="0"
        android:toYDelta="200"
        />
    <alpha
        android:duration="200"
        android:fromAlpha="1"
        android:toAlpha="0"
        />
</set>
```

drawable文件
custom_view_select_list_pop_selector_btn.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_pressed="true" >
        <shape>
            <solid android:color="#cccccccc"/>
        </shape>
    </item>
    <item>
        <shape>
            <solid android:color="@android:color/white"/>
        </shape>
    </item>
</selector>
```


custom_view_select_pop_selector_btn.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_pressed="true" >
        <shape>
            <solid android:color="#cccccccc"/>
            <corners android:radius="10dp" />
        </shape>
    </item>
    <item  >
        <shape>
            <solid android:color="@android:color/white"/>
            <corners android:radius="10dp"/>
        </shape>
    </item>
</selector>
```

styles.xml
```xml
<resources>
    <style name="popwin_anim_style">
        <item name="android:windowEnterAnimation">@anim/ppwindow_show_anim</item>
        <item name="android:windowExitAnimation">@anim/ppwindow_hide_anim</item>
    </style>
</resources>
```

### 演示

![ActionSheetListView.png](http://upload-images.jianshu.io/upload_images/2587638-08c0947ddeda1f06.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


![ActionSheetView.png](http://upload-images.jianshu.io/upload_images/2587638-776940524ea90c08.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


GitHub源码：[zyao89/ZActionSheetView](https://github.com/zyao89/ZActionSheetView)

---


`作者：Zyao89；转载请保留此行，谢谢；`

个人博客：[http://zyao89.me](http://zyao89.me)

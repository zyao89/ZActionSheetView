package com.zyao89.actionsheet

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.TextView

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
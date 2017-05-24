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
package com.zyao89.actionsheet

import android.view.View
import java.io.Serializable

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
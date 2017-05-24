package com.zyao89.actionsheet

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.*
import java.util.*

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
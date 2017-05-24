package com.zyao89.zactionsheetview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.zyao89.actionsheet.ActionSheetBtnInfo
import com.zyao89.actionsheet.ActionSheetListView
import com.zyao89.actionsheet.ActionSheetView
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById(R.id.btn1).setOnClickListener(this)
        findViewById(R.id.btn2).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btn1 -> {
                LinkedList<ActionSheetBtnInfo>().apply {
                    ActionSheetBtnInfo("选项一").let {
                        add(it)
                    }
                    ActionSheetBtnInfo("选项二").let {
                        add(it)
                    }
                    ActionSheetBtnInfo("选项三").let {
                        add(it)
                    }
                }.let {
                    ActionSheetView(this, it).apply {
                        show()
                    }
                }
            }
            R.id.btn2 -> {
                LinkedList<ActionSheetBtnInfo>().apply {
                    ActionSheetBtnInfo("选项一").let {
                        add(it)
                    }
                    ActionSheetBtnInfo("选项二").let {
                        add(it)
                    }
                    ActionSheetBtnInfo("选项三").let {
                        add(it)
                    }
                }.let {
                    ActionSheetListView(this, it).apply {
                        show()
                    }
                }
            }
            else -> {

            }
        }
    }
}

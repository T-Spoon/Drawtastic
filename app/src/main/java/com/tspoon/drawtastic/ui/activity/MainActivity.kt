package com.tspoon.drawtastic.ui.activity

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.KeyEvent
import android.view.View
import butterknife.BindView
import butterknife.OnClick
import com.tspoon.drawtastic.R
import com.tspoon.drawtastic.ui.view.DrawingView

class MainActivity : BaseActivity() {

    @BindView(R.id.activity_main_drawing_view) lateinit var mDrawingView: DrawingView
    @BindView(R.id.activity_main_fab) lateinit var mFab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onStart() {
        super.onStart()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        // Disable Volume buttons
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            return true
        } else {
            return super.onKeyDown(keyCode, event)
        }
    }

    @OnClick(R.id.activity_main_fab)
    fun onClickRefresh(view: View) {
        mDrawingView.clear()
    }
}

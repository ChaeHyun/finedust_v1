package ch.breatheinandout.screen.common.toolbar

import android.graphics.drawable.ColorDrawable

interface ToolbarHelper {
    fun setToolbarTitle(title: String)
    fun setToolbarBackgroundColor(color: ColorDrawable)
    fun setToolbarVisibility(visible: Boolean)
}
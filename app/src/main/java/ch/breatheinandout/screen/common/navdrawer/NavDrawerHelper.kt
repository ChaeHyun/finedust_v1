package ch.breatheinandout.screen.common.navdrawer

import android.graphics.drawable.ColorDrawable

interface NavDrawerHelper {
    fun setToolbarTitle(title: String)
    fun setToolbarBackgroundColor(color: ColorDrawable)

    fun setToolbarVisibility(visible: Boolean)
    fun applyStatusBarColor(resId: Int)
    fun resetToolbarColor()

    fun setupToolbarOptionsMenu()
    fun clearToolbarOptionsMenu()

    fun isDrawerOpen() : Boolean
    fun closeDrawer()
}
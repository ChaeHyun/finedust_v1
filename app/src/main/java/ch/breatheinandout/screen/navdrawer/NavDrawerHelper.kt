package ch.breatheinandout.screen.navdrawer

import android.graphics.drawable.ColorDrawable

interface NavDrawerHelper {
    fun setToolbarTitle(title: String)
    fun setToolbarBackgroundColor(color: ColorDrawable)

    fun setToolbarVisibility(visible: Boolean)
}
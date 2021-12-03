package ch.breatheinandout.screen.common.toolbar

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar
import ch.breatheinandout.R
import ch.breatheinandout.screen.common.widgetview.BaseWidgetView

class ToolbarWidgetView constructor(
    layoutInflater: LayoutInflater,
    parent: ViewGroup?
) : BaseWidgetView(layoutInflater, parent, R.layout.layout_toolbar_content) {

    private val toolbar: Toolbar = findViewById(R.id.toolbar_content)

    fun setupOptionsMenu(@MenuRes menuId: Int, listener: Toolbar.OnMenuItemClickListener) {
        toolbar.inflateMenu(menuId)
        toolbar.setOnMenuItemClickListener(listener)
    }

    fun clearOptionsMenuMenu() {
        toolbar.menu.clear()
    }

    fun setToolbarTitle(title: String) {
        toolbar.title = title
    }

    fun setBackgroundColor(color: ColorDrawable) {
        toolbar.background = color
    }
}

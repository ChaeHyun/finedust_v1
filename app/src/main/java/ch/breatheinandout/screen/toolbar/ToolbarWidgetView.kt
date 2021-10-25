package ch.breatheinandout.screen.toolbar

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import ch.breatheinandout.R
import ch.breatheinandout.screen.widgetview.BaseWidgetView

class ToolbarWidgetView constructor(
    private val layoutInflater: LayoutInflater,
    private val parent: ViewGroup?
) : BaseWidgetView(layoutInflater, parent, R.layout.layout_toolbar_content) {

    private val toolbar: Toolbar = findViewById(R.id.toolbar_content)

    fun setToolbarTitle(title: String) {
        toolbar.title = title
    }

    fun setBackgroundColor(color: ColorDrawable) {
        toolbar.background = color
    }
}

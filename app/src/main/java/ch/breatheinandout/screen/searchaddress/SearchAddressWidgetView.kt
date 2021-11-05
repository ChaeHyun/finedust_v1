package ch.breatheinandout.screen.searchaddress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import ch.breatheinandout.screen.toolbar.ToolbarHelper
import ch.breatheinandout.screen.widgetview.BaseObservableWidgetView
import ch.breatheinandout.domain.searchaddress.SearchedAddress

abstract class SearchAddressWidgetView(
    layoutInflater: LayoutInflater,
    parent: ViewGroup?,
    @LayoutRes val layoutId: Int
) : BaseObservableWidgetView<SearchAddressWidgetView.Listener>(layoutInflater, parent, layoutId), ToolbarHelper {

    interface Listener {
        fun onSearchQuerySubmit(query: String)
        fun onClickAsUp()
        fun onClickAddressItem(item: SearchedAddress)
    }

    abstract fun showProgressIndication()
    abstract fun hideProgressIndication()

    abstract fun render(viewState: SearchAddressViewState)
}
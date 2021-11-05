package ch.breatheinandout.screen.searchaddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ch.breatheinandout.screen.widgetview.WidgetViewFactory
import ch.breatheinandout.searchaddress.SearchedAddress
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchAddressFragment : Fragment(), SearchAddressWidgetView.Listener {

    @Inject lateinit var widgetViewFactory: WidgetViewFactory

    private lateinit var widgetView: SearchAddressWidgetView
    private val viewModel: SearchAddressViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        widgetView = widgetViewFactory.createSearchAddressWidgetView(parent)
        widgetView.setToolbarVisibility(visible = false)
        return widgetView.getRootView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(viewLifecycleOwner, { widgetView.render(it) })
    }


    override fun onStart() {
        super.onStart()
        widgetView.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        widgetView.unregisterListener(this)
    }

    override fun onSearchQuerySubmit(query: String) {
        Logger.v("query@Fragment -> $query")
        viewModel.search(query)
    }

    override fun onClickAsUp() {
        Logger.v("[AS UP] Event clicked.")
        findNavController().navigateUp()
    }

    override fun onClickAddressItem(item: SearchedAddress) {
        Logger.v("[Recycler Item Clicked] ${item.addressLine.addr}")
        // TODO : Before navigate to the other screen, save it in the local database.
        viewModel.save(item)
        // TODO : add to bundle, then navigate back to the airqualityFragment with it.
    }
}
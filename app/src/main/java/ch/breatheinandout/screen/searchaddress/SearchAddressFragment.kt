package ch.breatheinandout.screen.searchaddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ch.breatheinandout.R
import ch.breatheinandout.common.Constants
import ch.breatheinandout.screen.common.widgetview.WidgetViewFactory
import ch.breatheinandout.domain.searchaddress.model.SearchedAddress
import ch.breatheinandout.screen.common.ScreenNavigator
import ch.breatheinandout.screen.searchaddress.widgetview.SearchAddressWidgetView
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchAddressFragment : Fragment(), SearchAddressWidgetView.Listener {

    @Inject lateinit var widgetViewFactory: WidgetViewFactory
    @Inject lateinit var screenNavigator: ScreenNavigator

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

    override fun onDestroyView() {
        super.onDestroyView()
        widgetView.setToolbarVisibility(visible = true)
    }

    override fun onSearchQuerySubmit(query: String) {
        Logger.v("query@Fragment -> $query")
        viewModel.search(query)
    }

    override fun onClickAsUp() {
        Logger.v("[AS UP] Event clicked.")
        screenNavigator.navigateUp()
    }

    override fun onClickAddressItem(item: SearchedAddress) {
        Logger.v("[Recycler Item Clicked] ${item.addressLine.addr}")
        viewModel.save(item)

        val bundle = Bundle().apply { putSerializable(Constants.KEY_SELECTED_ADDRESS, item) }
        screenNavigator.navigateWithBundle(R.id.AirQualityFragment, bundle)
    }
}
package ch.breatheinandout.screen.searchaddress

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ch.breatheinandout.R
import ch.breatheinandout.screen.navdrawer.NavDrawerHelper
import com.google.android.material.snackbar.Snackbar
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchAddressWidgetViewImpl(
    private val inflater: LayoutInflater,
    private val parent: ViewGroup?,
    private val navDrawerHelper: NavDrawerHelper
) : SearchAddressWidgetView(inflater, parent, R.layout.fragment_search_address), SearchView.OnQueryTextListener {

    private val progressIndicator: SwipeRefreshLayout = findViewById(R.id.swipeLayout)
    private val homeAsUp: ImageView = findViewById(R.id.img_as_up)
    private val searchView: SearchView = findViewById(R.id.searchView)

    private val textDescription: TextView = findViewById(R.id.text_description)
    private val recyclerView: RecyclerView = findViewById(R.id.recyclerview_address)
    private val adapter: SearchedAddressAdapter = SearchedAddressAdapter { item ->
        getListeners().map { it.onClickAddressItem(item) }
        Toast.makeText(getContext(), item.addressLine.umdName, Toast.LENGTH_SHORT).show()
    }

    init {
        progressIndicator.isEnabled = false
        homeAsUp.setOnClickListener { getListeners().forEach { it.onClickAsUp() } }

        val testQuery = "서구"
        searchView.setQuery(testQuery, false)
        searchView.apply {
            maxWidth = Int.MAX_VALUE
            isSubmitButtonEnabled = true
            setOnQueryTextListener(this@SearchAddressWidgetViewImpl)
        }

        // RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(getContext())
        recyclerView.adapter = adapter
    }

    override fun render(viewState: SearchAddressViewState) {
        when(viewState) {
            Error -> { hideProgressIndication() }
            Loading -> { showProgressIndication() }
            is SearchAddressContent -> { bindAddressItems(viewState) }
        }
    }

    private fun bindAddressItems(content: SearchAddressContent) {
        val items = content.searchedAddressList
        if (items.isEmpty()) {
            textDescription.visibility = View.VISIBLE
            recyclerView.visibility = View.INVISIBLE
            Snackbar.make(getRootView(), getString(R.string.no_search_result), Snackbar.LENGTH_LONG).show()
            return
        }

        adapter.bindAddressData(items)
        recyclerView.visibility = View.VISIBLE
        textDescription.visibility = View.INVISIBLE

        hideProgressIndication()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            getListeners().forEach { it.onSearchQuerySubmit(query) }
            Toast.makeText(getContext(), "search > \"$query\"", Toast.LENGTH_SHORT).show()
        }
        Logger.v("<onQueryTextSubmit> $query")
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
//        if (newText != null && newText.length > 1 && newText.length <= 8)
//            Logger.v("<onQueryTextChange> $newText")
        return false
    }

    override fun showProgressIndication() {
        progressIndicator.isRefreshing = true
    }

    override fun hideProgressIndication() {
        CoroutineScope(Dispatchers.IO).launch {
            Thread.sleep(1500)              // Pretend there is a few loading time.
            progressIndicator.isRefreshing = false
        }
    }

    override fun setToolbarTitle(title: String) {
        navDrawerHelper.setToolbarTitle(title)
    }

    override fun setToolbarBackgroundColor(color: ColorDrawable) {
        navDrawerHelper.setToolbarBackgroundColor(color)
    }

    override fun setToolbarVisibility(visible: Boolean) {
        navDrawerHelper.setToolbarVisibility(visible)
    }
}
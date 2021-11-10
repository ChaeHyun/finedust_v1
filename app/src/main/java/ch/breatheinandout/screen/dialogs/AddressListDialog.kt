package ch.breatheinandout.screen.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.breatheinandout.R
import ch.breatheinandout.common.Constants
import ch.breatheinandout.domain.searchaddress.model.SearchedAddress
import ch.breatheinandout.screen.ScreenNavigator
import ch.breatheinandout.screen.searchaddress.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddressListDialog : DialogFragment() {
    @Inject lateinit var screenNavigator: ScreenNavigator

    private val viewModel: SearchAddressViewModel by viewModels()
    private lateinit var dismissIcon: ImageView
    private lateinit var recyclerView: RecyclerView
    private val adapter = AddressListAdapter(
        { pos, item -> selectItem(pos, item) },
        { pos, item -> deleteItem(pos, item) }
    )

    private val dummyOnTop = listOf(SearchedAddress(Constants.FORCE_GPS))
    private var data: MutableList<SearchedAddress> = emptyList<SearchedAddress>().toMutableList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_address_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDismissIcon(view)
        setupRecyclerView(view)

        viewModel.viewState.observe(viewLifecycleOwner, {
            render(it)
        })
        viewModel.read()
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.dialog_address_listView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun setupDismissIcon(view: View) {
        dismissIcon = view.findViewById(R.id.imageFinish)
        dismissIcon.setOnClickListener { dismiss() }
    }

    private fun render(viewState: SearchAddressViewState) {
        if (viewState is SearchAddressContent) {
            val list: List<SearchedAddress> = viewState.searchedAddressList
            data = list.toMutableList()
            val showList = dummyOnTop + list
            adapter.bindAddressList(showList)
            buildDialog(requireView())
        }
    }

    private fun selectItem(position: Int, item: SearchedAddress) {
        val bundle = Bundle()
        if (position == 0) {
            bundle.putSerializable(Constants.KEY_SELECTED_ADDRESS, dummyOnTop[0])
        } else {
            Toast.makeText(context, "[SELECT] ${item.addressLine.umdName} .. ", Toast.LENGTH_SHORT).show()
            bundle.putSerializable(Constants.KEY_SELECTED_ADDRESS, item)
        }
        screenNavigator.navigateWithBundle(R.id.AirQualityFragment, bundle)
    }

    private fun deleteItem(position: Int, item: SearchedAddress) {
        if (position > 0) {
            Toast.makeText(context, "[DELETE] ${item.addressLine.addr} .. ", Toast.LENGTH_SHORT).show()
            viewModel.delete(data, item)
        }
    }

    private fun buildDialog(dialogView: View) {
        MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()
    }
}
package ch.breatheinandout.screen.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.breatheinandout.R
import ch.breatheinandout.domain.searchaddress.model.SearchedAddress

class AddressListAdapter constructor(
    private val onItemClickListener: (Int, SearchedAddress) -> Unit,
    private val onDeleteClickListener: (Int, SearchedAddress) -> Unit
) : RecyclerView.Adapter<AddressListAdapter.AddressItemViewHolder>() {
    class AddressItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textAddress: TextView = view.findViewById(R.id.address)
        val deleteIcon: ImageView = view.findViewById(R.id.img_delete)
    }

    private var items: List<SearchedAddress> = ArrayList(0)

    fun bindAddressList(list: List<SearchedAddress>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.dialog_address_item, parent, false)
        return AddressItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AddressItemViewHolder, position: Int) {
        val item = items[position]
        holder.textAddress.text = item.addressLine.addr
        holder.textAddress.setOnClickListener {
            onItemClickListener.invoke(position, item)
        }
        when (position) {
            0 -> holder.deleteIcon.visibility = View.INVISIBLE
            else -> holder.deleteIcon.setOnClickListener {
                onDeleteClickListener.invoke(position, item)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
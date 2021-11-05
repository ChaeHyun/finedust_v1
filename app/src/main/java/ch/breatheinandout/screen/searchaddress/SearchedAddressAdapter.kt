package ch.breatheinandout.screen.searchaddress

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.breatheinandout.R
import ch.breatheinandout.domain.searchaddress.SearchedAddress

class SearchedAddressAdapter constructor(
    private val onItemClickListener: (SearchedAddress) -> Unit
) : RecyclerView.Adapter<SearchedAddressAdapter.SearchItemViewHolder>() {
    inner class SearchItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textAddress: TextView = view.findViewById(R.id.item_address)
    }

    private var items: List<SearchedAddress> = ArrayList(0)

    fun bindAddressData(list: List<SearchedAddress>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.search_address_item, parent, false)
        return SearchItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        holder.textAddress.text = items[position].addressLine.addr
        holder.itemView.setOnClickListener { onItemClickListener.invoke(items[position]) }
    }

    override fun getItemCount(): Int = items.size
}

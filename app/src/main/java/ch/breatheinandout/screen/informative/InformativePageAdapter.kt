package ch.breatheinandout.screen.informative

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class InformativePageAdapter constructor(
    val parent: Fragment,
    private val urlList: List<String>,
    private val fromTextList: List<String>
) : FragmentStateAdapter(parent) {

    private var pages: ArrayList<Fragment> = arrayListOf()

    init {
        urlList.zip(fromTextList).forEach { pair ->
            pages.add(InformativePage.newInstance(pair.first, pair.second))
        }
    }

    override fun getItemCount(): Int {
        return pages.size
    }

    override fun createFragment(position: Int): Fragment {
        return pages[position]
    }
}
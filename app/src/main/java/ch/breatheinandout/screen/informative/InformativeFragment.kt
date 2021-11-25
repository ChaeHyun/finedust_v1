package ch.breatheinandout.screen.informative

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import ch.breatheinandout.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InformativeFragment : Fragment() {

    private lateinit var adapter: InformativePageAdapter
    private lateinit var viewPager: ViewPager2
    private val tabTitles = listOf("NullSchool", "KAQ", "Tenki", "AQI")

    private val argUrls by lazy {
        listOf(
            getString(R.string.informative_url_null_school),
            getString(R.string.informative_url_kaq),
            getString(R.string.informative_url_tenki),
            getString(R.string.informative_url_aqi)
        )
    }

    private val argFromTexts by lazy {
        listOf(
            getString(R.string.informative_from_text_null_school),
            getString(R.string.informative_from_text_kaq),
            getString(R.string.informative_from_text_tenki),
            getString(R.string.informative_from_text_aqi)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_informative, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = InformativePageAdapter(this, argUrls, argFromTexts)
        viewPager = view.findViewById(R.id.pager)
        viewPager.also {
            it.isUserInputEnabled = false
            it.adapter = adapter

            val tabLayout: TabLayout = view.findViewById(R.id.tab_layout)
            linkTabLayoutAndViewPager(tabLayout)
        }
    }

    private fun linkTabLayoutAndViewPager(tabLayout: TabLayout) {
        TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
            if (pos <= tabTitles.size) {
                tab.text = tabTitles[pos]
            }
        }.attach()
    }
}
package ch.breatheinandout.screen.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ch.breatheinandout.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import ch.breatheinandout.domain.forecast.O3
import ch.breatheinandout.domain.forecast.PM10
import ch.breatheinandout.domain.forecast.PM25
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ForecastFragment : Fragment() {
    private val viewModel: ForecastViewModel by viewModels()

    private lateinit var adapter: ForecastPageAdapter
    private lateinit var viewPager: ViewPager2

    private val tabTitles = listOf(PM10.code, PM25.code, O3.code)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.forecastLiveData.observe(viewLifecycleOwner, {
            Logger.v("Check Group -> $it")

            // ViewPager2
            adapter = ForecastPageAdapter(this, it)
            viewPager = view.findViewById(R.id.pager)
            viewPager.adapter = adapter

            val tabLayout: TabLayout = view.findViewById(R.id.tab_layout)
            linkTabLayoutAndViewPager(tabLayout)
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.getForecast()
    }

    private fun linkTabLayoutAndViewPager(tabLayout: TabLayout) {
        TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
            if (pos <= tabTitles.size) {
                tab.text = tabTitles[pos]
            }
        }.attach()
    }


}
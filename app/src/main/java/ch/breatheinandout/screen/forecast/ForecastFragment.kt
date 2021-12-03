package ch.breatheinandout.screen.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import ch.breatheinandout.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import ch.breatheinandout.common.Constants
import ch.breatheinandout.domain.forecast.model.O3
import ch.breatheinandout.domain.forecast.model.PM10
import ch.breatheinandout.domain.forecast.model.PM25
import ch.breatheinandout.screen.forecast.pages.ForecastPageAdapter
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
    private var backPressedTime : Long = 0
    private val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val currentTime = System.currentTimeMillis()
            val intervalTime = currentTime - backPressedTime

            if (intervalTime in 0..Constants.BACK_PRESSED_INTERVAL) {
                isEnabled = false
                requireActivity().onBackPressed()
                activity?.onBackPressed()
            }
            else {
                backPressedTime = currentTime
                activity?.let { Toast.makeText(it, getString(R.string.message_for_back_pressed), Toast.LENGTH_SHORT).show() }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        viewModel.forecastLiveData.observe(viewLifecycleOwner, {
            Logger.v("Check Group -> $it")

            // ViewPager2
            adapter = ForecastPageAdapter(this, it)
            viewPager = view.findViewById(R.id.pager)
            viewPager.adapter = adapter

            val tabLayout: TabLayout = view.findViewById(R.id.tab_layout)
            linkTabLayoutAndViewPager(tabLayout)
            tabLayout.selectTab(tabLayout.getTabAt(1))      // To load the next page earlier.
            tabLayout.selectTab(tabLayout.getTabAt(0))
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
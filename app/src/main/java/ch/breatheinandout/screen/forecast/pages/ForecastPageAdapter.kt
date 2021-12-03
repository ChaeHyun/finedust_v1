package ch.breatheinandout.screen.forecast.pages

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ch.breatheinandout.domain.forecast.model.ForecastInfoGroup
import ch.breatheinandout.domain.forecast.model.O3
import ch.breatheinandout.domain.forecast.model.PM10
import ch.breatheinandout.domain.forecast.model.PM25

class ForecastPageAdapter constructor(
    parent: Fragment,
    forecastInfoGroup: ForecastInfoGroup
) : FragmentStateAdapter(parent) {

    private val pages: ArrayList<Fragment> = arrayListOf(
        ForecastInfoPage.newInstance(PM10.code, forecastInfoGroup.pm10),
        ForecastInfoPage.newInstance(PM25.code, forecastInfoGroup.pm25),
        ForecastInfoPage.newInstance(O3.code, forecastInfoGroup.o3)
    )

    override fun getItemCount(): Int {
        return pages.size
    }

    override fun createFragment(position: Int): Fragment {
        return pages[position]
    }
}
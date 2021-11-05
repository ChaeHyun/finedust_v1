package ch.breatheinandout.screen

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.get
import ch.breatheinandout.R
import com.orhanobut.logger.Logger

class ScreenNavigator constructor(
    private val activity: AppCompatActivity
) {
    private lateinit var navController: NavController

    fun initNavController(navHostId: Int) {
        val navHostFragment = activity.supportFragmentManager.findFragmentById(navHostId) as NavHostFragment
        navController = navHostFragment.navController
    }

    fun navigate(target: Int) {
        if (isCurrentDestination(target)) {
            Logger.i("[ You are currently in the same destination as the target destination. ]")
            return
        }

        when (navController.currentDestination) {
            navController.graph[R.id.AirQualityFragment] -> {
                fromAirQualityFragmentTo(target)
            }
            navController.graph[R.id.SearchAddressFragment] -> {
                fromSearchAddressFragmentTo(target)
            }
            else -> {
                navController.navigate(target)
            }
        }
    }

    fun navigateWithBundle(target: Int, bundle: Bundle) {
        if (isCurrentDestination(target)) {
            Logger.i("[ You are currently in the same destination as the target destination. ]")
            return
        }

        when (navController.currentDestination) {
            navController.graph[R.id.SearchAddressFragment] -> {
                fromSearchAddressFragmentTo(target, bundle)
            }
            else -> throw IllegalAccessException("Not allowed to navigate to $target")
        }
    }

    private fun fromSearchAddressFragmentTo(@LayoutRes target: Int, bundle: Bundle? = null) {
        if (target == R.id.AirQualityFragment) {
            navController.navigate(R.id.action_SearchAddressFragment_to_AirQualityFragment, bundle)
        } else {
            throw IllegalAccessException("Not allowed to navigate to $target")
        }
    }

    private fun fromAirQualityFragmentTo(target: Int) {
        when (target) {
            R.id.SearchAddressFragment -> {
                navController.navigate(R.id.action_AirQualityFragment_to_SearchAddressFragment)
            }
            else -> throw IllegalAccessException("Not allowed to navigate to $target")
        }
    }

    private fun isCurrentDestination(@LayoutRes target: Int): Boolean {
        if (navController.currentDestination == navController.graph[target]) {
            return true
        }
        return false
    }

    fun navigateUp() {
        navController.navigateUp()
    }
}
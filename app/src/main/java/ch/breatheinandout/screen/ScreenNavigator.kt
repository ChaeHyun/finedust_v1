package ch.breatheinandout.screen

import android.os.Bundle
import androidx.annotation.IdRes
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
    private val className = ScreenNavigator::class.simpleName
    private lateinit var navController: NavController

    private val dialogs: List<Int> = listOf(R.id.AddressListDialog)
    private val topLevelDestinations: List<Int> = listOf(R.id.AirQualityFragment, R.id.ForecastFragment)

    fun initNavController(navHostId: Int) {
        val navHostFragment = activity.supportFragmentManager.findFragmentById(navHostId) as NavHostFragment
        navController = navHostFragment.navController
    }

    fun showDialog(target: Int) {
        if (!dialogs.contains(target)) {
            Logger.e("[THERE IS NO MATCHING TARGET DIALOG HERE. at $className]")
            return
        }
        navController.navigate(target)

    }

    fun navigate(target: Int) {
        if (isCurrentDestination(target)) {
            Logger.i("[ You are currently in the same destination as the target destination. at $className]")
            return
        }

        when (navController.currentDestination) {
            navController.graph[R.id.AirQualityFragment] -> {
                fromAirQualityFragmentTo(target)
            }
            navController.graph[R.id.SearchAddressFragment] -> {
                fromSearchAddressFragmentTo(target)
            }
            navController.graph[R.id.ForecastFragment] -> {
                fromForecastFragmentTo(target)
            }
            // TODO : fromInformativeFragment
            navController.graph[R.id.InformativeFragment] -> {
                fromInformativeFragmentTo(target)
            }
            else -> {
                navController.navigate(target)
            }
        }
    }

    fun navigateWithBundle(target: Int, bundle: Bundle) {
        if (isCurrentDestination(target)) {
            Logger.i("[ You are currently in the same destination as the target destination.  at $className]")
            return
        }

        when (navController.currentDestination) {
            navController.graph[R.id.SearchAddressFragment] -> {
                fromSearchAddressFragmentTo(target, bundle)
            }
            navController.graph[R.id.AddressListDialog] -> {
                fromAddressListDialogTo(target, bundle)
            }
            else -> throw IllegalAccessException("[$className] Not allowed to navigate to $target")
        }
    }

    private fun fromAddressListDialogTo(target: Int, bundle: Bundle?) {
        popUpToTopLevelDestinations(target)
        navController.navigate(target, bundle)
    }

    private fun fromInformativeFragmentTo(target: Int) {
        popUpToTopLevelDestinations(target)
        navController.navigate(target)
    }

    private fun fromForecastFragmentTo(target: Int) {
        when (target) {
            R.id.AirQualityFragment -> {
                popUpToTopLevelDestinations(target)
                navController.navigate(R.id.AirQualityFragment)
            }
            else -> navController.navigate(target)
        }
    }

    private fun fromSearchAddressFragmentTo(target: Int, bundle: Bundle? = null) {
        popUpToTopLevelDestinations(target)
        navController.navigate(target, bundle)
    }

    private fun fromAirQualityFragmentTo(target: Int) {
        when (target) {
            R.id.SearchAddressFragment -> {
                navController.navigate(R.id.SearchAddressFragment)
            }
            R.id.ForecastFragment -> {
                popUpToTopLevelDestinations(target)
                navController.navigate(R.id.ForecastFragment)
            }
            else -> navController.navigate(target)
        }
    }

    private fun popUpToTopLevelDestinations(@IdRes target: Int) {
        if (topLevelDestinations.contains(target)) {
            topLevelDestinations.forEach { dest -> navController.popBackStack(dest, true) }
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
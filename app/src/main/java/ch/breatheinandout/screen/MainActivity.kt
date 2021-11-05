package ch.breatheinandout.screen

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import ch.breatheinandout.R
import ch.breatheinandout.common.permissions.PermissionRequester
import ch.breatheinandout.screen.navdrawer.NavDrawerHelper
import ch.breatheinandout.screen.navdrawer.NavDrawerWidgetView
import ch.breatheinandout.screen.widgetview.WidgetViewFactory
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity(), NavDrawerHelper, NavDrawerWidgetView.Listener {

    @Inject lateinit var permissionRequester: PermissionRequester
    @Inject lateinit var widgetViewFactory: WidgetViewFactory

    private lateinit var widgetView: NavDrawerWidgetView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        widgetView = widgetViewFactory.createNavDrawerWidgetView(null)
        setContentView(widgetView.getRootView())
        Logger.i("Done -> MainActivity#onCreate()")

        syncToolbarAndDrawer()
    }

    private fun syncToolbarAndDrawer() {
        val navHostFragment = supportFragmentManager.findFragmentById(widgetView.getFrameLayout().id) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfigWithDrawer = AppBarConfiguration(
            setOf(R.id.AirQualityFragment),         // Top-level destinations
            widgetView.getDrawerLayout())

        widgetView.getToolbarWidgetView().apply { setupWithNavController(navController, appBarConfigWithDrawer) }
    }

    override fun onStart() {
        super.onStart()
        widgetView.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        widgetView.unregisterListener(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
//        Logger.v("[PERMISSION] Delegate the result to the PermissionProvider.")
        // It's inevitable since Activity is in charge of this job by the Android framework.
        // This method call must be launched here to delegate permission granted results To [PermissionRequester].
        permissionRequester.onRequestPermissionResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onNavItemClicked(item: NavDrawerWidgetView.DrawerItem) {
        Logger.d(" # check -> $item")
        when (item) {
            NavDrawerWidgetView.DrawerItem.Home -> {
                // TODO: Something when `Home` menu item is clicked.
            }
            NavDrawerWidgetView.DrawerItem.Search -> {
                findNavController(widgetView.getFrameLayout().id).navigate(R.id.searchAddressFragment)
            }
            NavDrawerWidgetView.DrawerItem.Setting -> {
                // TODO: when `Setting` menu item is clicked.
            }
        }
    }

    // ------- Features of Toolbar -------
    override fun setToolbarTitle(title: String) {
        widgetView.setToolbarTitle(title)
    }

    override fun setToolbarBackgroundColor(color: ColorDrawable) {
        widgetView.setToolbarBackgroundColor(color)
    }

    override fun setToolbarVisibility(visible: Boolean) {
        widgetView.setToolbarVisibility(visible)
    }
}
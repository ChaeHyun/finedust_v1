package ch.breatheinandout.screen

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import ch.breatheinandout.R
import ch.breatheinandout.common.permissions.PermissionRequester
import ch.breatheinandout.screen.airquality.DefaultColor
import ch.breatheinandout.screen.navdrawer.NavDrawerHelper
import ch.breatheinandout.screen.navdrawer.NavDrawerWidgetView
import ch.breatheinandout.screen.navdrawer.NavDrawerWidgetView.*
import ch.breatheinandout.screen.widgetview.WidgetViewFactory
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity(), NavDrawerHelper, Listener {

    @Inject lateinit var permissionRequester: PermissionRequester
    @Inject lateinit var widgetViewFactory: WidgetViewFactory
    @Inject lateinit var screenNavigator: ScreenNavigator

    private lateinit var widgetView: NavDrawerWidgetView
    private var isReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        widgetView = widgetViewFactory.createNavDrawerWidgetView(null)
        setContentView(widgetView.getRootView())
        syncToolbarAndDrawer()
        screenNavigator.initNavController(widgetView.getFrameLayout().id)

    }

    private fun syncToolbarAndDrawer() {
        val navHostFragment = supportFragmentManager.findFragmentById(widgetView.getFrameLayout().id) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfigWithDrawer = AppBarConfiguration(
            setOf(
                R.id.AirQualityFragment,
                R.id.ForecastFragment
            ),         // Top-level destinations
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

    override fun onBackPressed() {
        if (isDrawerOpen()) {
            closeDrawer()
        } else {
            super.onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        // It's inevitable since Activity is in charge of this job by the Android framework.
        // This method call must be launched here to delegate permission granted results To [PermissionRequester].
        permissionRequester.onRequestPermissionResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onNavItemClicked(item: DrawerItem) {
        Logger.v(" # [NavDrawerItem.Click] check -> ${item::class.simpleName}")
        when (item) {
            DrawerItem.SelectAddress, DrawerItem.AppInfoDialog -> { screenNavigator.showDialog(item.resId) }
            DrawerItem.Forecast, DrawerItem.Home, DrawerItem.Search,
            DrawerItem.Informative, DrawerItem.Setting -> { screenNavigator.navigate(item.resId) }
        }
    }

    // ------- Features of Toolbar -------
    override fun setToolbarTitle(title: String) = widgetView.setToolbarTitle(title)
    override fun setToolbarBackgroundColor(color: ColorDrawable) = widgetView.setToolbarBackgroundColor(color)
    override fun setToolbarVisibility(visible: Boolean) = widgetView.setToolbarVisibility(visible)
    override fun setupToolbarOptionsMenu() = widgetView.setupToolbarOptionsMenu()
    override fun clearToolbarOptionsMenu() = widgetView.clearToolbarOptionsMenu()
    override fun resetToolbarColor() {
        setToolbarBackgroundColor(ColorDrawable(DefaultColor.defaultColor[0]))
        applyStatusBarColor(DefaultColor.defaultColor[1])
    }

    override fun applyStatusBarColor(resId: Int) {
        val window = this.window
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = resId
        }
    }

    override fun isDrawerOpen(): Boolean = widgetView.isDrawerOpen()
    override fun closeDrawer() = widgetView.closeDrawer()

}
package ch.breatheinandout.screen.navdrawer

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import ch.breatheinandout.R
import ch.breatheinandout.screen.widgetview.WidgetViewFactory
import com.google.android.material.navigation.NavigationView

class NavDrawerWidgetViewImpl(
    private val inflater: LayoutInflater,
    private val parent: ViewGroup?,
    private val widgetViewFactory: WidgetViewFactory
) : NavDrawerWidgetView(inflater, parent, R.layout.activity_main) {



    private val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
    private val frameLayout: FrameLayout = findViewById(R.id.nav_host_fragment_content_main)
    private val navigationView: NavigationView = findViewById(R.id.nav_view)

    init {

        navigationView.setNavigationItemSelectedListener { menuItem ->
            drawerLayout.closeDrawers()
            val drawerItem: DrawerItem = when (menuItem.itemId) {
                R.id.nav_home -> DrawerItem.Home
                R.id.nav_search -> DrawerItem.Search
                R.id.nav_setting -> DrawerItem.Setting
                else -> {
                    throw IllegalStateException("This menu item is not supported yet.")
                }
            }
            getListeners().map { it.onNavItemClicked(drawerItem) }

            return@setNavigationItemSelectedListener false
        }
    }

    // ------- Features of NavDrawer -------
    override fun isDrawerOpen(): Boolean = drawerLayout.isOpen
    override fun openDrawer() = drawerLayout.openDrawer(GravityCompat.START)
    override fun closeDrawer() = drawerLayout.closeDrawers()


    // ------- Getter methods of View Component -------
    override fun getNavigationView(): NavigationView = navigationView
    override fun getDrawerLayout(): DrawerLayout = drawerLayout
    override fun getFrameLayout(): FrameLayout = frameLayout
}
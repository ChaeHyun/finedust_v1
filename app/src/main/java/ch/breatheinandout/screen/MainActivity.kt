package ch.breatheinandout.screen

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import ch.breatheinandout.R
import ch.breatheinandout.databinding.ActivityMainBinding
import ch.breatheinandout.screen.navdrawer.NavDrawerHelper
import ch.breatheinandout.screen.navdrawer.NavDrawerWidgetView
import ch.breatheinandout.screen.widgetview.WidgetViewFactory
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity(), NavDrawerHelper, NavDrawerWidgetView.Listener {

    @Inject lateinit var widgetViewFactory: WidgetViewFactory

    private lateinit var widgetView: NavDrawerWidgetView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        widgetView = widgetViewFactory.createNavDrawerWidgetView(null)
        setContentView(widgetView.getRootView())
        Logger.i("Done -> MainActivity#onCreate()")
    }

    override fun onStart() {
        super.onStart()
        widgetView.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        widgetView.unregisterListener(this)
    }

    override fun onNavItemClicked(item: NavDrawerWidgetView.DrawerItem) {
        Logger.d(" # check -> $item")
        when (item) {
            NavDrawerWidgetView.DrawerItem.Home -> {
                Toast.makeText(this, "[Home] clicked!", Toast.LENGTH_SHORT).show()
            }
            NavDrawerWidgetView.DrawerItem.Search -> {
                Toast.makeText(this, "[Search] clicked!", Toast.LENGTH_SHORT).show()
            }
            NavDrawerWidgetView.DrawerItem.Setting -> {
                Toast.makeText(this, "[Setting] clicked!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun setToolbarTitle(title: String) {
        TODO("Not yet implemented")
    }

    override fun setToolbarBackgroundColor(color: ColorDrawable) {
        TODO("Not yet implemented")
    }

    override fun setToolbarVisibility(visible: Boolean) {
        TODO("Not yet implemented")
    }
}
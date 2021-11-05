package ch.breatheinandout.dependencyinjection

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import ch.breatheinandout.common.permissions.PermissionRequester
import ch.breatheinandout.screen.ScreenNavigator
import ch.breatheinandout.screen.navdrawer.NavDrawerHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {
    @Provides
    fun navDrawerHelper(activity: Activity) : NavDrawerHelper = activity as NavDrawerHelper

    @Provides
    fun appCompatActivity(activity: Activity) : AppCompatActivity = activity as AppCompatActivity

    @Provides
    fun layoutInflater(activity: AppCompatActivity) : LayoutInflater = LayoutInflater.from(activity)

    @ActivityScoped
    @Provides
    fun permissionProvider(activity: Activity) : PermissionRequester = PermissionRequester(activity)

    @ActivityScoped
    @Provides
    fun screenNavigator(activity: AppCompatActivity) : ScreenNavigator = ScreenNavigator(activity)
}
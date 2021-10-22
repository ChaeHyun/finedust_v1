package ch.breatheinandout.dependencyinjection

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @Provides
    fun appCompatActivity(activity: Activity) : AppCompatActivity = activity as AppCompatActivity

    @Provides
    fun layoutInflater(activity: AppCompatActivity) : LayoutInflater = LayoutInflater.from(activity)
}
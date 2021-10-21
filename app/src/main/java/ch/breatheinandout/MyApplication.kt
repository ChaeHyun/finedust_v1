package ch.breatheinandout

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Logger.addLogAdapter(AndroidLogAdapter())
        Logger.v("LOGGER TEST")
    }
}
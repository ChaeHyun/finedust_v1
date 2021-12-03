package ch.breatheinandout.screen.activity

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.concurrent.thread

private const val TIME_SPLASH_SCREEN_SHOWN: Long = 600

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {
    var isReadyToDraw: Boolean = false

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        thread(start = true) {
            Thread.sleep(TIME_SPLASH_SCREEN_SHOWN)
            /* This period of time can be used to set up initial data loading for the large data.
               but it is not necessary for now . */
            isReadyToDraw = true
        }
    }
}
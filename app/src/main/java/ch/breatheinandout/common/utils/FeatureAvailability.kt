package ch.breatheinandout.common.utils

import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.location.LocationManagerCompat
import javax.inject.Inject

class FeatureAvailability @Inject constructor(
    private val locationManager: LocationManager,
    private val connectivityManager: ConnectivityManager
) {
    fun isGpsFeatureOn() : Boolean = LocationManagerCompat.isLocationEnabled(locationManager)

    fun isNetworkActive() : Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val isNetworkActive = connectivityManager.activeNetwork ?: return false
            connectivityManager.getNetworkCapabilities(isNetworkActive) ?: return false
            return true
        } else {
            // In case of API Level below 23...
            val isNetworkActive = connectivityManager.activeNetworkInfo ?: return false
            return isNetworkActive.isConnectedOrConnecting
        }
    }
}
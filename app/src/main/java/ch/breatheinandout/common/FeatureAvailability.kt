package ch.breatheinandout.common

import android.location.LocationManager
import androidx.core.location.LocationManagerCompat
import javax.inject.Inject

class FeatureAvailability @Inject constructor(
    private val locationManager: LocationManager
) {
    fun isGpsFeatureOn() : Boolean = LocationManagerCompat.isLocationEnabled(locationManager)
}
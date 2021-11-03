package ch.breatheinandout.screen.airquality

import ch.breatheinandout.location.model.LocationPoint
import ch.breatheinandout.nearbystation.model.NearbyStation

sealed class AirQualityViewState

object Loading : AirQualityViewState()
object Error : AirQualityViewState()
object Refresh : AirQualityViewState()

data class Content(
    val location: LocationPoint,
    val nearbyStation: NearbyStation,
) : AirQualityViewState()
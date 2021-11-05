package ch.breatheinandout.screen.airquality

import ch.breatheinandout.domain.location.model.LocationPoint
import ch.breatheinandout.domain.nearbystation.model.NearbyStation

sealed class AirQualityViewState

object Loading : AirQualityViewState()
object Error : AirQualityViewState()
object Refresh : AirQualityViewState()

data class Content(
    val location: LocationPoint,
    val nearbyStation: NearbyStation,
) : AirQualityViewState()
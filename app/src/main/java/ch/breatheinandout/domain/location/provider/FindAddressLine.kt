package ch.breatheinandout.domain.location.provider

import android.content.Context
import android.location.Address
import android.location.Geocoder
import ch.breatheinandout.domain.location.model.address.AddressLine
import ch.breatheinandout.domain.location.model.address.AddressLineMapper
import ch.breatheinandout.domain.location.model.coordinates.Coordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*
import javax.inject.Inject


class FindAddressLine @Inject constructor(
    private val context: Context,
    private val mapper: AddressLineMapper
) {
    @Throws(IOException::class)
    suspend fun findAddress(wgsCoords: Coordinates): AddressLine = withContext(Dispatchers.IO) {
        val geocoder = Geocoder(context, Locale.KOREA)
        try {
            @Suppress("BlockingMethodInNonBlockingContext")
            val addresses: List<Address> = geocoder.getFromLocation(
                wgsCoords.latitudeY.toDouble(),
                wgsCoords.longitudeX.toDouble(),
                1
            )
            return@withContext mapper.mapToDomainModel(addresses[0])
        } catch (exception: IOException) {
            // if the network is unavailable or any other I/O problem occurs
            throw exception
        }
    }
}
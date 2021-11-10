package ch.breatheinandout.domain.lastused

import ch.breatheinandout.common.utils.PreferencesUtils
import ch.breatheinandout.domain.location.model.LocationPoint
import com.orhanobut.logger.Logger
import javax.inject.Inject

class SaveLastUsedLocationUseCase @Inject constructor(
    private val prefsUtils: PreferencesUtils
) {

    fun save(mode: String, location: LocationPoint) {
        val lastUsed = LastUsedLocation(mode, location)
        prefsUtils.putObject(PreferencesUtils.KEY_LAST_UPDATED, lastUsed)
        Logger.v("[PREFS.SAVE] $mode, $location")
    }
}
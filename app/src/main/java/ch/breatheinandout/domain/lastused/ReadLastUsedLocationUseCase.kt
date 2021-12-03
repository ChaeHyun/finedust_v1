package ch.breatheinandout.domain.lastused

import ch.breatheinandout.common.utils.PreferencesUtils
import ch.breatheinandout.domain.lastused.model.LastUsedLocation
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import javax.inject.Inject

class ReadLastUsedLocationUseCase @Inject constructor(
    private val prefsUtils: PreferencesUtils
){
    private val typeToken = object : TypeToken<LastUsedLocation>() {}.type

    fun read() : LastUsedLocation {
        val retrieved = prefsUtils.getObject(PreferencesUtils.KEY_LAST_UPDATED, "", typeToken) as LastUsedLocation
        Logger.v("[PREFS.READ] -> $retrieved")
        return retrieved
    }
}
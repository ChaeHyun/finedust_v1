package ch.breatheinandout.screen.setting

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ch.breatheinandout.R
import ch.breatheinandout.screen.ScreenNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {
    @Inject lateinit var screenNavigator: ScreenNavigator
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context?.setTheme(R.style.Theme_SettingScreenColorTheme)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = FILE_SETTINGS
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val prefs: SharedPreferences = preferenceManager.sharedPreferences

        val addAddr: Preference? = findPreference(KEY_ADD_ADDRESS)
        addAddr?.onPreferenceClickListener = this

        val listAddr: Preference? = findPreference(KEY_LIST_ADDRESS)
        listAddr?.onPreferenceClickListener = this

    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        when (preference?.key) {
            KEY_LIST_ADDRESS -> {
                screenNavigator.showDialog(R.id.AddressListDialog)
            }
            KEY_ADD_ADDRESS -> {
                screenNavigator.navigate(R.id.SearchAddressFragment)
            }
        }
        return true
    }

    companion object {
        const val FILE_SETTINGS = "prefs_setting"

        // ----- KEY -----
        const val KEY_ADD_ADDRESS = "KEY_NEW_ADDRESS"
        const val KEY_LIST_ADDRESS = "KEY_LIST_ADDRESS"
    }
}
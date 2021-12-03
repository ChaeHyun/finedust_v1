package ch.breatheinandout.screen.setting

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ch.breatheinandout.R
import ch.breatheinandout.screen.common.ScreenNavigator
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

        val askMail: Preference? = findPreference(KEY_ASK_MAIL)
        askMail?.onPreferenceClickListener = this

        val recommend: Preference? = findPreference(KEY_RECOMMEND)
        recommend?.onPreferenceClickListener = this
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        when (preference?.key) {
            KEY_LIST_ADDRESS -> {
                screenNavigator.showDialog(R.id.AddressListDialog)
            }
            KEY_ADD_ADDRESS -> {
                screenNavigator.navigate(R.id.SearchAddressFragment)
            }
            KEY_ASK_MAIL -> {
                askMail()
            }
            KEY_RECOMMEND -> {
                recommend()
            }
        }
        return true
    }

    private fun askMail() {
        val uri: Uri = Uri.parse("mailto:zephyrish9@gmail.com")
        val email: Intent = Intent(Intent.ACTION_SENDTO, uri).apply {
            putExtra(Intent.EXTRA_SUBJECT, "들숨날숨 앱 문의사항")
            putExtra(Intent.EXTRA_TEXT, "문의사항을 입력해주세요.")
        }
        startActivity(email)
    }

    private fun recommend() {
        val marketUri = Uri.parse("https://play.google.com/store/apps/details?id=ch.breatheinandout")
        val recommendIntent = Intent(Intent.ACTION_SEND).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "[들숨날숨 - 대기환경/미세먼지/초미세먼지]\n")
            putExtra(Intent.EXTRA_TEXT, "\n우리동네 실시간 대기환경 쉽게 확인하세요.\n${marketUri}")
        }
        startActivity(Intent.createChooser(recommendIntent, "들숨날숨 추천하기"))
    }

    companion object {
        const val FILE_SETTINGS = "prefs_setting"

        // ----- KEY -----
        const val KEY_ADD_ADDRESS = "KEY_NEW_ADDRESS"
        const val KEY_LIST_ADDRESS = "KEY_LIST_ADDRESS"
        const val KEY_ASK_MAIL = "KEY_ASK_MAIL"
        const val KEY_RECOMMEND = "KEY_RECOMMEND"
    }
}
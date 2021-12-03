package ch.breatheinandout.screen.dialogs

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import ch.breatheinandout.R
import ch.breatheinandout.screen.activity.MainActivity
import com.orhanobut.logger.Logger

class AppInfoDialog : DialogFragment() {

    private lateinit var dismissIcon: ImageView
    private lateinit var textContent: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_app_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            val componentName = ComponentName(requireActivity(), MainActivity::class.java)
            val pInfo = requireActivity().packageManager.getPackageInfo(componentName.packageName, 0)

            val versionName = pInfo.versionName

            textContent = view.findViewById(R.id.text_version_name)
            textContent.text = String.format(getString(R.string.app_info_version_name), versionName)
        } catch (pe: PackageManager.NameNotFoundException) {
            Logger.e("[Error] Failed to get info from the PackageManager.")
        }


        dismissIcon = view.findViewById(R.id.imageFinish)
        dismissIcon.setOnClickListener { dismiss() }

    }
}
package ch.breatheinandout.screen.airquality

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import ch.breatheinandout.R
import ch.breatheinandout.common.permissions.PermissionMember
import ch.breatheinandout.common.permissions.PermissionRequester
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AirQualityFragment : Fragment() , PermissionRequester.Listener{

    @Inject lateinit var permissionRequester: PermissionRequester
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_airquality, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.v("onViewCreated()")

        // Testing for requesting permissions
        button = view.findViewById(R.id.button)

        // TestFragment.kt
        button.setOnClickListener {
            Toast.makeText(context, "PERMISSION BUTTON CLICKED", Toast.LENGTH_SHORT).show()
            // 권한 요청하기
            permissionRequester.requestPermission(
                PermissionMember.FineLocation,
                PermissionMember.REQUEST_CODE_PERMISSION
            )
        }
    }

    override fun onStart() {
        super.onStart()
        permissionRequester.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        permissionRequester.unregisterListener(this)
    }

    // TestFragment.kt
    override fun onRequestPermissionResult(
        requestCode: Int,
        result: PermissionRequester.PermissionResult,
    ) {
        // Check the received result in fragment
        result.granted.map { Logger.v("# GRANTED: ${it.getAndroidPermission()}") }
        result.denied.map { Logger.v("# DENIED: ${it.getAndroidPermission()}") }
        result.doNotAsk.map { Logger.v("# DoNotAsk: ${it.getAndroidPermission()}") }
    }

    override fun onPermissionRequestCancelled(requestCode: Int) {
        Logger.d(" Permission Cancelled -> requestCode: $requestCode")
    }
}
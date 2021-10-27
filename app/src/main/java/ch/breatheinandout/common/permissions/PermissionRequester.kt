package ch.breatheinandout.common.permissions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ch.breatheinandout.common.BaseObservable
import javax.inject.Inject

/**
 * It handles the process of requesting and granting runtime-permissions.
 * It should be a scoped-instance to share the result with multiple child fragments.
 *
 * Activity must be provided because it's essential to use ContextCompat to request Permission in the Android Framework.
* */
class PermissionRequester @Inject constructor(private val activity: Activity) : BaseObservable<PermissionRequester.Listener>() {

    interface Listener {
        fun onRequestPermissionResult(requestCode: Int, result: PermissionResult)
        fun onPermissionRequestCancelled(requestCode: Int)
    }

    class PermissionResult(
        val granted: List<PermissionMember>,
        val denied: List<PermissionMember>,
        val doNotAsk: List<PermissionMember>
    )

    fun hasPermission(permission: PermissionMember): Boolean {
        return ContextCompat.checkSelfPermission(activity, permission.getAndroidPermission()) == PackageManager.PERMISSION_GRANTED
    }

    fun hasAllPermission(permissions: Array<PermissionMember>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission.getAndroidPermission()) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    fun requestPermission(permission: PermissionMember, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission.getAndroidPermission()), requestCode)
    }

    fun requestAllPermissions(permissions: Array<PermissionMember>, requestCode: Int) {
        val androidPermissions: Array<String> = permissions.map { it.getAndroidPermission() }.toTypedArray()
        ActivityCompat.requestPermissions(activity, androidPermissions, requestCode)
    }

    // This is the method taking a delegated job from Activity.onRequestPermissionResult.
    fun onRequestPermissionResult(requestCode: Int, androidPermissions: Array<out String>, grantResults: IntArray) {
        if (androidPermissions.isEmpty() || grantResults.isEmpty())
            notifyPermissionsRequestCancelled(requestCode)

        val grantedPermissions: MutableList<PermissionMember> = mutableListOf()
        val deniedPermissions: MutableList<PermissionMember> = mutableListOf()
        val doNotAskPermissions: MutableList<PermissionMember> = mutableListOf()

        androidPermissions.forEachIndexed { index, permissionItem ->
            val permission = PermissionMember.fromAndroidPermission(permissionItem)

            when {
                grantResults[index] == PackageManager.PERMISSION_GRANTED -> {
                    grantedPermissions.add(permission)          // Permission granted.
                }
                ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionItem) -> {
                    deniedPermissions.add(permission)           // Permission denied.
                }
                else -> doNotAskPermissions.add(permission)     // Permission denied and do not ask again
            }
        }
        val result = PermissionResult(grantedPermissions, deniedPermissions, doNotAskPermissions)
        notifyPermissionResult(requestCode, result)
    }


    private fun notifyPermissionResult(requestCode: Int, result: PermissionResult) {
        getListeners().map { it.onRequestPermissionResult(requestCode, result) }
    }

    private fun notifyPermissionsRequestCancelled(requestCode: Int) {
        getListeners().map { it.onPermissionRequestCancelled(requestCode) }
    }
}
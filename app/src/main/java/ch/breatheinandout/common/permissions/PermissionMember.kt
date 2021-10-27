package ch.breatheinandout.common.permissions

sealed class PermissionMember(
    private val androidPermission: String
) {
    companion object {
        const val REQUEST_CODE_PERMISSION = 300

        @JvmStatic
        fun fromAndroidPermission(requestPermission: String): PermissionMember {
            // List<KClass<out PermissionMember> -> PermissionMember
            return PermissionMember::class.sealedSubclasses
                .map { it.objectInstance as PermissionMember }
                .first { it.getAndroidPermission() == requestPermission }
        }
    }

    fun getAndroidPermission() : String = androidPermission

    // The list of permissions that needs to be acquired.
    object FineLocation: PermissionMember(androidPermission = "android.permission.ACCESS_FINE_LOCATION")
}


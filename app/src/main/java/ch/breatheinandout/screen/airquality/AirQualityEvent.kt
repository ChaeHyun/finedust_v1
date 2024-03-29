package ch.breatheinandout.screen.airquality

import ch.breatheinandout.common.permissions.PermissionMember

sealed class AirQualityEvent

data class Permission(val permission: PermissionMember) : AirQualityEvent()
data class ToastMessage(val message: String) : AirQualityEvent()

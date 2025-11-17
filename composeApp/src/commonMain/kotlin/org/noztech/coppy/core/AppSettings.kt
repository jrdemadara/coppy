package org.noztech.coppy.core

import com.russhwolf.settings.Settings

class AppSettings(private val settings:  Settings) {
    companion object {
        private const val KEY_IS_FIRST_LAUNCH = "is_first_launch"
        private const val KEY_IS_BIOMETRIC_ENABLED = "is_biometric_enabled"
        private const val KEY_FCM_TOKEN = "fcm_token"
    }

    fun setFirstLaunch() {
        settings.putBoolean(KEY_IS_FIRST_LAUNCH, false)
    }

    fun setBiometric(isEnabled: Boolean) {
        settings.putBoolean(KEY_IS_BIOMETRIC_ENABLED, isEnabled)
    }

    fun saveFCMToken(fcmToken: String) {
        settings.putString(KEY_FCM_TOKEN, fcmToken)
    }

    fun isFirstLaunch(): Boolean {
        return settings.getBoolean(KEY_IS_FIRST_LAUNCH, true)
    }

    fun isBiometricEnabled(): Boolean {
        return settings.getBoolean(KEY_IS_BIOMETRIC_ENABLED, false)
    }
}
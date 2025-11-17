package org.noztech.coppy.feature.auth

import androidx.lifecycle.ViewModel
import org.noztech.coppy.core.util.BiometricAuthResult
import org.noztech.coppy.core.util.BiometricAuthStatus
import org.noztech.coppy.core.util.BiometricAuthenticator


class AuthViewModel(private val biometricAuthenticator: BiometricAuthenticator): ViewModel() {

    fun initiateBiometricLogin() {
        when (biometricAuthenticator.canAuthenticate()) {
            BiometricAuthStatus.AVAILABLE -> {
                biometricAuthenticator.authenticate("Login", "Please authenticate to log in") { result ->
                    when (result) {
                        BiometricAuthResult.Success -> println("Authentication successful!")
                        BiometricAuthResult.Failure -> println("Authentication failed.")
                        BiometricAuthResult.Error -> println("Authentication error.")
                        BiometricAuthResult.UserCancelled -> println("Authentication cancelled by user.")
                    }
                }
            }
            BiometricAuthStatus.NOT_ENROLLED -> println("Biometrics not enrolled.")
            BiometricAuthStatus.NO_HARDWARE -> println("No biometric hardware available.")
            BiometricAuthStatus.UNKNOWN_ERROR -> println("Unknown biometric error.")
        }
    }
}
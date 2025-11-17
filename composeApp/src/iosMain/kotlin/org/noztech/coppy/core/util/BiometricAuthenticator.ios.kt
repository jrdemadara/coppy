package org.noztech.coppy.core.util

import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicy.LAPolicyDeviceOwnerAuthenticationWithBiometrics
import platform.LocalAuthentication.LAError.LAErrorUserCancel
import platform.LocalAuthentication.LAError.LAErrorBiometryNotEnrolled

actual class BiometricAuthenticator {

    actual fun canAuthenticate(): BiometricAuthStatus {
        val context = LAContext()
        var error: NSError? = null
        if (context.canEvaluatePolicy(LAPolicyDeviceOwnerAuthenticationWithBiometrics, error.ptr)) {
            return BiometricAuthStatus.AVAILABLE
        } else {
            val laError = error?.let { it as LAError }
            return when (laError?.code) {
                LAErrorBiometryNotEnrolled -> BiometricAuthStatus.NOT_ENROLLED
                // Add other LAError codes as needed for more specific handling
                else -> BiometricAuthStatus.UNKNOWN_ERROR
            }
        }
    }

    actual fun authenticate(title: String, description: String, onResult: (BiometricAuthResult) -> Unit) {
        val context = LAContext()
        context.evaluatePolicy(
            LAPolicyDeviceOwnerAuthenticationWithBiometrics,
            "Authenticate to access this feature", // Reason string for iOS
            completionHandler = { success, error ->
                if (success) {
                    onResult(BiometricAuthResult.Success)
                } else {
                    val laError = error?.let { it as LAError }
                    when (laError?.code) {
                        LAErrorUserCancel -> onResult(BiometricAuthResult.UserCancelled)
                        else -> onResult(BiometricAuthResult.Error)
                    }
                }
            }
        )
    }
}
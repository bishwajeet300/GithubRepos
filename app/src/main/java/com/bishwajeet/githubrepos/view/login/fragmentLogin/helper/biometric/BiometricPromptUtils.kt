package com.bishwajeet.githubrepos.view.login.fragmentLogin.helper.biometric

import android.util.Log
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.bishwajeet.githubrepos.R

object BiometricPromptUtils {

    private const val TAG = "BiometricPromptUtils"

    fun createBiometricPrompt(
        activity: FragmentActivity,
        authSuccess: (BiometricPrompt.AuthenticationResult) -> Unit
    ): BiometricPrompt {

        val executor = ContextCompat.getMainExecutor(activity)

        val callback = object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.e(TAG, "errCode is $errorCode and errString is $errString")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.i(TAG, "Biometric authentication successful")
                authSuccess(result)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.e(TAG, "Biometric authentication failed due to unknown reason")
            }
        }

        return BiometricPrompt(activity, executor, callback)
    }


    fun createPromptInfo(activity: FragmentActivity): BiometricPrompt.PromptInfo {

        return BiometricPrompt.PromptInfo.Builder().apply {
            setTitle(activity.getString(R.string.prompt_info_title))
            setSubtitle(activity.getString(R.string.prompt_info_subtitle))
            setDescription(activity.getString(R.string.prompt_info_description))
            setConfirmationRequired(false)
            setNegativeButtonText(activity.getString(R.string.prompt_info_use_app_password))
        }.build()
    }
}
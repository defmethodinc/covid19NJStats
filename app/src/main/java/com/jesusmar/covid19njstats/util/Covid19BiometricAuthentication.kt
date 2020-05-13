package com.jesusmar.covid19njstats.util

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.util.concurrent.Executor
import javax.net.ssl.HttpsURLConnection

open class Covid19BiometricAuthentication(private val context: Context) {

    lateinit var covid19AuthCallBacks: Covid19AuthCallBacks

    fun setCallBack(covid19AuthCallBacks: Covid19AuthCallBacks){
        this.covid19AuthCallBacks = covid19AuthCallBacks
    }

    interface Covid19AuthCallBacks {
        fun onSuccess()
        fun onFail()
    }


    fun runAuthentication() {

        val authenticationCallBack = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {
                super.onAuthenticationError(errorCode, errString)
                covid19AuthCallBacks.onFail()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                covid19AuthCallBacks.onSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                covid19AuthCallBacks.onFail()
            }
        }

        val executor: Executor = ContextCompat.getMainExecutor(context)
        val biometricPrompt = BiometricPrompt(context as FragmentActivity, executor, authenticationCallBack)
        val biometricManager = BiometricManager.from(context)

        val promptInfo: BiometricPrompt.PromptInfo = when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                BiometricPrompt.PromptInfo.Builder()
                    .setConfirmationRequired(false)
                    .setTitle("Login")
                    .setNegativeButtonText("Continue without Authenticate")
                    .build()
            else ->
                BiometricPrompt.PromptInfo.Builder()
                    .setConfirmationRequired(false)
                    .setDeviceCredentialAllowed(true)
                    .setTitle("Login")
                    .build()
        }

        biometricPrompt.authenticate(promptInfo)
    }
}
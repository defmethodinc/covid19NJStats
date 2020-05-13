package com.jesusmar.covid19njstats.util

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import com.google.gson.Gson
import com.jesusmar.covid19njstats.BuildConfig
import com.jesusmar.covid19njstats.R
import com.jesusmar.covid19njstats.models.Auth0Token
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.*
import java.net.SocketTimeoutException
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class Auth0AuthenticationTask(private val context: Context) : AsyncTask<Any, Void, Auth0Token>() {

    lateinit var mListener: Auth0Listener

    interface Auth0Listener {
        fun success()
        fun fail()
    }

    fun setListener(listener: Auth0Listener) {
        mListener = listener
    }

    fun getToken() {
        execute()
    }

    companion object{
        fun getAuth0Token(): Auth0Token? {

            val body =
                "{\"client_id\":\"${BuildConfig.CLIENT_ID}\",\"client_secret\":\"${BuildConfig.CLIENT_SECRET}\",\"audience\":\"${BuildConfig.AUDIENCE}\",\"grant_type\":\"${BuildConfig.GRANT_TYPE}\"}"
            val reqBody = RequestBody.create(MediaType.get("application/json"), body)
            val response = Covid19API.getAuth0Service().getToken(reqBody).execute()

            return if (response.isSuccessful) { response.body()!! } else null
        }
    }

    override fun doInBackground(vararg params: Any?): Auth0Token? {
        return getAuth0Token()
    }

    override fun onPostExecute(result: Auth0Token?) {
        if (result != null) {
            val editor: SharedPreferences.Editor = context.applicationContext.getSharedPreferences(
                context.applicationContext.getString(R.string.sharedPrefs),
                Context.MODE_PRIVATE
            ).edit()
            editor.putString(
                context.getString(R.string.auth0TokenKey),
                "${result.token_type} ${result.access_token}"
            )
            editor.apply()
        } else {
            mListener.fail()
        }
        mListener.success()
        super.onPostExecute(result)
    }
}
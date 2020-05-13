package com.jesusmar.covid19njstats.notification

import android.content.Context
import android.os.AsyncTask
import com.jesusmar.covid19njstats.BuildConfig
import com.jesusmar.covid19njstats.R
import com.jesusmar.covid19njstats.util.Auth0AuthenticationTask
import com.jesusmar.covid19njstats.util.Covid19API
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class ChannelRegister(): AsyncTask<Any, Void ,String>() {

    fun register(token: String) {
        execute(token)
    }

    override fun doInBackground(vararg params: Any?): String {

        val jsonInputString = JSONObject()
        val jToken = JSONObject()
        jToken.put("token", params[0])
        jsonInputString.put("channel", jToken )

        val tokenResult = Auth0AuthenticationTask.getAuth0Token()

        val headers = mapOf<String,String>(Pair("Content-Type","application/json"), Pair("Authorization:", "${tokenResult!!.token_type} ${tokenResult.access_token}" ))

        val body = RequestBody.create(MediaType.get("application/json"), jsonInputString.toString() )

        val response = Covid19API.getCovid19APIService().register(headers, body).execute()

        return if (response.isSuccessful) response.body()!!.toString() else "Error trying to store firebase Token"
    }
}

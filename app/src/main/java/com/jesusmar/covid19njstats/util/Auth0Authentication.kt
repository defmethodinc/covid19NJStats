package com.jesusmar.covid19njstats.util

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.sax.TextElementListener
import android.util.Log
import com.google.gson.Gson
import com.jesusmar.covid19njstats.BuildConfig
import com.jesusmar.covid19njstats.R
import com.jesusmar.covid19njstats.models.Auth0Token
import java.io.*
import java.net.SocketTimeoutException
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class Auth0AuthenticationTask(private val context: Context): AsyncTask<Any, Void ,String>() {

    lateinit var mListener : Auth0Listener

    interface Auth0Listener {
        fun success()
        fun fail()
    }

    fun setListener(listener: Auth0Listener) {
        mListener = listener
    }

    fun getToken(){
        execute()
    }

    fun getAuth0Token(): String {
        var result : String
        try {
            val url = URL("https://jesusmar.auth0.com/oauth/token")
            val connection = url.openConnection() as HttpsURLConnection
            connection.requestMethod = "POST"
            connection.doInput = true
            connection.doOutput = false
            connection.setRequestProperty("Content-Type", "application/json")

            val body = "{\"client_id\":\"${BuildConfig.CLIENT_ID}\",\"client_secret\":\"${BuildConfig.CLIENT_SECRET}\",\"audience\":\"${BuildConfig.AUDIENCE}\",\"grant_type\":\"${BuildConfig.GRANT_TYPE}\"}"

            try {
                connection.connect()
                val os = connection.outputStream
                val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                writer.write(body)
                writer.flush()
                writer.close()
                os.close()
            } catch (a: IOException) {
                Log.e("POST", "Ops - Could not save the Token")
            }

            val httpResult: Int = connection.responseCode

            if (httpResult == HttpsURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream

                val reader = BufferedReader(InputStreamReader(inputStream))
                val stringBuilder = StringBuilder()
                var line: String?
                try {
                    while(reader.readLine().also { line  = it } != null) {
                        stringBuilder.append(line + "\n")
                    }
                } catch (e: IOException){
                    e.printStackTrace()
                } finally {
                    try {
                        inputStream.close()
                    } catch (e: IOException){
                        e.printStackTrace()
                    }
                }
                result = stringBuilder.toString()
            } else {
                result = "deu chabu no server"
            }
        } catch(e: SocketTimeoutException){
            e.printStackTrace()
            result = "connection timeout"
        } catch (e: Exception){
            e.printStackTrace()
            result = "deu chabu desconehcido"
        }
        return result
    }


    override fun doInBackground(vararg params: Any?): String {
        return getAuth0Token()
    }

    override fun onPostExecute(result: String?) {
        if (result != null){
            val tokenObject = Gson().fromJson(result, Auth0Token::class.java)
            val editor: SharedPreferences.Editor = context.getSharedPreferences(context.getString(R.string.sharedPrefs), Context.MODE_PRIVATE).edit()
            editor.putString(context.getString(R.string.auth0TokenKey), "${tokenObject.token_type} ${tokenObject.access_token}")
            editor.apply()
        } else {
            mListener.fail()
        }
        mListener.success()
        super.onPostExecute(result)
    }
}
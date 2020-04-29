package com.jesusmar.covid19njstats.util

import android.content.Context

import android.os.AsyncTask
import com.jesusmar.covid19njstats.R
import java.io.*
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.nio.charset.Charset

class GetDataFromAPITask(private val mUrl: String, private val context: Context): AsyncTask<Any, Void ,String>() {

    private lateinit var mDataListener: DataListener

    fun getData(){
        execute()
    }

    private fun getAuthenticatedData(token:String):String {
        var result : String
        try {
            val url = URL(mUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.doInput = true
            connection.doOutput = false

            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", token)

            val httpResult: Int = connection.responseCode

            if (httpResult == HttpURLConnection.HTTP_OK) {
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
        val shared = context.getSharedPreferences(context.getString(R.string.sharedPrefs), 0)
        val apiToken = shared.getString(context.getString(R.string.auth0TokenKey), "")!!

        return getAuthenticatedData(apiToken)
    }

    override fun onPostExecute(result: String?) {
        if (result != null){
            mDataListener.onSuccess(result)
        } else {
            mDataListener.onError()
        }
        super.onPostExecute(result)
    }

    fun setDataListener(listener: DataListener): GetDataFromAPITask {
        mDataListener = listener
        return this
    }

    interface DataListener {
        fun onSuccess(data:String)
        fun onError()
    }
}
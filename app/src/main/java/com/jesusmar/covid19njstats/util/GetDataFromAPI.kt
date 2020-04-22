package com.jesusmar.covid19njstats.util

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL


class GetDataFromAPITask(url: String): AsyncTask<Any, Void ,String>() {

    private lateinit var mDataListener: DataListener
    private val mUrl = url


    fun getData(){
        execute()
    }

    override fun doInBackground(vararg params: Any?): String {
        var result : String
        try {
            val url = URL(mUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.doInput = true
            connection.doOutput = false


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
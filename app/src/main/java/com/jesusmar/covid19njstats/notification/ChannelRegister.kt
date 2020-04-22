package com.jesusmar.covid19njstats.notification

import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class ChannelRegister: AsyncTask<Any, Void ,String>() {
    override fun doInBackground(vararg params: Any?): String {
        var result : String
        try {
            val url = URL("https://covid19njapi.herokuapp.com/channels")
            val connection : HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doInput = true
            connection.doOutput = true

            connection.setRequestProperty("Content-Type", "application/json; utf-8")
            val jsonInputString = JSONObject()
            val jToken = JSONObject()
                jToken.put("token", params[0])
                jsonInputString.put("channel", jToken )
            try {
                connection.connect()
                val os = connection.outputStream
                val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                writer.write(jsonInputString.toString())
                writer.flush()
                writer.close()
                os.close()
            } catch (a: IOException) {
                Log.e("POST", "Ops - Could not save the Token")
            }

            val httpResult: Int = connection.responseCode

            if (httpResult == HttpURLConnection.HTTP_CREATED) {
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
                result = "Not expected response: ${HttpURLConnection.HTTP_CREATED}"
            }
        } catch(e: SocketTimeoutException){
            e.printStackTrace()
            result = "connection timeout"
        } catch (e: Exception){
            e.printStackTrace()
            result = "Ops - something wrong "
        }
        return result
    }
}

package com.jesusmar.covid19njstats.util

import android.content.Context

import android.os.AsyncTask
import com.jesusmar.covid19njstats.R

class GetDataFromAPITask(private val context: Context,
                         private val mUrl: String,
                         private val urlParams: String? = null
                         ): AsyncTask<Any, Void ,Any>() {

    private lateinit var mDataListener: DataListener

    fun getData(){
        execute()
    }

    override fun doInBackground(vararg params: Any?): Any {
        val shared = context.applicationContext.getSharedPreferences(context.getString(R.string.sharedPrefs), 0)
        val apiToken = shared.getString(context.applicationContext.getString(R.string.auth0TokenKey), "")!!
        return getAuthenticatedData(apiToken)!!
    }

    private fun getAuthenticatedData(apiToken: String): Any? {
        val headers = mapOf<String,String>(Pair("Content-Type","application/json"), Pair("Authorization:", apiToken))

        return when (mUrl) {
            context.getString(R.string.api_today) -> {
                 Covid19API.getCovid19APIService().today(headers).execute().body()!!
            }
            context.getString(R.string.api_all) -> {
                 Covid19API.getCovid19APIService().comparison(headers).execute().body()!!
            }
            context.getString(R.string.api_growth) -> {
                Covid19API.getCovid19APIService().growth(headers, urlParams!!).execute().body()!!
            }
            else -> {
                null
            }
        }
    }

    override fun onPostExecute(result: Any?) {
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
        fun onSuccess(data: Any?)
        fun onError()
    }


}
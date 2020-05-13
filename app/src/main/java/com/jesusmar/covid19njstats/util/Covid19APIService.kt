package com.jesusmar.covid19njstats.util

import com.jesusmar.covid19njstats.models.Auth0Token
import com.jesusmar.covid19njstats.models.ResponseData
import com.jesusmar.covid19njstats.models.ResponseDataGrowth
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object Covid19API {

    fun getCovid19APIService(): Covid19APIService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://covid19njapi.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(Covid19APIService::class.java)
    }

    fun getAuth0Service(): Auth0APIService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jesusmar.auth0.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(Auth0APIService::class.java)
    }

    interface Covid19APIService {
        @GET("/today")
        fun today(@HeaderMap headers: Map<String, String> ): Call<ResponseData>

        @GET("/all")
        fun comparison(@HeaderMap headers: Map<String, String> ): Call<ResponseData>

        @GET("/growth_nj")
        fun state_growth(@HeaderMap headers: Map<String, String> ): Call<ResponseDataGrowth>

        @GET("/growth_essex")
        fun essex_growth(@HeaderMap headers: Map<String, String> ): Call<ResponseDataGrowth>

        @POST("/channels")
        fun register(@HeaderMap headers: Map<String, String>, @Body body: RequestBody): Call<ResponseBody>
    }

    interface Auth0APIService {
        @POST("/oauth/token")
        fun getToken(@Body body: RequestBody): Call<Auth0Token>
    }
}
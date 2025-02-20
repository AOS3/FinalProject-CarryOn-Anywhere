package com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI

import com.google.gson.GsonBuilder

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TourAPIRetrofitClient {
    private const val BASE_URL = "https://apis.data.go.kr/B551011/KorService1/"

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val instance: TourAPIInterface by lazy {
        retrofit.create(TourAPIInterface::class.java)
    }
}
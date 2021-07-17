package com.jtk.tugaseas.Data

import android.content.res.Resources
import com.jtk.tugaseas.Data.Models.Response
import com.jtk.tugaseas.R
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsApi {

    @Headers("Authorization: 9e9bb9e225e4463ba988029176d06585")
    @GET("top-headlines")
    fun getHeadlinesFromIndonesia(
        @Query("country") country: String = "id"): Call<Response>

    @Headers("Authorization: 9e9bb9e225e4463ba988029176d06585")
    @GET("everything")
    fun searchNews(
        @Query("q") query: String,
        @Query("sortBy") sortBy: String = "relevancy"
    ) : Call<Response>

    @Headers("Authorization: 9e9bb9e225e4463ba988029176d06585")
    @GET("top-headlines")
    fun getNewsByCategory(
        @Query("category") category: String
    ) : Call<Response>
}
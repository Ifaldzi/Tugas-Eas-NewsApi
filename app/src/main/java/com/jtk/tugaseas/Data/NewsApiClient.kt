package com.jtk.tugaseas.Data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsApiClient {
    companion object {
        private final val BASE_URL = "https://newsapi.org/v2/"
        private var retrofit: Retrofit? = null

        fun getApiClient(): Retrofit? {
            if(retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
    }
}
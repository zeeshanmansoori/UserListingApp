package com.zee.userlistingapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        private val retrofit by lazy {
            val factory = GsonConverterFactory.create()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(factory)
                .build()

        }

        val api by lazy {
            retrofit.create(GitUserSearchApi::class.java)
        }
    }
}
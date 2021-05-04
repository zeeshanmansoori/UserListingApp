package com.zee.userlistingapp.api

import com.zee.userlistingapp.BuildConfig
import com.zee.userlistingapp.model.User
import com.zee.userlistingapp.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


const val BASE_URL = "https://api.github.com/"
interface GitUserSearchApi {
    @Headers(
        "Authorization: key=${BuildConfig.CLIENT_SECRET_KEY}"
    )
    @GET("search/users")
    suspend fun getFilterUsers(@Query("q") q:String,
                               @Query("per_page") perPage:Int = 50,
    ):
            Response<UserResponse>



    @Headers(
        "Authorization: key=${BuildConfig.CLIENT_SECRET_KEY}"
    )
    @GET("users/{user}")
    suspend fun getUser(@Path("user",encoded = true) user:String,
                        @Query("per_page") perPage:Int = 50,
    ):
            Response<User>






}
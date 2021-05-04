package com.zee.userlistingapp.model

import java.io.Serializable

data class User(
    val login:String="",
    val avatar_url:String="",
    val url:String=""
):Serializable

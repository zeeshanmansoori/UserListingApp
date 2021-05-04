package com.zee.userlistingapp.model

import java.io.Serializable

data class User(
    val login:String="",
    val avatar_url:String="",
    val url:String="",
    val public_repos:Int=0,
    val followers:Int = 0,
    val following:Int = 0,
    val name:String="",
    val email:String=""
):Serializable

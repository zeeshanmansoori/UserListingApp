package com.zee.userlistingapp.utilities

object GithubConstants {
    const val REDIRECT_URI = "http://localhost:8080/login/oauth2/code/github/redirect"
    const val SCOPE = "read:user,user:email"
    const val AUTHURL = "https://github.com/login/oauth/authorize"
    const val TOKENURL = "https://github.com/login/oauth/access_token"
}
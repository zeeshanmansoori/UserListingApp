package com.zee.userlistingapp.ui

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.google.android.material.button.MaterialButton
import com.zee.userlistingapp.BuildConfig
import com.zee.userlistingapp.R
import com.zee.userlistingapp.ui.home.HomeActivity
import com.zee.userlistingapp.utilities.GithubConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import java.io.OutputStreamWriter
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection

class LoginActivity : AppCompatActivity() {

    lateinit var githubAuthURLFull: String
    lateinit var githubdialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val state = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())

        Log.d("zeeshan", "state $state")
        githubAuthURLFull =
            GithubConstants.AUTHURL + "?client_id=" + BuildConfig.CLIENT_ID + "&scope=" +
                    GithubConstants.SCOPE + "&redirect_uri=" +
                    GithubConstants.REDIRECT_URI +
                    "&state=" + state

        findViewById<MaterialButton>(R.id.login_btn)
            .setOnClickListener {
                setupGithubWebviewDialog(githubAuthURLFull)
            }


    }

    //@SuppressLint("SetJavaScriptEnabled")
    private fun setupGithubWebviewDialog(url: String) {
        githubdialog = Dialog(this)
        val webView = WebView(this)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.webViewClient = MyGithubWebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
        githubdialog.setContentView(webView)
        githubdialog.show()
    }

    // A client to know about WebView navigations
    // For API 21 and above
    @Suppress("OverridingDeprecatedMember")
    inner class MyGithubWebViewClient : WebViewClient() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request!!.url.toString().startsWith(GithubConstants.REDIRECT_URI)) {
                handleUrl(request.url.toString())

                // Close the dialog after getting the authorization code
                if (request.url.toString().contains("code=")) {
                    githubdialog.dismiss()
                    saveToSharePref()
                    moveToHome()

                }
                return true
            }
            return false
        }

        // For API 19 and below
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith(GithubConstants.REDIRECT_URI)) {
                handleUrl(url)

                // Close the dialog after getting the authorization code
                if (url.contains("?code=")) {
                    githubdialog.dismiss()
                    saveToSharePref()
                    moveToHome()
                }
                return true
            }
            return false
        }

        // Check webview url for access token code or error
        private fun handleUrl(url: String) {
            val uri = Uri.parse(url)
            if (url.contains("code")) {
                val githubCode = uri.getQueryParameter("code") ?: ""
                Log.d("zeeshan", " githubcode $githubCode")
                requestForAccessToken(githubCode)
            }
        }


        //goto authorization server
        private fun requestForAccessToken(code: String) {
            val grantType = "authorization_code"

            val postParams =
                "grant_type=" + grantType + "&code=" + code + "&redirect_uri=" +
                        GithubConstants.REDIRECT_URI + "&client_id=" +
                        BuildConfig.CLIENT_ID + "&client_secret=" + BuildConfig.CLIENT_SECRET_KEY
            GlobalScope.launch(Dispatchers.Default) {
                val url = URL(GithubConstants.TOKENURL)
                val httpsURLConnection =
                    withContext(Dispatchers.IO) { url.openConnection() as HttpsURLConnection }
                httpsURLConnection.requestMethod = "POST"
                httpsURLConnection.setRequestProperty(
                    "Accept",
                    "application/json"
                )
                httpsURLConnection.doInput = true
                httpsURLConnection.doOutput = true
                val outputStreamWriter = OutputStreamWriter(httpsURLConnection.outputStream)
                withContext(Dispatchers.IO) {
                    outputStreamWriter.write(postParams)
                    outputStreamWriter.flush()
                }
                val response = httpsURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                withContext(Dispatchers.Main) {
                    val jsonObject = JSONTokener(response).nextValue() as JSONObject

                    val accessToken = jsonObject.getString("access_token") //The access token

                    // Get user's id, first name, last name, profile pic url
                    fetchGithubUserProfile(accessToken)
                }
            }
        }


        private fun fetchGithubUserProfile(token: String) {
            GlobalScope.launch(Dispatchers.Default) {
                val tokenURLFull =
                    "https://api.github.com/user"

                val url = URL(tokenURLFull)
                val httpsURLConnection =
                    withContext(Dispatchers.IO) { url.openConnection() as HttpsURLConnection }
                httpsURLConnection.requestMethod = "GET"
                httpsURLConnection.setRequestProperty("Authorization", "Bearer $token")
                httpsURLConnection.doInput = true
                httpsURLConnection.doOutput = false
                val response = httpsURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                val jsonObject = JSONTokener(response).nextValue() as JSONObject
                Log.i("GitHub Access Token: ", token)

                // GitHub Id
                val githubId = jsonObject.getInt("id")
                Log.i("GitHub Id: ", githubId.toString())

                // GitHub Display Name
                val githubDisplayName = jsonObject.getString("login")
                Log.i("GitHub Display Name: ", githubDisplayName)

                // GitHub Email
                val githubEmail = jsonObject.getString("email")
                Log.i("GitHub Email: ", githubEmail)

                // GitHub Profile Avatar URL
                val githubAvatarURL = jsonObject.getString("avatar_url")
                Log.d("zeeshan", "Github Profile Avatar URL: $githubAvatarURL")

            }
        }
    }

    private fun saveToSharePref(){
        val pref = getSharedPreferences("loggedin", MODE_PRIVATE).edit()
        pref.putBoolean("loggedin",true)
        pref.apply()
    }

    private fun moveToHome(){
        Intent(this@LoginActivity, HomeActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }
}
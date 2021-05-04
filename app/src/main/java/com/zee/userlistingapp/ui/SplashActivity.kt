package com.zee.userlistingapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.zee.userlistingapp.R
import com.zee.userlistingapp.ui.home.HomeActivity
import java.util.concurrent.Executor

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({

            val pref = getSharedPreferences("loggedin", Context.MODE_PRIVATE)

            val condition = pref.getBoolean("loggedin",false)


            if (condition){
                startActivity(Intent(this,HomeActivity::class.java))
            }else{
                startActivity(Intent(this,LoginActivity::class.java))
            }
            finish()

        }, 1500)
    }
}
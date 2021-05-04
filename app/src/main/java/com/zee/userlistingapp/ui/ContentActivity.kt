package com.zee.userlistingapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.squareup.picasso.Picasso
import com.zee.userlistingapp.R
import com.zee.userlistingapp.databinding.ActivityContentBinding
import com.zee.userlistingapp.model.User

class ContentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_content)

        val user = intent.getSerializableExtra("user") as User

        binding.apply {
            Picasso.get().load(user.avatar_url).into(userProfile)
            name.text = user.login
            url.text = user.url

        }

    }
}
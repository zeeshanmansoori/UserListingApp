package com.zee.userlistingapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.squareup.picasso.Picasso
import com.zee.userlistingapp.R
import com.zee.userlistingapp.api.RetrofitInstance
import com.zee.userlistingapp.databinding.ActivityContentBinding
import com.zee.userlistingapp.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_content)

        val user = intent.getSerializableExtra("user") as User
        binding.name.text = user.login
        binding.url.text = user.url
        Picasso.get().load(user.avatar_url).into(binding.userProfile)
        CoroutineScope(Dispatchers.Main).launch{
            try {

                val user = RetrofitInstance.api.getUser(user.login).body()
                binding.apply {
                    if (user!=null) {
                        publicRepos.text = user.public_repos.toString()
                        followers.text = user.followers.toString()
                        email.text = user.email
                        name.text = user.name
                    }

                }
            }
            catch (e:Exception){

            }


        }




    }
}
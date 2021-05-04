package com.zee.userlistingapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.zee.userlistingapp.R
import com.zee.userlistingapp.databinding.ActivityHomeBinding
import com.zee.userlistingapp.model.User
import com.zee.userlistingapp.ui.ContentActivity


class HomeActivity : AppCompatActivity(), HomeAdapter.UserItemClickListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var mAdapter:HomeAdapter
    private lateinit var viewmodel:UserVewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewmodel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(UserVewModel::class.java)
        mAdapter = HomeAdapter(this)

        binding.recyclerView.apply {
            adapter = mAdapter
        }

        viewmodel.ls.observe(this){
            binding.indicator.visibility = View.GONE
            if (it.size>50)
                mAdapter.submitList(it.subList(0,49))
            else
                mAdapter.submitList(it)

        }


        binding.seachView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    binding.indicator.visibility = View.VISIBLE
                    viewmodel.filter(query)

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

    }

    override fun onUserItemClicked(user: User) {
        Intent(this,ContentActivity::class.java).apply {
            putExtra("user",user)
            startActivity(this)
        }
    }

}
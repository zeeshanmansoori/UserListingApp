package com.zee.userlistingapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.zee.userlistingapp.R
import com.zee.userlistingapp.databinding.ActivityHomeBinding
import com.zee.userlistingapp.model.User
import com.zee.userlistingapp.ui.BottomSheet
import com.zee.userlistingapp.ui.ContentActivity


class HomeActivity : AppCompatActivity(), HomeAdapter.UserItemClickListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var mAdapter:HomeAdapter
    private lateinit var viewmodel:UserVewModel
    private var qualifier = ""
    private var countA = ""
    private var conditionA = ""
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
            mAdapter.submitList(it)

        }

        Snackbar.make(binding.recyclerView,"click on search icon to search",Snackbar.LENGTH_LONG).show()

        binding.filter.setOnClickListener {
            BottomSheet(object :FilterClickedListener{
                override fun onFilterClosed(repo: String, condition: String, count: String) {
                    qualifier = repo+condition+count
                    conditionA = condition
                    countA = count
                }

            },conditionA,countA
            ).show(supportFragmentManager,null)

        }
        binding.seachView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    binding.indicator.visibility = View.VISIBLE
                    viewmodel.filter(query.trim()+qualifier)

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


    interface FilterClickedListener{
        fun onFilterClosed(repo: String,condition:String,count:String)
    }
}
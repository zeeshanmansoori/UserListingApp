package com.zee.userlistingapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zee.userlistingapp.model.User
import com.zee.userlistingapp.api.RetrofitInstance
import kotlinx.coroutines.launch

class UserVewModel:ViewModel() {
    val ls = MutableLiveData<List<User>>()



     fun filter(q:String){
         viewModelScope.launch {
             RetrofitInstance.api.getFilterUsers(q).body()?.apply {
                 ls.value = this.items
             }
         }

    }
}
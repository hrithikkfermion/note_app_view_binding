package com.example.note_app_vb.activities.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.note_app_vb.activities.utils.DataStoreManager
import com.example.note_app_vb.local.UserRepository

//Here we pass Shared preferences instance
class LoginActivityViewModelFactory(private val dataStoreManager: DataStoreManager,
                                    private val userRepository : UserRepository
)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(LoginActivityViewModel::class.java)){
            return LoginActivityViewModel(dataStoreManager,userRepository) as T
        }

        throw IllegalArgumentException("Unknown View Model Class")
    }

}
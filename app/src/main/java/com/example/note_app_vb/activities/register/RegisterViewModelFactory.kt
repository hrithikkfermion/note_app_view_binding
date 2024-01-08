package com.aj.noteappajkotlinmvvm.activities.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.note_app_vb.activities.register.RegisterActivityViewModel
import com.example.note_app_vb.activities.utils.DataStoreManager
import com.example.note_app_vb.local.UserRepository

class RegisterActivityViewModelFactory(
    private val dataStoreManager: DataStoreManager,
    private val userRepository: UserRepository,
    private val mobileNo: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(RegisterActivityViewModel::class.java)){
            return RegisterActivityViewModel(dataStoreManager, userRepository, mobileNo) as T
        }

        throw IllegalArgumentException("Unknown View Model Class")
    }
}
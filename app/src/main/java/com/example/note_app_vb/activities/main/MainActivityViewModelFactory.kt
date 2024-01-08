package com.example.note_app_vb.activities.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.note_app_vb.activities.utils.DataStoreManager

class MainActivityViewModelFactory(private val dataStoreManager: DataStoreManager) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
            return MainActivityViewModel(dataStoreManager) as T
        }

        throw IllegalArgumentException("Unknown View Model Class")
    }

}
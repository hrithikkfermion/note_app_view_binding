package com.example.note_app_vb.fragments.refer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.note_app_vb.di.ActivityContext

class ReferViewModelFactory(
    private val activityContext : ActivityContext,
    private val mobileNo : String
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(ReferViewModel::class.java)){
            return ReferViewModel(activityContext,mobileNo) as T
        }

        throw IllegalArgumentException("Unknown View Model Class")
    }
}
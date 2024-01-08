package com.example.note_app_vb.activities.home.sharedviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeActivityViewModel : ViewModel() {
    private val _fabValue = MutableLiveData<Boolean>().apply {
        value = true
    }
    val isFabVisible = _fabValue

    val isButtonClicked = MutableLiveData<Boolean>()

    init {
        isButtonClicked.value = false
    }

    fun addNote(){
        isButtonClicked.value = true
    }


}
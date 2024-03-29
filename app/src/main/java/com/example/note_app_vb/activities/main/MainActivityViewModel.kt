package com.example.note_app_vb.activities.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note_app_vb.activities.utils.Constants
import com.example.note_app_vb.activities.utils.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    //Below boolean value decides flow of the app.
    val isUserLoggedIn = MutableLiveData<Boolean>()

    init {
        checkUserIdExist()
    }

    private fun checkUserIdExist() {
        viewModelScope.launch(Dispatchers.IO) {
            val userId : String = dataStoreManager.getUserId()
            //Saving this value for quick reference in all over app for valid condition only
            if(userId.isNotBlank()){
                Constants.userID = userId.toLong()
            }
            withContext(Dispatchers.Main){
                //If userId is blank -> false... else(userId exist) -> true
                isUserLoggedIn.value = userId.isNotBlank()
            }
        }
    }

}
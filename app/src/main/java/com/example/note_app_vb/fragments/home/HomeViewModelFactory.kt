package com.example.note_app_vb.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.note_app_vb.activities.utils.DataStoreManager
import com.example.note_app_vb.local.NoteRepository


class HomeViewModelFactory(private val dataStoreManager: DataStoreManager, private val noteRepository: NoteRepository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(dataStoreManager,noteRepository) as T
        }

        throw IllegalArgumentException("Unknown View Model Class")
    }
}
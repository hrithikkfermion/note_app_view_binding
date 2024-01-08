package com.example.note_app_vb.activities.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.note_app_vb.activities.note.model.Note
import com.example.note_app_vb.local.NoteRepository

class NoteActivityViewModelFactory(private val noteObj: Note, private val noteRepository : NoteRepository)  : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(NoteActivityViewModel::class.java)){
            return NoteActivityViewModel(noteObj,noteRepository) as T
        }

        throw IllegalArgumentException("Unknown View Model Class")
    }

}
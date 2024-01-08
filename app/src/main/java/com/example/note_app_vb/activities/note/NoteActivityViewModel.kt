package com.example.note_app_vb.activities.note

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note_app_vb.NoteApplication.Companion.getAppContext
import com.example.note_app_vb.NoteApplication.Companion.shortToast
import com.example.note_app_vb.R
import com.example.note_app_vb.activities.note.model.Note
import com.example.note_app_vb.activities.utils.Constants
import com.example.note_app_vb.local.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NoteActivityViewModel(noteObj: Note, private val noteRepository : NoteRepository)
    : ViewModel() {

    private var noteId: Long = 0 //This is used for storing 'noteId' for case of 'update'

    val isAddOrUpdateNoteStatus = MutableLiveData<String>()


    var inputNoteTitle = MutableLiveData<String>()


    var inputNoteContent = MutableLiveData<String>()

    init {
        isAddOrUpdateNoteStatus.value = "freshLaunch" //Initial value given
        inputNoteTitle.value = ""
        inputNoteContent.value = ""
        //Below we use ternary operator in kotlin style(java style ternary operator not supported)
        //https://www.baeldung.com/kotlin/ternary-operator
        Constants.NOTE_ACTION = if(noteObj.noteId > 0L)  Constants.UPDATE_NOTE else Constants.ADD_NOTE
        //Autofill data for Update note condition
        if(Constants.NOTE_ACTION == Constants.UPDATE_NOTE){
            autoFillNoteDataIfExist(noteObj)
        }
    }

    fun validateNoteData(noteTitle:String,noteContent:String){
//        val noteTitle = inputNoteTitle.value!!
//        val noteContent = inputNoteContent.value!!

        //Step 1: Check 'noteTitle' empty or not.
        if(noteTitle.isEmpty()){
            shortToast(getAppContext()!!.resources.getString(R.string.titleCannotBeEmpty))
        }
        //Step 2: Check 'noteContent' empty or not.
        else if(noteContent.isEmpty()){
            shortToast(getAppContext()!!.resources.getString(R.string.noteContentCannotBeEmpty))
        }
        //Step 3: Add note scenario
        else if(Constants.NOTE_ACTION == Constants.ADD_NOTE){
            //shortToast("Exclusive ADD")
            viewModelScope.launch (Dispatchers.IO) {
                val noteId : Long? = noteRepository.insertNote(Note(noteTitle, noteContent,0L, Constants.userID))
                withContext(Dispatchers.Main){
                    if(noteId!! > 0) {
                        isAddOrUpdateNoteStatus.value = Constants.REFRESH_NOTES
                        clearFields()
                    }
                }
            }
        }
        //Step 4: Update note scenario
        else if(Constants.NOTE_ACTION == Constants.UPDATE_NOTE){
            //NoteApplication().showToast("Exclusive UPDATE")
            viewModelScope.launch (Dispatchers.IO) {
                val noOfRows : Int = noteRepository.updateNote(noteTitle, noteContent,noteId)

                withContext(Dispatchers.Main){
                    if(noOfRows > 0) {
                        isAddOrUpdateNoteStatus.value = Constants.REFRESH_NOTES
                        clearFields()
                    }
                }
            }
        }
    }

    private fun autoFillNoteDataIfExist(noteObj: Note){
        //Only for valid noteId update function will work.
        if(noteObj.noteId > 0L) {
            noteId = noteObj.noteId
            inputNoteTitle.value = noteObj.noteTitle
            inputNoteContent.value = noteObj.noteContent
        }
    }

    //Post successful login clear InputFields
    private fun clearFields() {
        inputNoteTitle.value = ""
        inputNoteContent.value = ""
    }


}
package com.example.note_app_vb.activities.note

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.example.note_app_vb.R
import com.example.note_app_vb.activities.utils.Constants
import com.example.note_app_vb.database.NoteDatabase
import com.example.note_app_vb.databinding.ActivityNoteBinding
import com.example.note_app_vb.local.NoteRepository

class NoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteBinding
    private val args: NoteActivityArgs by navArgs()
    private lateinit var viewModel: NoteActivityViewModel
    lateinit var factory: NoteActivityViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        val noteRepository = NoteRepository(NoteDatabase.getDatabase(this))
        //Here we are passing 'note' Object & 'noteRepository' for db queries access.
        factory = NoteActivityViewModelFactory(args.note, noteRepository)
        viewModel = ViewModelProvider(this, factory)[NoteActivityViewModel::class.java]
        viewModel.isAddOrUpdateNoteStatus.observe(this) { noteStatus ->
            when (noteStatus) {
                //REFRESH_NOTES will refresh the RecyclerView list.
                Constants.REFRESH_NOTES -> {
                    Constants.NOTE_CALLBACK_ACTION = Constants.REFRESH_NOTES
                    onBackPress()
                }
            }
        }
        binding.apply {
            btnSaveNote.setOnClickListener {
                viewModel.validateNoteData(
                    binding.edtxtTitle.text.toString(),
                    binding.edtxtNoteContent.text.toString()
                )
            }
            edtxtTitle.setText(viewModel.inputNoteTitle.value.toString())
            edtxtNoteContent.setText(viewModel.inputNoteContent.value.toString())
        }

        //Below back button support
        val toolbar = supportActionBar
        if (toolbar != null) {
            toolbar.setDisplayShowTitleEnabled(true)
            toolbar.setDisplayHomeAsUpEnabled(true)
            //Based upon argument type title of the NoteActivity changes
            toolbar.setTitle(
                if (args.note.noteId > 0L)
                    R.string.update_note_title
                else
                    R.string.add_note_title
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPress()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onBackPress() {
        finish() //Just one step back we are navigating.
    }
}
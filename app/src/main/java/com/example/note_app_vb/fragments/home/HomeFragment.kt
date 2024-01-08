package com.example.note_app_vb.fragments.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.note_app_vb.NoteApplication.Companion.getDataStoreManager
import com.example.note_app_vb.R
import com.example.note_app_vb.activities.home.HomeActivity
import com.example.note_app_vb.activities.home.sharedviewmodel.HomeActivityViewModel
import com.example.note_app_vb.activities.login.LoginActivity
import com.example.note_app_vb.activities.note.model.Note
import com.example.note_app_vb.activities.utils.Constants
import com.example.note_app_vb.adapter.NoteAdapter
import com.example.note_app_vb.database.NoteDatabase
import com.example.note_app_vb.databinding.FragmentHomeBinding
import com.example.note_app_vb.local.NoteRepository


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var factory : HomeViewModelFactory
    private lateinit var noteAdapter: NoteAdapter
    private val homeActivityViewModel by activityViewModels<HomeActivityViewModel>()
    private var notes : MutableList<Note> = arrayListOf() //Here empty array annoted by "arrayListOf()"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpRecyclerview()

        homeActivityViewModel.isButtonClicked.observe(viewLifecycleOwner){ isClickedStatus ->

            when(isClickedStatus){
                true -> {
                    val note = Note("", "", 0L, Constants.userID)
                    val action = HomeFragmentDirections.actionNavigationHomeToNoteActivity(note)
                    //findNavController best usage inside fragment.
                    findNavController().navigate(action)
                    //We are resetting the value so double instance of NoteActivity can be avoided.
                    homeActivityViewModel.isButtonClicked.value = false
                }
                false -> print("Fab button disabled")
            }
        }

        viewModel.isUserLoggedIn.observe(viewLifecycleOwner){ loginStatus ->
            when(loginStatus){
                false -> {
                    activity?.finish()
                    goToLogin()
                }
                true -> print("User status logged in")
            }
        }
    }

    private fun initView() {
        val noteRepository = NoteRepository(NoteDatabase.getDatabase(requireView().context))
        homeActivityViewModel.isFabVisible.value = true
        factory = HomeViewModelFactory(getDataStoreManager(requireView().context), noteRepository)
        viewModel = ViewModelProvider(activity as HomeActivity, factory)[HomeViewModel::class.java]
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.top_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.logout -> {
                        viewModel.logOutUser()
                        return true
                    }
                }
                return false
            }

        }, viewLifecycleOwner)
        // Inflate the layout for this fragment
    }
    private fun setUpRecyclerview() {
        noteAdapter = NoteAdapter(viewModel)
        //binding.view.apply used to get rid of 'binding' writing repeatedly
        binding.rvNoteList.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL,false)
            setHasFixedSize(true)
            adapter = noteAdapter
        }

        //Below observer will observe change in NotesList and based upon that changes will be delivered.
        viewModel.notesList.observe(viewLifecycleOwner) { note ->
            noteAdapter.differ.submitList(note)
            updateUI(note)
            notes = note as MutableList<Note>
        }

        //Here we can get access to ongoing 'activity' instance
        loadNoteList()
    }
    private fun loadNoteList(){
        activity?.let {
            viewModel.getAllNotes(Constants.userID)
        }
    }

    private fun updateUI(note: List<Note>?) {
        if(note!!.isNotEmpty()){
            binding.cardView.visibility = View.GONE
            binding.rvNoteList.visibility = View.VISIBLE
        }
        else{
            binding.cardView.visibility = View.VISIBLE
            binding.rvNoteList.visibility = View.GONE
        }
    }
    override fun onResume() {
        super.onResume()
        if(Constants.NOTE_CALLBACK_ACTION == Constants.REFRESH_NOTES){
            Constants.NOTE_CALLBACK_ACTION = "" //Resetting value to empty.
            loadNoteList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }
    private fun goToLogin(){
        val myIntent = Intent(activity, LoginActivity::class.java)
        this.startActivity(myIntent)
    }


}